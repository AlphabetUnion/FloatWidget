package com.jack.floatui.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 *
 */
public class MainActivity extends Activity implements MyScrollView.OnScrollViewListener {

    public static String TAG = "MainActivity";
    MyScrollView myScrollView;
    LinearLayout mBuyLayout;
    int floatHeight, buyTop, scrollTop;
    View suspendView = null;

    int screenWidth;
    WindowManager mWindowManager;
    WindowManager.LayoutParams windowSuspendParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myScrollView = (MyScrollView) findViewById(R.id.scrollView);
        myScrollView.setOnScrollViewListener(this);

        mBuyLayout = (LinearLayout) findViewById(R.id.buy);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();//?????????

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            floatHeight = mBuyLayout.getHeight();
//            floatWidth  = mBuyLayout.getWidth();
            buyTop = mBuyLayout.getTop();//购买布局距离顶部的距离
            scrollTop = myScrollView.getTop();//ScrollView距离顶部的距离
            Log.i(TAG, "购买布局距离顶部的距离：" + buyTop + "\nScrollView距离顶部的距离:" + scrollTop);
        }
    }

    /**
     * scrollY代表停止后的Y轴距离
     *
     * @param scrollY
     */
    @Override
    public void onScroll(int scrollY) {

        if (scrollY > buyTop) {
            //滑动的坐标大于购买视图顶部位置，则显示浮窗
            if (suspendView == null)
            showSuspend();
        } else {
            //滑动Y轴小于购买视图位置，则隐藏
            if (suspendView !=null)
            removeSuspend();
        }
    }

    private void showSuspend() {
        if ( null ==suspendView ) {
            Log.i(TAG,"执行这里");
            suspendView = LayoutInflater.from(this).inflate(R.layout.activity_main_real, null);
            if (windowSuspendParams == null) {
                windowSuspendParams = new WindowManager.LayoutParams();
                windowSuspendParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                windowSuspendParams.format = PixelFormat.RGBA_8888;
                windowSuspendParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                windowSuspendParams.gravity = Gravity.TOP;
                windowSuspendParams.width = screenWidth;//屏幕宽
                windowSuspendParams.height = floatHeight;
                windowSuspendParams.x = 0;
                windowSuspendParams.y = scrollTop;//scrollTop
                Log.i(TAG,"宽：" + screenWidth + "高：" + floatHeight);
            }

        }

        mWindowManager.addView(suspendView, windowSuspendParams);

    }

    private void removeSuspend() {
        if ( null != suspendView ) {
            mWindowManager.removeView(suspendView);
            suspendView = null;
        }
    }
}
