package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import models.security.User;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "T_TOPIC")
public class Topic extends Model {

    @Required
    @Column(nullable = false, length = 100)
    public String title;

    @Required
    @Column(nullable = false, length = 500)
    public String content;

    public String date;

    public String lastUpdate;

    @ManyToOne
    public Board board;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public List<Reply> replies = new ArrayList<Reply>();

    @ManyToOne(fetch = FetchType.EAGER)
    public User user;
    
    @Transient
    public int replyCount = 0;
    @Transient
    public boolean opened = true;
}
