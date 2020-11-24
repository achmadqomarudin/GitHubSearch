package com.project.githubsearchrepo.Model;

import java.util.List;

public class GeneralRepository {

    private int total_count;
    private List<Repository> items;

    public GeneralRepository() {
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<Repository> getItems() {
        return items;
    }

    public void setItems(List<Repository> items) {
        this.items = items;
    }
}
