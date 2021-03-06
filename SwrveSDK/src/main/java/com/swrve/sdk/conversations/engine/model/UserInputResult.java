package com.swrve.sdk.conversations.engine.model;

import java.io.Serializable;

public class UserInputResult implements Serializable{
    public static final String TYPE_SINGLE_CHOICE = "choice";
    public static final String TYPE_VIDEO_PLAY = "play";
    public String type, conversationId,  pageTag, fragmentTag;
    public Object result;

    public String getType() {
        return type;
    }

    public String getPageTag() {
        return pageTag;
    }

    public String getResultAsString() {
        return result.toString();
    }

    public Object getResult() {
        return result;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public boolean isSingleChoice() {
        return type.equalsIgnoreCase(TYPE_SINGLE_CHOICE);
    }
}
