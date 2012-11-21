package com.daoiqi.simassaitant;

import com.daoiqi.simassaitant.SimpleSIM.ContextMenuOption;
import com.daoiqi.simassaitant.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContactInfoActivity extends Activity {
	public static final String COMMAND = "command";

	Bundle bundle;
	Contact oldcontact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.contact_info);

		Intent intent = getIntent();
		bundle = intent.getExtras();

		oldcontact = bundle.get("oldContact") == null ? null : (Contact) bundle
				.get("oldContact");

		final TextView name = (TextView) findViewById(R.id.editname);
		final TextView tel = (TextView) findViewById(R.id.edittel);
		final TextView id = (TextView) findViewById(R.id.editid);

		// if (bundle != null) {// 初始化数据，如果是新建，这不初始化数据
		// name.setText(bundle.getString(Contact.USER_NAME));
		// tel.setText(bundle.getString(Contact.USER_TEL));
		// id.setText(String.valueOf(Integer.parseInt(bundle
		// .getString(Contact.USER_ID)) + 1));
		// }
		String username = intent.getStringExtra(Contact.USER_NAME) != null ? intent
				.getStringExtra(Contact.USER_NAME) : "";
		String usertel = intent.getStringExtra(Contact.USER_TEL) != null ? intent
				.getStringExtra(Contact.USER_TEL) : "";
		String userid = intent.getStringExtra(Contact.USER_ID) != null ? String
				.valueOf(Integer.parseInt(intent
						.getStringExtra(Contact.USER_ID)) + 1) : "";
		name.setText(username);
		tel.setText(usertel);
		id.setText(userid);

		Button cancel = (Button) findViewById(R.id.cantactInfo_cancel);
		Button save = (Button) findViewById(R.id.cantactInfo_save);
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				int command = getIntent().getIntExtra(COMMAND, 0);
				SIMManager sm = new SIMManager(ContactInfoActivity.this);
				Intent intent = ContactInfoActivity.this.getIntent();
				Contact newcontact = new Contact(name.getText()
						.toString(), tel.getText().toString());
				int newcontactid = Integer.parseInt(id.getText()
						.toString()) - 1;
				newcontact.setId("" + newcontactid);
				switch (command) {
				case ContextMenuOption.NEW_SIM:// TODO 新建
					sm.saveContact(newcontact);
					System.out.println(newcontact);
					
					
					break;
				case ContextMenuOption.EDIT:
					Contact updatecontact = new Contact(name.getText()
							.toString(), tel.getText().toString());
					int updatecontactid = Integer.parseInt(id.getText()
							.toString()) - 1;
					updatecontact.setId("" + updatecontactid);
					System.out.println(ContactInfoActivity.this.oldcontact);
					
					sm.updateContact(ContactInfoActivity.this.oldcontact,newcontact);//更新
					System.out.println(newcontact);
//					intent.putExtra(Contact.USER_NAME, tel.getText().toString());
//					intent.putExtra(Contact.USER_TEL, tel.getText().toString());
//					intent.putExtra(Contact.USER_ID, tel.getText().toString());
					break;
				default:
					
					break;
				}
				ContactInfoActivity.this.setResult(RESULT_OK, intent);
				ContactInfoActivity.this.finish();
			}
		});
	}
}
