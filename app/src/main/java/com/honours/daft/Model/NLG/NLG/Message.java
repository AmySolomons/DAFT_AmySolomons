package com.honours.daft.Model.NLG.NLG;

import java.util.LinkedHashMap;

public class Message {

    private final String messageID;
    private final String relation;
    private final LinkedHashMap<String, String> arguments;

    public Message(String id, String relation, LinkedHashMap<String, String> arguments){
        messageID = id;
        this.relation = relation;
        this.arguments = arguments;
    }

    public String getMessageID(){return messageID;}
    public String getRelation(){return relation;}
    public LinkedHashMap<String, String> getArguments(){return arguments;}

}
