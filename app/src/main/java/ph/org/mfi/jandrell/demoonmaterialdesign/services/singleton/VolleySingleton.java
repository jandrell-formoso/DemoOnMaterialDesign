package ph.org.mfi.jandrell.demoonmaterialdesign.services.singleton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import ph.org.mfi.jandrell.demoonmaterialdesign.application.MFIApplication;

/**
 * Created by Jandrell on 3/25/2015.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;

    public VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MFIApplication.getAppContext());
    }

    public static VolleySingleton getInstance() {
        if(mInstance==null) {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
