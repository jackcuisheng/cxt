package com.cxt.gps.UI.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.cxt.gps.R;


public class FlippingLoadingDialog extends BaseDialog {
	private AnimationDrawable mAnimation;
	//private FlippingImageView mFivIcon; 
	private ImageView mFivIcon; 
	private HandyTextView mHtvText;
	private String mText;

	public FlippingLoadingDialog(Context context, String text) {
		super(context);
		mText = text;
		init();
		setCanceledOnTouchOutside(false);
	}

	private void init() {
		setContentView(R.layout.common_flipping_loading_diloag);
		//mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (HandyTextView) findViewById(R.id.loadingdialog_htv_text);
		//mFivIcon.startAnimation();
		mHtvText.setText(mText);
		
		mFivIcon = (ImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mAnimation = (AnimationDrawable) mFivIcon.getBackground();
        
		mFivIcon.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
	}

	public void setText(String text) {
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			//mAnimation.stop();
			super.dismiss();
		}
	}
}


