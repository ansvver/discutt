package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.security.User;
import play.db.jpa.Model;

@Entity(name="T_READ_RECORD")
public class ReadRecord extends Model {
    @ManyToOne
    public User user;
    @ManyToOne
    public Topic topic;
    public int lastReplySize;
    
    public ReadRecord(User user, Topic topic){
        this.user = user;
        this.topic = topic;
    }
}
