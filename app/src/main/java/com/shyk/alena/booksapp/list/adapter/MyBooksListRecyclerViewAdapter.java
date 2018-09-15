package com.shyk.alena.booksapp.list.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shyk.alena.booksapp.R;
import com.shyk.alena.booksapp.models.BooksVolume;
import com.shyk.alena.booksapp.list.ListFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyBooksListRecyclerViewAdapter extends RecyclerView.Adapter<MyBooksListRecyclerViewAdapter.ViewHolder> {

    private List<BooksVolume> items;
    private final ListFragment.OnListFragmentInteractionListener mListener;

    public MyBooksListRecyclerViewAdapter(List<BooksVolume> items, ListFragment.OnListFragmentInteractionListener listener) {
        this.items = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookslist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (items != null) {
            holder.mItem = items.get(position);
            holder.title.setText(items.get(position).getVolumeInfo().getTitle());
            if (items.get(position).getVolumeInfo().getImageLinks() != null) {
                Picasso.get()
                        .load(items.get(position).getVolumeInfo().getImageLinks().getThumbnail())
                        .fit()
                        .centerCrop()
                        .into(holder.bookImage);
            }
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {

                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    private void add(BooksVolume booksVolume) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(booksVolume);
        notifyItemInserted(items.size() - 1);


    }

    public void addAll(List<BooksVolume> list) {
        for (BooksVolume booksVolume : list) {
            add(booksVolume);
        }
    }

    private void remove(BooksVolume booksVolume) {
        int position = items.indexOf(booksVolume);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    private BooksVolume getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView title;
        final ImageView bookImage;
        BooksVolume mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title_tv);
            bookImage = view.findViewById(R.id.book_img);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
