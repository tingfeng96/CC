package sse.ustc.cc.main;

import sse.ustc.cc.utils.MyApplication;
import sse.ustc.cc_example.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class MyInfoSettingActivity extends Activity {

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		setContentView(R.layout.view_myinfo_setting);
		MyApplication.getInstance().addActivity(this);//注册当前Activity
		
	}

	
}
