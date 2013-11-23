package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.security.User;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "T_REPLY")
public class Reply extends Model {

    @Required
    @Column(nullable = false, length = 1024)
    public String content;

    public String date;

    @ManyToOne
    public Topic topic;

    @ManyToOne
    public User user;

}
