package businessObjects;

import com.ventruxinformatics.prodcast.domain.CustomersLogin;
import com.ventruxinformatics.prodcast.domain.Distributor;
import com.ventruxinformatics.prodcast.domain.EmployeeDetails;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by God on 4/22/2017.
 */

public class SessionInformations {
    private CustomersLogin customerDetails;
    private List<Distributor> allDistributors;
    private EmployeeDetails employee;

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
}
