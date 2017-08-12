package com.ventruxinformatics.prodcast.domain;


//import com.primeforce.prodcast.dao.Distributor;
//import com.primeforce.prodcast.businessobjects.Customer;
import com.ventruxinformatics.prodcast.domain.Customer;
import com.ventruxinformatics.prodcast.domain.Distributor;
import com.ventruxinformatics.prodcast.domain.ProdcastDTO;
//import com.primeforce.prodcast.businessobjects.NewCustomerRegistrationDetails;

import java.util.List;

/**
 * Created by Hai on 11/2/2016.
 */
public class CustomerLoginDTO<T> extends ProdcastDTO {
    private boolean verified,registered;
    private T result;
    private List<Distributor> distributors;

    private List<Customer> customers;
    private List<Distributor> distributorsPublic;

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<Distributor> distributors) {
        this.distributors = distributors;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Distributor> getDistributorsPublic() {
        return distributorsPublic;
    }

    public void setDistributorsPublic(List<Distributor> distributorsPublic) {
        this.distributorsPublic = distributorsPublic;
    }
}