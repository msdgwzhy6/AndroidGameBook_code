/* ������վ��http://www.androidbks.com
* �ǽ�iOS���ã�http://www.51work6.com
* �ǽ�iOS�������߿��ã�http://v.51work6.com
* �ǽ�iOS��������΢����http://weibo.com/u/3215753973
* ����΢����http://weibo.com/516inc
* �ٷ�csdn���ͣ�http://blog.csdn.net/tonny_guan
* QQ��1575716557 ���䣺jylong06@163.com
*/ 

package com.work6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class C_Activity extends Activity {

	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c);

		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(C_Activity.this, A_Activity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

	}

}
