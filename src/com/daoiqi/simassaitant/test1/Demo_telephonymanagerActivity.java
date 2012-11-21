package com.daoiqi.simassaitant.test1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.daoiqi.simassaitant.R;

public class Demo_telephonymanagerActivity extends Activity
{
    /** Called when the activity is first created. */
    
    ListView showView;
    
    //状态名数组
    String[] statusNames;
    
    //手机状态的集合
    ArrayList<String> statusValues = new ArrayList<String>();
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_telephonymanager);
        
        //获取系统的TelePhoneyManager
        TelephonyManager phoneManger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        
        //自己定义:各种状态的数组
        statusNames = getResources().getStringArray(R.array.statusNames);
        
        //获取SIM卡的状态数组
        String[] simState = getResources().getStringArray(R.array.simState);
        
        //获取代表电话网络类型的数组
        String[] phoneType = getResources().getStringArray(R.array.phoneType);
        
        //获取设备编号
        statusValues.add(phoneManger.getDeviceId());
        //系统平台版本
        statusValues.add(phoneManger.getDeviceSoftwareVersion() != null ? phoneManger.getDeviceSoftwareVersion()
                : "未知");
        //网路运营商代号
        statusValues.add(phoneManger.getNetworkOperator());
        // 获取手机网络类型
        statusValues.add(phoneType[phoneManger.getPhoneType()]);
        //网路运营商名称
        statusValues.add(phoneManger.getNetworkOperatorName());
        
        //获取设备所在位置
        statusValues.add(phoneManger.getCellLocation() != null ? phoneManger.getCellLocation()
                .toString()
                : "未知位置");
        
        //获取SIM可的国别
        statusValues.add(phoneManger.getSimCountryIso());
        
        //获取sim卡的序列号
        statusValues.add(phoneManger.getSimSerialNumber());
        
        //获取sim卡的状态
        statusValues.add(simState[phoneManger.getSimState()]);
        
        showView = (ListView) findViewById(R.id.listview2);
        ArrayList<Map<String, String>> status = new ArrayList<Map<String, String>>();
        //遍历statusValues集合，将statusNames、statusValues
        //的数据封装到List<Map<String , String>>集合中
        for (int i = 0; i < statusValues.size(); i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", statusNames[i]);
            map.put("value", statusValues.get(i));
            status.add(map);
        }
        // 使用SimpleAdapter封装List数据
        SimpleAdapter adapter = new SimpleAdapter(this, status, R.layout.line,
                new String[] { "name", "value" }, new int[] { R.id.user_name,
                        R.id.user_tel });
        // 为ListView设置Adapter
        showView.setAdapter(adapter);
        
    }
    
}
