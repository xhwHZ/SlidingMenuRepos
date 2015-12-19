package com.xhw.slide.menu;

import com.xhw.slide.menu.view.SlideMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		final SlideMenu slideMenu=(SlideMenu) this.findViewById(R.id.slideMenu);
		ImageView iv_back=(ImageView) this.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				slideMenu.switchMenu();
			}
		});
	}

}
