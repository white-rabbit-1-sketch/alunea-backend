package me.silvermail.backend.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class UsedDomain {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;

    protected String domain;

    protected Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}