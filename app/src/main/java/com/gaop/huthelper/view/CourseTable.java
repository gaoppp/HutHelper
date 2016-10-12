package com.gaop.huthelper.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gaop.huthelper.CourseInfoGallery;
import com.gaop.huthelper.CustomDate;
import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.adapter.CourseInfoAdapter;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.utils.DensityUtils;
import com.gaop.huthelper.utils.ScreenUtils;
import com.gaop.huthelper.view.Activity.AddCourseActivity;
import com.gaop.huthelper.view.Activity.CourseItemActivity;
import com.gaop.huthelperdao.Lesson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


/**
 * Created by gaop1 on 2016/7/10.
 */
public class CourseTable extends Fragment {
    protected static final String TAG = "CourseTable";
    /**
     * 第一个月份格子
     */
    @BindView(R.id.tv_month)
    TextView mMonth;

    /**
     * 周一至周日格子
     */
    @BindViews({R.id.tv_monday_course, R.id.tv_tuesday_course, R.id.tv_wednesday_course, R.id.tv_thursday_course, R.id.tv_friday_course, R.id.tv_saturday_course, R.id.tv_sunday_course})
    List<TextView> mWeekViews;

    /**
     * 日期
     */
    @BindViews({R.id.ll_layout1, R.id.ll_layout2, R.id.ll_layout3, R.id.ll_layout4, R.id.ll_layout5, R.id.ll_layout6, R.id.ll_layout7})
    List<LinearLayout> mLayouts;

    @BindViews({R.id.tv_day1, R.id.tv_day2, R.id.tv_day3, R.id.tv_day4, R.id.tv_day5, R.id.tv_day6, R.id.tv_day7})
    List<TextView> mTextViews;

    /**
     * 课程表
     */
    @BindView(R.id.ll_weekView)
    LinearLayout courseTableLayout;

    /**
     * 时间轴
     */
    @BindView(R.id.ll_time)
    LinearLayout timeLayout;

    /**
     * 课程轴
     */
    @BindView(R.id.rl_courses)
    RelativeLayout courseLayout;

    /**
     * 课程轴
     */
    @BindView(R.id.rl_user_courses)
    RelativeLayout mUserCourseLayout;

    @BindView(R.id.scroll_body)
    ScrollView scrollView;

    /**
     * 课程格子平均宽度 高度
     **/
    protected int aveWidth, gridHeight;
    //第一个格子宽度
    private int firstGridWidth;
    //一小时区域
    RelativeLayout.LayoutParams mHourParams;

    RelativeLayout.LayoutParams mNumTextParams;
    //时间文字
    RelativeLayout.LayoutParams mHourTextParams;
    //时间截止线
    RelativeLayout.LayoutParams mHourLineParams;


    public int CurrWeek;

    private static final int WEEK = 7;
    private static final int TOTAL_COL = 7;
    private static final int TOTAL_ROW = 1;

    private CustomDate mShowDate;//自定义的日期  包括year month day
    protected Context mContext;


    private View curClickView;

    private boolean isInit;

    public final String[] HOURS = {"8:00", "8:55", "10:00", "10:55", "14:00", "14:55", "16:00", "16:55", "19:00", "19:55"};

    private int month = 0, year = 0;
    protected Map<Integer, List<Lesson>> textviewLessonMap = new HashMap<Integer, List<Lesson>>();
    protected List<TextView> courseTextViewList = new ArrayList<TextView>();


    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setShowDate(CustomDate mShowDate) {
        this.mShowDate = mShowDate;
    }


    private View view;

    CourseInfoInitMessageHandler courseInfoInitMessageHandler = new CourseInfoInitMessageHandler(this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_table, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInit) {
            isInit = true;
            firstGridWidth = DensityUtils.dp2px(mContext, 30 + 2);
            aveWidth = (ScreenUtils.getScreenWidth(mContext) - firstGridWidth) / 7;
            gridHeight = ScreenUtils.getScreenHeight(mContext) / 12;
        }
        CurrWeek=DateUtil.getNowWeek();
        //初始化24小时view
        initTwentyFourHourViews();
        //导入日期
        initDate();


    }

    /**
     * 初始化日期数据
     */
    public void initDate() {
        if (mShowDate == null) {
            mShowDate = DateUtil.getNextSunday();
        }
        fillDate();
        mMonth.setText(mShowDate.getMonth() < 10 ? "0" + mShowDate.getMonth() : mShowDate.getMonth() + "月");
    }

    /**
     * 初始化添加课程点击事件
     */
    private void initTwentyFourHourViews() {
        initViewParams();
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j <= 8; j++) {
                if (j == 1) {
                    addTimeView(i);
                } else if (i % 2 == 0) {
                    //i 节数 j  周数
                    addDotView(i, j - 1);
                    //可以点击的TextView 用来添加课程
                    TextView tx = new TextView(mContext);
                    tx.setId((i - 1) * 7 + j);
                    //相对布局参数
                    RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth, gridHeight * 2);
                    //设置他们的相对位置
                    if (j == 2) {
                        if (i > 1) {
                            rp.addRule(RelativeLayout.BELOW, (i - 3) * 7 + j);
                        }
                    } else {
                        //字体样式
                        tx.setTextAppearance(mContext, R.style.courseTableText);
                        rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 7 + j - 1);
                        rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 7 + j - 1);
                        tx.setText("");
                    }

                    tx.setLayoutParams(rp);
                    tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //num--1357   week 1234567
                            int num = (v.getId() - 2) / 7;
                            int weekofday = (v.getId() - 1) % 7;
                            if (weekofday == 0) {
                                weekofday = 7;
                            }
                            //莫忘了计算点击的时间时 加上开始时间
                            if (curClickView != null) {
                                curClickView.setBackground(null);
                                if (curClickView.getId() == v.getId()) {
                                    curClickView = null;

                                    //Toast.makeText(mContext, "节" + num + "xin" + weekofday, Toast.LENGTH_SHORT).show();
                                    //跳转到添加课程界面
                                    startActivityForResult(new Intent(mContext, AddCourseActivity.class),101);
                                    return;
                                }
                            }
                            curClickView = v;
                            curClickView.setBackground(getResources().getDrawable(R.drawable.bg_course_add));
                            curClickView.setAlpha((float) 0.5);
                        }
                    });
                    courseLayout.addView(tx);
                }
            }
        }
    }

    /**
     * 时间轴
     *
     * @param hour 几节
     */
    public void addTimeView(int hour) {
        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(mHourParams);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(mHourTextParams);
        textView.setTextAppearance(mContext, R.style.weekViewTimeText);
        textView.setText("" + hour);
        layout.addView(textView);
        textView=new TextView(mContext);
        textView.setLayoutParams(mNumTextParams);
        textView.setTextAppearance(mContext,R.style.weekViewNumText);
        textView.setText(HOURS[hour-1]);
        layout.addView(textView);
        //第10节横线不显示
        if (hour != 10) {
            TextView lineView = new TextView(mContext);
            lineView.setLayoutParams(mHourLineParams);
            lineView.setBackgroundColor(getResources().getColor(R.color.week_view_black));
            layout.addView(lineView);
        }
        timeLayout.addView(layout);
    }

    /**
     * 周视图上面的小点
     *
     * @param hour
     * @param j
     */
    public void addDotView(int hour, int j) {
        if (j == 7 || hour == 10) {
            return;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(mContext, 3), DensityUtils.dp2px(mContext, 3));
        params.topMargin = hour * gridHeight;
        params.leftMargin = aveWidth * j;

        ImageView view = new ImageView(mContext);
        view.setLayoutParams(params);
        view.setBackgroundColor(getResources().getColor(R.color.week_view_text_date));

        courseLayout.addView(view);
    }

    /**
     * 初始化viewparams
     */
    private void initViewParams() {
        if (mHourParams == null) {
            mHourParams = new RelativeLayout.LayoutParams(firstGridWidth, gridHeight);
        }
        if (mHourTextParams == null) {
            mHourTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            mHourTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mHourTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        if(mNumTextParams==null){
            mNumTextParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            mNumTextParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mNumTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mNumTextParams.topMargin=6;
        }

        if (mHourLineParams == null) {
            mHourLineParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(mContext, (float) 1.5));
            mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            mHourLineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    /**
     * 导入日期数据
     */
    public void fillDate() {
        fillWeekDate();

        courseTextViewList.clear();
        //更新数据前 移除view
        mUserCourseLayout.removeAllViews();
        //更新视图
        //courseInfoInitMessageHandler.sendMessage()


        courseInfoInitMessageHandler.sendEmptyMessage(111);
    }

    /**
     * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
     */
    private void fillWeekDate() {
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
        int year = mShowDate.year;
        int month = mShowDate.month;
        int day = mShowDate.day;
        for (int i = TOTAL_COL - 1; i >= 0; i--) {
            day -= 1;
            if (day < 1) {
                if (month == 1) {
                    year--;
                    month = 12;
                } else {
                    month--;
                }
                day = lastMonthDays;
            }
            CustomDate date = CustomDate.modifiDayForObject(year, month, day);
            date.year = year;
            date.week = i;
            //今天
            if (DateUtil.isToday(date)) {
                mTextViews.get(i).setTextColor(mContext.getResources().getColor(R.color.white));
                mTextViews.get(i).setText("" + day);
                mLayouts.get(i).setBackgroundColor(mContext.getResources().getColor(R.color.week_view_date_highted));
                continue;
            }
            mTextViews.get(i).setText("" + day);
            mTextViews.get(i).setTextColor(mContext.getResources().getColor(R.color.textColor_black));
            mLayouts.get(i).setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        }
    }


    class CourseInfoInitMessageHandler extends Handler {
        WeakReference<CourseTable> mTable;

        public CourseInfoInitMessageHandler(CourseTable table) {
            mTable = new WeakReference<CourseTable>(table);
        }

        @Override
        public void handleMessage(Message msg) {
            //获取课程
            List<Lesson> lessonList=new ArrayList<>();
            if(DBHelper.getLessonDao()!=null) {
                lessonList =  DBHelper.getLessonDao();
            }
            Lesson upperCourse = null;

            int lastListSize;
            //七种颜色的背景
            int[] background = {R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5, R.drawable.kb6, R.drawable.kb7};

            do {
                lastListSize = lessonList.size();
                Iterator<Lesson> it = lessonList.iterator();
                while (it.hasNext()) {
                    Lesson le = it.next();
                    if (true) {
                        //判断开始周数是否小于当前周数，结束周数是否大于当前周数
                        //判断单双周
                        upperCourse = le;//设置为顶层课
                        it.remove();
                        break;
                    }
                }
                if (upperCourse != null) {

                    List<Lesson> lList = new ArrayList<>();
                    lList.add(upperCourse);
                    int index = -1;
                    if (CommUtil.ifHaveCourse(upperCourse,  mTable.get().CurrWeek)) {
                        index = 0;
                    }
                    it = lessonList.iterator();
                    //查找是否有重叠的课
                    while (it.hasNext()) {
                        Lesson lesson = it.next();
                        if (lesson.getDjj() == upperCourse.getDjj() && lesson.getXqj() == upperCourse.getXqj()) {
                            boolean change=false;
                            for (int i = 0; i <lList.size() ; i++) {
                                 if(lList.get(i).getName().equals(lesson.getName())){
                                    if (CommUtil.ifHaveCourse(lesson, mTable.get().CurrWeek)) {
                                        upperCourse = lesson;
                                        index=i;
                                    }
                                    change=true;
                                     it.remove();
                                }
                            }
                            if(!change){
                            lList.add(lesson);
                            it.remove();
                            if (CommUtil.ifHaveCourse(lesson,  mTable.get().CurrWeek)) {
                                upperCourse = lesson;
                                index++;
                            }
                            }
                        }
                    }
                    final int upperCourseIndex = index;
                    TextView lesson = new TextView(mTable.get().mContext);
                    lesson.setId(10 * upperCourse.getXqj() + upperCourse.getDjj());
                    mTable.get().textviewLessonMap.put(lesson.getId(), lList);
                    lesson.setText(upperCourse.getName() + "@" + upperCourse.getRoom());
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(mTable.get().aveWidth - 6, mTable.get().gridHeight * 2 - 10);
                    rlp.topMargin = ((upperCourse.getDjj()+1)/2 - 1) * mTable.get().gridHeight * 2 + 3;
                    rlp.leftMargin = mTable.get().firstGridWidth + (upperCourse.getXqj() - 1) * mTable.get().aveWidth + 5;
                    lesson.setTextSize(12);
                    lesson.setPadding(10, 10, 10, 10);
                    if (index != -1) {
                        int bgRes = background[(int) (Math.random() * (background.length - 1))];//随机获取背景色
                        lesson.setBackgroundResource(bgRes);//设置背景
                        lesson.setTextColor(Color.WHITE);
                    } else {
                        lesson.setBackgroundResource(R.drawable.kbno);
                        lesson.setTextColor(Color.GRAY);
                    }
                    lesson.setLayoutParams(rlp);
                    //处理点击事件
                    lesson.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<Integer, List<Lesson>> map = mTable.get().textviewLessonMap;
                            final List<Lesson> tempList = map.get(v.getId());
                            if (tempList.size() > 1) {
                                //如果有多个课程，则设置点击弹出gallery 3d 对话框
                                LayoutInflater layoutInflater = (LayoutInflater) mTable.get().mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View galleryView = layoutInflater.inflate(R.layout.gallery_3d_lesson, null);
                                final AlertDialog coursePopupDialog = new AlertDialog.Builder(mTable.get().mContext, R.style.CustomDialog).create();
                                coursePopupDialog.setCanceledOnTouchOutside(true);
                                coursePopupDialog.setCancelable(true);

                                coursePopupDialog.show();
                                WindowManager.LayoutParams params = coursePopupDialog.getWindow().getAttributes();
                                params.width = ViewGroup.LayoutParams.FILL_PARENT;

                                coursePopupDialog.getWindow().setAttributes(params);
                                CourseInfoAdapter adapter = new CourseInfoAdapter(mTable.get().mContext, tempList, ScreenUtils.getScreenWidth(mTable.get().mContext), mTable.get().CurrWeek);
                                CourseInfoGallery gallery = (CourseInfoGallery) galleryView.findViewById(R.id.lesson_gallery);
                                gallery.setSpacing(10);
                                gallery.setAdapter(adapter);
                                gallery.setSelection(upperCourseIndex);
                                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                                        Lesson courseInfo = tempList.get(arg2);
                                        Intent intent = new Intent();
                                        Bundle mBundle = new Bundle();
                                        mBundle.putSerializable("lesson", courseInfo);
                                        intent.putExtras(mBundle);
                                        intent.setClass(mTable.get().mContext, CourseItemActivity.class);
                                        startActivityForResult(intent,102);
                                      //  mTable.get().mContext.startActivityf(intent);
                                        coursePopupDialog.dismiss();
                                    }
                                });

                                coursePopupDialog.setContentView(galleryView);
                            } else {
                                Intent intent = new Intent();
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("lesson", tempList.get(0));
                                intent.putExtras(mBundle);
                                intent.setClass(mTable.get().mContext, CourseItemActivity.class);
                                startActivityForResult(intent,102);
                            }
                        }
                    });
                    //添加布局
                    mTable.get().mUserCourseLayout.addView(lesson);
                    mTable.get().courseTextViewList.add(lesson);
                    upperCourse = null;
                }
            } while (lessonList.size() < lastListSize && lessonList.size() != 0);

        }
    }




    public void changeWeek(int week,CustomDate date) {
        textviewLessonMap = new HashMap<Integer, List<Lesson>>();
        courseTextViewList = new ArrayList<TextView>();
        CurrWeek=week;
        mShowDate=date;
        fillDate();
        mMonth.setText(String.valueOf(mShowDate.getMonth()) + "月");
    }
    public void removeAllViews(){
        courseLayout.removeAllViews();
        mUserCourseLayout.removeAllViews();
        timeLayout.removeAllViews();
    }
}
