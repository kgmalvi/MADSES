package com.android.mads.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

/**
 * Created by Administrator on 06-12-2015.
 */
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue myRequest;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getmInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (myRequest == null) {
            myRequest = Volley.newRequestQueue(getApplicationContext());
        }

        return myRequest;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Objects tag) {
        if (myRequest != null) {
            myRequest.cancelAll(tag);
        }
    }

}
