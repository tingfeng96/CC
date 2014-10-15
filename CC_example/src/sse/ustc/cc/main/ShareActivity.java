package sse.ustc.cc.main;

import java.util.ArrayList;
import java.util.List;

import sse.ustc.cc.adapter.FragmentAdapter;
import sse.ustc.cc.fragment.HistoryFragment;
import sse.ustc.cc.fragment.MyShareFragment;
import sse.ustc.cc_example.R;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class ShareActivity extends FragmentActivity {
    private static final String TAG = "ShareActivity";
    private ViewPager mPager;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();  
    private FragmentAdapter mFragmentAdapter;  
    private ImageView ivBottomLine;
    private TextView tvTabActivity, tvTabGroups;
    private MyShareFragment myShareFg; 
    private HistoryFragment mHistoryFg; 
    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_share);
        resources = getResources();
        InitWidth();
        InitTextView();
        InitViewPager();
    }

    private void InitTextView() {
        tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
        tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);
       

        tvTabActivity.setOnClickListener(new MyOnClickListener(0));
        tvTabGroups.setOnClickListener(new MyOnClickListener(1));
       
    }

    private void InitViewPager() {
    	myShareFg = new MyShareFragment(); 
    	mHistoryFg = new HistoryFragment(); 
        mPager = (ViewPager) findViewById(R.id.vPager);
        mFragmentList = new ArrayList<Fragment>();
        mFragmentList.add(myShareFg); 
        mFragmentList.add(mHistoryFg);
        mFragmentAdapter = new FragmentAdapter(  
                this.getSupportFragmentManager(), mFragmentList);  
        mPager.setAdapter(mFragmentAdapter);  
        mPager.setCurrentItem(0);  
        LayoutInflater mInflater = getLayoutInflater();
        //View activityView = mInflater.inflate(R.layout.activity_tab_chat, null);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitWidth() {
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
        Log.i("MainActivity", "offset=" + offset);

        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } 
                tvTabActivity.setTextColor(resources.getColor(R.color.white));
                break;
           
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
                    tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } 
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}