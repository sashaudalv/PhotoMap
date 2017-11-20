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

import com.alexdev.photomap.App;
import com.alexdev.photomap.R;
import com.alexdev.photomap.database.DatabaseManager;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;
import com.alexdev.photomap.network.NetworkManager;
import com.alexdev.photomap.network.callbacks.UserLoadListener;
import com.alexdev.photomap.ui.user.UserDetailsActivity;
import com.alexdev.photomap.utils.UiUtils;
import com.alexdev.photomap.utils.Utils;
import com.alexdev.photomap.utils.exceptions.DirectoryCreationNotPermittedException;
import com.alexdev.photomap.utils.exceptions.ThisDrawableNotSupportedException;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoViewerActivity extends AppCompatActivity implements UserLoadListener {

    public static final String EXTRA_PHOTO = "extra_photo";
    public static final String EXTRA_USER = "extra_user";

    private static final int PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE = 333;

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

    @Inject
    NetworkManager mNetworkManager;
    @Inject
    DatabaseManager mDatabaseManager;

    private boolean mIsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);

        App.get(getApplicationContext()).getAppComponent().inject(this);

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
        if (mPhoto.getIsInFavorites()) {
            mDatabaseManager.deleteFromFavorites(mUser, mPhoto);
            handleLikeButton();
        } else if (mUser == null) {
            if (mPhoto.getOwnerSocialId() < 0) {
                handleGroupPhotoOwner();
                return;
            }
            mNetworkManager.loadUser(mPhoto.getOwnerSocialId(), this);
        } else {
            mDatabaseManager.saveToFavorites(mUser, mPhoto);
            handleLikeButton();
        }
    }

    private void handleLikeButton() {
        mPhoto.setIsInFavorites(!mPhoto.getIsInFavorites());
        UiUtils.switchDrawableTint(mLikeButton.getDrawable(), this,
                mPhoto.getIsInFavorites(), R.color.colorIconGrey, R.color.colorIconRed);
    }

    @Override
    public void onUserLoadComplete(User user) {
        mUser = user;
        mDatabaseManager.saveToFavorites(mUser, mPhoto);
        handleLikeButton();
    }

    @Override
    public void onUserLoadError() {
        Toast.makeText(this, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isListenerVisible() {
        return mIsVisible;
    }

    private void openUserDetails() {
        if (mPhoto.getOwnerSocialId() < 0) {
            handleGroupPhotoOwner();
            return;
        }
        Intent intent = new Intent(this, UserDetailsActivity.class);
        if (mUser == null) {
            intent.putExtra(UserDetailsActivity.EXTRA_USER_SOCIAL_ID, mPhoto.getOwnerSocialId());
        } else {
            intent.putExtra(UserDetailsActivity.EXTRA_USER, mUser);
        }
        startActivity(intent);
    }

    private void handleGroupPhotoOwner() {
        Toast.makeText(this, R.string.toast_groups_not_supported, Toast.LENGTH_SHORT).show();
    }

    private void downloadPhoto() {
        boolean isPermissionGranted = Utils.checkAndRequestPermissions(this,
                PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!isPermissionGranted) return;

        Observable.defer(() -> {
            try {
                return Observable.just(Utils.saveDrawableToFile(this, mPhotoView.getDrawable(), false));
            } catch (IOException | ThisDrawableNotSupportedException | DirectoryCreationNotPermittedException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        savedFile -> {
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            intent.setData(Uri.fromFile(savedFile));
                            sendBroadcast(intent);

                            Toast.makeText(
                                    this,
                                    R.string.toast_download_success,
                                    Toast.LENGTH_SHORT
                            ).show();
                        },
                        error -> Toast.makeText(
                                this,
                                R.string.toast_download_error,
                                Toast.LENGTH_SHORT).show()
                );
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

    @Override
    protected void onResume() {
        super.onResume();
        mIsVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsVisible = false;
    }
}
