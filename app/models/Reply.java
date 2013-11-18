package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.security.User;
import play.db.jpa.Model;

@Entity(name = "T_REPLY")
public class Reply extends Model {
    @Column(nullable = false, length = 1024)
    public String content;

    @ManyToOne
    public Topic topic;

    @ManyToOne
    public User user;
}
