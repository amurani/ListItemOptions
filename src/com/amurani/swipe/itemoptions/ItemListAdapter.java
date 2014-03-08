package com.amurani.swipe.itemoptions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class ItemListAdapter extends BaseAdapter {
	
	boolean isNew = false;
	
	Context mContext;
	ItemListAdapter mItemsAdapter;
	
	OptionsItem newOptionsItem = null;
	
	List<String> items = new ArrayList<String>();
	String[] list = Utilities.currencies;
	
	public ItemListAdapter(Context mContext) {
		this.mContext = mContext;
		this.mItemsAdapter = this;
		
		for (int i = 0; i < list.length; i++)
			items.add(list[i]);
	}
	
	public void addNewItem(String text) {
		isNew = true;
		items.add(0, text);
	}
	
	public void showNewestItem() {
		if (newOptionsItem != null) {
			newOptionsItem.showAddedItem();
			isNew = false;
		}
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OptionsItem mOptionsItem = new OptionsItem(mContext, items.get(position));
		mOptionsItem.setOnItemRemovedListener(new OnItemRemvedListener() {
			@Override
			public void onItemRemoved(int index) {
				items.remove(index);
				notifyDataSetChanged();
			}
		});
		
		if (position == 0 && isNew) {
			Toast.makeText(mContext, "Here", Toast.LENGTH_LONG).show();
			newOptionsItem = mOptionsItem;
			return mOptionsItem.getAddedItem();
		} else
			return mOptionsItem.getItem();
	}
}