package com.stackbase.mobapp.objects;

public class Message extends JSONObj {
    private String messageType = "";
    private String content = "";
    private Long time = 0l;
    private Boolean read = false;
    private String jsonFile;
    public enum MessageType {
        SYSTEM_MESSAGE,
        USER_MESSAGE
    }

    public Message() {}
    public Message(String jsonFile) {
        this.fromJSON(jsonFile);
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String type) {
        this.messageType = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean isRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }
}
