package com.wxz.ebook.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.ChapterListBean;
import com.wxz.ebook.bean.ChapterRead;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.adapter.BookDetailsListItemAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;
import retrofit2.Call;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
@SuppressLint("ValidFragment")
public class BookDetailsListFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private String book_id;
    private List<BookMixAToc.mixToc.Chapters> chaptersList;
    private BookDetailsListItemAdapter itemAdapter;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    @SuppressLint("ValidFragment")
    public BookDetailsListFragment(String book_id) {
        this.book_id = book_id;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookDetailsListFragment newInstance(String book_id) {
        BookDetailsListFragment fragment = new BookDetailsListFragment(book_id);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_details_list_fragment_item_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.book_details_list);
        }
        chaptersList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new BookDetailsListItemAdapter(chaptersList);
        recyclerView.setAdapter(itemAdapter);
        String titleName = "bookDetailsList" + book_id;
        Observable<BookMixAToc> bookMixATocObservable = BookApi.getInstance(new OkHttpClient())
                .getBookMixAToc(book_id,"chapter");
        CacheProviders.getUserCache(getContext())
                .getBookMixAToc(bookMixATocObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(BookMixAToc bookMixAToc) {
                        if(bookMixAToc.mixToc != null){
                            chaptersList.addAll(bookMixAToc.mixToc.chapters);
                            itemAdapter.notifyDataSetChanged();
                            itemAdapter.setOnListFragmentInteractionListener(new OnListFragmentInteractionListener() {
                                @Override
                                public void onListFragmentInteraction(BookMixAToc.mixToc.Chapters item) throws IOException {

                                }
                            });
                        }
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BookMixAToc.mixToc.Chapters item) throws IOException;
    }
}
