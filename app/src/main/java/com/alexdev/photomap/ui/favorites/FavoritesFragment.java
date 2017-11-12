package com.alexdev.photomap.ui.favorites;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;
import com.alexdev.photomap.ui.interfaces.ReselectableFragment;
import com.alexdev.photomap.ui.photo.PhotoViewerActivity;
import com.alexdev.photomap.ui.user.UserDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesFragment extends Fragment implements ReselectableFragment, FavoritesListRVAdapter.OnItemClickListener {

    @BindView(R.id.favorites_rv)
    RecyclerView mFavoritesRV;
    @BindView(R.id.empty_list_hint)
    ViewGroup mEmptyListHint;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LayoutManager mLayoutManager;
    private Adapter mAdapter;
    private List<Pair<User, Photo>> mFavoritesList = new ArrayList<>();

    {
        mFavoritesList.add(new Pair<>(new User(1, "pavel", "durov", "https://i.pinimg.com/originals/7c/c7/a6/7cc7a630624d20f7797cb4c8e93c09c1.png"),
                new Photo(1, "https://b1.filmpro.ru/c/455897.700xp.jpg", 1, "Lorem ipsum dolor sit amet", 1509980225786L, 0L, true)));
        mFavoritesList.add(new Pair<>(new User(150150, "nik", "safronov", "https://i.pinimg.com/originals/7c/c7/a6/7cc7a630624d20f7797cb4c8e93c09c1.png"),
                new Photo(2, "https://b1.filmpro.ru/c/455897.700xp.jpg", 150150, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit", 1509980225786L, 0L, true)));
        mFavoritesList.add(new Pair<>(new User(1, "pavel", "durov", "https://i.pinimg.com/originals/7c/c7/a6/7cc7a630624d20f7797cb4c8e93c09c1.png"),
                new Photo(1, "https://b1.filmpro.ru/c/45587.700xp.jpg", 1, "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium", 1509980225786L, 0L, true)));
        mFavoritesList.add(new Pair<>(new User(150150, "nik", "safronov", "https://i.pinimg.com/originals/7c/c7/a6/7cc7a630624d20f7797cb4c8e93c09c1.png"),
                new Photo(2, "https://b1.filmpro.ru/c/45897.700xp.jpg", 150150, null, 1509980225786L, 0L, true)));
    }

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new LinearLayoutManager(getContext());
        } else {
            mLayoutManager = new StaggeredGridLayoutManager(
                    getResources().getInteger(R.integer.landscape_grid_span_count),
                    StaggeredGridLayoutManager.VERTICAL
            );
        }
        mFavoritesRV.setLayoutManager(mLayoutManager);
        mAdapter = new FavoritesListRVAdapter(mFavoritesList, getContext(), this);
        mFavoritesRV.setAdapter(mAdapter);
        if (mFavoritesList.isEmpty()) showEmptyListHint();
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        return view;
    }

    private void showEmptyListHint() {
        mFavoritesRV.setVisibility(View.GONE);
        mEmptyListHint.setVisibility(View.VISIBLE);
    }

    private void onRefresh() {
        //TODO try to upload data from db
    }

    @Override
    public void onFragmentReselected() {
        if (!mFavoritesList.isEmpty()) mLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onItemPhotoClick(int position) {
        Intent intent = new Intent(getContext(), PhotoViewerActivity.class);
        intent.putExtra(PhotoViewerActivity.EXTRA_PHOTO, mFavoritesList.get(position).second);
        startActivity(intent);
    }

    @Override
    public void onItemUserClick(int position) {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.EXTRA_USER, mFavoritesList.get(position).first);
        startActivity(intent);
    }
}
