package com.gaop.huthelper.net;


import android.content.Context;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.Model.DepInfo;
import com.gaop.huthelper.Model.Electric;
import com.gaop.huthelper.Model.ExamData;
import com.gaop.huthelper.Model.Goods;
import com.gaop.huthelper.Model.GoodsListItem;
import com.gaop.huthelper.Model.HttpResult;
import com.gaop.huthelper.Model.MyGoodsItem;
import com.gaop.huthelper.Model.SayData;
import com.gaop.huthelper.Model.UpdateMsg;
import com.gaop.huthelper.jiekou.AddGoodsAPI;
import com.gaop.huthelper.jiekou.AddSayLikeAPI;
import com.gaop.huthelper.jiekou.AllClassAPI;
import com.gaop.huthelper.jiekou.ChangeUserNameAPI;
import com.gaop.huthelper.jiekou.CourseTableAPI;
import com.gaop.huthelper.jiekou.DeleteGoodAPI;
import com.gaop.huthelper.jiekou.ElectricAPI;
import com.gaop.huthelper.jiekou.ExamAPI;
import com.gaop.huthelper.jiekou.FeedBackAPI;
import com.gaop.huthelper.jiekou.FileUploadAPI;
import com.gaop.huthelper.jiekou.GetExpLessonAPI;
import com.gaop.huthelper.jiekou.GoodListAPI;
import com.gaop.huthelper.jiekou.GoodsAPI;
import com.gaop.huthelper.jiekou.GradeAPI;
import com.gaop.huthelper.jiekou.MyGoodListAPI;
import com.gaop.huthelper.jiekou.SayAPI;
import com.gaop.huthelper.jiekou.UpdateAPI;
import com.gaop.huthelper.jiekou.UserDataAPI;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.PrefUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Lesson;
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
 * Created by 高沛 on 2016/7/24.
 */
public class HttpMethods {
    public static final String BASE_URL = "http://218.75.197.121:8888/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

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
     * 分页获取说说列表
     *
     * @param subscriber
     * @param pagenum
     */
    public void getSayList(Subscriber<HttpResult<SayData>> subscriber, int pagenum) {

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
    public void getMySayList(Subscriber<HttpResult<SayData>> subscriber, String user_id) {

        SayAPI sayListAPI = retrofit.create(SayAPI.class);
        sayListAPI.getSayListById(user_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 点赞说说
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
     * 获取我的商品
     *
     * @param subscriber
     * @param num
     * @param rember_code
     */
    public void getMyGoodsList(Subscriber<HttpResult<List<MyGoodsItem>>> subscriber, String num, String rember_code) {
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
     * @param rember_code
     * @param id
     */
    public void getGoodsContent(Subscriber<HttpResult<Goods>> subscriber, String num, String rember_code, String id) {

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
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpResult<List<CourseGrade>>, String>() {
                    @Override
                    public String call(HttpResult<List<CourseGrade>> studentGrade) {

                        if (studentGrade.getMsg().equals("ok")) {

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

                                //处理成绩数据并存入数据库
                                for (CourseGrade grade : studentGrade.getData()) {
                                    Trem trem = new Trem();
                                    trem.setContent(grade.getXN() + "学年第" + grade.getXQ() + "学期");
                                    trem.setXN(grade.getXN());
                                    trem.setXQ(grade.getXQ());
                                    trim.add(trem);
                                    if (grade.getCXBJ() == null)
                                        grade.setCXBJ("0");
                                    if ("0".equals(grade.getCXBJ())) {
                                        allsf = allsf + Float.valueOf(grade.getXF());
                                        allNum++;
                                    }
                                    Float g;
                                    if (grade.getBKCJ() != null) {
                                        g = Math.max(Float.valueOf(grade.getZSCJ()), Float.valueOf(grade.getBKCJ()));
                                    } else {
                                        g = Float.valueOf(grade.getZSCJ());
                                    }


                                    if (grade.getJD() == null || grade.getJD().equals("0")) {

                                        if (g >= 60) {
                                            grade.setJD((g - 50) / 10 + "");
                                        } else {
                                            grade.setJD("0");
                                        }
                                    }

                                    allJd += Float.valueOf(grade.getXF()) * Float.valueOf(grade.getJD());


                                    if (g >= 60) {
                                        getsf = getsf + Float.valueOf(grade.getXF());
                                        allScore += g;
                                        if ("1".equals(grade.getCXBJ())) {
                                            cxsf = cxsf + Float.valueOf(grade.getXF());
                                            nopassxf -= Float.valueOf(grade.getXF());
                                            --noPassNum;
                                        }
                                    } else {
                                        if ("0".equals(grade.getCXBJ())) {
                                            ++noPassNum;
                                            nopassxf += Float.valueOf(grade.getXF());
                                        }
                                    }
                                }

                                Allgrade.setAllsf(allsf);
                                Allgrade.setNoPassNum(noPassNum);
                                Allgrade.setGetsf(getsf);
                                Allgrade.setCxsf(cxsf);
                                Allgrade.setNopassxf(nopassxf);
                                Allgrade.setAllNum(allNum);
                                Allgrade.setAvgScore(allScore / (studentGrade.getData().size() - noPassNum));
                                Allgrade.setAvgJd(allJd / allsf);
                            }
                            DBHelper.deleteAllCourseGrade();
                            DBHelper.deleteAllTrem();
                            DBHelper.deleteAllGrade();
                            DBHelper.insertGradeDao(Allgrade);
                            DBHelper.insertCourseGradeList(studentGrade.getData());
                            DBHelper.insertTremList(new ArrayList(trim));
                            PrefUtil.setBoolean(context, "isLoadGrade", true);

                        }
                        return studentGrade.getMsg();
                    }
                })
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
                                }
                            }
                            DBHelper.deleteAllLesson();
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
    public void checkUpdate(Subscriber<HttpResult<UpdateMsg>> subscriber) {
        UpdateAPI updateAPI = retrofit.create(UpdateAPI.class);
        updateAPI.getUpdateData()
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
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<HttpResult<User>, String>() {
                    @Override
                    public String call(HttpResult<User> userHttpResult) {
                        if ("ok".equals(userHttpResult.getMsg())) {
                            userHttpResult.getData().setRember_code(userHttpResult.getRemember_code_app());
                            DBHelper.deleteAllUser();
                            DBHelper.insertUserDao(userHttpResult.getData());
                            PrefUtil.setBoolean(context, "isLoadUser", true);
                            PrefUtil.setBoolean(context, "isLoadCourseTable", false);
                            PrefUtil.setBoolean(context, "isLoadGrade", false);
                            PrefUtil.setBoolean(context,"isLoadExam",false);
                            PrefUtil.setBoolean(context,"isLoadExpLesson",false);
                        }
                        return userHttpResult.getMsg();
                    }
                })
                .subscribe(subscriber);
    }

    /**
     * 获取所有班级信息
     *
     * @param subscriber
     */
    public void getAllClass(Subscriber<List<DepInfo>> subscriber) {
        AllClassAPI allClassAPI = retrofit.create(AllClassAPI.class);
        allClassAPI.getAllClass()
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
     * @param bodyMap
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
     * @param user
     * @param tit
     * @param content
     * @param price
     * @param price_src
     * @param Class
     * @param attr
     * @param hidden
     * @param phone
     * @param qq
     * @param wechat
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
     * @param user
     * @param username
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

    public void GetExpLessons(final Context context, Subscriber<String> subscriber, String xh, String code) {
        GetExpLessonAPI api = retrofit.create(GetExpLessonAPI.class);
        api.getExpLesson(xh, code)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<HttpResult<List<Explesson>>, String>() {
                    @Override
                    public String call(HttpResult<List<Explesson>> listHttpResult) {
                        if(listHttpResult.getMsg().equals("ok")){
                            DBHelper.deleteAllExpLesson();
                            DBHelper.insertListExpLesson(listHttpResult.getData());
                            PrefUtil.setBoolean(context, "isLoadExpLesson", true);
                        }
                        return listHttpResult.getMsg();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    public void GetExamData(final Context context,Subscriber<ExamData> subscriber,String xh){
       ExamAPI api = retrofit.create(ExamAPI.class);
        api.getExamData(xh, CommUtil.getMD5(xh+"apiforapp!"))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ExamData, ExamData>() {
                    @Override
                    public ExamData call(ExamData listHttpResult) {
                        if(listHttpResult.getCode()==100){
                            DBHelper.deleteAllExam();
                            DBHelper.insertListExam(listHttpResult.getRes().getExam());
                            PrefUtil.setBoolean(context, "isLoadExam", true);
                        }
                        return listHttpResult;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
