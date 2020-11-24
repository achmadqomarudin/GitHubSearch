package com.project.githubsearchrepo.Presenter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.githubsearchrepo.Model.GeneralRepository;
import com.project.githubsearchrepo.R;
import com.project.githubsearchrepo.Tools.Utils;
import com.project.githubsearchrepo.View.IGeneralRepositoryView;

public class GeneralRepositoryPresenter implements IGeneralRepositoryPresenter {

    private IGeneralRepositoryView repositoryView;
    private RequestQueue queue;
    private Context context;

    public GeneralRepositoryPresenter(Context context, IGeneralRepositoryView repositoryView) {
        queue = Volley.newRequestQueue(context);
        this.repositoryView = repositoryView;
        this.context = context;
    }

    @Override
    public void getRepositories(final String keyword, final int pageIndex) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.SEARCH_REPO_URL.concat(context.getString(R.string.searchRepositoriesUrlParameters, keyword, String.valueOf(pageIndex))),//"?q=" + keyword + "&per_page=5" + "&page=" + pageIndex
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GeneralRepository generalRepository = gson.fromJson(response, GeneralRepository.class);
                        repositoryView.getRepositoriesResult(generalRepository);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                repositoryView.getRepositoriesResult(null);
            }
        });
        queue.add(stringRequest);
    }
}
