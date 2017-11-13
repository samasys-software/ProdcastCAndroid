package com.samayu.prodcastc.businessObjects;

import com.samayu.prodcastc.businessObjects.domain.Category;
import com.samayu.prodcastc.businessObjects.domain.Country;
import com.samayu.prodcastc.businessObjects.domain.Customer;
import com.samayu.prodcastc.businessObjects.domain.CustomerRegistration;
import com.samayu.prodcastc.businessObjects.domain.CustomersLogin;
import com.samayu.prodcastc.businessObjects.domain.Distributor;
import com.samayu.prodcastc.businessObjects.domain.EmployeeDetails;
import com.samayu.prodcastc.businessObjects.domain.OrderDetails;
import com.samayu.prodcastc.businessObjects.domain.Product;
import com.samayu.prodcastc.businessObjects.domain.ProductFlavors;
import com.samayu.prodcastc.businessObjects.domain.ProductOptions;
import com.samayu.prodcastc.businessObjects.domain.ServiceTicket;

import java.util.List;

/**
 * Created by God on 4/22/2017.
 */

public class SessionInfo {
    private CustomersLogin customerDetails;
    private CustomerRegistration registeredCustomer;
    private List<Distributor> allDistributors;
    private EmployeeDetails employee;
    private Customer currentCustomer;
    private  List<Category> categoryDetails;
    private  List<Product> ProductDetails;
    private  List<OrderDetails> entry;
    private Customer billsForCustomer;
    private List<Country> countries;

    private List<ServiceTicket> serviceSupport;

    public List<ProductOptions> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductOptions> productOptions) {
        this.productOptions = productOptions;
    }

    public List<ProductFlavors> getProductFlavors() {
        return productFlavors;
    }

    public void setProductFlavors(List<ProductFlavors> productFlavors) {
        this.productFlavors = productFlavors;
    }

    private List<ProductOptions> productOptions;
    private List<ProductFlavors> productFlavors;

    public List<ServiceTicket> getServiceSupport() {
        return serviceSupport;
    }

    public void setServiceSupport(List<ServiceTicket> serviceSupport) {
        this.serviceSupport = serviceSupport;
    }


    private static SessionInfo ourInstance = new SessionInfo();

    public static SessionInfo getInstance() {
        return ourInstance;
    }

    private SessionInfo() {
    }

    public CustomersLogin getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomersLogin customerDetails) {
        this.customerDetails = customerDetails;
    }


    public List<Distributor> getAllDistributors() {
        return allDistributors;
    }

    public void setAllDistributors(List<Distributor> allDistributors) {
        this.allDistributors = allDistributors;
    }


    public EmployeeDetails getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDetails employee) {
        this.employee = employee;
    }


    public CustomerRegistration getRegisteredCustomer() {
        return registeredCustomer;
    }

    public void setRegisteredCustomer(CustomerRegistration registeredCustomer) {
        this.registeredCustomer = registeredCustomer;
    }


    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }


    public List<Category> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(List<Category> categoryDetails) {
        this.categoryDetails = categoryDetails;
    }


    public List<Product> getProductDetails() {
        return ProductDetails;
    }

    public void setProductDetails(List<Product> productDetails) {
        ProductDetails = productDetails;
    }


    public List<OrderDetails> getEntry() {
        return entry;
    }

    public void setEntry(List<OrderDetails> entry) {

        this.entry = entry;
    }


    public Customer getBillsForCustomer() {
        return billsForCustomer;
    }

    public void setBillsForCustomer(Customer billsForCustomer) {
        this.billsForCustomer = billsForCustomer;
    }

    public void destroy(){
        ourInstance = new SessionInfo();
    }


    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
