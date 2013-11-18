package models.security;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import play.data.validation.Match;
import play.data.validation.MinSize;
import play.db.jpa.Model;
import utils.Globals;

@Entity(name = "T_RESOURCE")
public class Resource extends Model {
    // 只能通过create or fetch创建
    @SuppressWarnings("unused")
    private Resource() {
    }

    @MinSize(value = 2)
    @Column(nullable = false, unique = true)
    public String name;

    @Match(message = "请求方式格式错误！", value = "^(ALL|POST|GET)$")
    @Column(nullable = false)
    public String method;

    @Column(nullable = false)
    @MinSize(value = 2)
    public String resource;

    @ManyToMany(mappedBy = "resources")
    public List<UserGroup> userGroups = new ArrayList<UserGroup>();

    @Transient
    public boolean checked;

    public Resource(String name, String resource, String method) {
        this.name = name;
        this.resource = resource;
        this.method = method;
    }

    public static Resource createOrFetch(String resource) {
        return createOrFetch(resource, resource);
    }

    /**
     * 创建默认资源，访问method为所有请求
     */
    public static Resource createOrFetch(String name, String resource) {
        return Resource.createOrFetch(name, resource, Globals.REQUEST_METHOD.ALL.name());
    }

    /**
     * 新建一个资源，如果数据库已有则不必新建
     */
    public static Resource createOrFetch(String name, String resource, String method) {
        Resource r =
                Resource.find("name = ? and resource = ? and method = ? ", name, resource, method)
                        .first();
        if (r != null) {
            return r;
        }
        return new Resource(name, resource, method);
    }
}
