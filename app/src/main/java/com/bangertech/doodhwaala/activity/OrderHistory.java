package com.bangertech.doodhwaala.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bangertech.doodhwaala.adapter.OrderHistoryListAdapter;
import com.bangertech.doodhwaala.beans.BeanOrderedHistory;
import com.bangertech.doodhwaala.R;
import com.bangertech.doodhwaala.manager.AsyncResponse;
import com.bangertech.doodhwaala.manager.MyAsynTaskManager;
import com.bangertech.doodhwaala.utils.AppUrlList;
import com.bangertech.doodhwaala.utils.CGlobal;
import com.bangertech.doodhwaala.utils.CUtils;
import com.bangertech.doodhwaala.utils.ConstantVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annutech on 9/24/2015.
 */
public class OrderHistory extends AppCompatActivity implements AsyncResponse {
    private Toolbar app_bar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<BeanOrderedHistory> bucketOrderedHistory;
    private String balance="";
    private TextView tvBalance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        tvBalance = (TextView) findViewById(R.id.tvBalance);
        app_bar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(app_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.order_history));
       // getSupportActionBar().setDisplayShowTitleEnabled(false);

      /*  ((LinearLayout) app_bar.findViewById(R.id.llBackNavigate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderHistory.this.finish();
            }
        });*/

        mRecyclerView= (RecyclerView)findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(OrderHistory.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        bucketOrderedHistory=new ArrayList<BeanOrderedHistory>();

        fetchOrderHistoryFromServer();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void fetchOrderHistoryFromServer()
    {
        bucketOrderedHistory.clear();
        MyAsynTaskManager myAsyncTask=new MyAsynTaskManager();
        myAsyncTask.delegate=this;
        myAsyncTask.setupParamsAndUrl("fetchOrderHistory", OrderHistory.this, AppUrlList.ACTION_URL,
                new String[]{"module", "action", "user_id"},
                new String[]{"plans", "orderHistory", CGlobal.getCGlobalObject().getUserId()});
        myAsyncTask.execute();
    }
    private void showOrderedHistory()
    {
        mAdapter=new OrderHistoryListAdapter(OrderHistory.this,bucketOrderedHistory);
        mRecyclerView.setAdapter(mAdapter);
        balance=getResources().getString(R.string.order_history_balance)+ConstantVariables.RUPEE_SIGN+balance;
        tvBalance.setText(balance);
    }


    @Override
    public void backgroundProcessFinish(String from, String output) {
        if(from.equalsIgnoreCase("fetchOrderHistory"))
        {
            try {
                JSONObject jsonObject = new JSONObject(output);
                if (jsonObject.getBoolean("result"))
                {
                    balance=jsonObject.getString("balance");
                    JSONArray jsonArrayOrders=new JSONArray(jsonObject.getString("orders"));
                    int orderLength=jsonArrayOrders.length();
                    JSONObject order=null;
                    JSONArray jsonArrayProducts=null;
                    JSONObject product=null;
                    int productLength=0;
                    if(orderLength>0)
                    {
                        BeanOrderedHistory beanOrderedHistory=null;
                        for(int index=0;index<orderLength;index++)
                        {
                            order=jsonArrayOrders.getJSONObject(index);
                            beanOrderedHistory=new BeanOrderedHistory();
                            beanOrderedHistory.setOrderedDate(order.getString("date"));
                            beanOrderedHistory.setOrderedAmount(order.getString("cost"));
                            jsonArrayProducts=new JSONArray(order.getString("products"));
                            productLength=jsonArrayProducts.length();
                            for(int length=0;length<productLength;length++)
                            {
                                product=jsonArrayProducts.getJSONObject(length);
                                beanOrderedHistory.setOrderedItems(product.getString("product_name")+" x "+product.getString("quantity"));

                            }
                            bucketOrderedHistory.add(beanOrderedHistory);
                           // CUtils.printLog("order",order.toString(), ConstantVariables.LOG_TYPE.ERROR);

                        }
                    }

              }
            }
            catch(Exception e)
            {

            }
            if(bucketOrderedHistory.size()>0)
                 showOrderedHistory();
        }

    }
}
