package com.wxz.ebook.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    @SuppressLint("ValidFragment")
    public BookDetailsListFragment(String book_id) {
        chaptersList = new ArrayList<>();
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
        View view = inflater.inflate(R.layout.book_details_list_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            itemAdapter = new BookDetailsListItemAdapter(chaptersList);
            recyclerView.setAdapter(itemAdapter);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String titleName = "bookDetailsList" + book_id;
        Observable<BookMixAToc> bookMixATocObservable = BookApi.getInstance(new OkHttpClient())
                .getBookMixAToc(book_id,"chapter");
        CacheProviders.getUserCache(getContext())
                .getBookMixAToc(bookMixATocObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookMixAToc>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BookMixAToc bookMixAToc) {
                        chaptersList.addAll(bookMixAToc.mixToc.chapters);
                        itemAdapter.notifyDataSetChanged();
                        mListener = new OnListFragmentInteractionListener() {
                            @Override
                            public void onListFragmentInteraction(BookMixAToc.mixToc.Chapters item) {
                                String titleName1 = "ChapterRead"+ book_id + "no" + item.id;
                                Observable<ChapterRead> chapterReadObservable = BookApi.getInstance(new OkHttpClient())
                                        .getChapterRead(item.link);
                                CacheProviders.getUserCache(getContext())
                                        .getChapterRead(chapterReadObservable,new DynamicKey(titleName1),new EvictDynamicKey(false))
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<ChapterRead>() {
                                            @Override
                                            public void onSubscribe(Disposable d) { }
                                            @Override
                                            public void onNext(ChapterRead chapterRead) {
                                                Log.e("test",chapterRead.chapter.body);
                                            }
                                            @Override
                                            public void onError(Throwable e) {}
                                            @Override
                                            public void onComplete() {}
                                        });
                            }
                        };
                        itemAdapter.setOnListFragmentInteractionListener(mListener);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BookMixAToc.mixToc.Chapters item);
    }
}
