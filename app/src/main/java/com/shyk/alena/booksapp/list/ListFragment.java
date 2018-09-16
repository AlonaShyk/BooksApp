package com.shyk.alena.booksapp.list;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.shyk.alena.booksapp.R;
import com.shyk.alena.booksapp.detail.DetailFragment;
import com.shyk.alena.booksapp.list.adapter.MyBooksListRecyclerViewAdapter;
import com.shyk.alena.booksapp.list.adapter.PaginationScrollListener;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;
import com.shyk.alena.booksapp.signIn.GoogleSignInActivity;

import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

public class ListFragment extends Fragment implements RetrofitListener, ListContract.View {

    private OnListFragmentInteractionListener mListener;
    private final String KEYWORD_FOR_START = "software";
    private MyBooksListRecyclerViewAdapter adapter;
    private ListPresenter presenter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    public ListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recycle_view);
        presenter = new ListPresenter(getContext(), this, this);
        presenter.create();
        presenter.loadFirstPage(KEYWORD_FOR_START);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        presenter.setupSearch(menu);
        presenter.setupHistory(menu);
        presenter.setupSignIn(menu);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onBook(BooksVolume booksVolume) {

    }

    @Override
    public void onList(List<BooksVolume> items) {
        presenter.onResult(false);
        presenter.onNextList(items);
    }

    @Override
    public void onError() {
        presenter.onResult(false);
    }


    @Override
    public void initSearch(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    presenter.onResult(true);
                    presenter.loadFirstPage(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        }
    }

    @Override
    public void initHistory(Menu menu) {
        MenuItem historyItem = menu.findItem(R.id.action_history);
        historyItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                presenter.loadBooksFromDB();
                return false;
            }
        });
    }

    @Override
    public void initSignIn(Menu menu) {
        MenuItem signIn = menu.findItem(R.id.action_signin);
        signIn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                presenter.onSignIn();
                return false;
            }
        });
    }

    @Override
    public void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyBooksListRecyclerViewAdapter(null, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                presenter.loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return presenter.isLastPage();
            }
        });
    }

    @Override
    public void clearAdapter() {
        adapter.clear();
    }

    @Override
    public void addNextPage(List<BooksVolume> items) {
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openDetail(String id) {
        DetailFragment detailFragment = DetailFragment.newInstance(id);
        FragmentTransaction ftb = getActivity().getSupportFragmentManager().beginTransaction();
        ftb.replace(R.id.container, detailFragment);
        ftb.addToBackStack(null);
        ftb.commit();
    }

    @Override
    public void openSignInActivity() {
        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
        } else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(BooksVolume item);
    }
}
