package com.sandeepani.utils;
public class Message {
     
    int _id=0;
    String _msg="";
    String _time="0";
 String _type="home";

     
    public Message(){
         
    }



    public Message(int id, String msg,String time){
        this._id = id;
        this._msg = msg;
        this._time = time;

    }
     
    public Message(String msg){
        this._msg = msg;
    }
    public int getID(){
        return this._id;
    }
     
    public void setID(int id){
        this._id = id;
    }
     
    public String getMsg(){
        return this._msg;
    }
     
    public void setMsg(String msg){
        this._msg = msg;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }
}