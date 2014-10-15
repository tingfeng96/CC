package sse.ustc.cc.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

//MyApplication�������洢ÿһ��activity����ʵ�ֹر�����activity�Ĳ���
public class MyApplication extends Application {
	
//������activity����
	private List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;
	
	private MyApplication(){}
	//�������ģʽ��ȡ��Ψһ��MyApplicationʵ��
	public static MyApplication getInstance(){
		if(instance == null)
			instance = new MyApplication();
		return instance;
	}
	//���activity��������
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	//�������е�activity��finish
	public void exitApp(){
		for(Activity activity : activityList){
			if(activity != null)
				activity.finish();
		}
		System.exit(0);
	}
	//��ջ���
	@Override
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
	
}
