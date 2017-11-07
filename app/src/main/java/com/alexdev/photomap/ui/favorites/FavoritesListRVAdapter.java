package com.alexdev.photomap.ui.favorites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesListRVAdapter extends RecyclerView.Adapter<FavoritesListRVAdapter.ViewHolder> {

    private final List<Pair<User, Photo>> mData;
    private final OnItemClickListener mListener;
    private final Context mContext;

    public FavoritesListRVAdapter(List<Pair<User, Photo>> data, Context context, OnItemClickListener listener) {
        mData = data;
        mContext = context;
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemPhotoClick(int position);

        void onItemUserClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_textview)
        TextView nameTextView;
        @BindView(R.id.avatar_image)
        ImageView avatarView;
        @BindView(R.id.date_textview)
        TextView dateTextView;
        @BindView(R.id.photo_image)
        ImageView photoView;
        @BindView(R.id.photo_text_textview)
        TextView photoText;
        @BindView(R.id.like_button)
        ImageButton likeButton;
        @BindView(R.id.share_button)
        ImageButton shareButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorites_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User itemUser = mData.get(position).first;
        Photo itemPhoto = mData.get(position).second;
        holder.avatarView.setOnClickListener(v -> mListener.onItemUserClick(position));
        holder.nameTextView.setText(itemUser.getName());
        holder.dateTextView.setText(
                SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                        .format(new Date(itemPhoto.getDate()))
        );
        holder.photoView.setOnClickListener(v -> mListener.onItemPhotoClick(position));
        if (itemPhoto.getText() == null) {
            holder.photoText.setVisibility(View.GONE);
        } else {
            holder.photoText.setText(itemPhoto.getText());
        }
        switchDrawableTint(holder.likeButton.getDrawable(), mContext, itemPhoto.getIsInFavorites(),
                R.color.colorIconGrey, R.color.colorIconRed);
        holder.likeButton.setOnClickListener(v -> {
            itemPhoto.setIsInFavorites(!itemPhoto.getIsInFavorites());
            switchDrawableTint(
                    holder.likeButton.getDrawable(),
                    mContext,
                    itemPhoto.getIsInFavorites(),
                    R.color.colorIconGrey,
                    R.color.colorIconRed
            );
        });
    }

    private void switchDrawableTint(Drawable drawable, Context context, boolean switcher, int colorResId1, int colorResId2) {
        DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(context, switcher ? colorResId2 : colorResId1));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
