package businessObjects;

import com.ventruxinformatics.prodcast.domain.CustomersLogin;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by God on 4/22/2017.
 */

public class SessionInformations {
    private CustomersLogin customerDetails;
    private List<JSONObject> allDistributors;
    private JSONObject distributor;
    private JSONObject outstandingBills;

    private static final SessionInformations ourInstance = new SessionInformations();

    public static SessionInformations getInstance() {
        return ourInstance;
    }

    private SessionInformations() {
    }





    public List<JSONObject> getAllDistributors() {
        return allDistributors;
    }

    public void setAllDistributors(List<JSONObject> allDistributors) {
        this.allDistributors = allDistributors;
    }

    public JSONObject getDistributor() {
        return distributor;
    }

    public void setDistributor(JSONObject distributor) {
        this.distributor = distributor;
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
}
