package com.amurani.swipe.itemoptions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class ListItemFragment extends Fragment {
	
	private LinearLayout mLinearLayout;
	
	private Context mContext;
	private DefinitionFragment mDefinitionFragment;
	private ViewPager mViewPager;
	
	public ListItemFragment getInstance(Context mContext, DefinitionFragment mDefinitionFragment, ViewPager mViewPager) {
		this.mContext = mContext;
		this.mDefinitionFragment = mDefinitionFragment;
		this.mViewPager = mViewPager;
		return this;
	}
	
	public View onCreateView(LayoutInflater mLayoutInflater, ViewGroup container, Bundle savdeInstanceState) {
		
		View mView = mLayoutInflater.inflate(R.layout.list_item_layout, container, false);
		
		mLinearLayout = (LinearLayout) mView.findViewById(R.id.parent);
		String[] list = Utilities.currencies;
		for (int i = 0; i < list.length/4; i++)
			addNewItem(false, list[i]);
		
		Button mButton = (Button) mView.findViewById(R.id.mButton);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addNewItem(true, "New Item");
			}
		});
		
		return mView;
	}
	
	public void addNewItem(boolean isNew, String value) {
		OptionsItem mOptionsItem = new OptionsItem(mContext, getFragmentManager(), value);
		mOptionsItem.setOnItemClickedListener(new OnItemClickedListener() {
			public void onItemClicked(String text) {
				mDefinitionFragment.setSearchWord(text);
				mViewPager.setCurrentItem(1);
			}
		});
		mOptionsItem.setOnItemRemovedListener(new OnItemRemvedListener() {
			@Override
			public void onItemRemoved(int index) {
				mLinearLayout.removeViewAt(index);
			}
		});
		if (isNew) {
			mLinearLayout.addView(mOptionsItem.getAddedItem(), 0);
			mOptionsItem.showAddedItem();
		} else
			mLinearLayout.addView(mOptionsItem.getItem());
	}
}