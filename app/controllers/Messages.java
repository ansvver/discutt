package controllers;

import java.util.List;

import models.Message;
import play.mvc.Before;

public class Messages extends Application {
    
    @Before
    public static void initMessages(){
        long currentUserID = currentUser().id;
        List<Message> msgs = Message.find("toUser.id = ? or fromUser.id = ? order by id desc", currentUserID, currentUserID).fetch();
        renderArgs.put("msgs", msgs);
    }
    
    public static void list(){
        render();
    }
    
    public static void save(Message message){
        message.opened = false;
        message.save();
    }
    
    public static void saveReply(Message message){
        message.opened = false;
        message.save();
        list();
    }
    
    public static void open(long msgID){
        Message msg = Message.findById(msgID);
        if(msg.toUser.id == currentUser().id){
            msg.opened = true;
            msg.save();
        }
        render(msg);
    }

    public static void reply(long lastMsgID){
        Message lastMsg = Message.findById(lastMsgID);
        render(lastMsg);
    }
}