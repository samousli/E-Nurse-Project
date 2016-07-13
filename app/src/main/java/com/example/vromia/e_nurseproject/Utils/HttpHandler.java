package com.example.vromia.e_nurseproject.Utils;

/**
 * Created by samousli on 5/6/2015.
 */

import com.loopj.android.http.*;

public class HttpHandler {
    private static final String BASE_URL = "http://nikozisi.webpages.auth.gr/enurse/";

    private static AsyncHttpClient client = new SyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get((url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post((url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}