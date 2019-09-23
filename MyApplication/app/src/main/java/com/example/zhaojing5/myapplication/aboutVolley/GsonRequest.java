package com.example.zhaojing5.myapplication.aboutVolley;

import android.net.http.HttpResponseCache;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class GsonRequest<T> extends Request<T> {
    private Response.Listener<T> mlistener;
    private Gson gson;
    private Class<T> clazz;

    public GsonRequest(String url, Response.ErrorListener errorlistener, Response.Listener<T> listener,Class<T> clazz) {
        super(url, errorlistener);
        this.mlistener = listener;
        gson = new Gson();
        this.clazz = clazz;
    }

    public GsonRequest(int method, String url, Response.ErrorListener errorlistener,Response.Listener<T> listener,Class<T> clazz) {
        super(method, url, errorlistener);
        this.mlistener = listener;
        gson = new Gson();
        this.clazz = clazz;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(gson.fromJson(jsonString,clazz),HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError());
        }
    }

    @Override
    protected void deliverResponse(T t) {
        mlistener.onResponse(t);
    }
}
