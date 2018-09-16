package com.shyk.alena.booksapp.base;

public interface BasePresenter<T extends BaseView> {
    void destroy();

    void onResult(boolean visibility);
}
