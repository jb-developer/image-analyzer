package dev.jbcu10.imageanalyzer.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import dev.jbcu10.imageanalyzer.R;
import dev.jbcu10.imageanalyzer.activity.HomeActivity;
import dev.jbcu10.imageanalyzer.adapter.AnalysisAdapter;
import dev.jbcu10.imageanalyzer.model.Analysis;
import dev.jbcu10.imageanalyzer.model.Food;
import dev.jbcu10.imageanalyzer.utils.FoodMapper;
import dev.jbcu10.imageanalyzer.utils.PackageManagerUtils;
import dev.jbcu10.imageanalyzer.utils.PermissionUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment    extends ListFragment implements AbsListView.OnScrollListener,
        AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String CLOUD_VISION_API_KEY = "AIzaSyC7cDjbP6SUusdVsEiCwfrQVTVrwniVNeg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String FILE_NAME = "temp.jpg";

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    ProgressDialog pDialog;

     private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private boolean isFood;
    private AnalysisAdapter mAdapter;
    private TextView mImageDetails;
     private ImageView mMainImage;
    private String  token = "";
    private String  result = "";
    JSONObject foodJson  = new JSONObject();
    View rootView;
    public CameraFragment() {
        // Required empty public constructor
    }
    private AdView mAdView;
    private void loadAds(){
        MobileAds.initialize(getActivity(), getResources().getString(R.string.interstitial_ad_unit_id));
        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);

        Log.d("ads id ", getResources().getString(R.string.interstitial_ad_unit_id));
        adView.setAdUnitId(  getResources().getString(R.string.interstitial_ad_unit_id));

        mAdView = rootView.findViewById(R.id.adView);
        //   AdRequest adRequest = new AdRequest.Builder().addTestDevice("6CAB464F621D12E6CDECD2CBDFAC92B1").build();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    private void init(){
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        pDialog = new ProgressDialog(getActivity());
        loadAds();
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getClientID();
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder
                    .setMessage(R.string.dialog_select_prompt)
                    .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isFood =false;
                            startGalleryChooser();
                        }
                    })
                    .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startCamera();
                        }
                    });
            builder.create().show();
        });

        mImageDetails = rootView.findViewById(R.id.image_details);
         mMainImage = rootView.findViewById(R.id.main_image);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        init();
        return rootView;
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(getActivity(), GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                getActivity(),
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(getActivity(), getContext().getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode ==  getActivity().RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(getActivity(), getContext().getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Analysis picking failed because " + e.getMessage());
                Toast.makeText(getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Analysis picker gave us a null image.");
            Toast.makeText(getActivity(), R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, List<Analysis>>() {
            @Override
            protected List<Analysis> doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getActivity().getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getActivity().getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);

                            Feature faceDetection = new Feature();
                            faceDetection.setType("FACE_DETECTION");
                            faceDetection.setMaxResults(10);
                            add(faceDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return null;
            }

            protected void onPostExecute(List<Analysis> result) {
                initializeGridView();
                 if (result != null) {
                     mImageDetails.setText("Upload is complete!");
                     onLoadMoreItems(result);
                     loadAds();
                }
                if(result ==null){
                    mImageDetails.setText("Upload is complete but no result is found!");

                }
            }
        }.execute();
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();



    @SuppressLint("StaticFieldLeak")
    private void getClientID(){
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                String json = "{\n" +
                        "  \"grant_type\": \"client_credentials\",\n" +
                        "  \"client_id\": \"3b94f9b17b45d2925cdd21f1c5a6b11e5b0155d1514787edda5044dbf0465111\",\n" +
                        "  \"client_secret\": \"341572e4a2fa433984d8cf5db1053e741c1cd0c534530db9b430e99839b4e091\",\n" +
                        "  \"scope\": \"public read write\"\n" +
                        "}";
                String url = "http://www.healthos.co/api/v1/oauth/token.json";
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    token =  jsonObject.getString("access_token");
                    Log.d("token:",token);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Test";
            }

            protected void onPostExecute(String result) {
                //  mImageDetails.setText(result);
            }
        }.execute();

    }
    @SuppressLint("StaticFieldLeak")
    private void searchFruit(final String fruit){
         new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {

                String url = "http://www.healthos.co/api/v1/search/food/items/"+fruit;
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization","Bearer "+token)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONArray jsonArray = new JSONArray(response.body().string());
                    String fruitId = jsonArray.getJSONObject(0).getString("food_item_id");
                    Log.d("fruitId:",fruitId);
                    return fruitId;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "null";
            }

            protected void onPostExecute(String res) {
            result = res;
            }
        }.execute();

    }
    @SuppressLint("StaticFieldLeak")
    private void getNutrients(final String fruitID){
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {

                String url = "http://www.healthos.co/api/v1/food/items/"+fruitID;
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization","Bearer "+token)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                     foodJson= new JSONObject(response.body().string());
                    Log.d(TAG, "onItemClick: "+ FoodMapper.mapFoodJSONtoFood(foodJson));

                    Log.d("food:",foodJson.toString());
                 } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Test";
            }

            protected void onPostExecute(String result) {
                //  mImageDetails.setText(result);
            }
        }.execute();

    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private List<Analysis> convertResponseToString(BatchAnnotateImagesResponse response) {
         List<Analysis> analyses = new LinkedList<>();
        /* try {
             Log.d("is face", response.getResponses().get(0).getFaceAnnotations().toString());
             List<FaceAnnotation> faceAnnotations = response.getResponses().get(0).getFaceAnnotations();
             if (faceAnnotations != null) {
                 for (FaceAnnotation faceAnnotation : faceAnnotations) {


                         analyses.add(new Analysis(   faceAnnotation.getAngerLikelihood(),"Anger"));
                         analyses.add(new Analysis(   faceAnnotation.getBlurredLikelihood(),"Blurred"));
                         analyses.add(new Analysis(   faceAnnotation.getHeadwearLikelihood(),"Head Wear"));
                         analyses.add(new Analysis(   faceAnnotation.getJoyLikelihood(),"Joy"));
                         analyses.add(new Analysis(   faceAnnotation.getSurpriseLikelihood(),"Surprise"));
                         analyses.add(new Analysis(   faceAnnotation.getSorrowLikelihood(),"Sorrow"));


                 }
             }
             return analyses;
           }
         catch (Exception e){
             Log.d("is face",e.getMessage());

         }
*/
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                if(!compareResult(label.getDescription())) {


                    analyses.add(new Analysis( String.valueOf( label.getScore()),label.getDescription()));

                }
            }
        }
        return analyses;
    }

    private boolean compareResult(String label){
        if(label.contains("food")){
            isFood=true;
        }
        return label.contains("food")||label.contains("fruit")||label.contains("mc")||label.contains("prod");
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(isFood) {
            Analysis analysis = mAdapter.getItem(i);
            searchFruit(analysis.getDescription());
            getNutrients(result);

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void initializeGridView() {
        mAdapter = new AnalysisAdapter(getActivity(), R.id.txt_name, R.id.imageView);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setListAdapter(mAdapter);
            }

        });
    }

    private void onLoadMoreItems(List<Analysis> relatives) {
        for (Analysis data : relatives) {
            mAdapter.add(data);
        }
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
        hideDialog();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener(this);
    }
}
