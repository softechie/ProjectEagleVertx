package com.pl2_vertx.log;

import java.util.UUID;

public class Log {

    private String logId;
    private String msg;
    private String date;
    private String time;

    public Log(String msg, String date, String time){
        this.logId = UUID.randomUUID().toString();
        this.msg = msg;
        this.date = date;
        this.time = time;
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
