package com.amurani.swipe.itemoptions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

public class MainActivity extends FragmentActivity {
	
	ViewPager mViewPager;
	ListItemFragment mListItemFragment;
	DefinitionFragment mDefinitionFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		mListItemFragment = new ListItemFragment();
		mDefinitionFragment = new DefinitionFragment();
		
		ViewPagerAdapter vpa = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(vpa);
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent ke) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mViewPager.getCurrentItem() != 0) {
			mViewPager.setCurrentItem(0);
			return true;
		}
		return super.onKeyDown(keyCode, ke);
	}
	
	// Page Adapter
	protected class ViewPagerAdapter extends FragmentPagerAdapter {
		
		Context context;
		
		public ViewPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			this.context = context;
		}
		
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return mListItemFragment.getInstance(context, mDefinitionFragment, mViewPager);
			case 1:
				return mDefinitionFragment.getInstance(context);
				default :
					return null;
			}
		}
		
		public int getCount() {
			return 2;
		}
	}
	
}