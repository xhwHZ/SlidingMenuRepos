package com.xhw.slide.menu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 侧滑菜单
 * 
 * @author admin
 *
 */
public class SlideMenu extends FrameLayout// extends ViewGroup
{

	/**
	 * 菜单界面
	 */
	private View menuView;

	/**
	 * 菜单宽度
	 */
	private int menuWidth;

	/**
	 * 主界面
	 */
	private View mainView;

	/**
	 * Scroller对象可以模拟一个滑动过程
	 */
	private Scroller scroller;

	public SlideMenu(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public SlideMenu(Context context)
	{
		super(context);
		init();
	}

	private void init()
	{
		scroller = new Scroller(getContext());
	}

	// 注意，ViewGroup的onMeasure方法测量起来很复杂，一般来说，我们自定义ViewGroup不去实现onMeasure方法
	// 我们去继承一些已经实现好onMeasure()方法的ViewGroup的派生类，例如FrameLayout
	/**
	 * widthMeasureSpec和heightMeasureSpec是系统测量SlideMenu时传入的参数，
	 * 这2个参数测量出的宽高能让SlideMenu充满窗体，其实是正好等于屏幕宽高
	 */
	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	// {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// // 测量所有直接子View的宽高
	//
	// // 菜单部分的高就是matchParent，不用我们设置，宽是240dp，需要我们动态获取
	// int menuMeasureWidth = MeasureSpec.makeMeasureSpec(menuWidth,
	// MeasureSpec.EXACTLY);
	// menuView.measure(menuMeasureWidth, heightMeasureSpec);
	//
	// // 主界面部分直接使用SlideMenu的测量参数，因为它的宽高都是MatchParent
	// mainView.measure(widthMeasureSpec, heightMeasureSpec);
	// }

	/**
	 * 在这里设置子View的布局位置 以父View的左上角为原点，建立一个坐标系 其中，menuView位于父View的左侧的隐藏位置
	 * 这个onLayout方法是在SlideMenu在其父布局中布局完成后
	 * ，即SlideMenu在RelativeLayout调用了SlideMenu.layout方法，之后调用这个方法
	 * ，传入的参数就是SlideMenu在其父View布局时的布局参数
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		// System.out.println("l="+l+",t="+t+",r="+r+",b="+b);
		// l=0,t=0,r=320,b=455 测试机器的屏幕分辨率是320*455
		// 因为SliceMenu的布局宽高属性设置为:(layout_width=match_parent,layout_height=match_parent)

		// menuView.layout(left, top, right, bottom);
		// left表示子View的左边界与父View左边界(父View的y轴)的水平距离
		// top表示子View的上边界与父View上边界(父View的x轴)的垂直距离
		// right表示子View右边界与父View左边界(父View的y轴)的水平距离
		// bottom表示View的下边界与父View上边界(父View的x轴)的垂直距离

		// 为了达到在父View左侧隐藏menuView的目的，将menuView的left设置为-1*menuView的宽度
		menuView.layout(-1 * menuWidth, 0, 0, menuView.getMeasuredHeight());

		// mainView与SlideMenu重叠
		mainView.layout(l, t, r, b);
	}

	// 当所有的直接(一级)子View加载完成后，此方法调用，可以在这里获取子View的引用
	// 注意，这里无法获取子View的宽高
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		menuView = this.getChildAt(0);
		menuWidth = menuView.getLayoutParams().width;
		mainView = this.getChildAt(1);
	}

	/**
	 * 记录每次手指按下的位置
	 */
	private int startX;

	/**
	 * 事件分发机制
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			this.startX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int detalX = Math.abs((int) (ev.getX() - startX));
			if (detalX > 8)
			{
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 监听触控事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			startX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int currentX = (int) event.getX();
			int detalX = currentX - startX;
			// ViewGroup中的Scroll,并不是Scroll (移动) ViewGroup的内容，
			// 而是Scroll (移动)ViewGroup的边框，你想显示左边的内容，需要边框往左移动，而不是内容往右移动,
			// 所以与detalX呈负相关
			// this.getScrollX()获取当前ViewGroup已经移动(Scroll)的X距离
			// int newScrollX =
			// -detalX;这个是下拉刷新控件的模式，不去拿已经设置过的值，移多少就设置多少，不用设置startX=currentX，记录的是每次的新的变化量。而设置startX=currentX是传承上一次的变化量，需要拿到上一次设置的值(getScrollX)
			int newScrollX = this.getScrollX() - detalX;// 根据上一次的值，微调这一次滑动的值
			// 越界处理===start=============
			if (newScrollX > 0)
				newScrollX = 0;
			if (newScrollX < -1 * menuWidth)
				newScrollX = -1 * menuWidth;
			// 越界处理===end===============
			this.scrollTo(newScrollX, 0);
			startX = currentX;
			break;
		case MotionEvent.ACTION_UP:
			// 平滑滚动：

			// 方法1：自定义动画

			// 判断菜单栏是否滑出了一半
			// ScrollAnimation animation=null;
			// // 如果滑出了一半
			// if (-1 * this.getScrollX() > menuWidth / 2)
			// {
			// // 打开菜单
			// //this.scrollTo(-menuWidth, 0); 不够平滑
			// animation=new ScrollAnimation(this, -menuWidth);
			// } else
			// // 没滑出一半
			// {
			// // 关闭菜单
			// //this.scrollTo(0, 0);
			// animation=new ScrollAnimation(this,0);
			// }
			// this.startAnimation(animation);

			// ===================华丽的分割线==================================

			// 方法2:使用Scroller，定义一个Scroller对象，重写computeScroll方法
			// Scroller并不是真正改变控件的位置，需要我们在computeScroll方法去改变
			// 如果滑出了一半
			if (-1 * this.getScrollX() > menuWidth / 2)
			{
				// 打开菜单
				openMenu();

			} else
			{
				// 关闭菜单
				closeMenu();
			}
			break;
		}
		return true;
	}

	/**
	 * 关闭Menu
	 */
	private void closeMenu()
	{
		// 关闭菜单
		scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 400);// (参数是变化量)
		// invalidate方法诱导computeScroll方法执行
		invalidate();
	}

	/**
	 * 打开Menu
	 */
	private void openMenu()
	{
		scroller.startScroll(getScrollX(), 0, -menuWidth - getScrollX(), 0,
				400);// (参数是变化量)
		// invalidate方法诱导computeScroll方法执行
		invalidate();
	}

	// scroller不会主动去调用这个方法
	// 调用invalidate方法会调用onDraw方法，而onDraw方法会调用
	@Override
	public void computeScroll()
	{
		super.computeScroll();
		if (scroller.computeScrollOffset())// 返回true，表示动画没结束
		{
			// Scroller并不是真正改变控件的位置，需要我们在computeScroll方法去改变
			// 获取Scroller视觉效果上的偏移，通过scrollTo方法，将它的假位移转成真位移
			this.scrollTo(scroller.getCurrX(), 0);
			invalidate();// 递归,动画逐渐执行
		}
	}

	/**
	 * 让指定View在一段时间内，scrollTo到指定位置
	 * 
	 * @author admin
	 *
	 */
	class ScrollAnimation extends Animation
	{
		private View v;
		private int startScrollX;
		private int totalScrollX;

		public ScrollAnimation(View v, int targetScrollX)
		{
			this.v = v;
			this.startScrollX = v.getScrollX();
			this.totalScrollX = targetScrollX - startScrollX;
			int duration = Math.abs(this.totalScrollX);
			this.setDuration(duration);
		}

		// interpolatedTime:动画执行的进度或者百分比
		// 下面小学练习
		// interpolatedTime----0----0.5----0.7----7
		// value---------------10----65----87----120
		// 当前值=起始值+interpolatedTime*总差值(即末值-初值)
		// 在指定时间内执行此方法，直到动画结束
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t)
		{
			super.applyTransformation(interpolatedTime, t);
			v.scrollTo((int) (startScrollX + totalScrollX * interpolatedTime),
					0);
		}

	}

	public void switchMenu()
	{
		if(this.getScrollX()==0)
		{
			openMenu();
		}else{
			closeMenu();
		}
	}
	
}
