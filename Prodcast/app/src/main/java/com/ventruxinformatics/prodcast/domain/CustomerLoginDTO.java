package com.ventruxinformatics.prodcast.domain;

/**
 * Created by sarathan732 on 8/9/2017.
 */

public class CustomerLoginDTO extends ProdcastDTO {

    private boolean registered;
    private boolean verified;
    private CustomersLogin result;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public CustomersLogin getResult() {
        return result;
    }

    public void setResult(CustomersLogin result) {
        this.result = result;
    }
}
