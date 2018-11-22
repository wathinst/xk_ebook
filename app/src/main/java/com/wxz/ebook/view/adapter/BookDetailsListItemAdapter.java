package com.wxz.ebook.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.view.fragment.BookDetailsListFragment.OnListFragmentInteractionListener;

import java.io.IOException;
import java.util.List;


public class BookDetailsListItemAdapter extends RecyclerView.Adapter<BookDetailsListItemAdapter.ViewHolder> {

    private List<BookMixAToc.mixToc.Chapters> chaptersList;
    private OnListFragmentInteractionListener mListener;

    public BookDetailsListItemAdapter(List<BookMixAToc.mixToc.Chapters> chaptersList) {
        this.chaptersList = chaptersList;
    }

    public void setOnListFragmentInteractionListener(OnListFragmentInteractionListener listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_details_list_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = chaptersList.get(position);
        holder.list_title.setText(chaptersList.get(position).title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    try {
                        mListener.onListFragmentInteraction(holder.mItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chaptersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView list_title;
        public final TextView list_flag;
        public BookMixAToc.mixToc.Chapters mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            list_title = (TextView) view.findViewById(R.id.book_details_list_title);
            list_flag = (TextView) view.findViewById(R.id.book_details_list_flag);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + list_flag.getText() + "'";
        }
    }
}
