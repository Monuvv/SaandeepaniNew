package com.sandeepani.Networkcall;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sandeepani.sharedPreference.PrefManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.view.R;
import com.sandeepani.volley.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vijay on 3/12/2015.
 */
public class WebServiceCall {

    public static String getToken;
    private static Activity mContext;
    static PrefManager sharedPref;
    Context context;
    private RequestCompletion mRequestCompletion;

    public WebServiceCall(Activity context) {
        mContext = context;
        mRequestCompletion = (RequestCompletion) context;
    }

    public void registerForPushApi(String regId) {
        String request_URL = mContext.getString(R.string.base_url) + "/app/registerForpush";
        LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
        parmKeyValue.put("platform", "1");

            //regId="APA91bEq-Udjm-1XYKrimadcYW0DMU8Iifs92wAV-oU5uUIq1Yj39d8Yz0LwBAVSyDekHNpEV8YaFk2vojZvIOsD3Z_ah-kvT7aYTTIoqXmZZGBbQzUgsf7rKap7uX1_KmnnKkaDxAKF";

        parmKeyValue.put("token", regId);
        JSONObject headerBodyParam = new JSONObject(parmKeyValue);
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, request_URL, headerBodyParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {
                            // handle response
                            CommonUtils.getLogs("===Response:::" + responseJson);
                            // handle response
                            Log.d("JSON Response", responseJson.toString());
                            //   TokenID(responseJson);
                            mRequestCompletion.onRequestCompletion(responseJson, null);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleNetworkError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            6000,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue.
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void unRegisterDevice(String regId){

        {
            String request_URL = mContext.getString(R.string.pushbotsUrl);
            LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
            parmKeyValue.put("platform", "1");

            parmKeyValue.put("token", regId);
            JSONObject headerBodyParam = new JSONObject(parmKeyValue);
            JsonObjectRequest req;
            try {
                req = new JsonObjectRequest(Request.Method.PUT, request_URL, headerBodyParam,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject responseJson) {
                                // handle response
                                CommonUtils.getLogs("===Response:::" + responseJson);
                                // handle response
                                Log.d("JSON Response", responseJson.toString());
                                //   TokenID(responseJson);
                                mRequestCompletion.onRequestCompletion(responseJson, null);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleNetworkError(error);
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("x-pushbots-appid", "550e9e371d0ab1de488b4569");
                        System.out.println("Headers: = " + headers);
                        return headers;
                    }
                };
                Log.d("Req", req.toString());
                req.setRetryPolicy(
                        new DefaultRetryPolicy(
                                6000,
                                1,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to volley request queue.
                AppController.getInstance().addToRequestQueue(req);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    public void LoginRequestApi(String userName, String Password) {
        String request_URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.login_url_endpoint);
        Log.d("LOGIN URL", request_URL);
        LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
        parmKeyValue.put("username", userName);
        parmKeyValue.put("password", Password);
        JSONObject headerBodyParam = new JSONObject(parmKeyValue);
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, request_URL, headerBodyParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {
                            // handle response
                            Log.d("JSON Response", responseJson.toString());
                            TokenID(responseJson);
                            mRequestCompletion.onRequestCompletion(responseJson, null);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleNetworkError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue.
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void postToServer(JSONObject jsonData, String postURL) {
        //  String request_URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.login_url_endpoint);
        Log.d("postURL", postURL);
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, postURL, jsonData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {

                            CommonUtils.getLogs("===Response:::" + responseJson);
                            // handle response
                            Log.d("JSON Response", responseJson.toString());
                            //   TokenID(responseJson);
                            mRequestCompletion.onRequestCompletion(responseJson, null);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CommonUtils.getLogs("ErroRRR:::" + error);
                            handleNetworkError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(new DefaultRetryPolicy(6000,
                    1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue.
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //used fot json array response
    public void getCallRequest(String url) {

        try {
            JsonArrayRequest req = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Log.d("JsonArrayObject", response.toString());
                            mRequestCompletion.onRequestCompletion(null, response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleNetworkError(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };

            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            6000,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void getJsonObjectResponse(String url) {
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            // handle response
                            Log.d("JsonObject-->>", response.toString());
                            mRequestCompletion.onRequestCompletion(response, null);
                        }
                    }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JsonObjecterror-->>", error.toString());
                    handleNetworkError(error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            req.setShouldCache(true);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param error volley netwotrk error handling
     */
    public void handleNetworkError(VolleyError error) {

        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
        // For AuthFailure, you can re login with user credentials.
        // For ClientError, 400 & 401, Errors happening on client side when sending api request.
        // In this case you can check how client is forming the api and debug accordingly.
        // For ServerError 5xx, you can do retry or handle accordingly.
        NetworkResponse response = error.networkResponse;
        String json = null;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                case 401:
                    json = new String(response.data);
                    Log.d("Volley error", "" + json);
//                     json = trimMessage(json, "message");
//                     if(json != null) displayMessage(json);
                    break;
            }
        }
        if (error instanceof NetworkError) {
        } else if (error instanceof ClientError) {
            mRequestCompletion.onRequestCompletionError("Something wrong.Please try again.");
            Log.e("ClientError::", error.getMessage());
        } else if (error instanceof ServerError) {
            mRequestCompletion.onRequestCompletionError("Something wrong.Please try again.");
            Log.e("ServerError::", error.toString());
        } else if (error instanceof AuthFailureError) {
            mRequestCompletion.onRequestCompletionError("AuthFailureError");
            Log.e("AuthFailureError::", error.toString());
        } else if (error instanceof ParseError) {
            mRequestCompletion.onRequestCompletionError("Something wrong.Please try again.");
            Log.e("ParseError", error.toString());
        } else if (error instanceof NoConnectionError) {
            mRequestCompletion.onRequestCompletionError("Please connect to network...");
            Log.e("NoConnectionError", error.toString());
        } else if (error instanceof TimeoutError) {
            mRequestCompletion.onRequestCompletionError("Connection Timeout");
            Log.e("TimeoutError", error.toString());
        }

    }

    /**
     * Storing the login data in shared preference
     */
    public static String TokenID(JSONObject response) {
        CommonUtils.getLogs("Login Access Token Stored");
        Log.d("Enterted...", "TokenID");
        sharedPref = new PrefManager(mContext);
        try {
            getToken = response.getString("access_token");
            sharedPref.SaveLoginTokenInInSharedPref(getToken);
            Log.d("getToken", getToken);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getToken;
    }

    //--------------------------------
    public void changePasswordRequestApi(String oldPassword, String newPassword, String confirmPassword) {
        String request_URL = mContext.getString(R.string.base_url) + mContext.getString(R.string.changepwd_url);
        Log.d("change pwd URL", request_URL);
        LinkedHashMap<String, String> parmKeyValue = new LinkedHashMap<String, String>();
        parmKeyValue.put("password", oldPassword);
        parmKeyValue.put("password_new", newPassword);
        parmKeyValue.put("password_new2", confirmPassword);
        JSONObject headerBodyParam = new JSONObject(parmKeyValue);
        JsonObjectRequest req;
        try {
            req = new JsonObjectRequest(Request.Method.POST, request_URL, headerBodyParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseJson) {
                            // handle response
                            Log.d("JSON Response", responseJson.toString());
                            mRequestCompletion.onRequestCompletion(responseJson, null);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            handleNetworkError(error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", getToken);
                    System.out.println("Headers: = " + headers);
                    return headers;
                }
            };
            Log.d("Req", req.toString());
            req.setRetryPolicy(
                    new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            req.setShouldCache(true);
            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //---------------------------------------
}