package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.security.User;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "T_MESSAGE")
public class Message extends Model {
    @Required
    @ManyToOne
    public User fromUser;

    @Required
    @ManyToOne
    public User toUser;
    // 是否被接收用户打开过
    public boolean opened = false;

    @Required
    @Column(length=1024)
    public String content;
    
    public Date createTime = new Date();
}