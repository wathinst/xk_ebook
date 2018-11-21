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
import android.widget.RatingBar;
import android.widget.TextView;

import com.wxz.ebook.R;
import com.wxz.ebook.api.BookImgApi;
import com.wxz.ebook.bean.HotReview;
import com.wxz.ebook.cache.CacheProviders;
import com.wxz.ebook.view.ui.RoundImageView;

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

public class BookDetailsReviewAdapter extends RecyclerView.Adapter<BookDetailsReviewAdapter.MyHolder> {

    private List<HotReview.Reviews> reviews;
    private OnItemClickListener onItemClickListener;

    public BookDetailsReviewAdapter(List<HotReview.Reviews> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_details_review, parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.user_title.setText(reviews.get(position).title);
        holder.user_nickname.setText(reviews.get(position).author.nickname);
        holder.user_lv.setText(String.valueOf(reviews.get(position).author.lv));
        holder.user_content.setText(reviews.get(position).content);
        holder.user_total.setText(String.valueOf(reviews.get(position).helpful.total));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position);
            }
        });
        String titleName = "UserImage"+ reviews.get(position).author._id;
        Observable<ResponseBody> bookImg = BookImgApi.getInstance(new OkHttpClient()).getImg(reviews.get(position).author.avatar);
        CacheProviders.getUserCache(holder.context).getImg(bookImg,new DynamicKey(titleName),new EvictDynamicKey(true))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (bitmap!=null){
                            holder.user_img.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView user_title,user_nickname,user_lv,user_content,user_total;
        RatingBar user_rating;
        RoundImageView user_img;
        Context context;
        public MyHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            user_title = itemView.findViewById(R.id.item_book_details_user_title);
            user_nickname= itemView.findViewById(R.id.item_book_details_user_nickname);
            user_lv= itemView.findViewById(R.id.item_book_details_user_lv);
            user_content= itemView.findViewById(R.id.item_book_details_user_content);
            user_total= itemView.findViewById(R.id.item_book_details_user_total);
            user_rating = itemView.findViewById(R.id.item_book_details_user_rating);
            user_img = itemView.findViewById(R.id.item_book_details_user_img);
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.onItemClickListener = onItemClickListener;
    }
}
