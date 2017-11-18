package com.alexdev.photomap.ui.user;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_USER_SOCIAL_ID = "extra_user_social_id";

    private static final String VK_APP_PACKAGE_ID = "com.vkontakte.android";

    private User mUser;
    private int mUserId;
    @BindView(R.id.layout_container)
    ViewGroup mLayoutContainer;
    @BindView(R.id.avatar_image)
    ImageView mAvatarImage;
    @BindView(R.id.name_text_view)
    TextView mNameTextView;
    @BindView(R.id.open_vk_fab)
    FloatingActionButton mVkOpenButton;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mUserId = getIntent().getIntExtra(EXTRA_USER_SOCIAL_ID, -1);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);

        if (mUser == null) {
            mLayoutContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            //TODO loadUser
        } else {
            initViews();
        }
    }

    private void initViews() {
        Picasso.with(this)
                .load(mUser.getAvatar())
                .error(R.drawable.ic_account_circle_blue_40dp)
                .into(mAvatarImage);

        mNameTextView.setText(mUser.getName());
        mVkOpenButton.setOnClickListener(view -> openUserLink(mUser.getUrl()));
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

    private void openUserLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo info : resInfo) {
            if (info.activityInfo == null) continue;
            if (VK_APP_PACKAGE_ID.equals(info.activityInfo.packageName)) {
                intent.setPackage(info.activityInfo.packageName);
                break;
            }
        }

        startActivity(intent);
    }

}
