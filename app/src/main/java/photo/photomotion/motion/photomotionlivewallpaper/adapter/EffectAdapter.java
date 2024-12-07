package photo.photomotion.motion.photomotionlivewallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

import photo.photomotion.motion.photomotionlivewallpaper.R;
import photo.photomotion.motion.photomotionlivewallpaper.callback.OnClickListener;
import photo.photomotion.motion.photomotionlivewallpaper.model.EffectData;
import photo.photomotion.motion.photomotionlivewallpaper.utils.OnSingleClickListener;

public class EffectAdapter extends Adapter<EffectAdapter.ViewHolder> {
    public int checkedPosition = 0;
    public Bitmap moBaseImage;
    public Context moContext;
    public List<EffectData> moEffectList;
    public OnClickListener moOnClickListner;
    int position = 0;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public ConstraintLayout loClItemMain;
        public ImageView loIvEffect;
        public ImageView loIvEffectBase;

        public ViewHolder(View view) {
            super(view);
            loClItemMain =  view.findViewById(R.id.CvMain);
            loIvEffect =  view.findViewById(R.id.iv_effect);
            loIvEffectBase =  view.findViewById(R.id.iv_effect_base);
        }
    }

    public EffectAdapter(Context context, List<EffectData> list, Bitmap bitmap,int position, OnClickListener onClickListner) {
        this.moEffectList = list;
        this.moContext = context;
        this.moBaseImage = bitmap;
        this.position = position;
        this.moOnClickListner = onClickListner;
    }

    public int getItemCount() {
        return this.moEffectList.size();
    }

    public EffectData getSelectedEffect() {
        return (EffectData) this.moEffectList.get(this.checkedPosition);
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.loIvEffectBase.setImageBitmap(this.moBaseImage);
        viewHolder.loIvEffect.setImageDrawable(this.moContext.getResources().getDrawable(((EffectData) this.moEffectList.get(i)).getEffectThumb()));
        int i2 = this.checkedPosition;
        if (i2 == -1) {
            viewHolder.loClItemMain.setBackground(null);
        } else if (i2 == i) {
            viewHolder.loClItemMain.setBackground(this.moContext.getResources().getDrawable(R.drawable.bg_effect));
        } else {
            viewHolder.loClItemMain.setBackground(null);
        }
        if (position != 0){
            viewHolder.loClItemMain.setBackground(moContext.getResources().getDrawable(R.drawable.bg_effect));
            if (checkedPosition != position) {
                EffectAdapter effectAdapter = EffectAdapter.this;
                effectAdapter.notifyItemChanged(effectAdapter.checkedPosition);
                checkedPosition = position;
                moOnClickListner.onClick(moEffectList.get(position), position);
            }
        }

        viewHolder.loClItemMain.setOnClickListener(new OnSingleClickListener() {
            public void onSingleClick(View view) {
                viewHolder.loClItemMain.setBackground(moContext.getResources().getDrawable(R.drawable.bg_effect));
                if (checkedPosition != i) {
                    EffectAdapter effectAdapter = EffectAdapter.this;
                    effectAdapter.notifyItemChanged(effectAdapter.checkedPosition);
                    checkedPosition = i;
                    moOnClickListner.onClick(moEffectList.get(i), i);
                }
            }
        });
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_effect, viewGroup, false));
    }
}
