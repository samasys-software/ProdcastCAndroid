package  com.samayu.prodcastc.businessObjects.dto;
import java.util.List;

import com.samayu.prodcastc.businessObjects.domain.Country;
import com.samayu.prodcastc.businessObjects.domain.TimeZone;

/**
 * Created by nandhini on 8/15/17.
 */

public class CountryDTO extends ProdcastDTO {
    private List<Country> result;
    private List<TimeZone> timezones;

    public List<Country> getResult() {
        return result;
    }

    public void setResult(List<Country> result) {
        this.result = result;
    }

    public List<TimeZone> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<TimeZone> timezones) {
        this.timezones = timezones;
    }
}