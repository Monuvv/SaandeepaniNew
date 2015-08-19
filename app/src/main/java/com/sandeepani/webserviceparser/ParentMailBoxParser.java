package com.sandeepani.webserviceparser;

import android.util.Log;

import com.sandeepani.model.MessageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by vijay on 3/30/15.
 */
public class ParentMailBoxParser {

    public static ParentMailBoxParser parentMailBoxParser = null;

    private ParentMailBoxParser() {

    }

    public static ParentMailBoxParser getInstance() {
        if (parentMailBoxParser == null) {
            parentMailBoxParser = new ParentMailBoxParser();
        }
        return parentMailBoxParser;
    }


    public ArrayList<HashMap<String, String>> getEmails(JSONObject object) {

        ArrayList<HashMap<String, String>> parentMailArrayList = new ArrayList<HashMap<String, String>>();
        LinkedHashMap<String, String> chatMap = null;
        if (object.has("numberOfConversations")) {
            try {
                int numberOfConversation = object.getInt("numberOfConversations");
                if (numberOfConversation > 0) {
                    if (object.has("conversations")) {
                        JSONArray conversationArray = object.getJSONArray("conversations");
                        int conversationSize = conversationArray.length();
                        for (int i = 0; i < conversationSize; i++) {
                            chatMap  = new LinkedHashMap<String, String>();
                            JSONObject conversationObj = conversationArray.getJSONObject(i);
                            MessageModel messageModel = new MessageModel();
                            if (conversationObj.has("fromId")) {
                                String fromId = conversationObj.getString("fromId");
                                chatMap.put("fromId", fromId);
                            }
                            if (conversationObj.has("fromName")) {
                                String fromId = conversationObj.getString("fromName");
                                chatMap.put("fromName", fromId);
                            }
                            if (conversationObj.has("toId")) {
                                String toId = conversationObj.getString("toId");
                                chatMap.put("toId", toId);
                            }
                            if (conversationObj.has("toName")) {
                                String toName = conversationObj.getString("toName");
                                chatMap.put("toName", toName);
                            }
                            if (conversationObj.has("threadId")) {
                                Long threadId = conversationObj.getLong("threadId");

                                String tid=String.valueOf(threadId);
                                chatMap.put("threadId", tid);
                            }

                            if (conversationObj.has("title")) {
                                String title = conversationObj.getString("title");
                                chatMap.put("title", title);
                            }
                            if (conversationObj.has("toDate")) {
                                String toDate = conversationObj.getString("toDate");
                                chatMap.put("toDate", toDate);
                            }
                            if (conversationObj.has("messages")) {
                                JSONArray mesagesArray = conversationObj.getJSONArray("messages");
                                int messagesSize = mesagesArray.length();
                                if (messagesSize > 0) {
                                    JSONObject messageObj = mesagesArray.getJSONObject(0);
                                    if (messageObj.has("messageText")) {
                                        String messageText = messageObj.getString("messageText");
                                        chatMap.put("messageText", messageText);
                                    }
                                    if (messageObj.has("messageId")) {
                                        String messageId = messageObj.getString("messageId");
                                        chatMap.put("messageId", messageId);
                                    }
                                }
                            }
                            parentMailArrayList.add(chatMap);
                            Log.i("parenMailMap", chatMap.toString());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.i("parentMailArrayList::", parentMailArrayList.toString());
        return parentMailArrayList;
    }

}