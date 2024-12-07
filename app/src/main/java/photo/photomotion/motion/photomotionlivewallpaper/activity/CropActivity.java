package photo.photomotion.motion.photomotionlivewallpaper.activity;

import static android.content.Intent.FLAG_RECEIVER_FOREGROUND;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivityCropBinding;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share;

public class CropActivity extends AppCompatActivity {

    private Uri mUri;
    private ActivityCropBinding binding;
    //    private CropImageView mCropView;
//    private View rootView;
    String size;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        size = getIntent().getStringExtra("size");
        position = getIntent().getIntExtra("effect_position", 0);

        if (size != null){
            switch (size) {
                case "fit":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                    break;
                case "square":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
                    break;
                case "3:4":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case "4:3":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case "9:16":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case "16:9":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case "7:5":
                    binding.cropImageView.setCustomRatio(7, 5);
                    break;
                case "free":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
                    break;
                case "circle":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case "Circle Square":
                    binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
                    break;
            }
        }


        Log.e("CropImageAct", Share.imageUrl);

        // Initialize crop view and UI setup
        initViews();
        if (savedInstanceState == null) {
            loadImage();
        }
    }

    private void initViews() {
        binding.buttonDone.setOnClickListener(view -> cropImage());
        binding.buttonFitImage.setOnClickListener(view -> {
            changecolor(binding.buttonFitImage);
            binding.cropImageView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
        });
        binding.button11.setOnClickListener(view -> {
            changecolor(binding.button11);
            binding.cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
        });
        binding.button34.setOnClickListener(view -> {
            changecolor(binding.button34);
            binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_3_4);
        });
        binding.button43.setOnClickListener(view -> {
            changecolor(binding.button43);
            binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_4_3);
        });
        binding.button916.setOnClickListener(view -> {
            changecolor(binding.button916);
            binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_9_16);
        });
        binding.button169.setOnClickListener(view -> {
            changecolor(binding.button169);
            binding.cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        });
        binding.buttonFree.setOnClickListener(view -> {
            changecolor(binding.buttonFree);
            binding.cropImageView.setCropMode(CropImageView.CropMode.FREE);
        });
        binding.buttonRotateLeft.setOnClickListener(view -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D));
        binding.buttonRotateRight.setOnClickListener(view -> binding.cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D));
        binding.buttonCustom.setOnClickListener(view -> {
            changecolor(binding.buttonCustom);
            binding.cropImageView.setCustomRatio(7, 5);
        });
        binding.buttonCircle.setOnClickListener(view -> {
            changecolor(binding.buttonCircle);
            binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        });
        binding.buttonShowCircleButCropAsSquare.setOnClickListener(view -> {
            changecolor(binding.buttonShowCircleButCropAsSquare);
            binding.cropImageView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
        });
        binding.tvBack.setOnClickListener(view -> finish());
    }

    private void loadImage() {
        if (Share.imageUrl == null || Share.imageUrl.isEmpty()) {
            Share.RestartApp(this);
        } else {
            Glide.with(this)
                    .load(Share.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.cropImageView);
        }
    }

    private final CropCallback mCropCallback = new CropCallback() {
        public void onSuccess(Bitmap bitmap) {
            StringBuilder sb = new StringBuilder();
            sb.append("mCropCallback:==>");
            sb.append(bitmap);
            Log.e("TAG", sb.toString());
            String str = "mCropCallback";
            Log.e(str, str);
            dismissProgress();
            String saveFaceInternalStorage = Share.saveFaceInternalStorage(bitmap, CropActivity.this);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onSuccess: cropimage ");
            sb2.append(saveFaceInternalStorage);
            String str2 = "cropfile";
            Log.e(str2, sb2.toString());
            Share.croppedImage = saveFaceInternalStorage;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onSuccess: Shrea.cropimage ");
            sb3.append(Share.croppedImage);
            Log.e(str2, sb3.toString());
            if (AlbumListActivity.activity != null) {
                AlbumListActivity.activity.finish();
            }
            CropActivity cropImageActivity = CropActivity.this;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("file:///");
            sb4.append(saveFaceInternalStorage);
            cropImageActivity.startResultActivity(Uri.parse(sb4.toString()));
        }

        public void onError(Throwable th) {
            dismissProgress();
        }
    };

    private void changecolor(LinearLayout button) {
        binding.buttonFitImage.setBackgroundColor(getColor(R.color.topBar));
        binding.button11.setBackgroundColor(getColor(R.color.topBar));
        binding.button34.setBackgroundColor(getColor(R.color.topBar));
        binding.button43.setBackgroundColor(getColor(R.color.topBar));
        binding.button916.setBackgroundColor(getColor(R.color.topBar));
        binding.button169.setBackgroundColor(getColor(R.color.topBar));
        binding.buttonFree.setBackgroundColor(getColor(R.color.topBar));
        binding.buttonCustom.setBackgroundColor(getColor(R.color.topBar));
        binding.buttonCircle.setBackgroundColor(getColor(R.color.topBar));
        binding.buttonShowCircleButCropAsSquare.setBackgroundColor(getColor(R.color.topBar));
        button.setBackgroundColor(getColor(R.color.secondary));
    }

    public void cropImage() {
        String str = "cropImage";
        Log.e(str, str);
        showProgress();
        binding.cropImageView.startCrop(createSaveUri(), this.mCropCallback, this.mSaveCallback);
    }

    private void showProgress() {
        // Show progress dialog logic
    }

    private void dismissProgress() {
        // Dismiss progress dialog logic
    }

    private Uri createSaveUri() {
        return Uri.fromFile(new File(getCacheDir(), "cropped"));
    }

    private final CropCallback cropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap bitmap) {
            Log.e("mCropCallback", "Cropping successful");
            String savedImage = Share.saveFaceInternalStorage(bitmap, CropActivity.this);
            Share.croppedImage = savedImage;
            startResultActivity(Uri.parse("file:///" + savedImage));
        }

        @Override
        public void onError(Throwable throwable) {
            dismissProgress();
        }
    };

    private LinearLayout mRootLayout;
    private final SaveCallback mSaveCallback = new SaveCallback() {
        public void onSuccess(Uri uri) {
            String str = "mSaveCallback";
            Log.e(str, str);
            dismissProgress();
        }

        public void onError(Throwable th) {
            dismissProgress();
        }
    };

    public void startResultActivity(Uri uri) {
        mUri = uri;
        Intent intent = new Intent(this, MotionEditActivity.class);
        intent.setData(uri);
        intent.putExtra("effect_position",position);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
