package com.gaop.huthelper;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * 上下文测试
     * @throws Exception
     */
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.gaop.huthelper", appContext.getPackageName());
    }

    @Test
    public void presenterTest() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.gaop.huthelper", appContext.getPackageName());
    }
}