package com.gaop.huthelper;

import com.gaop.huthelper.utils.ButtonUtils;
import com.gaop.huthelper.utils.DateUtil;
import com.gaop.huthelper.view.activity.BaseActivity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by 高沛 on 2017/6/3.
 */

public class UtilsTest {

    /**
     * 按钮连续点击测试
     *
     * @throws Exception
     */
    @Test
    public void btn_Test() throws Exception {
        ButtonUtils.isFastDoubleClick();
        assertEquals(true, ButtonUtils.isFastDoubleClick());
    }

    /**
     * 日期工具测试
     *
     * @throws Exception
     */
    @Test
    public void date_Test() throws Exception {
        assertEquals(5, DateUtil.getCurrentMonthDay());
        assertNotNull(DateUtil.getNextSunday());
    }

    /**
     * 屏幕工具测试
     *
     * @throws Exception
     */
    @Test
    public void screen_Test() throws Exception {
        BaseActivity application;
        application = mock(BaseActivity.class);

//        assertNotNull(ScreenUtils.getScreenHeight(MApplication.AppContext));
    }


}
