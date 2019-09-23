package com.example.zhaojing5.myapplication.View;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhaojing5.myapplication.R;
import com.example.zhaojing5.myapplication.aboutVolley.GsonRequest;
import com.example.zhaojing5.myapplication.aboutVolley.Weather;
import com.example.zhaojing5.myapplication.aboutVolley.WeatherInfo;
import com.example.zhaojing5.myapplication.aboutVolley.XmlRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UseHttpActivity extends Activity {
    public static final String TAG = UseHttpActivity.class.getSimpleName();
    public Unbinder unbinder;
    public RequestQueue requestQueue;
    private ImageLoader imageLoader;

    @BindView(R.id.btn_getHttp)
    Button btn_getHttp;
    @BindView(R.id.btn_postHttp)
    Button btn_postHttp;
    @BindView(R.id.btn_responseJson)
    Button btn_responseJson;
    @BindView(R.id.iv_imageloader)
    ImageView imageView;
    @BindView(R.id.networkImage)
    NetworkImageView networkImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_layout);
        unbinder = ButterKnife.bind(this);
        createRequestQueue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_getHttp,R.id.btn_postHttp,R.id.btn_responseJson,R.id.btn_imageLoader})
    public void onclick(View view){
        Log.d(TAG,"click view,id is " + view.getId());
        switch (view.getId()){
            case R.id.btn_getHttp:
                sendGetHttp();
                break;
            case R.id.btn_postHttp:
                sendPostHttp();
                break;
            case R.id.btn_responseJson:
                sendJsonArrayRequest();
                sendJsonHttp();
                break;
            case R.id.btn_imageLoader:
                sendImageRequest();
                useImageLoader();
                break;
            default:
                break;
        }
    }

    private void sendGetHttp(){
        //GET
        StringRequest stringRequest = new StringRequest("https://www.baidu.com/", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "response success! \n" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG,"response failed! \n" + volleyError.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void sendPostHttp(){
        //POST
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://www.baidu.com/", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "response success! \n" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("params1","value1");
                params.put("params2","value2");
                return params;
            }
        };
        requestQueue.add(stringRequest1);
    }

    private void sendJsonHttp(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://www.baidu.com/", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG,"json string response is" + jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG,"error response is " + volleyError.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendJsonArrayRequest(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://www.baidu.com/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d(TAG, "json array request succeed! \n" + jsonArray.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG,"json array request failed! \n " + volleyError.getMessage());
            }
        });
    }

    private void sendImageRequest(){
        ImageRequest imageRequest = new ImageRequest("https://www.baidu.com", new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(TAG, "onResponse: bitmap is " + bitmap == null ? "null" : "not null");
            }
        }, 100, 100, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG,"onErrorResponse " + volleyError.getMessage());
            }
        });
    }

    private void useImageLoader(){
        imageLoader = new ImageLoader(requestQueue, new LruImageCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        imageLoader.get("https://www.baidu.com/",imageListener);
    }

    private void useNetworkImage(){
        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl("https://www.baidu.com",imageLoader);
    }

    private void sendXmlRequest(){
        XmlRequest xmlRequest = new XmlRequest(Request.Method.GET, "http://flash.weather.com.cn/wmaps/xml/china.xml",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG,"xml request error " + volleyError.getMessage());
                    }
                },
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser xmlPullParser) {
                        try {
                            int eventType = xmlPullParser.getEventType();
                            while(eventType != XmlPullParser.END_DOCUMENT){
                                switch (eventType){
                                    case XmlPullParser.START_TAG:
                                        String nodeName = xmlPullParser.getName();
                                        if("city".equals(nodeName)){
                                            String pName = xmlPullParser.getAttributeName(0);
                                            Log.d("TAG","pName is " + pName);
                                        }
                                        break;
                                }
                                eventType = xmlPullParser.next();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        requestQueue.add(xmlRequest);
    }

    private void sendGsonRequest(){
        GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(Request.Method.GET, "http://www.weather.com.cn/data/sk/101010100.html",
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG,"response error " + volleyError.getMessage());
                    }
                },
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        WeatherInfo weatherInfo = weather.getWeatherinfo();
                        if(weatherInfo != null){
                            Log.d(TAG,"response info is " + weatherInfo.getCity() + " " + weatherInfo.getTemp() + " " + weatherInfo.getTime());
                        }else{
                            Log.d(TAG,"response null or parse gson error");
                        }
                    }
                },Weather.class);
        requestQueue.add(gsonRequest);
    }



    private void createRequestQueue(){
        requestQueue = Volley.newRequestQueue(this);
    }
}
