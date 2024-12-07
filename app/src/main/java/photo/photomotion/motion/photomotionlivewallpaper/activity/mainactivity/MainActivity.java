package photo.photomotion.motion.photomotionlivewallpaper.activity.mainactivity;

import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import photo.photomotion.motion.photomotionlivewallpaper.ApplicationClass;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.activity.CropActivity;
import photo.photomotion.motion.photomotionlivewallpaper.activity.GalleryListActivity;
import photo.photomotion.motion.photomotionlivewallpaper.activity.MotionEditActivity;
import photo.photomotion.motion.photomotionlivewallpaper.activity.ProfileActivity;
import photo.photomotion.motion.photomotionlivewallpaper.activity.SettingActivity;
import photo.photomotion.motion.photomotionlivewallpaper.adapter.EffectAdapter;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivityHomeBinding;
import photo.photomotion.motion.photomotionlivewallpaper.model.EffectData;
import photo.photomotion.motion.photomotionlivewallpaper.photoAlbum.MyAlbumActivity;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM = 103;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2 = 104;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3 = 105;
    public String mSelectedOutputPath;
    public String mSelectedImagePath;
    public Uri mSelectedImageUri;

    public ImageAdapter moEffectAdapter;
    public List<EffectData> moEffectlist = new ArrayList();

    ActivityHomeBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSize();
        setEffectAdapter();
        initViewListeners();
        if (ApplicationClass.checkForStoragePermission(this)) {
            ApplicationClass.deleteTemp();
        }
    }

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mSelectedImagePath = mSelectedOutputPath;
                        if (stringIsNotEmpty(mSelectedImagePath)) {
                            File fileImageClick = new File(mSelectedImagePath);
                            if (fileImageClick.exists()) {
                                mSelectedImageUri = FileProvider.getUriForFile(MainActivity.this, "photo.photomotion.motion.photomotionlivewallpaper" + ".provider", fileImageClick);
                                if (mSelectedImageUri == null) {
                                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Share.SAVED_BITMAP = mSelectedImageUri;
                                Share.imageUrl = mSelectedImagePath;
                                startActivity(new Intent(MainActivity.this, CropActivity.class));
                            }
                        }
                    }

                }
            });
    public void setEffectAdapter() {
        moEffectlist.clear();
        moEffectlist.add(new EffectData(-1, "", "", true, R.drawable.noeffect, ""));
        moEffectlist.add(new EffectData(0, "animation0.json", "", false, R.drawable.filteranim0, ""));
        moEffectlist.add(new EffectData(1, "animation1.json", "animation1", false, R.drawable.filteranim1, ""));
        moEffectlist.add(new EffectData(2, "anim.json", "anim", false, R.drawable.filteranim12, ""));
        moEffectlist.add(new EffectData(3, "animation3.json", "animation3", false, R.drawable.filteranim3, ""));
        moEffectlist.add(new EffectData(4, "animation4.json", "animation4", false, R.drawable.filteranim4, ""));
        moEffectlist.add(new EffectData(5, "animation5.json", "animation5", false, R.drawable.filteranim5, ""));
        moEffectlist.add(new EffectData(6, "animation6.json", "", false, R.drawable.filteranim6, ""));
        moEffectlist.add(new EffectData(8, "animation8.json", "animation8", false, R.drawable.filteranim8, ""));
        moEffectlist.add(new EffectData(9, "animation9.json", "", false, R.drawable.filteranim9, ""));
        moEffectlist.add(new EffectData(10, "animation10.json", "", false, R.drawable.anim10_white_hart, ""));
        moEffectlist.add(new EffectData(12, "animation12.json", "animation12", false, R.drawable.filteranim12, ""));
        moEffectlist.add(new EffectData(13, "animation13.json", "animation13", false, R.drawable.filteranim13, ""));
        moEffectlist.add(new EffectData(14, "animation14.json", "animation14", false, R.drawable.filteranim14, ""));
        moEffectlist.add(new EffectData(16, "animation16.json", "animation16", false, R.drawable.filteranim16, ""));
        moEffectlist.add(new EffectData(17, "animation17.json", "animation17", false, R.drawable.filteranim17, ""));
        moEffectlist.add(new EffectData(18, "animation18.json", "animation18", false, R.drawable.filteranim18, ""));
        moEffectlist.add(new EffectData(19, "animation19.json", "animation19", false, R.drawable.filteranim19, ""));
        moEffectlist.add(new EffectData(26, "animation26.json", "", false, R.drawable.filteranim26, ""));
        moEffectlist.add(new EffectData(27, "animation27.json", "animation27", false, R.drawable.filteranim27, ""));
        moEffectlist.add(new EffectData(28, "animation28.json", "animation28", false, R.drawable.filteranim28, ""));
        moEffectlist.add(new EffectData(29, "animation29.json", "animation29", false, R.drawable.filteranim29, ""));
        moEffectlist.add(new EffectData(30, "animation30.json", "animation30", false, R.drawable.filteranim30, ""));
        moEffectlist.add(new EffectData(31, "animation31.json", "animation31", false, R.drawable.filteranim31, ""));

        moEffectAdapter = new ImageAdapter(this, moEffectlist, BitmapFactory.decodeResource(getResources(), R.drawable.filter_thumb), (effectData, i) -> {

        });

        binding.recyclEffect.setAdapter(moEffectAdapter);

    }

    public static boolean requestCameraPermission(Activity activity, int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                return false;
            }

        } else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }

        return true;
    }


    public static boolean checkForCameraPermission(Activity activity) {

        return ActivityCompat.checkSelfPermission(activity, CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void initSize(){
        binding.btnFit.setOnClickListener(view -> startSizeActivity("fit"));
        binding.btnSquare.setOnClickListener(view -> startSizeActivity("square"));
        binding.btn34.setOnClickListener(view -> startSizeActivity("3:4"));
        binding.btn43.setOnClickListener(view -> startSizeActivity("4:3"));
        binding.btn916.setOnClickListener(view -> startSizeActivity("9:16"));
        binding.btn169.setOnClickListener(view -> startSizeActivity("16:9"));
        binding.btn75.setOnClickListener(view -> startSizeActivity("7:5"));
        binding.btnFree.setOnClickListener(view -> startSizeActivity("free"));
        binding.btnCircle.setOnClickListener(view -> startSizeActivity("circle"));
        binding.btnCirSquare.setOnClickListener(view -> startSizeActivity("Circle Square"));


    }

    public void startSizeActivity(String size){
        Intent intent = new Intent(this, GalleryListActivity.class);
        intent.putExtra("size", size);
        startActivity(intent);
    }
    private void initViewListeners() {
        binding.carousel.registerLifecycle(getLifecycle());
        ArrayList<CarouselItem> list = new ArrayList<>();
        list.add(new CarouselItem(R.drawable.banner1, "Photo by Aaron Wu on Unsplash"));
        list.add(new CarouselItem(R.drawable.banner2, "Photo by Aaron Wu on Unsplash"));
        list.add(new CarouselItem(R.drawable.banner3, "Photo by Aaron Wu on Unsplash"));
        binding.carousel.setData(list);
        binding.carousel.setData(list);

        binding.linearLayout9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("size", "fit");
                startActivity(intent);
            }
        });


        binding.btnGallery.setOnClickListener(view -> {
            if (requestCameraPermission(MainActivity.this, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2)) {
                Intent intent = new Intent(this, GalleryListActivity.class);
                intent.putExtra("size", "fit");
                startActivity(intent);
            }
        });

        binding.btnMyCreation.setOnClickListener(view -> {
            if (requestCameraPermission(MainActivity.this, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3)) {
                Intent intent = new Intent(this, MyAlbumActivity.class);
                startActivity(intent);
            }
        });


        binding.btnSetting.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });
    }

    private void toOpenCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "photo.photomotion.motion.photomotionlivewallpaper" + ".provider", createImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            cameraResultLauncher.launch(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM: {
                if (!checkForCameraPermission(this)) {
                    Toast.makeText(this, "Phone Camera permision not granted", Toast.LENGTH_SHORT).show();
                } else if (!ApplicationClass.checkForStoragePermission(this)) {
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
                } else if (checkForCameraPermission(this) && ApplicationClass.checkForStoragePermission(this)) {
                    toOpenCamera();
                    return;
                }
                requestCameraPermission(MainActivity.this, MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM);
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM2: {
                if (!ApplicationClass.checkForStoragePermission(this))
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(this, GalleryListActivity.class);
                    startActivity(intent);
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_CAMERA_PIP_CAM3: {
                if (!ApplicationClass.checkForStoragePermission(this))
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(this, MyAlbumActivity.class);
                    startActivity(intent);
                }
            }
        }
    }


    private File createImageFile() {
        File storageDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + "photo.photomotion.motion.photomotionlivewallpaper" + "/CamPic/");
        storageDir.mkdirs();
        File image = null;
        try {
            image = new File(storageDir, "Temp");
            if (image.exists())
                image.delete();
            image.createNewFile();

            mSelectedOutputPath = image.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

}
