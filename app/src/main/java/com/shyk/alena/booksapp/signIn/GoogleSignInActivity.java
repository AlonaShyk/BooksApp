package com.shyk.alena.booksapp.signIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.shyk.alena.booksapp.R;

import static com.shyk.alena.booksapp.signIn.GoogleSignInPresenter.RC_SIGN_IN;

public class GoogleSignInActivity extends AppCompatActivity implements GoogleSignInContract.View {
    public static final String TAG = "GoogleActivity";
    private TextView status;
    private TextView detail;
    private GoogleSignInPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        GoogleSignInContract.View view1 = this;
        presenter = new GoogleSignInPresenter(getBaseContext(), view1);
        status = findViewById(R.id.status);
        detail = findViewById(R.id.detail);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAction(view);
            }
        });
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAction(view);
            }
        });
        findViewById(R.id.disconnect_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onAction(view);
            }
        });
        presenter.initializeAuth(getString(R.string.default_client_id));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            presenter.signIn(data);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.sign_in_button:
                presenter.startActivity();
                break;
            case R.id.sign_out_button:
                presenter.signOut();
                break;
            case R.id.disconnect_button:
                presenter.revokeAccess();
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent) {
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            status.setText(getString(R.string.google_status_fmt, user.getEmail()));
            detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            status.setText(R.string.signed_out);
            detail.setText(null);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void authFailed() {
        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
    }


}
