package models.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.db.jpa.Model;
import utils.Globals;

@Entity(name = "T_USER_GROUP")
public class UserGroup extends Model {
    @Column(nullable = false, unique = true)
    @MinSize(value = 2)
    public String name;
    @MaxSize(value = 1024)
    public String detail;
    @OneToMany(mappedBy = "userGroup")
    public List<User> users = new ArrayList<User>();
    @ManyToMany(cascade = CascadeType.PERSIST)
    public List<Resource> resources = new ArrayList<Resource>();

    public int getUserNum() {
        return this.users.size();
    }

    /**
     * 如果是核心用户组，不能删除.
     * @return
     */
    public boolean getIsPrivateGroup() {
        if (this.name.equals(Globals.GROUP_ADMIN) || this.name.equals(Globals.GROUP_ANONYMOUS)
                || this.name.equals(Globals.GROUP_NORMAL)) {
            return true;
        }
        return false;
    }
}
