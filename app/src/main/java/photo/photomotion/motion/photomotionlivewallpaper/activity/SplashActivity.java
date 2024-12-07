package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.activity.mainactivity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}
