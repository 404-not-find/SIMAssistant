package com.daoiqi.simassaitant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daoiqi.simassaitant.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SimpleSIM extends Activity {

	ListView listview;
	SIMManager ma;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_sim);

		listview = (ListView) findViewById(R.id.contactlist);
		// String aa[]={"ddd","dd"};
		//
		// ArrayAdapter<String> s= new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, aa);
		// listview.setAdapter(s);

		ma = new SIMManager(this);
		List<Contact> list = ma.getContactByPinyin();
		for (Contact contact : list) {
			System.out.println(contact);
		}

		// TODO hashmap 可能是个错误的选项，不方便读取，如果能直接Contact就好了
		List<Map<String, String>> l = new ArrayList<Map<String, String>>();
		for (Contact contact : list) {
			l.add(contact.toHashMap());
		}

		SimpleAdapter sa = new SimpleAdapter(this, l,
				R.layout.activity_simple_sim, new String[] { Contact.USER_NAME,
						Contact.USER_TEL }, new int[] { R.id.user_name,
						R.id.user_tel });

		listview.setAdapter(sa);

		addAction();
	}

	protected void addAction() {
		ActivityAction.addCallContactAction(listview, this);
		ActivityAction.addContextMenu(listview, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_simple_sim, menu);
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		ActivityAction.addContextMenuAction(item, this);
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//刷新列表最好了
		if (data != null ) {
			Toast.makeText(getApplicationContext(),
					data.getStringExtra(Contact.USER_TEL), Toast.LENGTH_SHORT)
					.show();
		}

	}

	public Contact getOneContact(int id) {
		// return listview.getAdapter().
		return null;
	}

	public ListView getListView() {
		return listview;
	}

	public static class ContextMenuOption {
		public static final int ADD = 1;
		public static final int EDIT = 2;
		public static final int DELETE = 3;
		public static final int CALL = 4;
		public static final int SMS = 5;
		public static final int NEW_SIM = 6;
	}

}
