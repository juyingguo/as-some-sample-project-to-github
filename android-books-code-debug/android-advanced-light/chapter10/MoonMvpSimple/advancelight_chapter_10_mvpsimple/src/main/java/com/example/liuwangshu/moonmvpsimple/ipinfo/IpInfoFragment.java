package com.example.liuwangshu.moonmvpsimple.ipinfo;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.liuwangshu.moonmvpsimple.R;
import com.example.liuwangshu.moonmvpsimple.model.IpData;
import com.example.liuwangshu.moonmvpsimple.model.IpInfo;

/**
 * Created by Administrator on 2016/12/29 0029.
 */

public class IpInfoFragment extends Fragment implements IpInfoContract.View {
    private static final String TAG = "IpInfoFragment";
    private TextView tv_country;
    private TextView tv_area;
    private TextView tv_city;
    private Button bt_ipinfo;
    private Dialog mDialog;
    private IpInfoContract.Presenter mPresenter;
    public static IpInfoFragment newInstance() {
        Log.i(TAG,"newInstance(),enter");
        return new IpInfoFragment();
    }
    public IpInfoFragment(){
        Log.i(TAG,"IpInfoFragment() constructor call.");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView,enter");
        View root = inflater.inflate(R.layout.fragment_ipinfo, container, false);
        tv_country= (TextView) root.findViewById(R.id.tv_country);
        tv_area= (TextView) root.findViewById(R.id.tv_area);
        tv_city= (TextView) root.findViewById(R.id.tv_city);
        bt_ipinfo= (Button) root.findViewById(R.id.bt_ipinfo);
        return root;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated,enter");
        mDialog=new ProgressDialog(getActivity());
        mDialog.setTitle("获取数据中");
        bt_ipinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getIpInfo("39.155.184.147");
            }
        });
    }
    @Override
    public void setPresenter(IpInfoContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public void setIpInfo(IpInfo ipInfo) {
        if(ipInfo!=null){
            tv_country.setText(ipInfo.getCountry());
            tv_area.setText(ipInfo.getArea());
            tv_city.setText(ipInfo.getCity());
        }
    }

    /*@Override
    public void setIpInfo(String ipInfo) {
        if(ipInfo!=null){
            tv_country.setText(ipInfo);
        }
    }*/

    @Override
    public void showLoading() {
        mDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity().getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelTask();
    }
}
