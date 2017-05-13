package com.gaop.huthelper.net;


import android.content.Context;

import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelper.db.DBHelper;
import com.gaop.huthelper.model.entity.CareerTalkData;
import com.gaop.huthelper.model.entity.CareerTalkItem;
import com.gaop.huthelper.model.entity.Electric;
import com.gaop.huthelper.model.entity.ExamData;
import com.gaop.huthelper.model.entity.Goods;
import com.gaop.huthelper.model.entity.GoodsListItem;
import com.gaop.huthelper.model.entity.HttpResult;
import com.gaop.huthelper.model.entity.Lose;
import com.gaop.huthelper.model.entity.OfferData;
import com.gaop.huthelper.model.entity.PageData;
import com.gaop.huthelper.model.entity.RankingApiData;
import com.gaop.huthelper.model.entity.Say;
import com.gaop.huthelper.model.entity.UpdateMsg;
import com.gaop.huthelper.model.entity.VideoData;
import com.gaop.huthelper.model.entity.WeatherData;
import com.gaop.huthelper.model.network.api.AddGoodsAPI;
import com.gaop.huthelper.model.network.api.AddSayLikeAPI;
import com.gaop.huthelper.model.network.api.CareerTalkAPI;
import com.gaop.huthelper.model.network.api.ChangeUserNameAPI;
import com.gaop.huthelper.model.network.api.CourseTableAPI;
import com.gaop.huthelper.model.network.api.DeleteGoodAPI;
import com.gaop.huthelper.model.network.api.ElectricAPI;
import com.gaop.huthelper.model.network.api.ExamAPI;
import com.gaop.huthelper.model.network.api.FeedBackAPI;
import com.gaop.huthelper.model.network.api.FileUploadAPI;
import com.gaop.huthelper.model.network.api.GetExpLessonAPI;
import com.gaop.huthelper.model.network.api.GoodListAPI;
import com.gaop.huthelper.model.network.api.GoodsAPI;
import com.gaop.huthelper.model.network.api.GradeAPI;
import com.gaop.huthelper.model.network.api.LoseAPI;
import com.gaop.huthelper.model.network.api.MyGoodListAPI;
import com.gaop.huthelper.model.network.api.OfferAPI;
import com.gaop.huthelper.model.network.api.RankingDataAPI;
import com.gaop.huthelper.model.network.api.SayAPI;
import com.gaop.huthelper.model.network.api.UpdateAPI;
import com.gaop.huthelper.model.network.api.UserDataAPI;
import com.gaop.huthelper.model.network.api.VideoAPI;
import com.gaop.huthelper.model.network.api.WeatherAPI;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Exam;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Lesson;
import com.gaop.huthelperdao.Ranking;
import com.gaop.huthelperdao.Trem;
import com.gaop.huthelperdao.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络封装
 * Created by 高沛 on 2016/7/24
 */
public class HttpMethods {

    public static final String BASE_URL = "http://218.75.197.121:8888/";

    private static final int DEFAULT_TIMEOUT = 5;

    private static Retrofit retrofit;

    //构造方法私有
    private HttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }


    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            retrofit = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        retrofit.baseUrl();
        return retrofit;
    }

    /**
     * 获取单例
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取电费数据
     *
     * @param subscriber
     * @param lou        宿舍楼号码
     * @param hao        寝室号码
     */
    public void getElecticData(Subscriber<Electric> subscriber, String lou, String hao) {
        ElectricAPI electricAPI;
        electricAPI = retrofit.create(ElectricAPI.class);

        electricAPI.electricData(lou, hao)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    /**
     * 分页获取失物招领
     *
     * @param subscriber
     * @param pagenum    页码 从1开始
     */
    public void getLose(Subscriber<HttpResult<PageData<Lose>>> subscriber, int pagenum) {
        LoseAPI loseAPI = retrofit.create(LoseAPI.class);
        loseAPI.getLosesList(pagenum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取指定id的失物招领
     *
     * @param subscriber
     * @param user_id
     */
    public void getLoseListById(Subscriber<HttpResult<PageData<Lose>>> subscriber, String user_id) {

        LoseAPI ListAPI = retrofit.create(LoseAPI.class);
        ListAPI.getLoseListById(user_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除失物招领`
     *
     * @param subscriber
     * @param num
     * @param code
     * @param id
     */
    public void deleteLose(Subscriber<HttpResult> subscriber, String num, String code, String id) {
        LoseAPI api = retrofit.create(LoseAPI.class);
        api.deleteLose(num, code, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 发布失物招领
     *
     * @param subscriber
     * @param user
     * @param attr
     */
    public void addLose(Subscriber<HttpResult<String>> subscriber, User user, String title, String locate, String time,
                        String content, String attr, String phone, String qq, String wechat) {
        LoseAPI API = retrofit.create(LoseAPI.class);
        API.addLose(user.getStudentKH(), user.getRember_code(), title, locate, time, content, attr, phone, qq, wechat)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 上传失物招领图片
     *
     * @param subscriber
     * @param des
     * @param bodyMap
     */
    public void UploadLoseImag(Subscriber<HttpResult<String>> subscriber, String des, MultipartBody.Part bodyMap) {
        LoseAPI fileUploadService = retrofit.create(LoseAPI.class);
        fileUploadService.uploadImage(bodyMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 分页获取说说列表
     *
     * @param subscriber
     * @param pagenum
     */
    public void getSayList(Subscriber<HttpResult<PageData<Say>>> subscriber, int pagenum) {

        SayAPI sayListAPI = retrofit.create(SayAPI.class);
        sayListAPI.getSayList(pagenum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取指定id的说说列表
     *
     * @param subscriber
     * @param user_id
     */
    public void getMySayList(Subscriber<HttpResult<PageData<Say>>> subscriber, String user_id) {

        SayAPI sayListAPI = retrofit.create(SayAPI.class);
        sayListAPI.getSayListById(user_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 点赞/取消说说
     *
     * @param subscriber
     * @param num
     * @param code
     * @param id
     */
    public void likeSay(Subscriber<HttpResult> subscriber, String num, String code, String id) {

        AddSayLikeAPI sayListAPI = retrofit.create(AddSayLikeAPI.class);
        sayListAPI.likeSay(num, code, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getLikedSays(Subscriber<HttpResult<List<String>>> subscriber, String num, String code) {
        AddSayLikeAPI sayListAPI = retrofit.create(AddSayLikeAPI.class);
        sayListAPI.getAllLikeSay(num, code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void addComment(Subscriber<HttpResult> subscriber, User user, String id, String comment) {
        SayAPI api = retrofit.create(SayAPI.class);
        api.addComment(user.getStudentKH(), user.getRember_code(), id, comment)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 删除说说
     *
     * @param subscriber
     * @param num
     * @param code
     * @param id
     */
    public void deleteSay(Subscriber<HttpResult> subscriber, String num, String code, String id) {
        SayAPI api = retrofit.create(SayAPI.class);
        api.deleteSay(num, code, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 发布说说
     *
     * @param subscriber
     * @param user
     * @param content
     * @param attr
     */
    public void addSay(Subscriber<HttpResult<String>> subscriber, User user, String content, String attr) {
        SayAPI sayAPI = retrofit.create(SayAPI.class);
        sayAPI.addSay(user.getStudentKH(), user.getRember_code(), content, attr)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    public void UploadSayImag(Subscriber<HttpResult<String>> subscriber, String des, MultipartBody.Part bodyMap) {
        SayAPI fileUploadService = retrofit.create(SayAPI.class);
        fileUploadService.uploadImage(bodyMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 分页获取商品列表
     *
     * @param subscriber
     * @param pagenum    页码
     */
    public void getGoodsList(Subscriber<GoodsListItem[]> subscriber, int pagenum) {

        GoodListAPI goodListAPI = retrofit.create(GoodListAPI.class);
        goodListAPI.getGoodsList(pagenum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取我的商品
     *
     * @param subscriber
     * @param num
     * @param rember_code
     */
    public void getMyGoodsList(Subscriber<ResponseBody> subscriber, String num, final String rember_code) {
        MyGoodListAPI goodListAPI = retrofit.create(MyGoodListAPI.class);
        goodListAPI.getMyGoodsList(num, rember_code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 删除商品
     *
     * @param subscriber
     * @param num
     * @param rember_code
     * @param id
     */

    public void deleteGoods(Subscriber<HttpResult> subscriber, String num, String rember_code, String id) {
        DeleteGoodAPI goodListAPI = retrofit.create(DeleteGoodAPI.class);
        goodListAPI.deleteGoods(num, rember_code, id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取商品详情
     *
     * @param subscriber
     * @param num
     * @param rember_code 校验码
     * @param id          商品id
     */
    public void getGoodsContent(Subscriber<HttpResult<Goods>> subscriber, String num, String rember_code, String id) {

        // Observable d=Observable.from("")
        GoodsAPI goodsAPI = retrofit.create(GoodsAPI.class);
        goodsAPI.getGoodsContent(num, rember_code, id).
                subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取成绩数据
     *
     * @param subscriber
     */
    public void getGradeData(final Context context, final Subscriber<String> subscriber, final User user) {
        GradeAPI gradeAPI = retrofit.create(GradeAPI.class);

        String sha1 = CommUtil.SHA1(user.getStudentKH() + user.getRember_code() + "f$Z@%");
        gradeAPI.getGrade(user.getStudentKH(), user.getRember_code(), sha1)
                .map(new Func1<HttpResult<List<CourseGrade>>, String>() {
                    @Override
                    public String call(HttpResult<List<CourseGrade>> studentGrade) {
                        if (studentGrade.getMsg().equals("ok")) {
                            List<CourseGrade> firstTestList = new ArrayList<CourseGrade>();
                            List<CourseGrade> cxTestList = new ArrayList<CourseGrade>();
                            List<CourseGrade> firstnoPassList = new ArrayList<CourseGrade>();

                            Grade Allgrade = new Grade();
                            float allsf = 0;//所选学分
                            float getsf = 0;//获取学分
                            float cxsf = 0;//重修学分
                            float nopassxf = 0;//未通过学分
                            float allScore = 0;//总分数
                            int noPassNum = 0;//未通过科目
                            float allJd = 0;//总学分绩点  学分绩点=学分*绩点
                            int allNum = 0;//总数目
                            Set<Trem> trim = new HashSet<Trem>();
                            if (studentGrade.getData() != null && studentGrade.getData().size() != 0) {
                                for (CourseGrade cg : studentGrade.getData()) {
                                    //获取学期
                                    Trem trem = new Trem();
                                    trem.setContent(cg.getXN() + "学年第" + cg.getXQ() + "学期");
                                    trem.setXN(cg.getXN());
                                    trem.setXQ(cg.getXQ());
                                    trim.add(trem);
                                    //区分正考与重修科目
                                    if (cg.getCXBJ() == null)
                                        cg.setCXBJ("0");
                                    //将重修与正考的分别存到不同的list
                                    if (cg.getCXBJ().equals("0"))
                                        firstTestList.add(cg);
                                    else
                                        cxTestList.add(cg);
                                }
                                //遍历正考list
                                for (CourseGrade cg : firstTestList) {
                                    //总科目与总学分累加
                                    allNum++;
                                    allsf += Float.valueOf(cg.getXF());
                                    //获取最大成绩
                                    Float g;
                                    if (cg.getBKCJ() != null) {
                                        g = Math.max(Float.valueOf(cg.getZSCJ()), Float.valueOf(cg.getBKCJ()));
                                    } else {
                                        g = Float.valueOf(cg.getZSCJ());
                                    }
                                    //考虑绩点为null 根据分数计算绩点
                                    if (cg.getJD() == null || cg.getJD().equals("0")) {
                                        if (g >= 60) {
                                            cg.setJD((g - 50) / 10 + "");
                                        } else {
                                            cg.setJD("0");
                                        }
                                    }
                                    //成绩与学分绩点累加
                                    allScore += g;
                                    allJd += Float.valueOf(cg.getXF()) * Float.valueOf(cg.getJD());
                                    //通过科目累加获得学分，未通过科目累加未通过科目与未通过学分，并添加到正考未通过泪飙
                                    if (g >= 60) {
                                        getsf += Float.valueOf(cg.getXF());
                                    } else {
                                        noPassNum++;
                                        firstnoPassList.add(cg);
                                        nopassxf += Float.valueOf(cg.getXF());
                                    }
                                }
                                //遍历重修列表
                                for (CourseGrade cg : cxTestList) {
                                    boolean isHave = false;
                                    //判断是否在正考未通过列表（转专业学生的一些科目没有正考，只有重修）
                                    for (CourseGrade g : firstnoPassList) {
                                        if (g.getKCMC().equals(cg.getKCMC())) {
                                            isHave = true;
                                            break;
                                        }
                                    }
                                    Float g;
                                    if (cg.getBKCJ() != null) {
                                        g = Math.max(Float.valueOf(cg.getZSCJ()), Float.valueOf(cg.getBKCJ()));
                                    } else {
                                        g = Float.valueOf(cg.getZSCJ());
                                    }

                                    //考虑绩点为null 根据分数计算绩点
                                    if (cg.getJD() == null || cg.getJD().equals("0")) {
                                        if (g >= 60) {
                                            cg.setJD((g - 50) / 10 + "");
                                        } else {
                                            cg.setJD("0");
                                        }
                                    }
                                    //累加成绩与学分绩点
                                    allScore += g;
                                    allJd += Float.valueOf(cg.getXF()) * Float.valueOf(cg.getJD());
                                    //有正考记录的，通过则将未通过科目减1，学分，重修学分减
                                    if (isHave) {
                                        if (g >= 60) {
                                            noPassNum--;
                                            nopassxf -= Float.valueOf(cg.getXF());
                                            cxsf = cxsf + Float.valueOf(cg.getXF());
                                        } else {

                                        }
                                    } else {
                                        //没有正考记录（转专业的） 科目累加
                                        allNum++;
                                        allsf += Float.valueOf(cg.getXF());
                                        //处理通过与未通过
                                        if (g >= 60) {
                                            cxsf = cxsf + Float.valueOf(cg.getXF());
                                            getsf += Float.valueOf(cg.getXF());
                                        } else {
                                            noPassNum++;
                                            nopassxf += Float.valueOf(cg.getXF());
                                        }
                                    }
                                }
                                //保存
                                Allgrade.setAllsf(allsf);
                                Allgrade.setNoPassNum(noPassNum);
                                Allgrade.setGetsf(getsf);
                                Allgrade.setCxsf(cxsf);
                                Allgrade.setNopassxf(nopassxf);
                                Allgrade.setAllNum(allNum);
                                Allgrade.setAvgScore(allScore / (studentGrade.getData().size()));
                                Allgrade.setAvgJd(allJd / allsf);
                            }
                            //数据库保存
                            DBHelper.deleteAllCourseGrade();
                            DBHelper.deleteAllTrem();
                            DBHelper.deleteAllGrade();
                            DBHelper.insertGradeDao(Allgrade);
                            DBHelper.insertCourseGradeList(studentGrade.getData());
                            DBHelper.insertTremList(new ArrayList(trim));
                            //设置导入标记
                            //PrefUtil.setBoolean(context, "isLoadGrade", true);
                        }
                        return studentGrade.getMsg();
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(subscriber);
    }

    public void getRankingData(final Context context, Subscriber<String> subscriber, User user) {
        RankingDataAPI api = retrofit.create(RankingDataAPI.class);
        api.getRankingData(user.getStudentKH(), user.getRember_code())
                .map(new Func1<RankingApiData, String>() {
                    @Override
                    public String call(RankingApiData rankingApiData) {
                        if ("ok".equals(rankingApiData.getMsg())) {
                            DBHelper.deleteAllRanking();
                            // 班级学年
                            List<Ranking> bjXn = rankingApiData.getData().get(1);
                            for (Ranking r : bjXn) {
                                r.setISBJ(true);
                                r.setISXN(true);
                            }
                            DBHelper.insertListRanking(bjXn);
                            //班级学期
                            List<Ranking> bjXq = rankingApiData.getData().get(2);
                            for (Ranking r : bjXq) {
                                r.setISBJ(true);
                                r.setISXN(false);
                            }
                            DBHelper.insertListRanking(bjXq);
                            //年级学年
                            List<Ranking> njXn = rankingApiData.getNdata().get(1);
                            for (Ranking r : njXn) {
                                r.setISXN(true);
                                r.setISBJ(false);
                            }
                            DBHelper.insertListRanking(njXn);
                            //年级学期
                            List<Ranking> njXq = rankingApiData.getNdata().get(2);

                            for (Ranking r : njXq) {
                                r.setISXN(false);
                                r.setISBJ(false);
                            }
                            DBHelper.insertListRanking(njXq);

                            //设置导入标记
                            PrefUtil.setBoolean(context, "isLoadGrade", true);
                        }
                        return rankingApiData.getMsg();
                    }
                }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取课程表
     *
     * @param subscriber
     * @param num        学号
     */

    public void getCourseTable(final Context context, Subscriber<String> subscriber, String num, String code) {
        CourseTableAPI courseTableAPI = retrofit.create(CourseTableAPI.class);
        courseTableAPI.getCourseTable(num, code)
                .map(new Func1<HttpResult<List<Lesson>>, String>() {
                    @Override
                    public String call(HttpResult<List<Lesson>> listHttpResult) {
                        if ("ok".equals(listHttpResult.getMsg())) {
                            if (listHttpResult.getData() != null && listHttpResult.getData().size() != 0) {
                                StringBuilder sb;
                                for (Lesson l : listHttpResult.getData()) {
                                    //建立索引 1-10（单）处理为 1 3 5 7 9
                                    sb = new StringBuilder();
                                    for (int i = l.getQsz(); i <= l.getJsz(); i++) {
                                        if ("单".equals(l.getDsz()) && i % 2 == 0) {
                                            continue;
                                        } else if ("双".equals(l.getDsz()) && i % 2 != 0) {
                                            continue;
                                        } else {
                                            sb.append(" " + i);
                                        }
                                    }
                                    l.setIndex(sb.toString());
                                    l.setAddbyuser(false);
                                }

                            }
                            DBHelper.deleteLessonbyImport();
                            DBHelper.insertListLessonDao(listHttpResult.getData());
                            PrefUtil.setBoolean(context, "isLoadCourseTable", true);
                        }
                        return listHttpResult.getMsg();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 反馈意见
     */
    public void feedBack(final Subscriber<String> subscriber, String tel, String content) {
        FeedBackAPI feedBackAPI = retrofit.create(FeedBackAPI.class);
        feedBackAPI.feed(tel, content)
                .map(new Func1<ResponseBody, String>() {

                    @Override
                    public String call(ResponseBody responseBody) {
                        String s = "";
                        try {
                            s = new String(responseBody.bytes(), "UTF-8");
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtil.showToastShort("数据异常");
                        }
                        return s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 检查更新
     */
    public void checkUpdate(Subscriber<HttpResult<UpdateMsg>> subscriber, String num, String vernum) {
        UpdateAPI updateAPI = retrofit.create(UpdateAPI.class);
        updateAPI.getUpdateData(num, vernum)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 获取学生信息
     *
     * @param subscriber
     * @param num        学号
     */
    public void getUserData(final Context context, Subscriber<String> subscriber, String num, String pass) {
        final UserDataAPI userDataAPI = retrofit.create(UserDataAPI.class);
        userDataAPI.getUserData(num, pass)
                .map(new Func1<HttpResult<User>, String>() {
                    @Override
                    public String call(HttpResult<User> userHttpResult) {

                        if ("ok".equals(userHttpResult.getMsg())) {
                            userHttpResult.getData().setRember_code(userHttpResult.getRemember_code_app());
                            DBHelper.deleteAllUser();
                            DBHelper.insertUserDao(userHttpResult.getData());
                            DBHelper.deleteAllNotice();
                            //处理标记
                            PrefUtil.setBoolean(context, "isLoadUser", true);
                            PrefUtil.setBoolean(context, "isLoadCourseTable", false);
                            PrefUtil.setBoolean(context, "isLoadGrade", false);
                            PrefUtil.setBoolean(context, "isLoadExam", false);
                            PrefUtil.setBoolean(context, "isLoadExpLesson", false);
                            //初始化全局User
                            MApplication.setUser(userHttpResult.getData());
                        }
                        return userHttpResult.getMsg();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 上传头像
     *
     * @param subscriber
     * @param num        学号
     * @param code       校验码
     * @param bodyMap    图片
     */
    public void UploadAvator(Subscriber<HttpResult<String>> subscriber, String num, String code, MultipartBody.Part bodyMap) {
        UserDataAPI fileUploadService = retrofit.create(UserDataAPI.class);
        fileUploadService.uploadAvatar(num, code, bodyMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 上传商品图片
     *
     * @param subscriber
     * @param des
     * @param bodyMap    图片
     */
    public void UploadFile(Subscriber<HttpResult<String>> subscriber, String des, MultipartBody.Part bodyMap) {
        FileUploadAPI fileUploadService = retrofit.create(FileUploadAPI.class);
        fileUploadService.uploadImage(bodyMap)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 发布商品
     *
     * @param subscriber
     * @param user       发布用户
     * @param tit        标题
     * @param content    内容
     * @param price      价格
     * @param price_src  原价
     * @param Class      分类
     * @param attr       新旧
     * @param hidden     图片
     * @param phone      手机
     * @param qq         QQ
     * @param wechat     微信
     */
    public void AddGoods(Subscriber<HttpResult<String>> subscriber, User user, String tit, String content, String price, String price_src,
                         int Class, int attr, String hidden, String phone, String qq, String wechat) {
        AddGoodsAPI addGoodsAPI = retrofit.create(AddGoodsAPI.class);
        addGoodsAPI.AddGoods(user.getStudentKH(), user.getRember_code(), tit, content, price, price_src,
                Class, attr, hidden, phone, qq, wechat)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 修改昵称
     *
     * @param subscriber
     * @param user       用户
     * @param username   新昵称
     */
    public void ChangeUserName(Subscriber<HttpResult> subscriber, User user, final String username) {
        ChangeUserNameAPI api = retrofit.create(ChangeUserNameAPI.class);
        api.changeUsername(user.getStudentKH(), user.getRember_code(), username)
                .map(new Func1<HttpResult, HttpResult>() {
                    @Override
                    public HttpResult call(HttpResult httpResult) {
                        if (httpResult.getMsg().equals("ok")) {
                            User u = DBHelper.getUserDao().get(0);
                            u.setUsername(username);
                            DBHelper.UpdateUser(u);
                        }
                        return httpResult;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 实验课表
     *
     * @param context    上下文
     * @param subscriber
     * @param xh         学号
     * @param code       校验码
     */
    public void GetExpLessons(final Context context, Subscriber<String> subscriber, String xh, String code) {
        GetExpLessonAPI api = retrofit.create(GetExpLessonAPI.class);
        api.getExpLesson(xh, code)
                .map(new Func1<HttpResult<List<Explesson>>, String>() {
                    @Override
                    public String call(HttpResult<List<Explesson>> listHttpResult) {
                        if (listHttpResult.getMsg().equals("ok")) {
                            DBHelper.deleteAllExpLesson();
                            DBHelper.insertListExpLesson(listHttpResult.getData());
                            PrefUtil.setBoolean(context, "isLoadExpLesson", true);
                        }
                        return listHttpResult.getMsg();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    /**
     * 考试数据
     *
     * @param context
     * @param subscriber
     * @param xh         学号
     */
    public void GetExamData(final Context context, Subscriber<ExamData> subscriber, String xh) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(PrefUtil.getString(context.getApplicationContext(), "test_plan", "http://218.75.197.122:84/") + "/")
                .build();
        ExamAPI api = retrofit.create(ExamAPI.class);
        api.getExamData(xh, CommUtil.getMD5(xh + "apiforapp!"))
                .map(new Func1<ExamData, ExamData>() {
                    @Override
                    public ExamData call(ExamData listHttpResult) {
                        if (listHttpResult.getCode() == 100) {
                            DBHelper.deleteAllExam();
                            for (Exam e : listHttpResult.getRes().getExam()) {
                                e.setIsCx(false);
                            }
                            DBHelper.insertListExam(listHttpResult.getRes().getExam());
                            for (Exam e : listHttpResult.getRes().getCxexam()) {
                                e.setIsCx(true);
                            }
                            DBHelper.insertListExam(listHttpResult.getRes().getCxexam());
                            PrefUtil.setBoolean(context, "isLoadExam", true);
                        }
                        return listHttpResult;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取Offer的Token
     *
     * @param context
     * @param subscriber
     */
    public void getOfferToken(final Context context, Subscriber<String> subscriber) {
        final OfferAPI offerAPI = retrofit.create(OfferAPI.class);
        offerAPI.getToken("A003", "04F96A77277FAAD8ABCEBDD6050CC92A")
                .map(new Func1<OfferData, String>() {
                    @Override
                    public String call(OfferData offerData) {
                        if (offerData.getR() == 1) {
                            //保存token
                            PrefUtil.setString(context, "OfferToken", offerData.getAccess_token());
                            return "ok";
                        }
                        return offerData.getMsg();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取校招薪水列表
     *
     * @param subscriber
     * @param token
     */
    public void getOfferList(Subscriber<OfferData> subscriber, String token) {
        OfferAPI offerAPI = retrofit.create(OfferAPI.class);
        offerAPI.getOfferList(token)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 搜索Offer
     *
     * @param subscriber
     * @param token
     * @param content    关键词
     */
    public void searchOffer(Subscriber<OfferData> subscriber, String token, String content) {
        OfferAPI offerAPI = retrofit.create(OfferAPI.class);
        offerAPI.searchOffer(token, content)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取天气数据
     *
     * @param subscriber
     */
    public void getWeather(Subscriber<WeatherData> subscriber) {
        WeatherAPI API = retrofit.create(WeatherAPI.class);
        API.getWeather().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取视频数据
     *
     * @param subscriber
     */
    public void getVideoData(Subscriber<VideoData> subscriber) {
        VideoAPI api = retrofit.create(VideoAPI.class);
        api.getVideoData().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getCareerTalkData(String id, Subscriber<CareerTalkData<CareerTalkItem>> subscriber) {
        CareerTalkAPI api = retrofit.create(CareerTalkAPI.class);
        api.getCareerTalk(id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
