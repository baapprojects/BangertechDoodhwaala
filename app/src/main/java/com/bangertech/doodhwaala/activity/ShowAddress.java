package com.bangertech.doodhwaala.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bangertech.doodhwaala.beans.BeanAddress;
import com.bangertech.doodhwaala.beans.BeanUserAddress;
import com.bangertech.doodhwaala.cinterfaces.IUserAddressList;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.DialogManager;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;


import com.bangertech.doodhwaala.adapter.UserAddressListAdapter;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.PreferenceManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/21/2015.
 */
public class ShowAddress  extends AppCompatActivity implements  AsyncResponse,IUserAddressList {
    private Toolbar app_bar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private String userId="1";
    private List<BeanAddress> listAddress=new ArrayList<BeanAddress>();
    private TextView no_address;

    private int selectedAddressIndex=0,
            newSelectedAddressIndex=0;
    private boolean outPut = false;
    boolean isCalledFromConfirmationScreen=false;
    private MenuItem action_edit_address;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getIntent().getExtras().getBoolean("IS_FROM_CONFIRMATION_SCREEN"))
                isCalledFromConfirmationScreen = true;

        }
        catch(Exception e)
        {

        }
       setContentView(R.layout.activity_show_address);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //app_bar.setPadding(0, CUtils.getStatusBarHeight(ShowAddress.this), 0, 0);
        getSupportActionBar().setTitle(R.string.addresses);
        no_address = (TextView) findViewById(R.id.no_address);
        mRecyclerView= (RecyclerView)findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ShowAddress.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        listAddress.clear();

        fetchAddressesFromServer();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //CUtils.printLog("onActivityResult",String.valueOf(requestCode)+"<>"+String.valueOf(resultCode), ConstantVariables.LOG_TYPE.ERROR);
        if(requestCode==ConstantVariables.SUB_ACTIVITY_ADD_EDIT_ADDRESS && resultCode==RESULT_OK)
            fetchAddressesFromServer();
    }

    private void fetchAddressesFromServer()
    {
        listAddress.clear();
        MyAsynTaskManager  myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("showUserAddressList", ShowAddress.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id"},
                new String[]{"user", "fetchAddresses", PreferenceManager.getInstance().getUserId()});
        myAsyncTask.execute();
    }
    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("showUserAddressList"))
        {
          if(output!=null) {
              fetchUserAddressList(output);
          } else {
              DialogManager.showDialog(ShowAddress.this, "Server Error Occurred! Try Again!");
          }
        }
        if(from.equalsIgnoreCase("makeDefaultAddress"))
        {
            if(output!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(output);
                    if (jsonObject.getBoolean("result")) {
                        CUtils.showUserMessage(ShowAddress.this, getString(R.string.default_address_updated_successfully));
                        if (isCalledFromConfirmationScreen) {
                            CGlobal.getCGlobalObject().setAddressId(listAddress.get(newSelectedAddressIndex).getAddressId());
                            setResult(RESULT_OK);
                            this.finish();
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

                        } else {
                            this.finish();
                            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        }


                    } else
                        CUtils.showUserMessage(ShowAddress.this, jsonObject.getString("msg"));

                } catch (Exception e) {

                }
            } else {
                DialogManager.showDialog(ShowAddress.this, "Server Error Occurred! Try Again!");
            }
        }

    }

    private void saveChangedDefaultAddressOnServer()
    {
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("makeDefaultAddress", ShowAddress.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id","address_id"},
                new String[]{"user", "makeDefaultAddress", PreferenceManager.getInstance().getUserId(),
                        listAddress.get(newSelectedAddressIndex).getAddressId()});
        myAsyncTask.execute();
    }

  /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        return true;
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_address_list, menu);
        action_edit_address = menu.findItem(R.id.action_edit_address);
        MenuItem action_add_address = menu.findItem(R.id.action_add_address);
        if(outPut) {
            action_edit_address.setVisible(true);
            action_add_address.setVisible(true);

        } else {
            action_edit_address.setVisible(false);
            action_add_address.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_address:
                startActivityForResult(new Intent(ShowAddress.this, AddEditAddress.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(getBundleForAddEditAddress(true)), ConstantVariables.SUB_ACTIVITY_ADD_EDIT_ADDRESS);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                break;
            case R.id.action_edit_address://action_save_default_address
                startActivityForResult(new Intent(ShowAddress.this, AddEditAddress.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtras(getBundleForAddEditAddress(false)), ConstantVariables.SUB_ACTIVITY_ADD_EDIT_ADDRESS);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                break;
            /*case R.id.action_save_default_address://action_save_default_address
                checkToSaveDefaultAddressOnBack();
                break;*/
            case android.R.id.home:
                checkToSaveDefaultAddressOnBack();
                //onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserAddressList(String addressList)
    {
        if(parseAndFormateAddressList(addressList))
        {
            mAdapter=new UserAddressListAdapter(ShowAddress.this,listAddress);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    private boolean parseAndFormateAddressList(String addressList)
    {

        boolean isSuccess=true;
        try {
            JSONObject jsonObject = new JSONObject(addressList);
           /* if(jsonObject.getString("result").equalsIgnoreCase("true"))*/
            outPut = jsonObject.getBoolean("result");
            if(jsonObject.getBoolean("result"))
            {
               // action_edit_address.setVisible(true);
                no_address.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                listAddress.clear();
                JSONArray array=jsonObject.getJSONArray("addresses");
                if(array.length()>0) {
                    BeanAddress bua;
                    JSONObject obj;
                    for(int i=0;i<array.length();i++) {

                        obj=array.getJSONObject(i);
                        if(obj!=null) {
                            bua = new BeanAddress();
                            bua.setAddressId(obj.getString("address_id"));
                            bua.setFullAddress(obj.getString("address"));
                            bua.setDefaultAddress(obj.getBoolean("default_address"));
                            if(bua.isDefaultAddress()) {
                                selectedAddressIndex = i;//INITIALIZE
                                newSelectedAddressIndex=selectedAddressIndex;
                            }

                            listAddress.add(bua);
                        }
                    }
                }
            } else {
                //action_edit_address.setVisible(false);
                no_address.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            isSuccess=false;
        }
        invalidateOptionsMenu();
        return isSuccess;
    }

    @Override
    public void selectedUserAddressOption(int index) {
        CUtils.printLog("selectedUserAddressOption",String.valueOf(index), ConstantVariables.LOG_TYPE.ERROR);
        newSelectedAddressIndex=index;
        for(int i=0;i<listAddress.size();i++) {
            if (newSelectedAddressIndex == i)
                listAddress.get(i).setDefaultAddress(true);
            else
                listAddress.get(i).setDefaultAddress(false);        }
        mAdapter.notifyDataSetChanged();

    }

    private void checkToSaveDefaultAddressOnBack()
    {
       if(selectedAddressIndex!=newSelectedAddressIndex) {
           alertToSaveNewDefaultAddress();
       }
        else {
           this.finish();
           overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
       }
    }
    public void alertToSaveNewDefaultAddress() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveChangedDefaultAddressOnServer();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        /*Toast.makeText(ShowAddress.this, "No Clicked",
                                Toast.LENGTH_LONG).show();*/
                        ShowAddress.this.finish();
                        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save new default address?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private Bundle getBundleForAddEditAddress(boolean isAdd)
    {
        Bundle bundle=new Bundle();
        bundle.putBoolean("ADD_ADDRESS", isAdd);
        if(listAddress.size()>0) {
            bundle.putString("ADDRESS_ID", listAddress.get(newSelectedAddressIndex).getAddressId());
        }
        return bundle;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkToSaveDefaultAddressOnBack();
    }
}


