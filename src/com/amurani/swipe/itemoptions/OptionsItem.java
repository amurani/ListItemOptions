package com.amurani.swipe.itemoptions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amurani.swipe.itemoptions.PopUpDialog.OnEditWordListener;

interface OnItemRemvedListener {
	public void onItemRemoved(int index);
}

interface OnOptionsFadedListener {
	public void onOptionsFaded();
}

interface OnItemClickedListener {
	public void onItemClicked(String text);
}

public class OptionsItem implements OnClickListener {

	final int ANIMATION_DURATION = 300;
	final int VIBRATION_DURATION = 100;
	final int TOAST_DURATION = 150;

	RelativeLayout item;
	TextView value;
	LinearLayout options;
	ImageButton back, edit, remove;

	PopUpDialog mPopUpDialog;

	Context mContext;
	FragmentManager mFragmentManager;

	OnItemRemvedListener mOnItemRemvedListener = new OnItemRemvedListener() {
		public void onItemRemoved(int index) { }
	};
	
	OnItemClickedListener mOnItemClickedListener = new OnItemClickedListener() {
		public void onItemClicked(String text) {}
	};

	public OptionsItem(Context context, FragmentManager mFragmentManager, String text) {
		this.mContext = context;
		this.mFragmentManager = mFragmentManager;

		LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		item = (RelativeLayout) mLayoutInflater.inflate(R.layout.item_layout,
				null);

		value = (TextView) item.findViewById(R.id.face_value);
		value.setText(text);
		changeWidth(); // Overwrite the width defined in the xml file
		value.setOnClickListener(this);
		value.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// Notify user action will be performed
				Vibrator mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
				mVibrator.vibrate(VIBRATION_DURATION);
				
				toggleItemOptions(value, 0, -value.getWidth(), -value.getWidth());
				return true;
			}

		});

		options = (LinearLayout) item.findViewById(R.id.options);

		back = (ImageButton) item.findViewById(R.id.back);
		back.setOnClickListener(this);

		edit = (ImageButton) item.findViewById(R.id.edit);
		edit.setOnClickListener(this);

		remove = (ImageButton) item.findViewById(R.id.remove);
		remove.setOnClickListener(this);

		// Pop up dialog
		mPopUpDialog = new PopUpDialog();
		mPopUpDialog.setOnEditWordListener(new OnEditWordListener() {
			public void onEditWord(String text) {
				value.setText(text);
				toggleItemOptions(value, 0, value.getWidth(), 0);
			}
		});
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			toggleItemOptions(value, 0, value.getWidth(), 0);
			break;
		case R.id.edit:
			Bundle mBundle = new Bundle();
			mBundle.putString(DefinitionFragment.WORD, value.getText().toString());
			mPopUpDialog.setArguments(mBundle);
			mPopUpDialog.show(mFragmentManager, "edit_word_fragment");
			break;
		case R.id.remove:
			removeItem();
			break;
		case R.id.face_value:
			mOnItemClickedListener.onItemClicked(value.getText().toString());
			break;
		case R.id.done:
			// Allow editing
			mPopUpDialog.dismiss();
			break;
		}
	}

	public RelativeLayout getItem() {
		return item;
	}

	public RelativeLayout getAddedItem() {
		item.setVisibility(View.INVISIBLE);
		value.setVisibility(View.INVISIBLE);
		options.setVisibility(View.INVISIBLE);
		return item;
	}

	public void showAddedItem() {
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) item.getLayoutParams();
		mLayoutParams.height = 0;
		item.setLayoutParams(mLayoutParams);
		
		HeightAnimation mHeightAnimation = new HeightAnimation(item, 0, getDpValue(48));
		mHeightAnimation.setDuration(ANIMATION_DURATION);
		mHeightAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				item.setVisibility(View.VISIBLE);
				fadeContent(true, value, new OnOptionsFadedListener() {
					@Override
					public void onOptionsFaded() {
						value.setVisibility(View.VISIBLE);
						options.setVisibility(View.VISIBLE);
					}
				});
				
			}

			public void onAnimationRepeat(Animation animation) { }
			public void onAnimationStart(Animation animation) { }
		});
		item.startAnimation(mHeightAnimation);
	}

	public void toast(String message) {
		Toast.makeText(mContext, message, TOAST_DURATION).show();
	}

	protected int getDpValue(int pixels) {
		return (int) (pixels * Resources.getSystem().getDisplayMetrics().density);
	}

	public void setOnItemRemovedListener(OnItemRemvedListener mOnItemRemvedListener) {
		this.mOnItemRemvedListener = mOnItemRemvedListener;
	}
	
	public void setOnItemClickedListener(OnItemClickedListener mOnItemClickedListener) {
		this.mOnItemClickedListener = mOnItemClickedListener;
	}

	protected void toggleItemOptions(final TextView value, int animateFromX, int animateToX, final int stopAt) {
		// Animate value movement to reveal the item's options
		TranslateAnimation mTranslateAnimation = new TranslateAnimation(animateFromX, animateToX, 0, 0);
		mTranslateAnimation.setDuration(ANIMATION_DURATION);
		mTranslateAnimation.setFillEnabled(true);
		mTranslateAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) value.getLayoutParams();
				mLayoutParams.leftMargin = stopAt;
				value.setLayoutParams(mLayoutParams);
			}

			public void onAnimationRepeat(Animation animation) { }
			public void onAnimationStart(Animation animation) { }
		});
		value.startAnimation(mTranslateAnimation);
	}

	protected void fadeContent(boolean fadeIn, final View mView, final OnOptionsFadedListener mOnOptionsFadedListener) {
		Animation mAnimation = (fadeIn) ? new AlphaAnimation(0, 1) : new AlphaAnimation(1, 0);
		mAnimation.setDuration(ANIMATION_DURATION);
		mAnimation.setFillAfter(true);
		mAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationEnd(Animation animation) {
				if (mOnOptionsFadedListener != null)
					mOnOptionsFadedListener.onOptionsFaded();
			}

			public void onAnimationRepeat(Animation animation) { }
			public void onAnimationStart(Animation animation) { }
		});
		mView.startAnimation(mAnimation);
	}

	protected void removeItem() {
		fadeContent(false, options, new OnOptionsFadedListener() {

			public void onOptionsFaded() { // Animate item hiding
				HeightAnimation mHeightAnimation = new HeightAnimation(item, getDpValue(48), 0);
				mHeightAnimation.setDuration(ANIMATION_DURATION);
				mHeightAnimation.setAnimationListener(new AnimationListener() {
					public void onAnimationEnd(Animation animation) {
						int index = ((LinearLayout) item.getParent()).indexOfChild(item);
						mOnItemRemvedListener.onItemRemoved(index);
					}

					public void onAnimationRepeat(Animation animation) { }
					public void onAnimationStart(Animation animation) { }
				});
				item.startAnimation(mHeightAnimation);
			}

		});
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	protected void changeWidth() {
		RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) value.getLayoutParams();
		WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display mDisplay = mWindowManager.getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point mPoint = new Point();
			mDisplay.getSize(mPoint);
			mLayoutParams.width = mPoint.x;
		} else {
			mLayoutParams.width = mDisplay.getWidth();
		}
		value.setLayoutParams(mLayoutParams);
	}

	protected class HeightAnimation extends Animation {

		private View mView;
		private float perValue;
		private int fromHeight;

		public HeightAnimation(View mView, int fromHeight, int toHeight) {
			this.mView = mView;
			this.fromHeight = fromHeight;
			this.perValue = toHeight - fromHeight;
		}

		public void applyTransformation(float interpolatedTime, Transformation mTransformation) {
			mView.getLayoutParams().height = (int) (fromHeight + perValue * interpolatedTime);
			mView.requestLayout();
		}

		public boolean willChangeBounds() {
			return true;
		}

	}
}
