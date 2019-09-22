package com.moondu.leilao.model.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Objects;

@IgnoreExtraProperties
public class User implements Serializable {

    private String mail;
    private Long id;
    private String name;
    private String profile;
    private String key;
    private String imei;
    private boolean savePassword;
    private boolean unlogged;
    private boolean loginInCache;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean mGetSavePassword() {
        return savePassword;
    }

    public void mSetSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }

    public boolean isUnlogged() {
        return unlogged;
    }

    public void setUnlogged(boolean unlogged) {
        this.unlogged = unlogged;
    }

    public boolean isLoginInCache() {
        return loginInCache;
    }

    public void setLoginInCache(boolean loginInCache) {
        this.loginInCache = loginInCache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
