package com.wxz.ebook.api;

import com.wxz.ebook.bean.AutoComplete;
import com.wxz.ebook.bean.BookBToc;
import com.wxz.ebook.bean.BookChapter;
import com.wxz.ebook.bean.BookDetail;
import com.wxz.ebook.bean.BookListDetail;
import com.wxz.ebook.bean.BookListTags;
import com.wxz.ebook.bean.BookLists;
import com.wxz.ebook.bean.BookMixAToc;
import com.wxz.ebook.bean.BookRead;
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

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookApiService {

    @GET("/book/recommend")
    Observable<Recommend> getRecomend(@Query("gender") String gender);

    /**
     * 获取正版源(若有) 与 盗版源
     * @param view
     * @param book
     * @return
     */
    @GET("/toc/{bookId}")
    Observable<BookChapter> getBookChapter(@Path("bookId") String bookId, @Query("view") String view);

    @GET("/atoc")
    Observable<List<BookSource>> getABookSource(@Query("view") String view, @Query("book") String book);

    /**
     * 只能获取正版源
     *
     * @param view
     * @param book
     * @return
     */
    @GET("/btoc")
    Observable<List<BookSource>> getBBookSource(@Query("view") String view, @Query("book") String book);

    @GET("/mix-atoc/{bookId}")
    Observable<BookMixAToc> getBookMixAToc(@Path("bookId") String bookId, @Query("view") String view);

    @GET("/mix-toc/{bookId}")
    Observable<BookRead> getBookRead(@Path("bookId") String bookId);

    @GET("/btoc/{bookId}")
    Observable<BookBToc> getBookBToc(@Path("bookId") String bookId, @Query("view") String view);

    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ChapterRead> getChapterRead(@Path("url") String url);

    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Call<ChapterRead> getChapterRead1(@Path("url") String url);

    /**
     * 热门评论
     *
     * @param book
     * @return
     */
    @GET("/post/review/best-by-book")
    Observable<HotReview> getHotReview(@Query("book") String book);

    @GET("/book-list/{bookId}/recommend")
    Observable<RecommendBookList> getRecommendBookList(@Path("bookId") String bookId, @Query("limit") String limit);

    @GET("/book/{bookId}/recommend")
    Observable<Recommend> getRecommendBook(@Path("bookId") String bookId);

    /**
     * 获取所有排行榜
     * http://api.zhuishushenqi.com/ranking/gender
     * @return
     */
    @GET("/ranking/gender")
    Observable<RankingList> getRanking();

    /**
     * 获取单一排行榜
     * 周榜：rankingId->_id
     * 月榜：rankingId->monthRank
     * 总榜：rankingId->totalRank
     *
     * @return
     */
    @GET("/ranking/{rankingId}")
    Observable<Rankings> getRanking(@Path("rankingId") String rankingId);

    /**
     * 获取主题书单列表
     * 本周最热：duration=last-seven-days&sort=collectorCount
     * 最新发布：duration=all&sort=created
     * 最多收藏：duration=all&sort=collectorCount
     *
     * @param tag    都市、古代、架空、重生、玄幻、网游
     * @param gender male、female
     * @param limit  20
     * @return
     */
    @GET("/book-list")
    Observable<BookLists> getBookLists(@Query("duration") String duration, @Query("sort") String sort, @Query("start") String start, @Query("limit") String limit, @Query("tag") String tag, @Query("gender") String gender);

    /**
     * 获取主题书单标签列表
     *
     * @return
     */
    @GET("/book-list/tagType")
    Observable<BookListTags> getBookListTags();

    /**
     * 获取书单详情
     *
     * @return
     */
    @GET("/book-list/{bookListId}")
    Observable<BookListDetail> getBookListDetail(@Path("bookListId") String bookListId);

    /**
     * 获取分类
     *
     * @return
     */
    @GET("/cats/lv2/statistics")
    Observable<CategoryList> getCategoryList();

    /**
     * 获取二级分类
     *
     * @return
     */
    @GET("/cats/lv2")
    Observable<CategoryListLv2> getCategoryListLv2();

    /**
     * 按分类获取书籍列表
     *
     * @param gender male、female
     * @param type   hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major  玄幻
     * @param minor  东方玄幻、异界大陆、异界争霸、远古神话
     * @param limit  50
     * @return http://api.zhuishushenqi.com/book/by-categories?gender=male&type=hot&major=都市&minor=都市生活&limit=10
     */
    @GET("/book/by-categories")
    Observable<BooksByCats> getBooksByCats(@Query("gender") String gender, @Query("type") String type, @Query("major") String major, @Query("minor") String minor, @Query("start") int start, @Query("limit") int limit);

    /**
     * 关键字自动补全
     *
     * @param query
     * @return
     */
    @GET("/book/auto-complete")
    Observable<AutoComplete> autoComplete(@Query("query") String query);

    /**
     * 书籍查询
     *
     * @param query
     * @return
     */
    @GET("/book/fuzzy-search")
    Observable<SearchDetail> searchBooks(@Query("query") String query);

    /**
     * 通过作者查询书名
     *
     * @param author
     * @return
     */
    @GET("/book/accurate-search")
    Observable<BooksByTag> searchBooksByAuthor(@Query("author") String author);

    @GET("/book/{bookId}")
    Observable<BookDetail> getBookDetail(@Path("bookId") String bookId);

    @GET("/book/by-tags")
    Observable<BooksByTag> getBooksByTag(@Query("tags") String tags, @Query("start") String start, @Query("limit") String limit);

    @GET("/book/hot-word")
    Observable<HotWord> getHotWord();

    @GET("/book")
    Observable<List<BookUpdated.Updated>> getBookUpdated(@Query("view") String view, @Query("id") String id);

    @GET("/toc")
    Observable<List<BookSummary.SummaryBean>> getBookSummary(@Query("view") String view, @Query("book") String book);

    @GET("/{imgUrl}")
    Observable<ResponseBody> getImg(@Path("imgUrl") String imgUrl);

}
