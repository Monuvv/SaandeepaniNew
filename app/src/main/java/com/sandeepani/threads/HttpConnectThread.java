package com.sandeepani.threads;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sandeepani.Networkcall.WebServiceCall;
import com.sandeepani.interfaces.AsyncTaskInterface;
import com.sandeepani.sharedPreference.StorageManager;
import com.sandeepani.utils.CommonUtils;
import com.sandeepani.view.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is an async task which is used for communicating with server.
 *
 * @author Sandeep
 */
public class HttpConnectThread extends AsyncTask<String, Integer, String> {

    private AsyncTaskInterface asyncTaskInterface; //Object for interface.
    public Context ctx;//activity context.
    private Dialog progressDialog;
    private JSONObject jObject;

    /**
     * Class Constructor.
     *
     * @param asyncTaskInterface interface object
     * @param jObject            Json object
     * @param ctx                activity context object
     */
    public HttpConnectThread(AsyncTaskInterface asyncTaskInterface, JSONObject jObject, Context ctx) {

        try {
            this.asyncTaskInterface = asyncTaskInterface; //asynctask reference.
            this.jObject = jObject;
            this.ctx = ctx;
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.getErrorLog("Error in HttpConnectThread Constructor:::::" + e.getMessage());
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progress bar is displayed.
        progressDialog = CommonUtils.getProgressDialog(ctx, ctx.getString(R.string.loading_text));
    }

    @Override
    protected String doInBackground(String... url) {
        String resultObject = null;
        if (CommonUtils.isNetworkAvailable(ctx)) {
            try {
                if (jObject != null) {
                    resultObject = sendJsonToServer(jObject, url[0]);
                } else {
                    resultObject = getJSONfromURL(url[0], ctx); // this url is called when a http request is made to get data for Add Appliance.
                }

                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resultObject;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            asyncTaskInterface.setAsyncTaskCompletionListener(result);
        } else {
            CommonUtils.getToastMessage(ctx, ctx.getString(R.string.network_error));
        }
        //progress bar dismissed.
        progressDialog.dismiss();
    }

    /**
     * user enter fieds data in the format of JSONObject sending registration
     * profile to server in json foramt
     *
     * @param jObject all profile fields data bind in profileJson Object
     * @param url     server url where our data to be store
     * @return return JSONObject, which contains user entered fields data
     */
    public static String sendJsonToServer(JSONObject jObject, String url) {
        JSONObject jObj = null;
        String json = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            //httpPost.setHeader("X-Auth-Token", WebServiceCall.getToken);

            HttpEntity entity;
            StringEntity s = new StringEntity(jObject.toString());
            s.setContentType("application/json");

            s.setContentEncoding(new BasicHeader("X-Auth-Token", WebServiceCall.getToken));
            entity = s;
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            json = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * This method communicates with the server and gets the Json object.
     *
     * @param url the customUrl to connect to.
     * @return returns json object containing the data.
     */
    public static String getJSONfromURL(String url, Context ctx) {

        InputStream is = null;
        String result = "";
        JSONObject jArray = null;
        // Http Post
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type", "application/json; charset=utf-8");
            httppost.setHeader("X-Auth-Token", StorageManager.readString(ctx, ctx.getString(R.string.pref_access_token), ""));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();

        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return result;
    }

}
