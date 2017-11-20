package com.alexdev.photomap.ui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ImageMapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public ImageMapInfoWindowAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View contents = mLayoutInflater.inflate(R.layout.info_window_image_map, null);
        ImageView imageView = contents.findViewById(R.id.photo_shortcut);
        Photo photo = (Photo) marker.getTag();
        if (photo != null) {
            Picasso.with(mContext)
                    .load(photo.getUrl())
                    .resize(100, 100)
                    .centerCrop()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (marker.isInfoWindowShown()) {
                                marker.hideInfoWindow();
                                marker.showInfoWindow();
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        return contents;
    }

}

