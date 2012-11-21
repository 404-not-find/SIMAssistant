package com.daoiqi.simassaitant;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.daoiqi.simassaitant.SimpleSIM.ContextMenuOption;

/**
 * 界面行为，有LiseView事件
 * 
 * @author d
 * 
 */
public class ActivityAction {

	/**
	 * 添加呼叫联系人事件
	 */
	public static void addCallContactAction(final ListView listview,
			final Activity activity) {
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 获得选中项的HashMap对象
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) listview
						.getItemAtPosition(arg2);
				String name = map.get(Contact.USER_NAME);
				String tel = map.get(Contact.USER_TEL);
				// Toast.makeText(getApplicationContext(),
				// "你选择了第"+arg2+"个Item，itemTitle的值是："+name+"itemContent的值是:"+tel,
				// Toast.LENGTH_SHORT).show();

				if (tel != null) {
					Intent phoneIntent = new Intent(
					// Intent.ACTION_CALL
							Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
					activity.startActivity(phoneIntent);
				} else {
					Toast.makeText(activity.getApplicationContext(), "不能输入为空",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public static void addPopMenu(final ListView listview,
			final Activity activity) {
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				return true;
			}
		});
	}

	/**
	 * 上下文菜单
	 * 
	 * @param listview
	 * @param activity
	 */
	public static void addContextMenu(final ListView listview,
			final Activity activity) {
		listview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			public void onCreateContextMenu(ContextMenu menu, View arg1,
					ContextMenuInfo arg2) {
				// TODO Auto-generated method stub
				menu.setHeaderTitle("操作");

				menu.add(Menu.NONE, SimpleSIM.ContextMenuOption.EDIT,
						Menu.NONE, "修改");
				menu.add(Menu.NONE, SimpleSIM.ContextMenuOption.CALL,
						Menu.NONE, "呼叫");
				menu.add(Menu.NONE, SimpleSIM.ContextMenuOption.SMS, Menu.NONE,
						"短信");
				menu.add(Menu.NONE, SimpleSIM.ContextMenuOption.NEW_SIM,
						Menu.NONE, "新建");
				menu.add(Menu.NONE, SimpleSIM.ContextMenuOption.DELETE,
						Menu.NONE, "删除");
				// Toast.makeText(activity.getApplicationContext(),"长按",
				// Toast.LENGTH_SHORT).show();

			}
		});
	}

	/**
	 * 添加上下文菜单事件
	 * 
	 * @param item
	 * @param activity
	 */
	public static void addContextMenuAction(MenuItem item, SimpleSIM activity) {

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		@SuppressWarnings("unchecked")
		Map<String, String> contactMap = (Map<String, String>) activity
				.getListView().getAdapter().getItem(menuInfo.position);

		Toast.makeText(activity.getApplicationContext(),
				contactMap.get(Contact.USER_TEL), Toast.LENGTH_SHORT).show();

		Contact selectedcontact = Contact.newContact(contactMap);

		switch (item.getItemId()) {
		case ContextMenuOption.EDIT:
			Intent edit = new Intent();
			edit.setClass(activity, ContactInfoActivity.class);

			// Bundle bundle = new Bundle();
			// bundle.putString(Contact.USER_NAME,
			// contact.get(Contact.USER_NAME));
			// bundle.putString(Contact.USER_TEL,
			// contact.get(Contact.USER_TEL));
			// bundle.putString(Contact.USER_ID, contact.get(Contact.USER_ID));
			// edit.putExtras(bundle);
			
			edit.putExtra("oldContact", Contact.newContact(contactMap));
			
			edit.putExtra(Contact.USER_NAME, contactMap.get(Contact.USER_NAME));
			edit.putExtra(Contact.USER_TEL, contactMap.get(Contact.USER_TEL));
			edit.putExtra(Contact.USER_ID, contactMap.get(Contact.USER_ID));
			edit.putExtra(ContactInfoActivity.COMMAND, SimpleSIM.ContextMenuOption.EDIT);

			activity.startActivityForResult(edit, 0);
			break;
		case ContextMenuOption.SMS:

			Uri smsToUri = Uri.parse("smsto:"
					+ contactMap.get(Contact.USER_TEL));
			Intent intent = new Intent(android.content.Intent.ACTION_SENDTO,
					smsToUri);
			// intent.putExtra("sms_body", "这是内容");
			activity.startActivity(intent);
			break;
		case ContextMenuOption.NEW_SIM:
			Intent newsim = new Intent();
			newsim.putExtra(ContactInfoActivity.COMMAND, SimpleSIM.ContextMenuOption.NEW_SIM);
			newsim.setClass(activity, ContactInfoActivity.class);
			activity.startActivityForResult(newsim, 0);
			break;
		case ContextMenuOption.DELETE:
			int state = activity.ma.deleteContact(selectedcontact);
			if (state == 0) {
				Toast.makeText(activity,
						"删除 " + selectedcontact.toString() + " 成功",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activity,
						"删除 " + selectedcontact.toString() + " 失败",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

	}

}
