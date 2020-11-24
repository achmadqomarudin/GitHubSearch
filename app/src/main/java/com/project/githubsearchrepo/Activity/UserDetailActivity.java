package com.project.githubsearchrepo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.githubsearchrepo.Adapter.AdapterRepository;
import com.project.githubsearchrepo.Listener.RecyclerViewItemClick;
import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.Model.User;
import com.project.githubsearchrepo.Presenter.IUserPresenter;
import com.project.githubsearchrepo.Presenter.UserPresenter;
import com.project.githubsearchrepo.R;
import com.project.githubsearchrepo.Tools.Utils;
import com.project.githubsearchrepo.View.IUserView;

import java.util.ArrayList;
import java.util.List;

import muyan.snacktoa.SnackToa;

public class UserDetailActivity extends AppCompatActivity implements IUserView {

    private ImageView ivUserImage;
    private TextView tvUserName;
    private TextView tvUserMail;
    private TextView tvUserFollowers;
    private TextView tvFollowing;
    private TextView tvUserRepos;

    private IUserPresenter presenter;

    private AdapterRepository adapterRepository;
    private List<Repository> repositories;
    private int totalRepositoryCount = 0;
    private int currentPageIndex = 1;

    private View llProgressLayout, llContentLayout, llLazyLoadProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        String userName = "";
        if (getIntent().getExtras() != null)
            userName = getIntent().getExtras().getString("userName");

        //Init Variable
        repositories = new ArrayList<>();
        adapterRepository = new AdapterRepository(this, repositories, new RecyclerViewItemClick() {
            @Override
            public void viewClickForUser(View view, Object item, int position) {
            }

            @Override
            public void viewClickForRepo(View view, Object item, int position) {
                Repository repository = (Repository) item;
                Intent intent = new Intent(UserDetailActivity.this, RepoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("repoFullName", repository.getFull_name());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Init presenter
        presenter = new UserPresenter(UserDetailActivity.this, this);

        //Init View
        ivUserImage = findViewById(R.id.ivUserImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserMail = findViewById(R.id.tvUserMail);
        tvUserFollowers = findViewById(R.id.tvUserFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        tvUserRepos = findViewById(R.id.tvUserRepos);
        llProgressLayout = findViewById(R.id.llProgressLayout);
        llContentLayout = findViewById(R.id.llContentLayout);
        llLazyLoadProgress = findViewById(R.id.llLazyLoadProgress);
        RecyclerView rvUserRepos = findViewById(R.id.rvUserRepos);

        //View Actions
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvUserRepos.setLayoutManager(mLayoutManager);
        rvUserRepos.setHasFixedSize(true);
        rvUserRepos.setAdapter(adapterRepository);
        rvUserRepos.setNestedScrollingEnabled(false);
        final String finalUserName = userName;
        final NestedScrollView mNestedScrollView = findViewById(R.id.nested_content);

        mNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = mNestedScrollView.getChildAt(mNestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (mNestedScrollView.getHeight() + mNestedScrollView
                        .getScrollY()));
                if (diff == 0) {
                    if (totalRepositoryCount != -1 && totalRepositoryCount > repositories.size()) {
                        llLazyLoadProgress.setVisibility(View.VISIBLE);
                        currentPageIndex++;
                        presenter.getUserRepositories(finalUserName, currentPageIndex);
                    }
                }
            }
        });

        presenter.getUserDetail(userName);
        presenter.getUserRepositories(finalUserName, currentPageIndex);
    }

    @Override
    public void getUserDetailResult(User user) {
        Glide.with(this).load(user.getAvatar_url()).into(ivUserImage);
        Utils utils = new Utils();
        tvUserName.setText(utils.nullControlFromString(this, user.getLogin()));
        tvUserMail.setText(utils.nullControlFromString(this, user.getEmail()));
        tvUserFollowers.setText(utils.nullControlFromString(this, String.valueOf(user.getFollowers())));
        tvFollowing.setText(utils.nullControlFromString(this, String.valueOf(user.getFollowing())));
        tvUserRepos.setText(utils.nullControlFromString(this, String.valueOf(user.getPublic_repos())));
        totalRepositoryCount = user.getPublic_repos();
        llContentLayout.setVisibility(View.VISIBLE);
        llProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void getUserRepositories(List<Repository> getRepositories) {
        if (getRepositories != null) {
            repositories.addAll(getRepositories);
            adapterRepository.notifyDataSetChanged();
            llLazyLoadProgress.setVisibility(View.GONE);
        } else {
            SnackToa.snackBarError(this, "Connection Error!");
        }
    }
}
