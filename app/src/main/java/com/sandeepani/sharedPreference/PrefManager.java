package com.sandeepani.sharedPreference;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PrefManager {
	// Shared Preferences
	SharedPreferences pref;
	// Editor for Shared preferences
	Editor editor;
	Context context;
	int PRIVATE_MODE = 0; 
	private static final String RESPONSE = "Response";
	public static final String KEY_LOGIN_TOKEN = "Login_Token";
    public static final String KEY_User_Name = "UserName";
	public PrefManager(Context context){
	
		this.context = context;
		pref = this.context.getSharedPreferences(RESPONSE, PRIVATE_MODE);
		editor = pref.edit();				 
	}

	/**
	 * 
	 * @param  token
	 */
	
	public void SaveLoginTokenInInSharedPref(String token){
		Log.d(" Set Login Token=", token);
		editor.putString(KEY_LOGIN_TOKEN,token);
		editor.commit();	
		Log.d("Login Token after store", pref.getString(KEY_LOGIN_TOKEN, null));
	}
	
	/**
	 * 
	 * @return stored login token.
	 */
	public String getLoginTokenFromSharedPref(){
		Log.d("get Login Token", pref.getString(KEY_LOGIN_TOKEN, null));
		String gettoken = pref.getString(KEY_LOGIN_TOKEN, null);
		Log.d("Get login token =", gettoken);
		return gettoken;
	}

    /**
     *
     * @param Username
     */

    public void SaveUserNameInInSharedPref(String Username){
        Log.d(" Set Username=", Username);
        editor.putString(KEY_User_Name,Username);
        editor.commit();
        Log.d("Username after store", pref.getString(KEY_User_Name, null));
    }

    /**
     *
     * @return stored username .
     */
    public String getUserNameFromSharedPref(){
        Log.d("get UserNAme", pref.getString(KEY_User_Name, null));
        String getUserName = pref.getString(KEY_User_Name, null);
        Log.d("Get UserName =", getUserName);
        return getUserName;
    }

	
}
