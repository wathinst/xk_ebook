package com.wxz.ebook.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.cache.CacheProviders;

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

public class BookMarketListAdapter extends RecyclerView.Adapter<BookMarketListAdapter.MyHolder> {

    private Context context;
    private List<BooksByCats.BooksBean> booksBeans;
    private OnItemClickListener onItemClickListener;

    public BookMarketListAdapter(Context context, List<BooksByCats.BooksBean> booksBeans) {
        this.context = context;
        this.booksBeans = booksBeans;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_market_class, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.item_title.setText(booksBeans.get(position).title);
        holder.item_author.setText(booksBeans.get(position).author);
        holder.item_shortIntro.setText(booksBeans.get(position).shortIntro);
        String titleName = "BookImage"+ booksBeans.get(position)._id;
        Observable<ResponseBody> bookImg = BookImgApi.getInstance(new OkHttpClient()).getImg(booksBeans.get(position).cover);
        CacheProviders.getUserCache(context).getImg(bookImg,new DynamicKey(titleName),new EvictDynamicKey(true))
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
                onItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksBeans.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView item_title,item_author,item_shortIntro;
        ImageView item_image;
        public MyHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_book_search_result_title);
            item_author = itemView.findViewById(R.id.item_book_search_result_author);
            item_shortIntro = itemView.findViewById(R.id.item_book_search_result_shortIntro);
            item_image = itemView.findViewById(R.id.item_book_search_result_image);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
