package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.http.protocol.HTTP;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnShare.setOnClickListener(view -> {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(HTTP.PLAIN_TEXT_TYPE);
            intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
            StringBuilder sb = new StringBuilder();
            sb.append("Download this amazing ");
            sb.append(getResources().getString(R.string.app_name));
            sb.append(" app from play store\n\n");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append("https://play.google.com/store/apps/details?id=");
            sb3.append(getPackageName());
            sb3.append("\n\n");
            intent.putExtra("android.intent.extra.TEXT", sb3.toString());
            startActivity(Intent.createChooser(intent, "Choose one"));
        });

        binding.linearLayoutRate.setOnClickListener(view -> {
            final String appPackageName = getApplication().getPackageName();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });

        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}