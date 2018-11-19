package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookImgApi;
import com.wxz.ebook.bean.Recommend;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.activity.BookDetailsActivity;

import java.io.InputStream;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class BookDetailsRecommendAdapter extends RecyclerView.Adapter<BookDetailsRecommendAdapter.MyHolder> {

    private List<Recommend.RecommendBooks> recommendBooksList;

    public BookDetailsRecommendAdapter(List<Recommend.RecommendBooks> recommendBooksList) {
        this.recommendBooksList = recommendBooksList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_details_recommend, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.item_title.setText(recommendBooksList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.context, BookDetailsActivity.class);
                intent.putExtra("book_id",recommendBooksList.get(position)._id);
                intent.putExtra("book_img_url",recommendBooksList.get(position).cover);
                intent.putExtra("book_title",recommendBooksList.get(position).title);
                holder.context.startActivity(intent);
            }
        });
        Observable<ResponseBody> bookImg = BookImgApi.getInstance(new OkHttpClient()).getImg(recommendBooksList.get(position).cover);
        CacheProviders.getUserCache(holder.context).getImg(bookImg,new DynamicKey(recommendBooksList.get(position).cover),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        holder.item_img.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public int getItemCount() {
        if (recommendBooksList.size()>=3){
            return 3;
        }else {
            return recommendBooksList.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        ImageView item_img;
        Context context;
        public MyHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_book_details_recommend_title);
            item_img = itemView.findViewById(R.id.item_book_details_recommend_img);
            context = itemView.getContext();
        }
    }
}
