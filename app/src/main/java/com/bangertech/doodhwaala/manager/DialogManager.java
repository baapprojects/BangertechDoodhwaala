package com.bangertech.doodhwaala.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.KeyEvent;
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

	public static void showOptionsDialog(final Activity parentActivity, String message)
	{


//    	AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
//    	builder.setMessage(message)
//    		.setPositiveButton("Close", dialogClickListener)
//    	    .show();

		customFont = Typeface.createFromAsset(parentActivity.getAssets(), "fonts/myriad.ttf");
		final Dialog dialog = new Dialog(parentActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_dialog_option);

		TextView msgText=(TextView)dialog.findViewById(R.id.body);
		msgText.setText(message);
		Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
		Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);

		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity.finish();
				dialog.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parentActivity.finish();
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	public static void showDialogRecommendedUpdate(final Activity parentActivity, String message)
	{

		customFont = Typeface.createFromAsset(parentActivity.getAssets(), "fonts/myriad.ttf");
		final Dialog dialog = new Dialog(parentActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_app_update_recommended_dialog);

		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int keyCode,
								 KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					parentActivity.finish();
					dialog.dismiss();
				}
				return true;
			}
		});

		TextView msgText=(TextView)dialog.findViewById(R.id.body);
		msgText.setText(message);
		Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);
		Button btn_cancel=(Button)dialog.findViewById(R.id.btn_cancel);

		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String appPackageName = parentActivity.getPackageName(); // getPackageName() from Context or Activity object
				try {
					parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
				parentActivity.finish();
				dialog.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public static void showDialogForceUpdate(final Activity parentActivity, String message)
	{

		//customFont = Typeface.createFromAsset(parentActivity.getAssets(), "fonts/myriad.ttf");
		final Dialog dialog = new Dialog(parentActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_alert_app_update_force_dialog);

		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int keyCode,
								 KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					parentActivity.finish();
					dialog.dismiss();
				}
				return true;
			}
		});

		TextView msgText=(TextView)dialog.findViewById(R.id.body);
		msgText.setText(message);
		Button btn_ok=(Button)dialog.findViewById(R.id.btn_ok);

		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String appPackageName = parentActivity.getPackageName(); // getPackageName() from Context or Activity object
				try {
					parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					parentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
				dialog.dismiss();
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

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
