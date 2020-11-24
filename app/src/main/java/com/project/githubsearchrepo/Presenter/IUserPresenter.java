package com.project.githubsearchrepo.Presenter;

public interface IUserPresenter{
    void getUserDetail(String userId);
    void getUserRepositories(String userName, int pageIndex);
}
