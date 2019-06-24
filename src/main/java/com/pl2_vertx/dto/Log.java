package com.pl2_vertx.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Log {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private String logId;
    private String msg;
    private String date;
    private String time;

    public Log(){
        this.logId = UUID.randomUUID().toString();
    }

    public Log(String msg){
        this.logId = UUID.randomUUID().toString();
        this.msg = msg;
        this.date = dateFormat.format(new Date());
        this.time = timeFormat.format(Calendar.getInstance().getTime());
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Log{" +
                "logId='" + logId + '\'' +
                ", msg='" + msg + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
