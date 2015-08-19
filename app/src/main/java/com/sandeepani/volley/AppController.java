package com.sandeepani.volley;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.sandeepani.model.ParentModel;
import com.sandeepani.model.StudentDTO;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    private int selectedStudentPosition = 0;
    private ParentModel parentModel;
    private StudentDTO studentDTO;

    private static AppController mAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mAppInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setParentData(ParentModel parentModel) {
        this.parentModel = parentModel;
    }

    public ParentModel getParentsData() {
        return parentModel;
    }

    public void setSelectedChild(int selectedStudentPosition) {
        this.selectedStudentPosition = selectedStudentPosition;

    }

    public int getSelectedChild() {
        return selectedStudentPosition;
    }
}
