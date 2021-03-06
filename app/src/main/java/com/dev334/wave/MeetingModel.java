package com.dev334.wave;

public class MeetingModel {
    private String Topic;
    private String Desc;
    private String Code;
    private String Org;

    public String getOrg() {
        return Org;
    }

    public void setOrg(String org) {
        Org = org;
    }

    public MeetingModel(){
        //
    }

    public MeetingModel(String topic, String desc, String code, String Org) {
        Topic = topic;
        Desc = desc;
        this.Code = code;
        this.Org=Org;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }
}
