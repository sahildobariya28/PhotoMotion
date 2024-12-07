package photo.photomotion.motion.photomotionlivewallpaper;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;

import androidx.core.app.ActivityCompat;

import photo.photomotion.motion.photomotionlivewallpaper.callback.OnProgressReceiver;

import java.io.File;

public class ApplicationClass extends Application {
    public static Context context;
    private static ApplicationClass myApplication;
    private static OnProgressReceiver onProgressReceiver;


    public static ApplicationClass getApplication() {
        return myApplication;
    }

    public static void setApplication(ApplicationClass application) {
        myApplication = application;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ApplicationClass.context = context;
    }

    public void onCreate() {
        super.onCreate();
        setApplication(this);
        StrictMode.setVmPolicy(new Builder().build());
        setContext(getApplicationContext());
    }

    public static boolean checkForStoragePermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }

        }
    }

    public static void deleteTemp() {
        ContextWrapper cw = new ContextWrapper(context);
        File localStore = cw.getDir("localStore", Context.MODE_PRIVATE);
        File files = cw.getDir("files", Context.MODE_PRIVATE);
        if (localStore.exists()) {
            deleteDir(localStore);
        }
        if (files.exists()) {
            deleteDir(files);
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }



    public OnProgressReceiver getOnProgressReceiver() {
        return this.onProgressReceiver;
    }

    public void setOnProgressReceiver(OnProgressReceiver onProgressReceiver) {
        this.onProgressReceiver = onProgressReceiver;
    }
}