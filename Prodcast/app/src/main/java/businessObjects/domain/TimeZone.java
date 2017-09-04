package businessObjects.domain;

/**
 * Created by nandhini on 01/09/17.
 */

public class TimeZone {

        private long timezoneId;
        private String countryId,timezone;

        public long getTimezoneId() {
            return timezoneId;
        }

        public void setTimezoneId(long timezoneId) {
            this.timezoneId = timezoneId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }


}
