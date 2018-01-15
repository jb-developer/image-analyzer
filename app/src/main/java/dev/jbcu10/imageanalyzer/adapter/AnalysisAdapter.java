package dev.jbcu10.imageanalyzer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import dev.jbcu10.imageanalyzer.R;
import dev.jbcu10.imageanalyzer.model.Analysis;


/**
 * Created by dev on 10/14/17.
 */

public class AnalysisAdapter extends ArrayAdapter<Analysis> {

    private final LayoutInflater mLayoutInflater;

    public AnalysisAdapter(final Context context, final int textViewResourceId, final int imageViewResourceId) {
        super(context, textViewResourceId, imageViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        AnalysisAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_firstaid, parent, false);
            viewHolder = new AnalysisAdapter.ViewHolder();
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name);
            viewHolder.txt_description = convertView.findViewById(R.id.txt_description);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AnalysisAdapter.ViewHolder) convertView.getTag();
        }
        final Analysis analysis = getItem(position);

        viewHolder.txt_name.setText(analysis.getDescription());
        try {
            long score = Math.round(100*Double.parseDouble(analysis.getScore()));
            viewHolder.txt_description.setText( score+ "%");
        }
        catch (Exception e){

            viewHolder.txt_description.setText( analysis.getScore() );
        }



        return convertView;
    }

    static class ViewHolder {
        TextView txt_name, txt_description;

    }

}
