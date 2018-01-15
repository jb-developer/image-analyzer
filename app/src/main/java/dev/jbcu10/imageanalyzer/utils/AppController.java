package dev.jbcu10.imageanalyzer.utils;

import android.app.Application;

import dev.jbcu10.imageanalyzer.model.Food;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private Food food;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static AppController getmInstance() {
        return mInstance;
    }

    public static void setmInstance(AppController mInstance) {
        AppController.mInstance = mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}