package com.sandeepani.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDBHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "messageManager";
	private static final String TABLE_MESSAGES = "messages";
	private static final String KEY_ID = "id";
	private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIME = "time";
    private static final String KEY_TYPE = "type";


	public MessageDBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " + KEY_MESSAGE + " TEXT," + KEY_TIME + " TEXT, "+KEY_TYPE+" TEXT )";
		db.execSQL(CREATE_MESSAGES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	void addMessage(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, message.getMsg());
        values.put(KEY_TIME, message.get_time());
        values.put(KEY_TYPE, message.get_type());

        // Inserting Row
		db.insert(TABLE_MESSAGES, null, values);
		db.close(); // Closing database connection
	}

	boolean getMessage(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		boolean status=false;

		try {
			Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_ID,
					KEY_MESSAGE}, KEY_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			  if (cursor.moveToFirst()) {
				    status=true;
				  
				  }    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			
			try {
				db.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return status;
	}

	public List<Message> getAllMessages() {
		List<Message> messageList = new ArrayList<Message>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Message message = new Message();
				message.setID(Integer.parseInt(cursor.getString(0)));
				message.setMsg(cursor.getString(1));
                message.set_time(cursor.getString(2));
                message.set_type(cursor.getString(3));
				messageList.add(message);
			} while (cursor.moveToNext());
		}

		return messageList;
	}

	public int updateMessage(Message Message) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, Message.getMsg());

		// updating row
		return db.update(TABLE_MESSAGES, values, KEY_ID + " = ?",new String[] { String.valueOf(Message.getID()) });
	}

	public void deleteMessage(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_MESSAGES, KEY_ID + " = ?",
				new String[] { String.valueOf(message.getID()) });
		db.close();
	}


	public int getMessageCount() {
		String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}