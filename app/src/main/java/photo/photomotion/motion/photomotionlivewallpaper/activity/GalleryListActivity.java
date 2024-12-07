package photo.photomotion.motion.photomotionlivewallpaper.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import photo.photomotion.motion.photomotionlivewallpaper.adapter.PhoneAlbumAdapter;
import photo.photomotion.motion.photomotionlivewallpaper.model.PhoneAlbum;
import photo.photomotion.motion.photomotionlivewallpaper.model.PhonePhoto;
import photo.photomotion.motion.photomotionlivewallpaper.utils.GridSpacingItemDecoration;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.databinding.ActivityGalleryAlbumBinding;


public class GalleryListActivity extends FragmentActivity {

    private ActivityGalleryAlbumBinding binding;
    private String size;
    private int position;
    private PhoneAlbumAdapter albumAdapter;
    private LinearLayout lin_no_photo;
    private ProgressBar progressBar;
    private RecyclerView rcv_album;
    private Vector<String> albumsNames = new Vector<>();
    private Vector<PhoneAlbum> phoneAlbums = new Vector<>();
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityGalleryAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        size = getIntent().getStringExtra("size");
        position = getIntent().getIntExtra("effect_position", 0);

        initViews();
        initViewAction();  // Initialize all view actions
        new LoadImageTask().execute();  // Load images in background

        binding.ivClose.setOnClickListener(view -> onBackPressed());
    }

    private void initViews() {
        rcv_album = findViewById(R.id.rcv_album);
        lin_no_photo = findViewById(R.id.lin_no_photo);
        progressBar = findViewById(R.id.progressBar);
        gridLayoutManager = new GridLayoutManager(this, 3);
        rcv_album.setLayoutManager(gridLayoutManager);
        rcv_album.addItemDecoration(new GridSpacingItemDecoration(3, 15, true));
        albumAdapter = new PhoneAlbumAdapter(this, phoneAlbums, size, position);
        rcv_album.setAdapter(albumAdapter);
    }

    public boolean initViewAction() {
        String[] projection = {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
        };

        try (Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)) {
            if (cursor == null || cursor.getCount() <= 0) return false;

            Log.i("DeviceImageManager", "Query count: " + cursor.getCount());
            lin_no_photo.setVisibility(View.GONE);
            rcv_album.setVisibility(View.VISIBLE);

            int albumNameIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int photoUriIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

            while (cursor.moveToNext()) {
                String albumName = cursor.getString(albumNameIndex);
                String photoUri = cursor.getString(photoUriIndex);
                int photoId = cursor.getInt(idIndex);

                if (new File(photoUri).length() == 0 || photoUri.endsWith(".gif")) continue;

                PhonePhoto phonePhoto = new PhonePhoto();
                phonePhoto.setAlbumName(albumName);
                phonePhoto.setPhotoUri(photoUri);
                phonePhoto.setId(photoId);

                PhoneAlbum album = getOrCreateAlbum(albumName, photoUri, photoId);
                album.getAlbumPhotos().add(phonePhoto);

                Log.i("DeviceImageManager", "Photo added to album => " + albumName);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private PhoneAlbum getOrCreateAlbum(String albumName, String coverUri, int albumId) {
        for (PhoneAlbum album : phoneAlbums) {
            if (album != null && album.getName().equalsIgnoreCase(albumName)) {
                return album;
            }
        }

        PhoneAlbum newAlbum = new PhoneAlbum();
        newAlbum.setId(albumId);
        newAlbum.setName(albumName);
        newAlbum.setCoverUri(coverUri);
        phoneAlbums.add(newAlbum);
        albumsNames.add(albumName);

        Log.i("DeviceImageManager", "New album created => " + albumName);
        return newAlbum;
    }

    private class LoadImageTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return initViewAction();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            if (success) {
                if (phoneAlbums != null) {
                    for (PhoneAlbum album : phoneAlbums) {
                        album.setCoverUri(album.getAlbumPhotos()
                                .get(album.getAlbumPhotos().size() - 1).getPhotoUri());
                    }
                }
                albumAdapter.notifyDataSetChanged();
            } else {
                rcv_album.setVisibility(View.GONE);
                lin_no_photo.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Share.lst_album_image.clear();
        finish();
    }
}
