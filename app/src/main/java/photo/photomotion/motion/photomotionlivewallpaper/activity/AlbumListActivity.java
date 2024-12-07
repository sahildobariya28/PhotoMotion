package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.recyclerview.widget.GridLayoutManager;

import photo.photomotion.motion.photomotionlivewallpaper.adapter.PhoneAlbumImagesAdapter;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivityAlbumBinding;
import photo.photomotion.motion.photomotionlivewallpaper.utils.GridSpacingItemDecoration;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share.KEYNAME;

public class AlbumListActivity extends Activity implements OnClickListener {
    public static AlbumListActivity activity;
    private PhoneAlbumImagesAdapter albumAdapter;
    private GridLayoutManager gridLayoutManager;
    String size;
    int position;
    private long mLastClickTime = 0;

    ActivityAlbumBinding binding;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        size = getIntent().getStringExtra("size");
        position = getIntent().getIntExtra("effect_position", 0);

        activity = this;

            initView();
            initViewAction();
    }


    private void initView() {
        if (albumAdapter == null) {
            gridLayoutManager = new GridLayoutManager(this, 3);
            binding.rcvAlbumImages.setLayoutManager(gridLayoutManager);
            binding.rcvAlbumImages.addItemDecoration(new GridSpacingItemDecoration(3, 15, true));

            try {
                Log.d("dsjfkljsdlfjsdf", "initView: "  + Share.lst_album_image);
                albumAdapter = new PhoneAlbumImagesAdapter(this, Share.lst_album_image, size, position);
                binding.rcvAlbumImages.setAdapter(albumAdapter);
                binding.tvTitleAlbumImage.setText(getIntent().getExtras().getString(KEYNAME.ALBUM_NAME));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    private void initView() {
//        gridLayoutManager = new GridLayoutManager(this, 3);
//        binding.rcvAlbumImages.setLayoutManager(gridLayoutManager);
//        binding.rcvAlbumImages.addItemDecoration(new GridSpacingItemDecoration(3, 15, true));
//        try {
//            albumAdapter = new PhoneAlbumImagesAdapter(this, Share.lst_album_image, size);
//            binding.rcvAlbumImages.setAdapter(albumAdapter);
//            binding.tvTitleAlbumImage.setText(getIntent().getExtras().getString(KEYNAME.ALBUM_NAME));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void initViewAction() {
        binding.ivBackAlbumImage.setOnClickListener(this);
    }

    public void onBackPressed() {
        finish();
    }

    public void onPause() {

        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View view) {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime();
            int id = view.getId();
            if (id == R.id.iv_back_album_image) {
                onBackPressed();
            }
        }
    }

}
