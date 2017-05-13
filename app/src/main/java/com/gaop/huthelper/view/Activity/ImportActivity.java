package com.gaop.huthelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaop.huthelper.R;
import com.gaop.huthelper.model.network.api.SubscriberOnNextListener;
import com.gaop.huthelper.model.rxbus.RxBus;
import com.gaop.huthelper.model.rxbus.event.MainEvent;
import com.gaop.huthelper.net.HttpMethods;
import com.gaop.huthelper.net.ProgressSubscriber;
import com.gaop.huthelper.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录Activity
 * Created by 高沛 on 2016/5/15.
 */
public class ImportActivity extends BaseActivity {
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.tv_login_message)
    TextView tvLoginMessage;

//    @BindView(R.id.et_import_num)
//    EditText etImportNum;
//    @BindView(R.id.et_import_password)
//    EditText etImportPassword;
//    @BindView(R.id.btn_ok_import)
//    Button btnOkImport;
//    @BindView(R.id.rl_import)
//    RelativeLayout rlImport;
//    @BindView(R.id.tv_fotgetpw)
//    TextView tvFotgetpw;
//    @BindView(R.id.iv_icon_import)
//    ImageView ivIcon;

    @Override
    public void initParms(Bundle parms) {
    }

    @Override
    public int bindLayout() {
        return R.layout.act_login;
    }

    @Override
    public void doBusiness(Context mContext) {
        ButterKnife.bind(this);
        SpannableString spanString = new SpannableString("密码默认为身份证后六位 忘记密码？");

        spanString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PW);
                startActivity(WebViewActivity.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.parseColor("#ff8f8f8f"));       //设置文件颜色
                ds.setUnderlineText(true);      //设置下划线
            }
        }, 12, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvLoginMessage.setText(spanString);
        tvLoginMessage.setMovementMethod(LinkMovementMethod.getInstance());
        //键盘控制
        //controlKeyboardLayout(rlImport, tvFotgetpw);
    }

    private boolean isNumValid(String num) {
        return num.length() == 11;
    }

    public void importData() {
        SubscriberOnNextListener getUserData = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String o) {
                if ("ok".equals(o)) {
                    RxBus.getInstance().send(new MainEvent(3));
                    enterMain();
                } else
                    ToastUtil.showToastShort(o);
            }
        };
        HttpMethods.getInstance().getUserData(ImportActivity.this,
                new ProgressSubscriber<String>(getUserData, ImportActivity.this),
                etLoginUsername.getText().toString(), etLoginPassword.getText().toString());
    }

    private void enterMain() {
        startActivity(new Intent(ImportActivity.this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.btn_login_sign_in)
    public void onViewClicked() {
        if (fastClick()) {
            if (isNumValid(etLoginUsername.getText().toString())) {
                importData();
            } else
                ToastUtil.showToastShort("学号有误");
        }
    }

//    /**
//     * 根据键盘控制图标消失隐藏
//     * @param root
//     * @param scrollToView
//     */
//    private void controlKeyboardLayout(final View root, final View scrollToView) {
//        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect rect = new Rect();
//                //获取root在窗体的可视区域
//                root.getWindowVisibleDisplayFrame(rect);
//                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
//                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
//                //若不可视区域高度大于100，则键盘显示
//                if (rootInvisibleHeight > 400) {
//                    int[] location = new int[2];
//                    //获取scrollToView在窗体的坐标
//                    scrollToView.getLocationInWindow(location);
//                    //计算root滚动高度，使scrollToView在可见区域
//                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
//                    root.scrollTo(0, srollHeight);
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(ivIcon, "alpha", 1f, 0f);
//                    animator.setDuration(100);
//                    animator.start();
//
//                } else {
//                    //键盘隐藏
//                    root.scrollTo(0, 0);
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(ivIcon, "alpha", 0f, 1f);
//                    animator.setDuration(10);
//                    animator.start();
//                }
//            }
//        });
//    }
//
//    @OnClick({R.id.btn_ok_import, R.id.tv_fotgetpw})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_ok_import:
//                if (fastClick()) {
//                    if (isNumValid(etImportNum.getText().toString())) {
//                        importData();
//                    } else
//                        ToastUtil.showToastShort("学号有误");
//                }
//                break;
//            case R.id.tv_fotgetpw:
//                Bundle bundle = new Bundle();
//                bundle.putInt("type", WebViewActivity.TYPE_CHANGE_PW);
//                startActivity(WebViewActivity.class, bundle);
//                break;
//        }
//    }
}
