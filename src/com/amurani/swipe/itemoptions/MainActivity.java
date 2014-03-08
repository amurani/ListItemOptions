package com.amurani.swipe.itemoptions;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	LinearLayout mLinearLayout;
	ItemListAdapter mItemListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mLinearLayout = (LinearLayout) this.findViewById(R.id.parent);
		String[] list = Utilities.currencies;
		for (int i = 0; i < list.length/4; i++) {
			addNewItem(false, list[i]);
		}
		
		/*mItemListAdapter = new ItemListAdapter(this);
		ListView mListView = (ListView) findViewById(R.id.items_list);
		mListView.setAdapter(mItemListAdapter);*/
		
		Button mButton = (Button) findViewById(R.id.mButton);
		mButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addNewItem(true, "New Item");
				
				/*mItemListAdapter.addNewItem("New Item");
				mItemListAdapter.notifyDataSetChanged();
				mItemListAdapter.showNewestItem();*/
				
			}
		});
	}
	
	public void addNewItem(boolean isNew, String value) {
		OptionsItem mOptionsItem = new OptionsItem(this, value);
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