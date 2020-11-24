package com.project.githubsearchrepo.Tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.project.githubsearchrepo.R;

public class Utils {

    public static final String API_URL = "https://api.github.com/";
    public static final String SEARCH_REPO_URL = API_URL + "search/repositories";
    public static final String USER_DETAIL_URL = API_URL + "users/";
    public static final String REPO_DETAIL_URL = API_URL + "repos/";


    public void hideSoftKeyboard(Context context, View edittext) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE
        );
        imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    }

    public String nullControlFromString(Context context, String txt) {
        return txt != null && !TextUtils.isEmpty(txt) ? txt : context.getString(R.string.notDefinedText);
    }
}
