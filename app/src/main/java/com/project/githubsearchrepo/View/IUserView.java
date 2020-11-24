package com.project.githubsearchrepo.View;

import com.project.githubsearchrepo.Model.Repository;
import com.project.githubsearchrepo.Model.User;

import java.util.List;

public interface IUserView {
    void getUserDetailResult(User user);
    void getUserRepositories(List<Repository> repositories);
}
