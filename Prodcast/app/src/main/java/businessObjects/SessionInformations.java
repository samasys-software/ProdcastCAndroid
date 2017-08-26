package businessObjects;

import businessObjects.domain.Bill;
import businessObjects.domain.Category;
import businessObjects.domain.Customer;
import businessObjects.domain.CustomerRegistration;
import businessObjects.domain.CustomersLogin;
import businessObjects.domain.Distributor;
import businessObjects.domain.EmployeeDetails;
import businessObjects.domain.Product;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by God on 4/22/2017.
 */

public class SessionInformations {
    private CustomersLogin customerDetails;
    private CustomerRegistration registeredCustomer;
    private List<Distributor> allDistributors;
    private EmployeeDetails employee;
    private Customer currentCustomer;
    private  List<Category> categoryDetails;
    private  List<Product> ProductDetails;


    private JSONObject outstandingBills;

    private static final SessionInformations ourInstance = new SessionInformations();

    public static SessionInformations getInstance() {
        return ourInstance;
    }

    private SessionInformations() {
    }



    public JSONObject getOutstandingBills() {
        return outstandingBills;
    }

    public void setOutstandingBills(JSONObject outstandingBills) {
        this.outstandingBills = outstandingBills;
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
}
