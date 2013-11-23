package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "T_BOARD")
public class Board extends Model {

    @Required
    @MaxSize(255)
    @MinSize(2)
    @Column(nullable = false)
    public String name;

    @Column(length = 4096)
    public String detail;

    public int orderNo;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    public List<Topic> topics = new ArrayList<Topic>();
}
