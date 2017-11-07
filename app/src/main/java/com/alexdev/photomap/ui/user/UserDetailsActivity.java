package com.alexdev.photomap.ui.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.User;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "extra_user";
    public static final String EXTRA_USER_ID = "extra_user_id";

    private User mUser;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mUserId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        mUser = getIntent().getParcelableExtra(EXTRA_USER);
        if (mUser != null) mUserId = mUser.getId();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "User ID is " + mUserId, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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
