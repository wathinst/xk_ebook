package com.wxz.ebook.api;

import com.wxz.ebook.bean.AutoComplete;
import com.wxz.ebook.bean.BookChapter;
import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.bean.BookListDetail;
import com.wxz.ebook.bean.BookListTags;
import com.wxz.ebook.bean.BookLists;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.BookSource;
import com.wxz.ebook.bean.BookSummary;
import com.wxz.ebook.bean.BookUpdated;
import com.wxz.ebook.bean.BooksByCats;
import com.wxz.ebook.bean.BooksByTag;
import com.wxz.ebook.bean.CategoryList;
import com.wxz.ebook.bean.CategoryListLv2;
import com.wxz.ebook.bean.ChapterRead;
import com.wxz.ebook.bean.HotReview;
import com.wxz.ebook.bean.HotWord;
import com.wxz.ebook.bean.RankingList;
import com.wxz.ebook.bean.Rankings;
import com.wxz.ebook.bean.Recommend;
import com.wxz.ebook.bean.RecommendBookList;
import com.wxz.ebook.bean.SearchDetail;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import io.reactivex.Observable;

public class BookApi {
    public static BookApi instance;

    private BookApiService service;

    public BookApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.zhuishushenqi.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create()) // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(BookApiService.class);
    }

    public static BookApi getInstance(OkHttpClient okHttpClient) {
        if (instance == null)
            instance = new BookApi(okHttpClient);
        return instance;
    }

    public Observable<Recommend> getRecommend(String gender) {
        return service.getRecomend(gender);
    }

    public Observable<BookMixAToc> getBookMixAToc(String bookId, String view) {
        return service.getBookMixAToc(bookId, view);
    }

    public Observable<BookChapter> getBookChapter(String bookId) {
        return service.getBookChapter(bookId,"chapters");
    }

    public synchronized Observable<ChapterRead> getChapterRead(String url) {
        return service.getChapterRead(url);
    }

    public synchronized Call<ChapterRead> getChapterRead1(String url) {
        return service.getChapterRead1(url);
    }

    public synchronized Observable<List<BookSource>> getBookSource(String view, String book) {
        return service.getABookSource(view, book);
    }

    public Observable<AutoComplete> getAutoComplete(String query) {
        return service.autoComplete(query);
    }

    public Observable<SearchDetail> getSearchResult(String query) {
        return service.searchBooks(query);
    }

    public Observable<BooksByTag> searchBooksByAuthor(String author) {
        return service.searchBooksByAuthor(author);
    }

    public Observable<BookDetail> getBookDetail(String bookId) {
        return service.getBookDetail(bookId);
    }

    public Observable<BooksByTag> getBooksByTag(String tags, String start, String limit) {
        return service.getBooksByTag(tags, start, limit);
    }

    public Observable<RankingList> getRanking() {
        return service.getRanking();
    }

    public Observable<Rankings> getRanking(String rankingId) {
        return service.getRanking(rankingId);
    }

    public Observable<BookLists> getBookLists(String duration, String sort, String start, String limit, String tag, String gender) {
        return service.getBookLists(duration, sort, start, limit, tag, gender);
    }

    public Observable<BookListTags> getBookListTags() {
        return service.getBookListTags();
    }

    public Observable<BookListDetail> getBookListDetail(String bookListId) {
        return service.getBookListDetail(bookListId);
    }

    public synchronized Observable<CategoryList> getCategoryList() {
        return service.getCategoryList();
    }

    public Observable<CategoryListLv2> getCategoryListLv2() {
        return service.getCategoryListLv2();
    }

    public Observable<BooksByCats> getBooksByCats(String gender, String type, String major, String minor, int start, @Query("limit") int limit) {
        return service.getBooksByCats(gender, type, major, minor, start, limit);
    }

    //获取热门评论
    public Observable<HotReview> getHotReview(String book){
        return service.getHotReview(book);
    };

    //获取推荐书单
    public Observable<RecommendBookList> getRecommendBookList(String bookId, String limit){
        return service.getRecommendBookList(bookId,limit);
    };

    //获取推荐书籍
    public Observable<Recommend> getRecommendBook(String bookId){
        return service.getRecommendBook(bookId);
    };

    public Observable<HotWord> getHotWord() {
        return service.getHotWord();
    }

    //获取推荐书单
    public Observable<List<BookUpdated.Updated>> getBookUpdated(String bookId){
        return service.getBookUpdated("updated",bookId);
    };

    //获取推荐书单
    public Observable<List<BookSummary.SummaryBean>> getBookSummary(String bookId){
        return service.getBookSummary("summary",bookId);
    };
}
