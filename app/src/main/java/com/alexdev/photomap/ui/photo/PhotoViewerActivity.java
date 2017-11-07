package com.alexdev.photomap.ui.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.ui.user.UserDetailsActivity;

public class PhotoViewerActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO = "extra_photo";

    private Photo mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        mPhoto = getIntent().getParcelableExtra(EXTRA_PHOTO);
    }

    private void openUserDetails() {
        if (mPhoto != null) {
            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.EXTRA_USER_ID, mPhoto.getOwnerId());
            startActivity(intent);
        }
    }

}
