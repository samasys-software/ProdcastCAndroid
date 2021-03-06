package com.samayu.prodcastc.businessObjects.dto;

import com.samayu.prodcastc.businessObjects.domain.Employee;

/**
 * Created by sarathan732 on 4/23/2016.
 */
public class LoginDTO extends ProdcastDTO {

    private boolean success;

    private Employee employee;

    private String timezone;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}