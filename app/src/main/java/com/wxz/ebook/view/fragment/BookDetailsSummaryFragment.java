package com.wxz.ebook.view.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.bean.HotReview;
import com.wxz.ebook.bean.Recommend;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.adapter.BookDetailsRecommendAdapter;
import com.wxz.ebook.view.adapter.BookDetailsReviewAdapter;
import com.wxz.ebook.view.adapter.BookDetailsTagAdapter;

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

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;


@SuppressLint("ValidFragment")
public class BookDetailsSummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String str;
    private BookDetail bookDetail;
    private TextView book_summary;
    private RecyclerView tagRecyclerView;
    private RecyclerView reviewRecyclerView;//BookDetailsReviewAdapter
    private RecyclerView recommendRecyclerView;//BookDetailsRecommendAdapter
    private BookDetailsTagAdapter tagAdapter;
    private BookDetailsReviewAdapter reviewAdapter;
    private BookDetailsRecommendAdapter recommendAdapter;
    private List<HotReview.Reviews> reviewsList;
    private List<Recommend.RecommendBooks> recommendBooksList;

    private OnFragmentInteractionListener mListener;

    @SuppressLint("ValidFragment")
    public BookDetailsSummaryFragment(BookDetail bookDetail) {
        this.bookDetail = bookDetail;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details_summary, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            book_summary = Objects.requireNonNull(getActivity()).findViewById(R.id.book_details_summary);
            tagRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.book_details_tag);
            reviewRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.book_details_review);
            recommendRecyclerView = Objects.requireNonNull(getActivity()).findViewById(R.id.book_details_recommend);
        }
        book_summary.setText(bookDetail.longIntro);

        tagAdapter = new BookDetailsTagAdapter(bookDetail.tags);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),HORIZONTAL,false);
        tagRecyclerView.setLayoutManager(manager);
        tagRecyclerView.setAdapter(tagAdapter);

        reviewsList = new ArrayList<>();
        reviewAdapter = new BookDetailsReviewAdapter(reviewsList);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        recommendBooksList = new ArrayList<>();
        recommendAdapter = new BookDetailsRecommendAdapter(recommendBooksList);
        recommendRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));//new GridLayoutManager(getContext(),3)
        recommendRecyclerView.setAdapter(recommendAdapter);

        initData();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initData(){
        String titleName = "bookDetailsReView" + bookDetail._id;
        Observable<HotReview> hotReviewObservable = BookApi.getInstance(new OkHttpClient())
                .getHotReview(bookDetail._id);
        CacheProviders.getUserCache(getContext())
                .getHotReview(hotReviewObservable,new DynamicKey(titleName),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotReview>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(HotReview hotReview) {
                        reviewsList.clear();
                        reviewsList.addAll(hotReview.reviews);
                        reviewAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
        String title2Name = "bookDetailsReView" + bookDetail._id;
        Observable<Recommend> recommendObservable = BookApi.getInstance(new OkHttpClient())
                .getRecommendBook(bookDetail._id);
        CacheProviders.getUserCache(getContext())
                .getRecommendBook(recommendObservable,new DynamicKey(title2Name),new EvictDynamicKey(false))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Recommend>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(Recommend recommend) {
                        recommendBooksList.clear();
                        recommendBooksList.addAll(recommend.books);
                        recommendAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }
}
