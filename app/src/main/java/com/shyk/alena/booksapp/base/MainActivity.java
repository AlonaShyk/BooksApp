package com.shyk.alena.booksapp.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.shyk.alena.booksapp.R;
import com.shyk.alena.booksapp.list.ListFragment;
import com.shyk.alena.booksapp.models.BooksVolume;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener {
    private ListFragment booksListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksListFragment = new ListFragment();
        FragmentTransaction ftb = getSupportFragmentManager().beginTransaction();
        ftb.replace(R.id.container, booksListFragment);
        ftb.commit();

    }

    @Override
    public void onListFragmentInteraction(BooksVolume item) {
        booksListFragment.openDetail(item.getId());
    }

}
