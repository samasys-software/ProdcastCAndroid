package com.samayu.prodcastc.businessObjects.domain;

/**
 * Created by kdsdh on 9/25/2017.
 */

public class ServiceTicket {
    private String issue,  phoneNumber, countryId;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
