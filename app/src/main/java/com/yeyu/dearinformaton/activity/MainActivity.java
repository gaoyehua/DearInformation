package com.yeyu.dearinformaton.activity;

import java.util.Random;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yeyu.dearinformaton.Constant.Constant;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.MyLinearLayout;
import com.yeyu.dearinformaton.View.SlideMenu;

/**
 * Created by gaoyehua on 2016/9/4.
 */
public class MainActivity extends Activity {
    private ListView menu_listview,main_listview;
    private SlideMenu slideMenu;
    private ImageView iv_head;
    private MyLinearLayout my_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        menu_listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Constant.sCheeseStrings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });
        main_listview.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Constant.NAMES){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView==null?super.getView(position, convertView, parent):convertView;
                //先缩小view
                ViewHelper.setScaleX(view, 0.5f);
                ViewHelper.setScaleY(view, 0.5f);
                //以属性动画放大
                ViewPropertyAnimator.animate(view).scaleX(1).setDuration(350).start();
                ViewPropertyAnimator.animate(view).scaleY(1).setDuration(350).start();
                return view;
            }
        });


        slideMenu.setOnDragStateChangeListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
//				Log.e("tag", "onOpen");
                menu_listview.smoothScrollToPosition(new Random().nextInt(menu_listview.getCount()));
            }
            @Override
            public void onDraging(float fraction) {
//				Log.e("tag", "onDraging fraction:"+fraction);
                ViewHelper.setAlpha(iv_head,1-fraction);
            }
            @Override
            public void onClose() {
//				Log.e("tag", "onClose");
                ViewPropertyAnimator.animate(iv_head).translationXBy(15)
                        .setInterpolator(new CycleInterpolator(4))
                        .setDuration(500)
                        .start();
            }
        });

        my_layout.setSlideMenu(slideMenu);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        menu_listview = (ListView) findViewById(R.id.menu_listview);
        main_listview = (ListView) findViewById(R.id.main_listview);
        slideMenu = (SlideMenu) findViewById(R.id.slideMenu);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        my_layout = (MyLinearLayout) findViewById(R.id.my_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
