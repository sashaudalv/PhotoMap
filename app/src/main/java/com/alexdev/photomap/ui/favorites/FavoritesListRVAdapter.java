package com.alexdev.photomap.ui.favorites;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.alexdev.photomap.utils.UiUtils;
import com.squareup.picasso.Picasso;

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

        void onItemLikeClick(int position, boolean shouldRemoveData);

        void onItemShareClick(int position, Drawable sharingDrawable);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header)
        ViewGroup header;
        @BindView(R.id.name_text_view)
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

        holder.header.setOnClickListener(v -> mListener.onItemUserClick(position));
        setAvatarView(holder.avatarView, itemUser);
        holder.nameTextView.setText(itemUser.getName());
        holder.dateTextView.setText(
                SimpleDateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                        .format(new Date(itemPhoto.getConvertedDate()))
        );
        setPhotoView(holder.photoView, itemPhoto, position);
        if (itemPhoto.getText().isEmpty()) {
            holder.photoText.setVisibility(View.GONE);
        } else {
            holder.photoText.setVisibility(View.VISIBLE);
            holder.photoText.setText(itemPhoto.getText());
        }
        setLikeButton(holder.likeButton, itemPhoto, position);
        holder.shareButton.setOnClickListener(v -> mListener.onItemShareClick(position,
                holder.photoView.getDrawable()));
    }

    private void setAvatarView(ImageView avatarView, User user) {
        Picasso.with(mContext)
                .load(user.getAvatar())
                .resize(100, 100)
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_blue_40dp)
                .error(R.drawable.ic_account_circle_blue_40dp)
                .into(avatarView);
    }

    private void setPhotoView(ImageView photoView, Photo photo, int position) {
        Picasso.with(mContext)
                .load(photo.getUrl())
                .error(R.drawable.ic_error_outline_grey_24dp)
                .into(photoView);
        photoView.setOnClickListener(v -> mListener.onItemPhotoClick(position));
    }

    private void setLikeButton(ImageButton likeButton, Photo photo, int position) {
        UiUtils.switchDrawableTint(likeButton.getDrawable(), mContext, photo.getIsInFavorites(),
                R.color.colorIconGrey, R.color.colorIconRed);
        likeButton.setOnClickListener(v -> {
            photo.setIsInFavorites(!photo.getIsInFavorites());
            UiUtils.switchDrawableTint(likeButton.getDrawable(), mContext, photo.getIsInFavorites(),
                    R.color.colorIconGrey, R.color.colorIconRed);
            mListener.onItemLikeClick(position, !photo.getIsInFavorites());
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
