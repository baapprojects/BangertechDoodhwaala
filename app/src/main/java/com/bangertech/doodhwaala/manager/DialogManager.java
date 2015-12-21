package com.bangertech.doodhwaala.manager;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.R;


public class DialogManager {

	static Typeface customFont;
	public static void showDialog(Activity parentActivity, String message)
	{


//    	AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//    	builder.setMessage(message)
//    		.setPositiveButton("Close", dialogClickListener)
//    	    .show();

		customFont = Typeface.createFromAsset(parentActivity.getAssets(), "fonts/myriad.ttf");
		final Dialog dialog = new Dialog(parentActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_dialog);

		TextView msgText=(TextView)dialog.findViewById(R.id.body);
		msgText.setText(message);
		Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

		dialog.show();
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

	public static void showAndFinishDialog(final Activity parentActivity, String message)
	{


//    	AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//    	builder.setMessage(message)
//    		.setPositiveButton("Close", dialogClickListener)
//    	    .show();

		customFont = Typeface.createFromAsset(parentActivity.getAssets(), "fonts/myriad.ttf");
		final Dialog dialog = new Dialog(parentActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_dialog);

		TextView msgText=(TextView)dialog.findViewById(R.id.body);
		msgText.setText(message);
		Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

		dialog.show();
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity.finish();
				dialog.dismiss();
			}
		});

	}
	
//	public static DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//	    @Override
//	    public void onClick(DialogInterface dialog, int which) {
//	        switch (which){
//	        case DialogInterface.BUTTON_NEUTRAL:
//	            //No button clicked
//	        	dialog.dismiss();
//	            break;
//	            
//	        }
//	    }
//	};

	
	
}
