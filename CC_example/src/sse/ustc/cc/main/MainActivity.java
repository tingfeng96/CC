package sse.ustc.cc.main;

import sse.ustc.cc.db.DataBase;
import sse.ustc.cc.utils.MyApplication;
import sse.ustc.cc.utils.ShareMethod;
import sse.ustc.cc.utils.SlidingMenu;
import sse.ustc.cc_example.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TabWidget;
import android.widget.TextView;


public class MainActivity extends Activity {
	private SlidingMenu mMenu;
	public ListView list[] = new ListView[7];
	private TabHost tabs = null;
	private ImageView imageView, courseView, shareView;
	private GestureDetector detector = null;
	private TextView menuButton = null;
	public Cursor[] cursor = new Cursor[7];
	public static DataBase db;
	public SimpleCursorAdapter adapter;
	private SharedPreferences preferences;
	// 定义手势动作两点之间的最小距离
	private final int FLIP_DISTANCE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		setContentView(R.layout.activity_main);
		MyApplication.getInstance().addActivity(this);

		db = new DataBase(MainActivity.this);
		preferences = getSharedPreferences("firstStart", Context.MODE_PRIVATE);

		/*
		 * 判断程序是否第一次运行，如果是创建数据库表
		 */
		if (preferences.getBoolean("firstStart", true)) {
			SingleInstance.createTable();
			(preferences.edit()).putBoolean("firstStart", false).commit();
			// finish();
		}
		imageView = (ImageView) findViewById(R.id.image);
		courseView = (ImageView) findViewById(R.id.one);
		shareView = (ImageView) findViewById(R.id.two);
		menuButton = (TextView) findViewById(R.id.menuButton);
		
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		
		list[0] = (ListView) findViewById(R.id.list0);
		list[1] = (ListView) findViewById(R.id.list1);
		list[2] = (ListView) findViewById(R.id.list2);
		list[3] = (ListView) findViewById(R.id.list3);
		list[4] = (ListView) findViewById(R.id.list4);
		list[5] = (ListView) findViewById(R.id.list5);
		list[6] = (ListView) findViewById(R.id.list6);
		tabs = (TabHost) findViewById(R.id.tabhost);
		// 创建手势检测器
		detector = new GestureDetector(this, new DetectorGestureListener());
		// 在配置任何的TabSpec之前，必须在TabHost上调用该方法
		tabs.setup();

		// 为主界面注册七个选项卡
		TabHost.TabSpec spec = null;
		addCard(spec, "tag1", R.id.list0, "日");
		addCard(spec, "tag2", R.id.list1, "一");
		addCard(spec, "tag3", R.id.list2, "二");
		addCard(spec, "tag4", R.id.list3, "三");
		addCard(spec, "tag5", R.id.list4, "四");
		addCard(spec, "tag6", R.id.list5, "五");
		addCard(spec, "tag7", R.id.list6, "六");

		// 修改tabHost选项卡中的字体的颜色
		TabWidget tabWidget = tabs.getTabWidget();
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			tv.setTextColor(0xff004499);
		}
		// 设置打开时默认的选项卡是当天的选项卡
		tabs.setCurrentTab(ShareMethod.getWeekDay());
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MyInfoSettingActivity.class);
				startActivity(intent);
			}
		});
		courseView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CourseActivity.class);
				startActivity(intent);
			}
		});
		shareView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ShareActivity.class);
				startActivity(intent);
			}
		});
		// 为菜单按钮绑定监听器
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMenu.toggle();
			}
		});
		// 用适配器为各选项卡添加所要显示的内容
		for (int i = 0; i < 7; i++) {
			cursor[i] = MainActivity.db.select(i);
			list[i].setAdapter(adapter(i));
		}

	}

	public void toggleMenu(View view) {
		mMenu.toggle();
	}

	// 子 方法:为主界面添加选项卡
	public void addCard(TabHost.TabSpec spec, String tag, int id, String name) {
		spec = tabs.newTabSpec(tag);
		spec.setContent(id);
		spec.setIndicator(name);
		tabs.addTab(spec);
	}

	/*
	 * 为每一个list提供数据适配器
	 */
	@SuppressWarnings("deprecation")
	public SimpleCursorAdapter adapter(int i) {
		return new SimpleCursorAdapter(this, R.layout.view_schedule_list, cursor[i],
				new String[] { "_id", "classes", "location", "teacher",
						"zhoushu" }, new int[] { R.id.number, R.id.ltext0,
						R.id.ltext1, R.id.ltext6, R.id.ltext7 });
	}

	/*
	 * 第一次运行时创建数据库表
	 */
	static class SingleInstance {
		static SingleInstance si;

		private SingleInstance() {
			for (int i = 0; i < 7; i++) {
				db.createTable(i);
			}
		}

		static SingleInstance createTable() {
			if (si == null)
				return si = new SingleInstance();
			return null;
		}
	}

	class DetectorGestureListener implements GestureDetector.OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		// 当用户在触屏上“滑过”时触发此方法
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int i = tabs.getCurrentTab();
			// 第一个触点事件的X坐标值减去第二个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从右向左滑动
			if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
				if (i < 6)
					tabs.setCurrentTab(i + 1);
				// float currentX = e2.getX();
				// list[i].setRight((int) (inialX - currentX));
				return true;
			}

			// 第二个触点事件的X坐标值减去第一个触点事件的X坐标值超过FLIP_DISTANCE，也就是手势从左向右滑动
			else if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
				if (i > 0)
					tabs.setCurrentTab(i - 1);
				return true;
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {

		}

	}

	// 覆写Activity中的onTouchEvent方法，将该Activity上的触碰事件交给GestureDetector处理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
}
