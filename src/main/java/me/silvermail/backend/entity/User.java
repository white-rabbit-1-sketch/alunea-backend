package me.silvermail.backend.entity;

import jakarta.persistence.*;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.entity.domain.AbstractDomain;
import me.silvermail.backend.entity.domain.Subdomain;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
public class User implements UserDetails {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String EMAIL_MODE_HARD_REPLACE = "hard-replace";
    public static final String EMAIL_MODE_SOFT_REPLACE = "soft-replace";

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    protected String id;
    protected String email;
    protected boolean isEmailVerified;
    protected String password;
    protected String role;
    @ManyToOne
    protected AbstractDomain favoriteDomain;
    @ManyToOne
    protected Mailbox favoriteMailbox;
    protected String language;
    protected String emailMode;
    protected Date createTime;
    protected Date updateTime;

    @OneToMany(mappedBy = "user")
    protected List<Mailbox> mailboxList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    protected List<MailboxAlias> mailboxAliasList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    protected List<Subdomain> subdomainList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

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

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AbstractDomain getFavoriteDomain() {
        return favoriteDomain;
    }

    public void setFavoriteDomain(AbstractDomain favoriteDomain) {
        this.favoriteDomain = favoriteDomain;
    }

    public Mailbox getFavoriteMailbox() {
        return favoriteMailbox;
    }

    public void setFavoriteMailbox(Mailbox favoriteMailbox) {
        this.favoriteMailbox = favoriteMailbox;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEmailMode() {
        return emailMode;
    }

    public void setEmailMode(String emailMode) {
        this.emailMode = emailMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Mailbox> getMailboxList() {
        return mailboxList;
    }

    public void setMailboxList(List<Mailbox> mailboxList) {
        this.mailboxList = mailboxList;
    }

    public List<MailboxAlias> getMailboxAliasList() {
        return mailboxAliasList;
    }

    public void setMailboxAliasList(List<MailboxAlias> mailboxAliasList) {
        this.mailboxAliasList = mailboxAliasList;
    }

    public List<Subdomain> getSubdomainList() {
        return subdomainList;
    }

    public void setSubdomainList(List<Subdomain> subdomainList) {
        this.subdomainList = subdomainList;
    }
}