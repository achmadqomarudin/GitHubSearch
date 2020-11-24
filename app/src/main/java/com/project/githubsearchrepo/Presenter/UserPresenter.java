package com.project.githubsearchrepo.Presenter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.Model.User;
import com.project.githubsearchrepo.R;
import com.project.githubsearchrepo.Tools.Utils;
import com.project.githubsearchrepo.View.IUserView;

import java.lang.reflect.Type;
import java.util.List;

public class UserPresenter implements IUserPresenter {

    private IUserView userView;
    private RequestQueue queue;
    private Context context;

    public UserPresenter(Context context, IUserView userView) {
        queue = Volley.newRequestQueue(context);
        this.userView = userView;
        this.context = context;
    }

    @Override
    public void getUserDetail(String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.USER_DETAIL_URL.concat(userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        userView.getUserDetailResult(user);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userView.getUserDetailResult(null);
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void getUserRepositories(String userName, int pageIndex) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.API_URL.concat(context.getString(R.string.userRepositoriesUrlParameters, userName, String.valueOf(pageIndex))), //"users/" + userName + "/repos?per_page=5&page=" + pageIndex
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Repository>>() {
                        }.getType();
                        List<Repository> repositories = gson.fromJson(response, listType);
                        userView.getUserRepositories(repositories);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userView.getUserRepositories(null);
            }
        });
        queue.add(stringRequest);
    }
}
