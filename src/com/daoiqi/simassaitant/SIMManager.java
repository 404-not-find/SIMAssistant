package com.daoiqi.simassaitant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.telephony.TelephonyManager;

import com.urlshow.pinyin.PinYin;
import com.urlshow.pinyin.ToneType;

public class SIMManager {
	TelephonyManager tmanager;
	private Activity activity;
	private ContentResolver resolver;
	private Uri simUri;

	public static final String SIM_ID = "_id";// 值从0开始，其他地方如果需要要自己加1
	public static final String SIM_NAME = "name";
	public static final String SIM_TAG = "tag";// 从sim卡读取出来是name列，但是保存的时候确实tag
	public static final String SIM_NUMBER = "number";
	public static final String SIM_EMAIL = "emails";

	public SIMManager(Activity activity) {
		this.activity = activity;
		tmanager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
		resolver = activity.getContentResolver();
		simUri = Uri.parse("content://icc/adn");
	}

	/**
	 * 按sim顺序读取联系人的信息
	 * 
	 * @return
	 */
	public List<Contact> getContactList() {
		List<Contact> list = new ArrayList<Contact>();

		Cursor cursor = query("content://icc/adn");// android 1.6 above

		if (cursor == null) {
			return list;
		}
		// mString += "第一列：" + cursor.getColumnName(0) + "\n";
		// mString += "第二列：" + cursor.getColumnName(1) + "\n";
		// mString += "列数：" + cursor.getColumnCount() + "\n";
		// mString += "行数：" + cursor.getCount() + "\n";
		if (cursor != null) {

			String[] columnNames = cursor.getColumnNames();
			for (String string : columnNames) {
				System.out.println(string);
			}

			while (cursor.moveToNext()) {

				// 取得联系人名字
				int nameFieldColumnIndex = cursor.getColumnIndex("name");
				// mString += cursor.getString(nameFieldColumnIndex) + "    ";
				// 取得电话号码
				int numberFieldColumnIndex = cursor.getColumnIndex("number");
				int idFieldColumnIndex = cursor.getColumnIndex("_id");
				// mString += cursor.getString(numberFieldColumnIndex) +
				Contact contact = new Contact();
				contact.setName(cursor.getString(nameFieldColumnIndex));
				contact.setTel(cursor.getString(numberFieldColumnIndex));
				contact.setId(cursor.getString(idFieldColumnIndex));
				list.add(contact);
			}

			cursor.close();
		}

		return list;
	}

	/**
	 * 按拼音顺序排列，和现在手机的排序是一样的。
	 * 
	 * @return
	 */
	public List<Contact> getContactByPinyin() {
		List<Contact> contactList = getContactList();
		for (Contact contact : contactList) {
			String pinYin = PinYin.getPinYin(contact.getName(),
					ToneType.QUAN_PIN).toUpperCase();
			contact.setQuanpinName(pinYin);
		}

		Collections.sort(contactList);
		return contactList;
	}

	protected Cursor query(String str) {
		Intent intent = new Intent();
		intent.setData(Uri.parse(str));
		Uri uri = intent.getData();
		return activity.getContentResolver().query(uri, null, null, null, null);
	}

	/**
	 * 新建一个联系人
	 * 
	 * @param newContact
	 * @return
	 */
	public int saveContact(Contact newContact) {

		// add it on the SIM card
		ContentValues newSimValues = new ContentValues();
		newSimValues.put(SIMManager.SIM_TAG, newContact.name);
		newSimValues.put(SIMManager.SIM_NUMBER, newContact.tel);
		newSimValues.put(SIMManager.SIM_ID, newContact._id);
		System.out.println("保存联系人" + newContact.name);
		Uri newSimRow = resolver.insert(simUri, newSimValues);

		// TODO: further row values: "AdnFull", "/adn/0"
		// TODO: null could also mean that the contact name was too long?
		return 0;
	}

	public int updateContact(Contact oldContact, Contact newContact) {
		ContentValues values = new ContentValues();
		values.clear();
		values.put("tag", oldContact.name);
		values.put("number", oldContact.tel);
		values.put("newTag", newContact.name);
		values.put("newNumber", newContact.tel);
		// Uri uri = Uri.parse("content://icc/adn");
		int res = this.resolver.update(simUri, values, null, null);

		return 0;
	}

	/**
	 * 删除一个联系人，依赖于联系人的id
	 * 
	 * @param contact
	 * @return 返回删除记录数，如果是1，删除成功，如果是0，失败
	 */
	public int deleteContact(Contact contact) {

		String tag = contact.name.trim();
		String tel = contact.tel.trim();
		String sql = SIM_TAG + "= '"+tag +"' AND "
					+SIM_NUMBER +"= '"+tel+"'";
		System.out.println(sql);
		int count = resolver.delete(
				simUri,sql
				, null);
		System.out.println("删除成功");
		System.out.println(count);

		return count;
	}

	/**
	 * 检查sim状态
	 * 
	 * @return
	 */
	public int getSimState() {
		return tmanager.getSimState();
	}
	
	public static enum StatusCode{
		SUCCESS,
	}

	public static void main(String[] args) {
		System.out.println(PinYin.getPinYin("z重", ToneType.QUAN_PIN));
	}

}
