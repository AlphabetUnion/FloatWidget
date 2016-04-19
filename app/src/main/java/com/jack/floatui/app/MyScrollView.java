package com.jack.floatui.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Jack on 2016/4/18.
 */
public class MyScrollView extends ScrollView {
    private int lastY;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (lastY != MyScrollView.this.getScrollY()) {
                lastY = MyScrollView.this.getScrollY();
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollViewListener != null) {
                onScrollViewListener.onScroll(MyScrollView.this.getScrollY());
            }
        }
    };

    /**
     * 重写onTouchEvent有以下两种情况：
     * 1.用户滑动后，手指在MyScrollView上,直接把抬起后的Y回调给Activity
     * 2.用户滑动后，MyScrollView仍在滚动，每隔5S发一次消息，在handler中处理最终Y
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollViewListener != null) {
            onScrollViewListener.onScroll(lastY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滑动监听值回调
     */
    private OnScrollViewListener onScrollViewListener;

    interface OnScrollViewListener {
        void onScroll(int scrollY);
    }

    public void setOnScrollViewListener(OnScrollViewListener onScrollViewListener) {
        this.onScrollViewListener = onScrollViewListener;
    }
}
