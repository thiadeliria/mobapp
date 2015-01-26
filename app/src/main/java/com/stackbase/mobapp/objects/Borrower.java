package com.stackbase.mobapp.objects;

import java.util.Date;

/**
 * Created by gengjh on 1/18/15.
 */
public class Borrower extends JSONObj {
    private String id = "";
    private String name = "";
    private String sex = "";
    private String nation = "";
    private Date birthday = null;
    private String address = "";
    private String location = "";
    private Date expiryFrom = null;
    private Date expiryTo = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String issue) {
        this.location = issue;
    }

    public Date getExpiryFrom() {
        return expiryFrom;
    }

    public void setExpiryFrom(Date validityDateFrom) {
        this.expiryFrom = validityDateFrom;
    }

    public Date getExpiryTo() {
        return expiryTo;
    }

    public void setExpiryTo(Date validityDateTo) {
        this.expiryTo = validityDateTo;
    }
}
