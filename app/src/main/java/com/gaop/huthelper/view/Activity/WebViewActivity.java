package com.gaop.huthelper.view.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaop.huthelper.DB.DBHelper;
import com.gaop.huthelper.R;
import com.gaop.huthelper.utils.CommUtil;
import com.gaop.huthelper.utils.ToastUtil;
import com.gaop.huthelperdao.User;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaop on 16-9-12.
 */
public class WebViewActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wv_webview)
    WebView webview;
    @BindView(R.id.pb_webview)
    ProgressBar pbWebview;
    @BindView(R.id.tv_homework_grade)
    TextView tvHomeworkgrade;

    private String Url;
    private String Title;
    private View mErrorView;
    private boolean mIsErrorPage;

    static final int TYPE_LIB = 1;
    static final int TYPE_EXAM = 2;
    static final int TYPE_CHANGE_PW=3;
    private int type;
    private User user;

    @Override
    public void initParms(Bundle parms) {
        type = parms.getInt("type");

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_webview;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        //,"expired_time":+time
        if (type == TYPE_EXAM) {
            user = DBHelper.getUserDao().get(0);
            String num = user.getStudentKH();
            String sha1 = CommUtil.SHA1(num) + "Xp@d";
            //Long time = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10)) + 24 * 3600;
            String url = "{\"jid\":\"" + sha1 + "\",\"device\":\"android x.x\",\"student_number\":\"" +
                    num + "\"}";
            try {
                Url = "http://218.75.197.121:8888/homework/?m=homework&client_token=" + URLDecoder.decode(url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtil.showToastShort("进入失败");
            }
            toolbar.setTitle("在线作业");
        } else if (type == TYPE_LIB) {
            toolbar.setTitle("图书馆");
            Url = "http://172.16.64.7:8080/opac/index";
            tvHomeworkgrade.setVisibility(View.GONE);
        }else if(type==TYPE_CHANGE_PW){
            toolbar.setTitle("修改密码");
            Url = "http://218.75.197.121:8888/auth/resetPass";
            tvHomeworkgrade.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pbWebview.setIndeterminate(false);
        pbWebview.setVisibility(View.VISIBLE);

        webview.getSettings().setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showErrorPage();

            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    pbWebview.setVisibility(View.GONE);
                } else {
                    pbWebview.setProgress(newProgress);
                }

            }

        });
        webview.loadUrl(Url);

    }

    @OnClick(R.id.tv_homework_grade)
    public void onClick() {
        if(fastClick()){
            user = DBHelper.getUserDao().get(0);
            String num = user.getStudentKH();
            String sha1 = CommUtil.SHA1(num) + "Xp@d";
           // Long time = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10)) + 24 * 3600;
            String url = "{\"jid\":\"" + sha1 + "\",\"device\":\"android x.x\",\"student_number\":\"" +
                    num + "\"}";
            String scoreurl = null;
            try {
               scoreurl = "http://218.75.197.121:8888/homework/?m=homework_score&client_token=" + URLDecoder.decode(url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ToastUtil.showToastShort("进入失败");
            }
            webview.loadUrl(scoreurl);
        }
    }

    protected void hideErrorPage() {
        RelativeLayout webParentView = (RelativeLayout) mErrorView.getParent();

        mIsErrorPage = false;
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(1);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        webParentView.addView(webview, 1, lp);

    }


    protected void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.online_error, null);
            ImageButton button = (ImageButton) mErrorView.findViewById(R.id.btn_refer);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!CommUtil.isOnline(getApplicationContext())) {
                        ToastUtil.showToastShort("网络不可用");
                    }
                    hideErrorPage();
                    webview.reload();
                }
            });
            mErrorView.setOnClickListener(null);
        }
    }


    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    protected void showErrorPage() {
        RelativeLayout webParentView = (RelativeLayout) webview.getParent();
        initErrorPage();
        while (webParentView.getChildCount() > 1) {
            webParentView.removeViewAt(1);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        webParentView.addView(mErrorView, 1, lp);
        mIsErrorPage = true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();//返回上一页面
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.removeAllViews();
        webview.destroy();
    }




}
