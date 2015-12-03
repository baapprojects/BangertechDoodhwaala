package com.bangertech.doodhwaala.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangertech.doodhwaala.beans.BeanAddress;
import com.bangertech.doodhwaala.beans.BeanUserAddress;
import com.bangertech.doodhwaala.cinterfaces.IUserAddressList;
import com.bangertech.doodhwaala.R;

import java.util.List;

/**
 * Created by annutech on 9/22/2015.
 */
public class UserAddressListAdapter extends RecyclerView.Adapter<UserAddressViewHolder> {

 private Activity activity;
 private  IUserAddressList iUserAddressList;
 private  List<BeanAddress> _listAddress;


public UserAddressListAdapter(Activity activity, List<BeanAddress> listAddress) {
        this._listAddress = listAddress;
        this.activity = activity;
        iUserAddressList=(IUserAddressList)this.activity;

 }

@Override
public int getItemCount() {
        return _listAddress.size();
        }

@Override
public void onBindViewHolder(UserAddressViewHolder contactViewHolder, int i) {
    BeanAddress beanAddress=_listAddress.get(i);
    contactViewHolder.address.setText(beanAddress.getFullAddress());
    contactViewHolder.address.setChecked(beanAddress.isDefaultAddress());
    contactViewHolder.address.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                 iUserAddressList.selectedUserAddressOption(Integer.parseInt(v.getTag().toString()));
                }
        });
    contactViewHolder.address.setTag(i);
}


@Override
public UserAddressViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.user_address_card, viewGroup, false);
        return new UserAddressViewHolder(itemView);
        }


}
