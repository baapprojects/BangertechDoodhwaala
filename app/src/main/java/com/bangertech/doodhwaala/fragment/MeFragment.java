package com.bangertech.doodhwaala.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.bangertech.doodhwaala.activity.LoginActivity;
import com.bangertech.doodhwaala.activity.OrderHistory;
import com.bangertech.doodhwaala.activity.ShowAddress;


import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.application.DoodhwaalaApplication;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.helpshift.Helpshift;

public class MeFragment extends Fragment /*implements View.OnClickListener*/{
   // View mRootView;
//RelativeLayout rlAddress, rlOrderHistory, rlSupport, rlLogout;

    public static MeFragment newInstance() {
        return new MeFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  mRootView = inflater.inflate(R.layout.fragment_me, container, false);
       // init();
        //return inflater.inflate(R.layout.fragment_me, container, false);
        ((RelativeLayout) mRootView.findViewById(R.id.rlAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ShowAddress.class));
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

            }
        });
        ((RelativeLayout) mRootView.findViewById(R.id.rlOrderHistory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OrderHistory.class));

            }
        });
        ((RelativeLayout) mRootView.findViewById(R.id.rlSupport)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helpshift.showConversation(getActivity());
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });
        ((RelativeLayout) mRootView.findViewById(R.id.rlLogout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLogoutDialogbox(getActivity());
            }
        });
        return mRootView;
    }

    public void customLogoutDialogbox(Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);

        //TextView header = (TextView) dialog.findViewById(R.id.title);
        //header.setText(title);
        //TextView text = (TextView) dialog.findViewById(R.id.text);
        //text.setText(message);

        Button logout = (Button) dialog.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getInstance().resetUserDetails();
                DoodhwaalaApplication.isUserLoggedIn = false;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                startActivity(intent);
                getActivity().finish();
                Log.e("Bijendra", "Logout");
                dialog.dismiss();
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        //dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

 /*   private void init() {
        rlAddress = (RelativeLayout) mRootView.findViewById(R.id.rlAddress);
        rlOrderHistory = (RelativeLayout) mRootView.findViewById(R.id.rlOrderHistory);
        rlSupport = (RelativeLayout) mRootView.findViewById(R.id.rlSupport);
        rlLogout = (RelativeLayout) mRootView.findViewById(R.id.rlLogout);

        rlAddress.setOnClickListener(this);
        rlOrderHistory.setOnClickListener(this);
        rlSupport.setOnClickListener(this);
        rlLogout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlAddress:
                Log.e("Bijendra", "Address");
                break;
            case R.id.rlOrderHistory:

                Log.e("Bijendra", "OrderHistory");
                break;
            case R.id.rlSupport:
                Log.e("Bijendra", "Support");
                break;
            case R.id.rlLogout:
                Log.e("Bijendra", "Logout");
                break;
        }

    }*/
}
