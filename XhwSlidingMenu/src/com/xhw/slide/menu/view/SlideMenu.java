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
 * �໬�˵�
 * 
 * @author admin
 *
 */
public class SlideMenu extends FrameLayout// extends ViewGroup
{

	/**
	 * �˵�����
	 */
	private View menuView;

	/**
	 * �˵����
	 */
	private int menuWidth;

	/**
	 * ������
	 */
	private View mainView;

	/**
	 * Scroller�������ģ��һ����������
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

	// ע�⣬ViewGroup��onMeasure�������������ܸ��ӣ�һ����˵�������Զ���ViewGroup��ȥʵ��onMeasure����
	// ����ȥ�̳�һЩ�Ѿ�ʵ�ֺ�onMeasure()������ViewGroup�������࣬����FrameLayout
	/**
	 * widthMeasureSpec��heightMeasureSpec��ϵͳ����SlideMenuʱ����Ĳ�����
	 * ��2�������������Ŀ������SlideMenu�������壬��ʵ�����õ�����Ļ���
	 */
	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	// {
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// // ��������ֱ����View�Ŀ��
	//
	// // �˵����ֵĸ߾���matchParent�������������ã�����240dp����Ҫ���Ƕ�̬��ȡ
	// int menuMeasureWidth = MeasureSpec.makeMeasureSpec(menuWidth,
	// MeasureSpec.EXACTLY);
	// menuView.measure(menuMeasureWidth, heightMeasureSpec);
	//
	// // �����沿��ֱ��ʹ��SlideMenu�Ĳ�����������Ϊ���Ŀ�߶���MatchParent
	// mainView.measure(widthMeasureSpec, heightMeasureSpec);
	// }

	/**
	 * ������������View�Ĳ���λ�� �Ը�View�����Ͻ�Ϊԭ�㣬����һ������ϵ ���У�menuViewλ�ڸ�View����������λ��
	 * ���onLayout��������SlideMenu���丸�����в�����ɺ�
	 * ����SlideMenu��RelativeLayout������SlideMenu.layout������֮������������
	 * ������Ĳ�������SlideMenu���丸View����ʱ�Ĳ��ֲ���
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		// System.out.println("l="+l+",t="+t+",r="+r+",b="+b);
		// l=0,t=0,r=320,b=455 ���Ի�������Ļ�ֱ�����320*455
		// ��ΪSliceMenu�Ĳ��ֿ����������Ϊ:(layout_width=match_parent,layout_height=match_parent)

		// menuView.layout(left, top, right, bottom);
		// left��ʾ��View����߽��븸View��߽�(��View��y��)��ˮƽ����
		// top��ʾ��View���ϱ߽��븸View�ϱ߽�(��View��x��)�Ĵ�ֱ����
		// right��ʾ��View�ұ߽��븸View��߽�(��View��y��)��ˮƽ����
		// bottom��ʾView���±߽��븸View�ϱ߽�(��View��x��)�Ĵ�ֱ����

		// Ϊ�˴ﵽ�ڸ�View�������menuView��Ŀ�ģ���menuView��left����Ϊ-1*menuView�Ŀ��
		menuView.layout(-1 * menuWidth, 0, 0, menuView.getMeasuredHeight());

		// mainView��SlideMenu�ص�
		mainView.layout(l, t, r, b);
	}

	// �����е�ֱ��(һ��)��View������ɺ󣬴˷������ã������������ȡ��View������
	// ע�⣬�����޷���ȡ��View�Ŀ��
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		menuView = this.getChildAt(0);
		menuWidth = menuView.getLayoutParams().width;
		mainView = this.getChildAt(1);
	}

	/**
	 * ��¼ÿ����ָ���µ�λ��
	 */
	private int startX;

	/**
	 * �¼��ַ�����
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
	 * ���������¼�
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
			// ViewGroup�е�Scroll,������Scroll (�ƶ�) ViewGroup�����ݣ�
			// ����Scroll (�ƶ�)ViewGroup�ı߿�������ʾ��ߵ����ݣ���Ҫ�߿������ƶ������������������ƶ�,
			// ������detalX�ʸ����
			// this.getScrollX()��ȡ��ǰViewGroup�Ѿ��ƶ�(Scroll)��X����
			// int newScrollX =
			// -detalX;���������ˢ�¿ؼ���ģʽ����ȥ���Ѿ����ù���ֵ���ƶ��پ����ö��٣���������startX=currentX����¼����ÿ�ε��µı仯����������startX=currentX�Ǵ�����һ�εı仯������Ҫ�õ���һ�����õ�ֵ(getScrollX)
			int newScrollX = this.getScrollX() - detalX;// ������һ�ε�ֵ��΢����һ�λ�����ֵ
			// Խ�紦��===start=============
			if (newScrollX > 0)
				newScrollX = 0;
			if (newScrollX < -1 * menuWidth)
				newScrollX = -1 * menuWidth;
			// Խ�紦��===end===============
			this.scrollTo(newScrollX, 0);
			startX = currentX;
			break;
		case MotionEvent.ACTION_UP:
			// ƽ��������

			// ����1���Զ��嶯��

			// �жϲ˵����Ƿ񻬳���һ��
			// ScrollAnimation animation=null;
			// // ���������һ��
			// if (-1 * this.getScrollX() > menuWidth / 2)
			// {
			// // �򿪲˵�
			// //this.scrollTo(-menuWidth, 0); ����ƽ��
			// animation=new ScrollAnimation(this, -menuWidth);
			// } else
			// // û����һ��
			// {
			// // �رղ˵�
			// //this.scrollTo(0, 0);
			// animation=new ScrollAnimation(this,0);
			// }
			// this.startAnimation(animation);

			// ===================�����ķָ���==================================

			// ����2:ʹ��Scroller������һ��Scroller������дcomputeScroll����
			// Scroller�����������ı�ؼ���λ�ã���Ҫ������computeScroll����ȥ�ı�
			// ���������һ��
			if (-1 * this.getScrollX() > menuWidth / 2)
			{
				// �򿪲˵�
				openMenu();

			} else
			{
				// �رղ˵�
				closeMenu();
			}
			break;
		}
		return true;
	}

	/**
	 * �ر�Menu
	 */
	private void closeMenu()
	{
		// �رղ˵�
		scroller.startScroll(getScrollX(), 0, 0 - getScrollX(), 0, 400);// (�����Ǳ仯��)
		// invalidate�����յ�computeScroll����ִ��
		invalidate();
	}

	/**
	 * ��Menu
	 */
	private void openMenu()
	{
		scroller.startScroll(getScrollX(), 0, -menuWidth - getScrollX(), 0,
				400);// (�����Ǳ仯��)
		// invalidate�����յ�computeScroll����ִ��
		invalidate();
	}

	// scroller��������ȥ�����������
	// ����invalidate���������onDraw��������onDraw���������
	@Override
	public void computeScroll()
	{
		super.computeScroll();
		if (scroller.computeScrollOffset())// ����true����ʾ����û����
		{
			// Scroller�����������ı�ؼ���λ�ã���Ҫ������computeScroll����ȥ�ı�
			// ��ȡScroller�Ӿ�Ч���ϵ�ƫ�ƣ�ͨ��scrollTo�����������ļ�λ��ת����λ��
			this.scrollTo(scroller.getCurrX(), 0);
			invalidate();// �ݹ�,������ִ��
		}
	}

	/**
	 * ��ָ��View��һ��ʱ���ڣ�scrollTo��ָ��λ��
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

		// interpolatedTime:����ִ�еĽ��Ȼ��߰ٷֱ�
		// ����Сѧ��ϰ
		// interpolatedTime----0----0.5----0.7----7
		// value---------------10----65----87----120
		// ��ǰֵ=��ʼֵ+interpolatedTime*�ܲ�ֵ(��ĩֵ-��ֵ)
		// ��ָ��ʱ����ִ�д˷�����ֱ����������
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
