package models.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity(name = "T_USER")
public class User extends Model {
    @Required
    @Column(nullable = false)
    @MinSize(value = 2)
    @MaxSize(value = 8)
    public String nickName;

    @Required
    @Column(nullable = false, unique = true)
    public String email;

    @Required
    @MinSize(value = 8)
    @Column(nullable = false)
    public String password;

    @Required
    @Transient
    public String passwordConfirm;

    @ManyToOne
    public UserGroup userGroup;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @PrePersist
    @PreUpdate
    public void hashPassword() {
        this.password = Codec.hexMD5(this.password);
    }

    @Override
    public String toString() {
        return this.nickName + " " + this.email + " " + this.password + " " + this.passwordConfirm;
    }
}
