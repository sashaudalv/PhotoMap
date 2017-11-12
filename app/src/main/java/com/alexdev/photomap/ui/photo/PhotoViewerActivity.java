package com.alexdev.photomap.ui.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.ui.user.UserDetailsActivity;
import com.alexdev.photomap.utils.UiUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO = "extra_photo";

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
    private boolean mIsBottomContainerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);

        mPhoto = getIntent().getParcelableExtra(EXTRA_PHOTO);

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
        intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, mPhoto.getOwnerId());
        startActivity(intent);
    }

    private void downloadPhoto() {
        //todo make it rx
        //todo show runtime permission dialog else return if no permission

        if (!(mPhotoView.getDrawable() instanceof BitmapDrawable)) return;

        BitmapDrawable bitmapDrawable = (BitmapDrawable) mPhotoView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        File storageDirectory = Environment.getExternalStorageDirectory();
        File appDirectory = new File(storageDirectory.getAbsolutePath() + "/" + getString(R.string.app_name));
        if (!appDirectory.exists()) {
            if (!appDirectory.mkdirs()) {
                return;
            }
        }
        String fileName = String.format(Locale.getDefault(), "%d.jpg", System.currentTimeMillis());
        File outFile = new File(appDirectory, fileName);
        boolean isSuccessfulDownload = false;
        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    getResources().getInteger(R.integer.picture_downloading_quality),
                    fileOutputStream
            );
            fileOutputStream.flush();
            isSuccessfulDownload = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isSuccessfulDownload) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(outFile));
            sendBroadcast(intent);
        }

        Toast.makeText(
                this,
                isSuccessfulDownload ? R.string.toast_download_success : R.string.toast_download_error,
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
