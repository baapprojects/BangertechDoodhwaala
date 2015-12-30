package com.bangertech.doodhwaala.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by annutech on 9/21/2015.
 */
public class CUtils {
    private static Pattern pattern;
    private static  Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }




    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public static boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    public static Typeface LightTypeFace(Context context)
    {
        Typeface customFontLight = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_light.otf");

        return customFontLight;
    }

    public static Typeface RegularTypeFace(Context context)
    {
        Typeface customFontRegular = Typeface.createFromAsset(context.getAssets(), "fonts/montserrat_regular.otf");

        return customFontRegular;
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static String deviceId(Context mContext){
        String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static void printLog(String TAG,String msg,ConstantVariables.LOG_TYPE TYPE
    )
    {
        if(ConstantVariables.IS_DEBUG)
        {
            if(TYPE==ConstantVariables.LOG_TYPE.ERROR) {
                if(msg!=null)
                Log.e(TAG, msg);
            }
            if(TYPE==ConstantVariables.LOG_TYPE.INFO) {
                if(msg!=null)
                Log.i(TAG, msg);
            }
            if(TYPE==ConstantVariables.LOG_TYPE.WARNING) {
                if(msg!=null)
                Log.w(TAG, msg);
            }
        }
    }
   /* public static void donwloadImageFromServer(Context context,ImageView imageView,String url)
    {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        //CUtils.printLog("getProductImage", url, ConstantVariables.LOG_TYPE.ERROR);
    }*/
    public static void downloadImageFromServer(Context context,ImageView imageView,String url)
    {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        //CUtils.printLog("getProductImage", url, ConstantVariables.LOG_TYPE.ERROR);
    }
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public static String[] parseCustomString( String delims, String value)
    {
        try {
            return value.split("[" + delims + "]");
        }
        catch(Exception e)
        {

        }
        return null;
    }
    public static String getFormattedImage(String product_image) {
        String imageUrl = product_image;
        if(imageUrl.length()>0)
            imageUrl=imageUrl.replace("\\/","/");

        return imageUrl;
    }
    public static void showUserMessage(Context context,String msg)
    {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
public static String formatMyDate(Date date)
{
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String dateToStr = format.format(date);
    return dateToStr;
}
   /* public static String getFormatedDateForMyPlan(String strDate,int moveIndex)
    {
       try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal=Calendar.getInstance();
            cal.setTime(format.parse(strDate));
            switch(moveIndex)
            {
                case ConstantVariables.MY_PLAN_PREVIOUS:
                    cal.add(Calendar.DATE, -1);
                    break;
                case ConstantVariables.MY_PLAN_NEXT:
                    cal.add(Calendar.DATE, 1);
                    break;

            }

            return format.format(cal.getTime());


        } catch (ParseException e) {
            // TODO Auto-generated catch block

        }

        return strDate;
    }*/
/*public boolean isMyPlanNextDate(String strDate)
{

    return false;
}*/


}
