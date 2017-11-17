package com.alexdev.photomap.ui.photo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;
import com.alexdev.photomap.ui.user.UserDetailsActivity;
import com.alexdev.photomap.utils.UiUtils;
import com.alexdev.photomap.utils.Utils;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO = "extra_photo";
    public static final String EXTRA_USER = "extra_user";
    private static final int PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE = 12345;

    @BindView(R.id.bottom_buttons_container)
    ViewGroup mBottomButtons;
    @BindView(R.id.photo_view)
    PhotoView mPhotoView;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.like_button)
    ImageButton mLikeButton;
    @BindView(R.id.user_info_button)
    ImageButton mUserInfoBtn;
    @BindView(R.id.download_button)
    ImageButton mDownloadBtn;
    private Photo mPhoto;
    private User mUser;
    private boolean mIsBottomContainerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);

        mPhoto = getIntent().getParcelableExtra(EXTRA_PHOTO);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);

        if (mPhoto != null) {
            Picasso.with(this)
                    .load(mPhoto.getUrl())
                    .error(R.drawable.ic_error_outline_grey_24dp)
                    .into(mPhotoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
            UiUtils.switchDrawableTint(mLikeButton.getDrawable(), this,
                    mPhoto.getIsInFavorites(), R.color.colorIconGrey, R.color.colorIconRed);
            mLikeButton.setOnClickListener(v -> onLikeBtnClick());
            mUserInfoBtn.setOnClickListener(v -> openUserDetails());
            mDownloadBtn.setOnClickListener(v -> downloadPhoto());
            mPhotoView.setOnPhotoTapListener((view, x, y) -> handleBottomContainerDisplay());
            mIsBottomContainerShown = true;
        } else {
            mPhotoView.setImageResource(R.drawable.ic_error_outline_grey_24dp);
        }
    }

    private void onLikeBtnClick() {
        //todo save or delete this data to db; load user data firstly before saving
        mPhoto.setIsInFavorites(!mPhoto.getIsInFavorites());
        UiUtils.switchDrawableTint(mLikeButton.getDrawable(), this,
                mPhoto.getIsInFavorites(), R.color.colorIconGrey, R.color.colorIconRed);
    }

    private void openUserDetails() {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        if (mUser == null) {
            intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, mPhoto.getOwnerId());
        } else {
            intent.putExtra(UserDetailsActivity.EXTRA_USER, mUser);
        }
        startActivity(intent);
    }

    private void downloadPhoto() {
        boolean isPermissionGranted = Utils.checkAndRequestPermissions(this,
                PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!isPermissionGranted) return;

        //todo make it rx
        File savedFile = null;
        try {
            savedFile = Utils.saveDrawableToFile(this, mPhotoView.getDrawable(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedFile != null) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(savedFile));
            sendBroadcast(intent);
        }

        Toast.makeText(
                this,
                savedFile != null ? R.string.toast_download_success : R.string.toast_download_error,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void handleBottomContainerDisplay() {
        if (mIsBottomContainerShown) {
            mBottomButtons.setVisibility(View.GONE);
        } else {
            mBottomButtons.setVisibility(View.VISIBLE);
        }
        mIsBottomContainerShown = !mIsBottomContainerShown;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadPhoto();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
