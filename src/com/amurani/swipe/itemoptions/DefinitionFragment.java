package com.amurani.swipe.itemoptions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DefinitionFragment extends Fragment {
	
	public static final String WORD = "word";
	
	private TextView mTextView;
	
	public DefinitionFragment getInstance(Context context) {
		return this;
	}
	
	public View onCreateView(LayoutInflater mLayoutInflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = mLayoutInflater.inflate(R.layout.definition_layout, container, false);
		
		mTextView = (TextView) mView.findViewById(R.id.define_word);
		
		return mView;
	}
	
	public void setSearchWord(String text) {		
		mTextView.setText(text);
	}
	
}
