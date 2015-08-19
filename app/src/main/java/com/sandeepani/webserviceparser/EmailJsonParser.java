package com.sandeepani.webserviceparser;

import android.content.Context;

import com.sandeepani.model.MessageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sandeep on 03-04-2015.
 */
public class EmailJsonParser {
    public static EmailJsonParser emailJsonParser = null;

    private EmailJsonParser() {
    }

    public static EmailJsonParser getInstance() {
        if (emailJsonParser == null) {
            emailJsonParser = new EmailJsonParser();
        }
        return emailJsonParser;
    }

    public ArrayList<MessageModel> getEmails(Context context, JSONObject object) {
        ArrayList<MessageModel> mailsList = new ArrayList<MessageModel>();
        if (object.has("numberOfConversations")) {
            try {
                int numberOfConversation = object.getInt("numberOfConversations");
                if (numberOfConversation > 0) {
                    if (object.has("conversations")) {
                        JSONArray conversationArray = object.getJSONArray("conversations");
                        int conversationSize = conversationArray.length();
                        for (int i = 0; i < conversationSize; i++) {
                            JSONObject conversationObj = conversationArray.getJSONObject(i);
                            MessageModel messageModel = new MessageModel();
                            if (conversationObj.has("fromId")) {
                                messageModel.setFromID(conversationObj.getString("fromId"));
                            }
                            if (conversationObj.has("title")) {
                                messageModel.setSubject(conversationObj.getString("title"));
                            }
                            if (conversationObj.has("toDate")) {
                                //messageModel.setDate(conversationObj.getString("toDate"));
                                messageModel.setDate("5:00PM");
                            }
                            if (conversationObj.has("messages")) {
                                JSONArray mesagesArray = conversationObj.getJSONArray("messages");
                                int messagesSize = mesagesArray.length();
                                if (messagesSize > 0) {
                                    JSONObject messageObj = mesagesArray.getJSONObject(0);
                                    if (messageObj.has("messageText")) {
                                        messageModel.setMessageText(messageObj.getString("messageText"));
                                    }
                                }
                            }
                            mailsList.add(messageModel);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return mailsList;
    }
}
