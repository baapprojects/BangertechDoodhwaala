package com.bangertech.doodhwaala.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.general.General;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


public class MyAsynTaskManager extends AsyncTask<String, String, String> {
	public static String from="";
	public AsyncResponse delegate=null;
	public Dialog progressDialog;
 	public static List<NameValuePair> params = new ArrayList<NameValuePair>();
	public static String posturl;
	public static Activity myActivity;
	public General general;
	
//	public MyAsynTaskManager(Activity myActivity, LoadListener loadListener)
//	{
//		this.myActivity = myActivity;
//		this.loadListener = loadListener;
//	}
	
	public void setupParamsAndUrl(String where, Activity activity, String url, String[] keywords, String[] values)
	{
		from=where;
		myActivity = activity;
		general = new General();
		posturl = url;
		params.clear();
		for(int i = 0; i < keywords.length; i++)
		{
			params.add(new BasicNameValuePair(keywords[i], values[i]));
		}
	}
	
	private void cancelAsynTask(){
		MyAsynTaskManager.this.cancel(true);
	}
	

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog=new Dialog(myActivity);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		progressDialog.setContentView(R.layout.progress_dialog_view);
		progressDialog.setCancelable(false);
		progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				Log.e("Progress Dialog Closed!", "Manually!");
				cancelAsynTask();
			}
		});
		progressDialog.show();
	}
	@Override
	protected String doInBackground(String... result) {
		System.gc();
		try 
		{
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(posturl);
			//Log.e("Post Url ", AppUrlList.ACTION_URL);
			Log.e("Post Url ", posturl);
			HttpParams paramss = client.getParams();
			HttpConnectionParams.setConnectionTimeout(paramss, 20000);
			HttpConnectionParams.setSoTimeout(paramss, 20000);
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse httpResponse = client.execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			if(is == null)
			{
				progressDialog.dismiss();
				return null;
			}
			else
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) 
				{
					sb.append(line + "\n");
				}

				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
				StringBuilder stringBuffer = new StringBuilder();
				String singleLine = null;

				while ((singleLine = reader.readLine()) != null) {
					stringBuffer.append(line);
				}
				is.close();
				Log.e("Reponse : ", sb.toString());
				return sb.toString();  
			}
		}
		catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			progressDialog.dismiss();
			myActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					DialogManager.showDialog(myActivity, "Connection Time Out! Try Again!");
				}
			});

		}
		catch(IOException e)
		{ 
			e.printStackTrace();
			progressDialog.dismiss();
			myActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					DialogManager.showDialog(myActivity, "Server Error Occured! Try Again!");
				}
			});
		}
		catch(final Exception e)
		{ 
			e.printStackTrace();
			progressDialog.dismiss();
			myActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					DialogManager.showDialog(myActivity, e.getMessage());
				}
			});
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(general.isNetworkAvailable(myActivity)) {
			if (result != null) {
				delegate.backgroundProcessFinish(from, result);
			} else {
				DialogManager.showDialog(myActivity, "Server Error Occurred! Try Again!");
			}
		} else {
			DialogManager.showDialog(myActivity, "Please check your internet connection");
		}

	}

//	abstract public static class LoadListener{
//		abstract public void onLoadComplete(String jsonResponse);
//		abstract public void onError(String errorMessage);
//	}
	
}


