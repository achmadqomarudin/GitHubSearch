package com.project.githubsearchrepo.Presenter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.Tools.Utils;
import com.project.githubsearchrepo.View.IRepositoryView;

public class RepositoryPresenter implements IRepositoryPresenter {

    private IRepositoryView repositoryView;
    private RequestQueue queue;

    public RepositoryPresenter(Context context, IRepositoryView repositoryView) {
        queue = Volley.newRequestQueue(context);
        this.repositoryView = repositoryView;
    }

    @Override
    public void getRepository(String repoFullName) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.REPO_DETAIL_URL.concat(repoFullName),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Repository repository = gson.fromJson(response, Repository.class);
                        repositoryView.getRepositoryResult(repository);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                repositoryView.getRepositoryResult(null);
            }
        });
        queue.add(stringRequest);
    }
}
