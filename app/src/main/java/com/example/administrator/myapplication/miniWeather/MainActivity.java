package com.example.administrator.myapplication.miniWeather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.bean.TodayWeather;
import com.example.administrator.myapplication.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/21.
 */
public class MainActivity extends Activity implements OnClickListener {

    private TextView cityTv, timeTv, humidityTv, weekTv,
            weekTv_b1, weekTv_today, weekTv_a1,weekTv_a2, weekTv_a3, weekTv_a4,
            pmDataTv, pmQualityTv, temperature_now,
            temperatureTv, climateTv, windTv, city_name_Tv,
            climateTv_b1,climateTv_today, climateTv_a1, climateTv_a2, climateTv_a3, climateTv_a4;
    private ImageView weatherImg, pmImg,
            weatherImg_b1,weatherImg_today, weatherImg_a1, weatherImg_a2, weatherImg_a3, weatherImg_a4;
    private ImageView mCitySelect;
    private ImageView mUpdateBtn;
    private ImageView mLocation;
    private ProgressBar pbar;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private static final int UPDATE_TODAY_WEATHER = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("myWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mLocation=(ImageView)findViewById(R.id.title_location);
        mLocation.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);
        pbar = (ProgressBar) findViewById(R.id.title_update_progress);

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        initView();
    }

    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        temperature_now = (TextView) findViewById(R.id.temperature_now);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);

        weekTv = (TextView) findViewById(R.id.week_today);
        weekTv_today=(TextView) findViewById(R.id.today_day);
        weekTv_a1 = (TextView) findViewById(R.id.a1_day);
        weekTv_a2 = (TextView) findViewById(R.id.a2_day);
        weekTv_a3 = (TextView) findViewById(R.id.a3_day);
        weekTv_a4 = (TextView) findViewById(R.id.a4_day);
        weekTv_b1 = (TextView) findViewById(R.id.b1_day);

        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality
        );
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature
        );
        climateTv = (TextView) findViewById(R.id.climate);
        climateTv_today = (TextView) findViewById(R.id.today_weather);
        climateTv_a1 = (TextView) findViewById(R.id.a1_weather);
        climateTv_a2 = (TextView) findViewById(R.id.a2_weather);
        climateTv_a3 = (TextView) findViewById(R.id.a3_weather);
        climateTv_a4 = (TextView) findViewById(R.id.a4_weather);
        climateTv_b1 = (TextView) findViewById(R.id.b1_weather);

        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        weatherImg_today = (ImageView) findViewById(R.id.today_weather_img);
        weatherImg_a1 = (ImageView) findViewById(R.id.a1_weather_img);
        weatherImg_a2 = (ImageView) findViewById(R.id.a2_weather_img);
        weatherImg_a3 = (ImageView) findViewById(R.id.a3_weather_img);
        weatherImg_a4 = (ImageView) findViewById(R.id.a4_weather_img);
        weatherImg_b1 = (ImageView) findViewById(R.id.b1_weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        temperature_now.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }//findId后并置为N/A

    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setB1_day(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setA1_day(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setA2_day(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setA3_day(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 5) {
                                eventType = xmlPullParser.next();
                                todayWeather.setA4_day(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type_1")) {
                                if (typeCount == 0) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setB1_type(xmlPullParser.getText());
                                }
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type")) {
                                Log.d("myWeather type", "" + typeCount);
                                if (typeCount == 2) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setType(xmlPullParser.getText());
                                } else if (typeCount == 4) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setA1_type(xmlPullParser.getText());
                                } else if (typeCount == 6) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setA2_type(xmlPullParser.getText());
                                } else if (typeCount == 8) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setA3_type(xmlPullParser.getText());
                                } else if (typeCount == 10) {
                                    eventType = xmlPullParser.next();
                                    todayWeather.setA4_type(xmlPullParser.getText());
                                }
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }//在这里解析XML成TodayWeather对象后返回

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
//                    Toast.makeText(MainActivity.this,address,Toast.LENGTH_SHORT).show();
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();

                    Log.d("myWeather", responseStr);
//                    parseXML(responseStr);

                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {

                        Log.d("myWeather", todayWeather.toString());
                    }
                    Message msg = new Message();
                    msg.what = UPDATE_TODAY_WEATHER;
                    msg.obj = todayWeather;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        temperature_now.setText(todayWeather.getWendu() + "℃");
        pmDataTv.setText(todayWeather.getPm25());

        int pm25 = Integer.parseInt(todayWeather.getPm25());
        if (pm25 <= 50)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        if (pm25 > 50 && pm25 <= 100)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        if (pm25 > 100 && pm25 <= 150)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        if (pm25 > 150 && pm25 <= 200)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        if (pm25 > 200 && pm25 <= 300)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        if (pm25 > 300)
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);

        setType(todayWeather.getType(), weatherImg);
        setType(todayWeather.getType(),weatherImg_today);
        setType(todayWeather.getA1_type(), weatherImg_a1);
        setType(todayWeather.getA2_type(), weatherImg_a2);
        setType(todayWeather.getA3_type(), weatherImg_a3);
        setType(todayWeather.getA4_type(), weatherImg_a4);
        setType(todayWeather.getB1_type(), weatherImg_b1);

        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        weekTv_today.setText(todayWeather.getDate());
        weekTv_a1.setText(todayWeather.getA1_day());
        weekTv_a2.setText(todayWeather.getA2_day());
        weekTv_a3.setText(todayWeather.getA3_day());
        weekTv_a4.setText(todayWeather.getA4_day());
        weekTv_b1.setText(todayWeather.getB1_day());

        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        climateTv_today.setText(todayWeather.getType());
        climateTv_a1.setText(todayWeather.getA1_type());
        climateTv_a2.setText(todayWeather.getA2_type());
        climateTv_a3.setText(todayWeather.getA3_type());
        climateTv_a4.setText(todayWeather.getA4_type());
        climateTv_b1.setText(todayWeather.getB1_type());
        windTv.setText("风力:" + todayWeather.getFengli());
        pbar.setVisibility(View.GONE);
        mUpdateBtn.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }

    public void setType(String weatherType, ImageView weatherImg) {
        if (weatherType.equals("暴雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        if (weatherType.equals("暴雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        if (weatherType.equals("大暴雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        if (weatherType.equals("大雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        if (weatherType.equals("大雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        if (weatherType.equals("多雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        if (weatherType.equals("雷阵雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        if (weatherType.equals("雷阵冰雹"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        if (weatherType.equals("晴"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        if (weatherType.equals("沙尘暴"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        if (weatherType.equals("特大暴雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        if (weatherType.equals("雾"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        if (weatherType.equals("小雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        if (weatherType.equals("小雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        if (weatherType.equals("阴"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        if (weatherType.equals("阵雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        if (weatherType.equals("阵雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        if (weatherType.equals("雨夹雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        if (weatherType.equals("中雪"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        if (weatherType.equals("中雨"))
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.title_location){
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
            mLocationOption.setMockEnable(true);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationListener = new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {

                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            //可在其中解析amapLocation获取相应内容。
                            Log.e("AmapSuccess",aMapLocation.getCity());
                            Toast.makeText(MainActivity.this, "城市是"+aMapLocation.getCity(), Toast.LENGTH_LONG).show();
                        }else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError","location Error, ErrCode:"
                                    + aMapLocation.getErrorCode() + ", errInfo:"
                                    + aMapLocation.getErrorInfo());
                        }
                    }
                }
            };
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(mLocationListener);
            //启动定位
            mLocationClient.startLocation();
        }

        if (v.getId() == R.id.title_city_manager) {
            Intent i = new Intent(this, CityActivity.class);
//          startActivity(i);
            startActivityForResult(i, 1);
        }

        if (v.getId() == R.id.title_update_btn) {

//            Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_SHORT).show();
            pbar.setVisibility(View.VISIBLE);
            mUpdateBtn.setVisibility(View.INVISIBLE);
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络OK");
//                Toast.makeText(MainActivity.this,cityCode,Toast.LENGTH_SHORT).show();
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为：" + newCityCode);
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("main_city_code", newCityCode);
            editor.commit();
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
}