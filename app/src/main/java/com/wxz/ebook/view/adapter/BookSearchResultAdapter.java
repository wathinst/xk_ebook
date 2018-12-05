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
import com.wxz.ebook.bean.SearchDetail;
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

public class BookSearchResultAdapter extends RecyclerView.Adapter<BookSearchResultAdapter.MyHolder> {

    private List<SearchDetail.SearchBooks> searchBooks;
    private Context context;

    public BookSearchResultAdapter(Context context,List<SearchDetail.SearchBooks> searchBooks) {
        this.context = context;
        this.searchBooks = searchBooks;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_search_result, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int index) {
        holder.item_title.setText(searchBooks.get(index).title);
        holder.item_author.setText(searchBooks.get(index).author);
        holder.item_shortIntro.setText(searchBooks.get(index).shortIntro);
        String titleName = "BookImage"+ searchBooks.get(index)._id;
        Observable<ResponseBody> bookImg = BookImgApi.getInstance(new OkHttpClient()).getImg(searchBooks.get(index).cover);
        CacheProviders.getUserCache(holder.context).getImg(bookImg,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        holder.item_image.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("book_id",searchBooks.get(index)._id);
                intent.putExtra("book_img_url",searchBooks.get(index).cover);
                intent.putExtra("book_title",searchBooks.get(index).title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchBooks.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView item_image;
        TextView item_title, item_author, item_shortIntro;
        View item_view;
        Context context;
        public MyHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            item_image = itemView.findViewById(R.id.item_book_search_result_image);
            item_title = itemView.findViewById(R.id.item_book_search_result_title);
            item_author = itemView.findViewById(R.id.item_book_search_result_author);
            item_shortIntro = itemView.findViewById(R.id.item_book_search_result_shortIntro);
            item_view = itemView.findViewById(R.id.item_book_search_result_view);
        }
    }
}
