package com.alexdev.photomap.ui.favorites;


import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexdev.photomap.App;
import com.alexdev.photomap.R;
import com.alexdev.photomap.database.DatabaseManager;
import com.alexdev.photomap.database.callbacks.FavoritesLoadListener;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.models.User;
import com.alexdev.photomap.ui.interfaces.ReselectableFragment;
import com.alexdev.photomap.ui.photo.PhotoViewerActivity;
import com.alexdev.photomap.ui.user.UserDetailsActivity;
import com.alexdev.photomap.utils.Utils;
import com.alexdev.photomap.utils.exceptions.DirectoryCreationNotPermittedException;
import com.alexdev.photomap.utils.exceptions.ThisDrawableNotSupportedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class FavoritesFragment extends Fragment implements ReselectableFragment,
        FavoritesListRVAdapter.OnItemClickListener, FavoritesLoadListener {

    private static final int PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE = 222;


    @BindView(R.id.favorites_rv)
    RecyclerView mFavoritesRV;
    @BindView(R.id.empty_list_hint)
    ViewGroup mEmptyListHint;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LayoutManager mLayoutManager;
    private Adapter mAdapter;
    private final List<Pair<User, Photo>> mFavoritesList = new ArrayList<>();

    @Inject
    DatabaseManager mDatabaseManager;


    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getActivity().getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

        mLayoutManager = new StaggeredGridLayoutManager(
                getResources().getInteger(
                        getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                                R.integer.portrait_grid_span_count :
                                R.integer.landscape_grid_span_count
                ),
                StaggeredGridLayoutManager.VERTICAL
        );
        mFavoritesRV.setLayoutManager(mLayoutManager);
        mAdapter = new FavoritesListRVAdapter(mFavoritesList, getContext(), this);
        mFavoritesRV.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this::onRefresh);
        mSwipeRefreshLayout.setRefreshing(true);
        mDatabaseManager.getFavorites(this);
        return view;
    }

    private void showEmptyListHint(boolean shouldShow) {
        mEmptyListHint.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    private void onRefresh() {
        mDatabaseManager.getFavorites(this);
    }

    @Override
    public void onFavoritesLoadComplete(List<Pair<User, Photo>> favoriteList) {
        mFavoritesList.clear();
        mFavoritesList.addAll(favoriteList);
        mAdapter.notifyDataSetChanged();
        if (isVisible()) {
            mSwipeRefreshLayout.setRefreshing(false);
            showEmptyListHint(mFavoritesList.isEmpty());
        }
    }

    @Override
    public boolean isListenerVisible() {
        return isVisible();
    }

    @Override
    public void onFragmentReselected() {
        if (!mFavoritesList.isEmpty()) mLayoutManager.scrollToPosition(0);
    }

    @Override
    public void onItemPhotoClick(int position) {
        Intent intent = new Intent(getContext(), PhotoViewerActivity.class);
        intent.putExtra(PhotoViewerActivity.EXTRA_PHOTO, mFavoritesList.get(position).second);
        intent.putExtra(PhotoViewerActivity.EXTRA_USER, mFavoritesList.get(position).first);
        startActivity(intent);
    }

    @Override
    public void onItemUserClick(int position) {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.EXTRA_USER, mFavoritesList.get(position).first);
        startActivity(intent);
    }

    @Override
    public void onItemLikeClick(int position, boolean shouldRemoveData) {
        if (shouldRemoveData) {
            mDatabaseManager.deleteFromFavorites(mFavoritesList.get(position));
        } else {
            mDatabaseManager.saveToFavorites(mFavoritesList.get(position));
        }
    }

    @Override
    public void onItemShareClick(int position, Drawable sharingDrawable) {
        boolean isPermissionGranted = Utils.checkAndRequestPermissions(this,
                PERMISSION_REQUEST_ACCESS_WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!isPermissionGranted) return;

        User itemUser = mFavoritesList.get(position).first;
        Photo itemPhoto = mFavoritesList.get(position).second;
        String sharingText;
        if (itemPhoto.getText().isEmpty()) {
            sharingText = String.format(Locale.getDefault(),
                    getString(R.string.template_sharing_photo_text),
                    itemPhoto.getUrl(), itemUser.getUrl());
        } else {
            sharingText = String.format(Locale.getDefault(),
                    getString(R.string.template_sharing_photo_text_with_description),
                    itemPhoto.getUrl(), itemPhoto.getText(), itemUser.getUrl());
        }

        Observable.defer(() -> {
            try {
                return Observable.just(Utils.saveDrawableToFile(getContext(), sharingDrawable, true));
            } catch (IOException | ThisDrawableNotSupportedException | DirectoryCreationNotPermittedException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(savedFile -> {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getContext(),
                                    getString(R.string.file_provider_authority), savedFile));
                            sharingIntent.setType("image/jpeg");
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(sharingIntent, getString(R.string.chooser_title_share_photo)));
                        },
                        error -> Toast.makeText(
                                getContext(),
                                R.string.toast_download_error,
                                Toast.LENGTH_SHORT).show()
                );
    }

}
