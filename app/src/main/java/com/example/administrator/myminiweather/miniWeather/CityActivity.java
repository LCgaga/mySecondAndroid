package com.example.administrator.myminiweather.miniWeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.myminiweather.R;
import com.example.administrator.myminiweather.app.MyApplication;
import com.example.administrator.myminiweather.bean.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityActivity extends Activity implements View.OnClickListener {

    private ImageView mBackBtn;
    private List<City> mCityList;
    private String cityCode = "101160101";
    private EditText mEditText;
    TextWatcher mTextWatcher;
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    ArrayList<Map<String, Object>> sData = new ArrayList<Map<String, Object>>();
    SimpleAdapter adapter;

    ListView mListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        MyApplication app = (MyApplication) getApplication();
        mCityList = app.getCityList();

        int len = mCityList.size();
        for (int i = 0; i < len; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("city", mCityList.get(i).getCity());
            item.put("number", mCityList.get(i).getNumber());
            mData.add(item);
        }

        mListView = (ListView) findViewById(R.id.list);
        adapter = new SimpleAdapter(this, mData, R.layout.simple_item,
                new String[]{"city", "number"}, new int[]{R.id.city, R.id.number});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
                Toast.makeText(CityActivity.this, "您选择了标题：" + position + "名称是" + mData.get(position).get("city"), Toast.LENGTH_LONG).show();
                cityCode = (String) mData.get(position).get("number");
            }
        });
        mTextWatcher = new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                temp = charSequence;
//                Log.d("myapp", "beforeTextChanged:" + temp);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("myapp", "onTextChanged:" + charSequence);
                sData.clear();
                String str = charSequence.toString();
                String citystr;
                int len = mCityList.size();
                for (int k = 0; k < len; k++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    citystr = mCityList.get(k).getCity();
                    if(citystr.length()>=str.length())

                    if (citystr.substring(0, str.length()).equals(str)) {
                        Log.d("onTextChanged", citystr);
                        Log.d("onTextChanged", str);
                        item.put("city", mCityList.get(k).getCity());
                        item.put("number", mCityList.get(k).getNumber());
                        sData.add(item);
                    }
                }
                adapter = new SimpleAdapter(CityActivity.this, sData, R.layout.simple_item,
                        new String[]{"city", "number"}, new int[]{R.id.city, R.id.number});
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                            long id) {
                        Toast.makeText(CityActivity.this, "您选择了标题：" + position + "名称是" + sData.get(position).get("city"), Toast.LENGTH_LONG).show();
                        cityCode = (String) sData.get(position).get("number");
                    }
                });

                Log.d("myapp", "onTextChanged:" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

                Log.d("miniWeather", "你要找的是" + editable);
            }
        };
        mEditText = (EditText) findViewById(R.id.search_edit);
        mEditText.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                Intent i = new Intent();
                i.putExtra("cityCode", cityCode);
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }
}
