package com.shyk.alena.booksapp.signIn;

import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;
import com.shyk.alena.booksapp.base.BasePresenter;
import com.shyk.alena.booksapp.base.BaseView;

class GoogleSignInContract {
    interface Model {
    }

    interface View extends BaseView {
        void updateUI(FirebaseUser user);

        void authFailed();

        void onClick(android.view.View v);

        void startActivityForResult(Intent intent);
    }

    interface Presenter extends BasePresenter<View> {
        void initializeAuth(String clientId);

        void startActivity();

        void signOut();

        void revokeAccess();

        void signIn(Intent data);
    }
}
