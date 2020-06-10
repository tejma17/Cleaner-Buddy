package com.example.garbagecleaner;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class slideradapter extends RecyclerView.Adapter<slideradapter.SliderViewHolder> {

    private List<slider> slideritem;
    ViewPager2 viewPager2;

    public slideradapter(List<slider> slideritem, ViewPager2 viewPager2) {
        this.slideritem = slideritem;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_images,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(slideritem.get(position));
        if(position == slideritem.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return slideritem.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageview;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(slider slide){
            imageview.setImageResource(slide.getImage());

        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            slideritem.addAll(slideritem);
            notifyDataSetChanged();
        }
    };

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
