package photo.photomotion.motion.photomotionlivewallpaper.activity.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.activity.GalleryListActivity;
import photo.photomotion.motion.photomotionlivewallpaper.callback.OnClickListener;
import photo.photomotion.motion.photomotionlivewallpaper.model.EffectData;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.EffectViewHolder> {

    private Context context;
    private List<EffectData> effectList;
    public Bitmap moBaseImage;
    public OnClickListener moOnClickListner;

    public ImageAdapter(Context context, List<EffectData> effectList, Bitmap bitmap, OnClickListener onClickListner) {
        this.context = context;
        this.effectList = effectList;
        this.moBaseImage = bitmap;
        this.moOnClickListner = onClickListner;
    }

    @NonNull
    @Override
    public EffectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item_image, parent, false);
        return new EffectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EffectViewHolder holder, int position) {
        EffectData effectData = effectList.get(position);

        holder.itemView.setOnClickListener(view -> {
            startSizeActivity(position);
        });
        holder.bind(effectData, position);
    }
    public void startSizeActivity(int position){
        Intent intent = new Intent(context, GalleryListActivity.class);
        intent.putExtra("effect_position", position);
        context.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        return effectList.size();
    }

    class EffectViewHolder extends RecyclerView.ViewHolder {

        private ImageView effectThumb, effectSet;
        private TextView textView;

        public EffectViewHolder(@NonNull View itemView) {
            super(itemView);
            effectThumb = itemView.findViewById(R.id.effectThumb);
            effectSet = itemView.findViewById(R.id.effectSet);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(EffectData effectData, int position) {
            textView.setText("Filter " + position);

            // Rounded Corners Image loading using Glide
            effectThumb.setImageBitmap(moBaseImage);
            Glide.with(itemView.getContext())
                    .load(effectData.effectThumb)  // Use local resource ID for image
                    .transform(new CenterCrop(), new RoundedCorners(16))  // Apply rounded corners
                    .into(effectSet);
        }
    }
}