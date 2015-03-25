package ph.org.mfi.jandrell.demoonmaterialdesign.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jandrell on 3/25/2015.
 */
public class MFIApplication extends Application {
    private static MFIApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MFIApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}
