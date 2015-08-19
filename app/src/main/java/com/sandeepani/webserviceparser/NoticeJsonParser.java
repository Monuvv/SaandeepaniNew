package com.sandeepani.webserviceparser;

import com.sandeepani.model.NoticeModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Antony on 06-06-2015.
 */
public class NoticeJsonParser {
    public static NoticeJsonParser noticeJsonParser = null;

    private NoticeJsonParser() {

    }

    public static NoticeJsonParser getInstance() {
        if (noticeJsonParser == null) {
            noticeJsonParser = new NoticeJsonParser();
        }
        return noticeJsonParser;
    }

    public ArrayList<NoticeModel> getNoticeList(JSONObject responseJson) {
        ArrayList<NoticeModel> noticeList = new ArrayList<NoticeModel>();
        try {
            //   JSONObject responseJson = new JSONObject(str);
            if (responseJson.has("Notices")) {
                JSONArray noticeArray = responseJson.getJSONArray("Notices");
                int noofnotice = noticeArray.length();
                for (int i = 0; i < noofnotice; i++) {
                    JSONObject noticeObj = noticeArray.getJSONObject(i);
                    NoticeModel noticeModel = new NoticeModel();
                    if (noticeObj.has("designation")) {
                        noticeModel.setDesignation(noticeObj.getString("designation"));
                    }
                    if (noticeObj.has("noticeId")) {
                        noticeModel.setNoticeId(noticeObj.getString("noticeId"));
                    }
                    if (noticeObj.has("topic")) {
                        noticeModel.setTopic(noticeObj.getString("topic"));
                    }
                    if (noticeObj.has("message")) {
                        noticeModel.setMessage(noticeObj.getString("message"));
                    }
                    if (noticeObj.has("senderName")) {
                        noticeModel.setSenderName(noticeObj.getString("senderName"));
                    }
                    if (noticeObj.has("signOff")) {
                        noticeModel.setSignOff(noticeObj.getString("signOff"));
                    }
                    if (noticeObj.has("day")) {
                        noticeModel.setDay(noticeObj.getString("day"));
                    }
                    if (noticeObj.has("date")) {
                        noticeModel.setDate(noticeObj.getString("date"));
                    }
                    if (noticeObj.has("salutation")) {
                        noticeModel.setSalutation(noticeObj.getString("salutation"));
                    }

                    noticeList.add(noticeModel);
                    noticeModel = null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeList;
    }
}