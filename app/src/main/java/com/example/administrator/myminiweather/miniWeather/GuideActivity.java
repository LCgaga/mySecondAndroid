package com.example.administrator.myminiweather.miniWeather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.myminiweather.R;
import com.example.administrator.myminiweather.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LC on 2016/11/30.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPagerAdapter vpAdapter;
    private ViewPager viewPager;
    private List<View> views;
    private Button btn;

    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2, R.id.iv3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);//只用findViewById的时候是在guide里面找
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        Boolean first = sharedPreferences.getBoolean("first",true);
        if(first){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("first", false);
            editor.commit();
        }else{
            Intent i =new Intent(GuideActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        initView();
        initDots();
        btn=(Button)views.get(2).findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(GuideActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);//似乎一般都使用这种方式得到view的对象
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1, null));
        views.add(inflater.inflate(R.layout.page2, null));
        views.add(inflater.inflate(R.layout.page3, null));
        vpAdapter = new ViewPagerAdapter(views, this);//context就是上下文，就是指的是这个Activity或者Application啥的（毕竟继承关系，Context的继承级别很高）
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(vpAdapter);//Adapter的意思就是负责这个组件的管理，比如显示啊啥的（提供数据给它就行）
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a = 0; a < ids.length; a++) {
            if (a == position) {
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            } else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
