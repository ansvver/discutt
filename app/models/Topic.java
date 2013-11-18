package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.security.User;
import play.db.jpa.Model;

@Entity(name = "T_TOPIC")
public class Topic extends Model {

    public String title;

    public String content;

    @ManyToOne
    public Board board;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY)
    public List<Reply> replies = new ArrayList<Reply>();

    @ManyToOne
    public User user;
}
