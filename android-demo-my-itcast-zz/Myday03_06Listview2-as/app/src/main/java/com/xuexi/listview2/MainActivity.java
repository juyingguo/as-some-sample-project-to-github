package com.xuexi.listview2;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	Button btn;
	private ListView lv;
	private final String TAG ="MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lv);
		
		btn = (Button) findViewById(R.id.btn_getItemcontacts);
		
	}
	
	//点击事件，当点击时加载条目内容
	public void getItemcontacts(View view){
		
		lv.setAdapter(new MyAdapter());
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 30;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
						
			View view = null;
			if(convertView==null){
				//1.布局填充器的第一种写法
				view = View.inflate(MainActivity.this, R.layout.item_contact, new RelativeLayout(getBaseContext()));
				//view = View.inflate(MainActivity.this, R.layout.item_contact, lv);
				//2.第二种写法，布局填充器
				//LayoutInflater flater = LayoutInflater.from(getApplicationContext());
				//view = flater.inflate(R.layout.item_contact, new RelativeLayout(getBaseContext()));//第三个参数为RelativeLayout.或者不设置即为null
				//Log.i(TAG, "MyAdapter-中inflate的三个参数的测试"+view.toString());
				//3.第三种写法,通过系统服务
//				LayoutInflater flater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				view = flater.inflate(R.layout.item_contacts, null);
				
				
			}else{
				view = convertView;//如果有就使用历史记录
			}
			
			return view;//返回生成的视图组件
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}
}
