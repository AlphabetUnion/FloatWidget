package com.alphabet.jack.floatwidget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;//通过setOnScrollListener()方法把Activity中的监听对象传入本类中使用
	/**
	 * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
	 */
	private int lastScrollY;

	public MyScrollView(Context context) {
		this(context, null);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置滚动接口
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/**
	 * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();//获得当前滚动到的y坐标值

			//此时的距离和记录下的距离不相等，再隔5毫秒给handler发送消息
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;//把当前滚到的y坐标值赋给lastScrollY变量，以便下次比较
				handler.sendMessageDelayed(handler.obtainMessage(), 5);
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);//把获得的当前滚动值传入，执行监听器中的处理
			}

		};

	};

	/**
	 * 重写onTouchEvent：
	 * 当用户的手在MyScrollView上面的时候，直接将MyScrollView滑动的Y方向距离回调给onScroll方法中；
	 * 当用户抬起手的时候，MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，
	 * 在handler中处理MyScrollView滑动的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){//����������Ѵ����������д����¼�ʱ�ѹ��������������y�����¼��lastScrollY,������������
			onScrollListener.onScroll(lastScrollY = this.getScrollY());//һ����ֵ��䣬һ���¼��������
		}
		switch(ev.getAction()){
			case MotionEvent.ACTION_UP:
				handler.sendMessageDelayed(handler.obtainMessage(), 5); // ���뿪����,��ʱ5���뷢��Ϣ��handler
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 滚动的回调接口，用于在滑动当前scroll组件时把滚动的值回传给Activity进行判断与处理
	 * @author xiaanming
	 *
	 */
	public interface OnScrollListener{
		/**
		 * 回调方法， 返回MyScrollView滑动的Y方向距离
		 * @param scrollY
		 */
		public void onScroll(int scrollY);
	}



}
