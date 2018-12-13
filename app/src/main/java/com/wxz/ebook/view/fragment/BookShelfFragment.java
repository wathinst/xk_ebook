package com.wxz.ebook.view.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wxz.ebook.R;
import com.wxz.ebook.api.BookApi;
import com.wxz.ebook.bean.BookInfoBean;
import com.wxz.ebook.bean.BookUpdated;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.tool.ComparatorBookInfo;
import com.wxz.ebook.tool.sql.FileHelper;
import com.wxz.ebook.tool.utils.DateUtil;
import com.wxz.ebook.view.activity.ReadPageActivity;
import com.wxz.ebook.view.adapter.BookCaseAdapter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
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

public class BookShelfFragment extends Fragment {
    FileHelper helper;
    List<BookInfoBean> bookInfoBeans;
    RecyclerView bookCase;
    BookCaseAdapter adapter;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.content_book_shelf,container,false);
        context = view.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //context.setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bookCase = Objects.requireNonNull(getActivity()).findViewById(R.id.book_case_list);
        }
        GridLayoutManager manager = new GridLayoutManager(context,3);
        bookCase.setLayoutManager(manager);

        helper =new FileHelper(context);

        bookInfoBeans = new ArrayList<>();
        adapter = new BookCaseAdapter(bookInfoBeans);
        bookCase.setAdapter(adapter);

        getBookData();

        adapter.setOnItemClickListener(new BookCaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(context, ReadPageActivity.class);
                intent.putExtra("bookInfoBean", bookInfoBeans.get(position));
                startActivityForResult(intent,2);
            }
            @Override
            public void onLongClick(int position) {
                BookInfoBean bookBean = bookInfoBeans.get(position);
                helper.delete(bookBean);
                bookInfoBeans.remove(bookBean);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getBookData(){
        bookInfoBeans.clear();
        bookInfoBeans.addAll(helper.getAll(null,"date DESC"));//DESC,ASC
        if(bookInfoBeans!=null && bookInfoBeans.size()>0){
            checkUpdated();
        }
        adapter.notifyDataSetChanged();
    }

    private void sendManager(String title,String contentText){
        // 获取通知服务对象NotificationManager
        NotificationManager notiManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            notiManager = (NotificationManager) Objects.requireNonNull(getActivity()).
                    getSystemService(getContext().NOTIFICATION_SERVICE);
        }
        // 创建Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        builder.setContentTitle(title)    // 通知标题
                .setContentText(contentText)  // 通知内容
                .setSmallIcon(R.mipmap.ic_xk_read);    // 通知小图标

        builder.setDefaults(Notification.DEFAULT_SOUND);    // 设置声音/震动等
        Notification notification = builder.build();
        // 设置通知的点击行为：自动取消/跳转等
        builder.setAutoCancel(true);
        // 通过NotificationManager发送通知
        assert notiManager != null;
        notiManager.notify(1001, notification);
    }

    public void checkUpdated(){
        String bookIds = "";
        for (int i = 0; i< bookInfoBeans.size();i++){
            if (i == bookInfoBeans.size()-1){
                bookIds = bookIds + bookInfoBeans.get(i).bookId;
            }else {
                bookIds = bookIds + bookInfoBeans.get(i).bookId + ",";
            }
        }
        String titleName = "XKRead_checkUpdated" + bookIds;
        Observable<List<BookUpdated.Updated>> bookUpdatedObservable = BookApi.getInstance(new OkHttpClient()).getBookUpdated(bookIds);
        CacheProviders.getUserCache(getContext()).getBookUpdated(bookUpdatedObservable,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookUpdated.Updated>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(List<BookUpdated.Updated> updateds) {
                        for (int i = 0; i< updateds.size();i++){
                            for (int j = 0; j< bookInfoBeans.size();j++) {
                                if (updateds.get(i)._id.equals(bookInfoBeans.get(j).bookId)){
                                    if (updateds.get(i).chaptersCount
                                            > bookInfoBeans.get(j).chaptersCount){
                                        sendManager(bookInfoBeans.get(j).name + "有更新",updateds.get(i).lastChapter);
                                        bookInfoBeans.get(j).isUpdatad = 1;
                                        bookInfoBeans.get(j).chaptersCount = updateds.get(i).chaptersCount;
                                        try {
                                            bookInfoBeans.get(j).updated = new DateUtil().dateToStamp1(updateds.get(i).updated);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        helper.update(bookInfoBeans.get(j));
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("checkUpdated",e.getMessage());
                    }
                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            getBookData();
        }
        if(requestCode == 1){
            getBookData();
        }
    }
}
