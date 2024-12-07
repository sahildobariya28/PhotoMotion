package photo.photomotion.motion.photomotionlivewallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import photo.photomotion.motion.photomotionlivewallpaper.model.PhonePhoto;

import photo.photomotion.motion.photomotionlivewallpaper.activity.AlbumListActivity;
import photo.photomotion.motion.photomotionlivewallpaper.utils.Share;
import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.model.PhoneAlbum;

public class PhoneAlbumAdapter extends RecyclerView.Adapter<PhoneAlbumAdapter.ViewHolder> {

    private final List<PhoneAlbum> albumList;
    private final Context context;
    private final String size;
    private final int position;

    public PhoneAlbumAdapter(Context context, List<PhoneAlbum> albumList, String size, int position) {
        this.context = context;
        this.albumList = albumList;
        this.size = size;
        this.position = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbum;
        ProgressBar progressBar;
        TextView tvAlbumName;

        public ViewHolder(View view) {
            super(view);
            tvAlbumName = view.findViewById(R.id.tv_album_name);
            ivAlbum = view.findViewById(R.id.iv_album);
            progressBar = view.findViewById(R.id.progressBar);

            view.setOnClickListener(v -> {
                Share.lst_album_image.clear();
                List<String> photoUris = albumList.get(getAdapterPosition())
                        .getAlbumPhotos()
                        .stream()
                        .map(PhonePhoto::getPhotoUri)
                        .collect(Collectors.toList());

                Collections.reverse(photoUris);
                Share.lst_album_image.addAll(new LinkedHashSet<>(photoUris)); // Remove duplicates

                Intent intent = new Intent(context, AlbumListActivity.class);
                intent.putExtra(Share.KEYNAME.ALBUM_NAME, albumList.get(getAdapterPosition()).getName());
                intent.putExtra("size", size);
                intent.putExtra("effect_position", position);
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(albumList.get(position).getCoverUri())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivAlbum);

        holder.tvAlbumName.setText(albumList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
