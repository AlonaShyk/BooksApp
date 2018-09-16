package com.shyk.alena.booksapp.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shyk.alena.booksapp.R;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.retrofit.RetrofitListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailFragment extends Fragment implements RetrofitListener, DetailContract.View {
    private static final String BOOK_ID = "bookId";
    private DetailPresenter presenter;
    private String bookId;
    private TextView title;
    private TextView author;
    private TextView numberPages;
    private TextView publisher;
    private TextView year;
    private TextView description;
    private ImageView bookImg;
    private ProgressBar progressBar;


    public DetailFragment() {
    }

    public static DetailFragment newInstance(String bookId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(BOOK_ID, bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getString(BOOK_ID);
        }
        presenter = new DetailPresenter(getContext(), this, this);
        presenter.createDBAccess();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        if (getActivity() != null) {
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            numberPages = view.findViewById(R.id.pages_number);
            publisher = view.findViewById(R.id.publisher);
            bookImg = view.findViewById(R.id.book_img);
            year = view.findViewById(R.id.year);
            description = view.findViewById(R.id.description);
        }
      presenter.getBook(bookId);
        return view;
    }


    @Override
    public void onBook(BooksVolume booksVolume) {
        presenter.onResult(false);
        presenter.showBook(booksVolume.getVolumeInfo());
        presenter.addToDatabase(booksVolume);

    }

    @Override
    public void onList(List<BooksVolume> items) {

    }

    @Override
    public void onError() {
        presenter.onResult(false);
    }


    @Override
    public void inflateDetail(String bookTitle,
                              String bookAuthor,
                              String imageLink,
                              int numberPage,
                              String bookPublisher,
                              String bookYear,
                              String bookDescription) {
        title.setText(String.format("Title: %s", bookTitle));
        author.setText(String.format("Author: %s", bookAuthor));
        Picasso.get()
                .load(imageLink)
                .fit()
                .centerCrop()
                .into(bookImg);
        numberPages.setText(String.format("Pages: %s", String.valueOf(numberPage)));
        publisher.setText(String.format("Publisher: %s", bookPublisher));
        year.setText(String.format("Date: %s", bookYear));
        if (bookDescription != null) {
            description.setText(Html.fromHtml(bookDescription));
        } else description.setText(R.string.no_description);
        description.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.VISIBLE);
        } else progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        presenter.destroy();
        super.onDestroyView();
    }
}
