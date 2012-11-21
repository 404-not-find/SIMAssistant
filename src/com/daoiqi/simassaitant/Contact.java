package com.daoiqi.simassaitant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 联系人
 * 
 * @author d
 * 
 */
public class Contact implements Comparable<Contact>,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -35858143052159730L;
	protected String name;
	protected String tel;
	protected String _id;

	protected String quanpinName;

	/**sim卡的姓名字段*/
	public static final String USER_NAME = "name";
	/**sim卡的电话字段*/
	public static final String USER_TEL = "tel";
	/**sim卡的序列字段*/
	public static final String USER_ID = "id";
	
	public static final String USER_QUANPIN = "quanpin";
	public Contact() {
		// TODO Auto-generated constructor stub
	}

	public Contact(String name, String tel) {
		this.name = name.trim();
		this.tel = tel.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel.trim();
	}

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id.trim();
	}

	public String getQuanpinName() {
		return quanpinName;
	}

	public void setQuanpinName(String quanpinName) {
		this.quanpinName = quanpinName.trim();
	}

	@Override
	public String toString() {
		return _id +" "+name + " " + tel;
	}

	public int compareTo(Contact another) {
		return this.getQuanpinName().compareTo( another.getQuanpinName());
	}
	
	/**
	 * 转化成Map对象，可以用来放到ListView中
	 * @return Map
	 */
	public Map<String,String> toHashMap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put(USER_NAME, name);
		map.put(USER_TEL, tel);
		map.put(USER_ID, _id);
		map.put(USER_QUANPIN, quanpinName);
		return map;
	}
	
	/**
	 * 根据Map里面的值构建一个联系人，如果存在，则赋值为空字符串
	 * @param contact Map联系人
	 * @return 一个联系人
	 */
	public static Contact newContact(Map<String,String> contact){
		Contact newContact = new Contact();
		newContact.setName(contact.get(Contact.USER_NAME)==null?"":contact.get(Contact.USER_NAME));
		newContact.setTel(contact.get(Contact.USER_TEL)==null?"":contact.get(Contact.USER_TEL));
		newContact.setId(contact.get(Contact.USER_ID)==null?"":contact.get(Contact.USER_ID));
		return newContact;
	}

}
