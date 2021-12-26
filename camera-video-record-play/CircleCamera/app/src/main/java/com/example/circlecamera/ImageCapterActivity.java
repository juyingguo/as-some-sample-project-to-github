package com.example.circlecamera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageCapterActivity extends Activity{
	
	private ImageView mImage;
    private Uri mSavedPicUri;
    private Button cancel;
    private Button send;
	
	private String thumbPath = null;
	
	private Uri uri ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		getIntentData();
		findViews();
		setViewData();
	}
	
	private void getIntentData() {
		uri = getIntent().getData();
		thumbPath = getIntent().getStringExtra("thumbPath");
	}
	
	private void setViewData() {
		if(uri != null){
			mImage.setImageURI(uri);
		}
	}

	private void findViews() {
		// TODO Auto-generated method stub
    	cancel = (Button)this.findViewById(R.id.rc_back);
        send = (Button)this.findViewById(R.id.rc_send);
        
        mImage = (ImageView)this.findViewById(R.id.rc_img);
        cancel.setOnClickListener(clickListener);
        send.setOnClickListener(clickListener);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			switch (id) {
			case R.id.rc_back:
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
				break;
				
			case R.id.rc_send:
				if(thumbPath != null){
					Intent intent = new Intent();
//					intent.setData(uri);
					intent.putExtra("thumbPath", thumbPath);
					setResult(RESULT_OK, intent);
				}
				finish();
				break;
			}
		}
	};
}
