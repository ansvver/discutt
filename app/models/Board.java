package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity(name = "T_BOARD")
public class Board extends Model {

    @Column(nullable = false)
    public String name;

    @Column(length = 4096)
    public String detail;

    public int orderNo;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    public List<Topic> topics = new ArrayList<Topic>();
}
