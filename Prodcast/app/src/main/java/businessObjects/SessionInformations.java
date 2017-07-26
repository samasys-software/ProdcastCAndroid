package businessObjects;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by God on 4/22/2017.
 */

public class SessionInformations {
    private JSONObject customerDetails;
    private List<JSONObject> allDistributors;
    private JSONObject distributor;

    private static final SessionInformations ourInstance = new SessionInformations();

    public static SessionInformations getInstance() {
        return ourInstance;
    }

    private SessionInformations() {
    }

    public JSONObject getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(JSONObject customerDetails) {
        this.customerDetails = customerDetails;
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
}
