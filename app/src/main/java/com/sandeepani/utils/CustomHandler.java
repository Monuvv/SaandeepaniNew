package com.sandeepani.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sandeepani.view.CommonToApp.NotificationActivity;
import com.pushbots.push.PBNotificationIntent;
import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CustomHandler extends BroadcastReceiver
{
    private static final String TAG = "CustomHandler";
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);
        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
            //Check for Pushbots Instance
			Pushbots pushInstance = Pushbots.sharedInstance();
            if(!pushInstance.isInitialized()){
                Log.d("Initializing Pushbots","");
                Pushbots.sharedInstance().init(context.getApplicationContext());
            }
			
			//Clear Notification array
            if(PBNotificationIntent.notificationsArray != null){
                PBNotificationIntent.notificationsArray = null;
            }
			
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_OPEN);
            Log.w(TAG, "User clicked notification with Message: " + PushdataOpen.get("msg"));
			
			//Start lanuch Activity
			String packageName = context.getPackageName();
            Intent resultIntent = new Intent( context , NotificationActivity.class);
           resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
		    resultIntent.putExtras(intent.getBundleExtra("pushData"));
			Pushbots.sharedInstance().startActivity(resultIntent);
			
            // Handle Push Message when received
        }else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){
            HashMap<?, ?> PushdataOpen = (HashMap<?, ?>) intent.getExtras().get(PBConstants.EVENT_MSG_RECEIVE);
            Log.w(TAG, "User Received notification with Message: " + PushdataOpen.get("msg"));

            String message = "";
            String type = "home";
            if(PushdataOpen.containsKey("msg"))
                message = PushdataOpen.get("msg").toString();
            else
            if(PushdataOpen.containsKey("message"))
                message = PushdataOpen.get("message").toString();

            if(PushdataOpen.containsKey("type")) {
                Log.w(TAG, "Type contains : " + PushdataOpen.get("type"));
                type = PushdataOpen.get("type").toString();
            }
            Log.w(TAG, "Type  is not der : ");

            MessageDBHandler messageBdHandler = new MessageDBHandler(context);

            Message m = new Message();
            m.setMsg(message);
            Date d = new Date();
            m.set_time(d.getTime()+"");
            m.set_type(type);
            messageBdHandler.addMessage(m);

            List<Message> messages=  messageBdHandler.getAllMessages();

            System.out.println("Message Size "+ messages.size());
            for (int i = 0; i < messages.size(); i++) {
                System.out.println("Message " + i + " " + messages.get(i).getMsg() + " " + messages.get(i).get_time() + " " + messages.get(i).get_type());
            }


        }
    }
}