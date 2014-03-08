package com.amurani.swipe.itemoptions;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;

public class PopUpDialog extends DialogFragment {
	
	public interface OnEditWordListener {
		public void onEditWord(String text);
	}
	
	private EditText word;
	private ImageButton done;
	
	private OnEditWordListener mOnEditWordListener = new OnEditWordListener() {
		public void onEditWord(String text) { }
	};
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog mDialog = super.onCreateDialog(savedInstanceState);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		mDialog.getWindow().getAttributes().windowAnimations = R.style.PopupStyleAnimations;
		return mDialog;
	}
	
	public View onCreateView(LayoutInflater mLayoutInflater, ViewGroup container, Bundle savedInstanceState) {
		View mView = mLayoutInflater.inflate(R.layout.edit_word_layout, container);
		
		word = (EditText) mView.findViewById(R.id.word);
		word.setText(getArguments().getString(DefinitionFragment.WORD));

		done = (ImageButton) mView.findViewById(R.id.done);
		done.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mOnEditWordListener.onEditWord(word.getText().toString());
				word.setText("");
				dismiss();
			}
		});
		
		return mView;
	}
	
	public void setOnEditWordListener(OnEditWordListener mOnEditWordListener) {
		this.mOnEditWordListener = mOnEditWordListener;
	}
	
	public void setWord(String text) {
		if (word != null)
			word.setText(text);
	}
	
}