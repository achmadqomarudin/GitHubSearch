package com.project.githubsearchrepo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.project.githubsearchrepo.Adapter.AdapterRepository;
import com.project.githubsearchrepo.Listener.EndlessRecyclerViewScrollListener;
import com.project.githubsearchrepo.Listener.RecyclerViewItemClick;
import com.project.githubsearchrepo.Model.GeneralRepository;
import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.Model.User;
import com.project.githubsearchrepo.Presenter.GeneralRepositoryPresenter;
import com.project.githubsearchrepo.Presenter.IGeneralRepositoryPresenter;
import com.project.githubsearchrepo.R;
import com.project.githubsearchrepo.Tools.Utils;
import com.project.githubsearchrepo.View.IGeneralRepositoryView;

import java.util.ArrayList;
import java.util.List;

import muyan.snacktoa.SnackToa;

public class MainActivity extends AppCompatActivity implements IGeneralRepositoryView {

    private TextInputEditText etSearchRepository;
    private View rlEmptyLayout, llProgressLayout, llLazyLoadProgress;
    private RecyclerView rvRepos;
    private FloatingActionButton fabRecyclerScroolTop;
    private ImageView imageOpenGithub;

    private AdapterRepository adapterRepository;
    private List<Repository> repositories;
    private int totalRepositoryCount = 0;
    private int currentPageIndex = 1;

    private String keyword = "";

    private IGeneralRepositoryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repo);

        setViews();
        initViews();
    }

    private void setViews() {
        imageOpenGithub     = findViewById(R.id.imgOpenGithub);
        etSearchRepository  = findViewById(R.id.etSearchRepository);
        rlEmptyLayout       = findViewById(R.id.rlEmptyLayout);
        llProgressLayout    = findViewById(R.id.llProgressLayout);
        llLazyLoadProgress  = findViewById(R.id.llLazyLoadProgress);
        rvRepos             = findViewById(R.id.rvRepos);
    }

    private void initViews() {
        //Init Variable
        repositories = new ArrayList<>();
        adapterRepository = new AdapterRepository(MainActivity.this, repositories, new RecyclerViewItemClick() {

            @Override
            public void viewClickForUser(View view, Object item, int position) {
                User user = ((Repository) item).getOwner();
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userName", user.getLogin());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void viewClickForRepo(View view, Object item, int position) {
                Repository repository = (Repository) item;
                Intent intent = new Intent(MainActivity.this, RepoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("repoFullName", repository.getFull_name());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Init Presenter
        presenter = new GeneralRepositoryPresenter(MainActivity.this, this);


        //View Actions
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvRepos.setLayoutManager(manager);
        //rvRepos.setHasFixedSize(true);
        rvRepos.setAdapter(adapterRepository);
        rvRepos.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalRepositoryCount > 0 && totalRepositoryCount > repositories.size()) {
                    llLazyLoadProgress.setVisibility(View.VISIBLE);
                    currentPageIndex++;
                    presenter.getRepositories(keyword, currentPageIndex);
                }
            }
        });

        imageOpenGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://github.com/achmadqomarudin");
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Choose Browser"));
            }
        });

        fabRecyclerScroolTop = findViewById(R.id.fabRecyclerScroolTop);
        rvRepos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0)
                    fabRecyclerScroolTop.hide();
                else
                    fabRecyclerScroolTop.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        etSearchRepository.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    repositories.clear();
                    adapterRepository.notifyDataSetChanged();
                    currentPageIndex = 1;
                    totalRepositoryCount = 1;
                    keyword = etSearchRepository.getText().toString();
                    new Utils().hideSoftKeyboard(MainActivity.this, etSearchRepository);
                    llProgressLayout.setVisibility(View.VISIBLE);
                    rlEmptyLayout.setVisibility(View.GONE);
                    fabRecyclerScroolTop.hide();
                    presenter.getRepositories(keyword, 1);
                    return true;
                }
                return false;
            }
        });

        fabRecyclerScroolTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvRepos.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void getRepositoriesResult(GeneralRepository generalRepository) {
        llProgressLayout.setVisibility(View.GONE);
        llLazyLoadProgress.setVisibility(View.GONE);
        if (generalRepository != null) {
            totalRepositoryCount = generalRepository.getTotal_count();
            repositories.addAll(generalRepository.getItems());
            adapterRepository.notifyDataSetChanged();
            if (repositories.isEmpty()) {
                fabRecyclerScroolTop.hide();
                rvRepos.setVisibility(View.GONE);
                rlEmptyLayout.setVisibility(View.VISIBLE);
            } else {
                rvRepos.setVisibility(View.VISIBLE);
                rlEmptyLayout.setVisibility(View.GONE);
            }
        } else {
            if (repositories.isEmpty()) {
                fabRecyclerScroolTop.hide();
                rvRepos.setVisibility(View.GONE);
                rlEmptyLayout.setVisibility(View.VISIBLE);
            } else {
                rvRepos.setVisibility(View.VISIBLE);
                rlEmptyLayout.setVisibility(View.GONE);
            }
            SnackToa.snackBarError(this, "Connection Error!");
        }
    }
}
