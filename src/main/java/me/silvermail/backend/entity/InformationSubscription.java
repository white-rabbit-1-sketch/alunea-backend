package me.silvermail.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class InformationSubscription {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    protected String email;

    protected String language;

    protected Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}