package at.qe.skeleton.external.model.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "location") // Specify the table name if needed
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationEntity implements Serializable {
        @Id
        @JsonProperty("owm_city_id") Long id;
        @JsonProperty("owm_city_name") String name;
        @JsonProperty("owm_latitude") String latitude;
        @JsonProperty("owm_longitude") String longitude;
        @JsonProperty("owm_country") String owmCountry;
        @JsonProperty("locality_short") String localityShort;
        @JsonProperty("locality_long") String localityLong;
        @JsonProperty("admin_level_1_short") String adminLevel1Short;
        @JsonProperty("admin_level_1_long") String adminLevel1Long;
        @JsonProperty("admin_level_2_short") String adminLevel2Short;
        @JsonProperty("admin_level_2_long") String adminLevel2Long;
        @JsonProperty("admin_level_3_short") String adminLevel3Short;
        @JsonProperty("admin_level_3_long") String adminLevel3Long;
        @JsonProperty("admin_level_4_short") String adminLevel4Short;
        @JsonProperty("admin_level_4_long") String adminLevel4Long;
        @JsonProperty("admin_level_5_short") String adminLevel5Short;
        @JsonProperty("admin_level_5_long") String adminLevel5Long;
        @JsonProperty("country_short") String countryShort;
        @JsonProperty("country_long") String countryLong;

        public LocationEntity() {
        }

        public LocationEntity(Long id, String name, String latitude, String longitude, String owmCountry, String localityShort, String localityLong, String adminLevel1Short, String adminLevel1Long, String adminLevel2Short, String adminLevel2Long, String adminLevel3Short, String adminLevel3Long, String adminLevel4Short, String adminLevel4Long, String adminLevel5Short, String adminLevel5Long, String countryShort, String countryLong, String postalCode) {
                this.id = id;
                this.name = name;
                this.latitude = latitude;
                this.longitude = longitude;
                this.owmCountry = owmCountry;
                this.localityShort = localityShort;
                this.localityLong = localityLong;
                this.adminLevel1Short = adminLevel1Short;
                this.adminLevel1Long = adminLevel1Long;
                this.adminLevel2Short = adminLevel2Short;
                this.adminLevel2Long = adminLevel2Long;
                this.adminLevel3Short = adminLevel3Short;
                this.adminLevel3Long = adminLevel3Long;
                this.adminLevel4Short = adminLevel4Short;
                this.adminLevel4Long = adminLevel4Long;
                this.adminLevel5Short = adminLevel5Short;
                this.adminLevel5Long = adminLevel5Long;
                this.countryShort = countryShort;
                this.countryLong = countryLong;
                this.postalCode = postalCode;
        }

        public Long getOwmCityId() {
                return id;
        }

        public void setOwmCityId(Long owmCityId) {
                this.id = owmCityId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getLatitude() {
                return latitude;
        }

        public void setLatitude(String latitude) {
                this.latitude = latitude;
        }

        public String getLongitude() {
                return longitude;
        }

        public void setLongitude(String longitude) {
                this.longitude = longitude;
        }

        public String getOwmCountry() {
                return owmCountry;
        }

        public void setOwmCountry(String owmCountry) {
                this.owmCountry = owmCountry;
        }

        public String getLocalityShort() {
                return localityShort;
        }

        public void setLocalityShort(String localityShort) {
                this.localityShort = localityShort;
        }

        public String getLocalityLong() {
                return localityLong;
        }

        public void setLocalityLong(String localityLong) {
                this.localityLong = localityLong;
        }

        public String getAdminLevel1Short() {
                return adminLevel1Short;
        }

        public void setAdminLevel1Short(String adminLevel1Short) {
                this.adminLevel1Short = adminLevel1Short;
        }

        public String getAdminLevel1Long() {
                return adminLevel1Long;
        }

        public void setAdminLevel1Long(String adminLevel1Long) {
                this.adminLevel1Long = adminLevel1Long;
        }

        public String getAdminLevel2Short() {
                return adminLevel2Short;
        }

        public void setAdminLevel2Short(String adminLevel2Short) {
                this.adminLevel2Short = adminLevel2Short;
        }

        public String getAdminLevel2Long() {
                return adminLevel2Long;
        }

        public void setAdminLevel2Long(String adminLevel2Long) {
                this.adminLevel2Long = adminLevel2Long;
        }

        public String getAdminLevel3Short() {
                return adminLevel3Short;
        }

        public void setAdminLevel3Short(String adminLevel3Short) {
                this.adminLevel3Short = adminLevel3Short;
        }

        public String getAdminLevel3Long() {
                return adminLevel3Long;
        }

        public void setAdminLevel3Long(String adminLevel3Long) {
                this.adminLevel3Long = adminLevel3Long;
        }

        public String getAdminLevel4Short() {
                return adminLevel4Short;
        }

        public void setAdminLevel4Short(String adminLevel4Short) {
                this.adminLevel4Short = adminLevel4Short;
        }

        public String getAdminLevel4Long() {
                return adminLevel4Long;
        }

        public void setAdminLevel4Long(String adminLevel4Long) {
                this.adminLevel4Long = adminLevel4Long;
        }

        public String getAdminLevel5Short() {
                return adminLevel5Short;
        }

        public void setAdminLevel5Short(String adminLevel5Short) {
                this.adminLevel5Short = adminLevel5Short;
        }

        public String getAdminLevel5Long() {
                return adminLevel5Long;
        }

        public void setAdminLevel5Long(String adminLevel5Long) {
                this.adminLevel5Long = adminLevel5Long;
        }

        public String getCountryShort() {
                return countryShort;
        }

        public void setCountryShort(String countryShort) {
                this.countryShort = countryShort;
        }

        public String getCountryLong() {
                return countryLong;
        }

        public void setCountryLong(String countryLong) {
                this.countryLong = countryLong;
        }

        public String getPostalCode() {
                return postalCode;
        }

        public void setPostalCode(String postalCode) {
                this.postalCode = postalCode;
        }

        @JsonProperty("postal_code") String postalCode;

        @Override
        public String toString() {
                return "LocationEntity{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        ", owmCountry='" + owmCountry + '\'' +
                        ", localityShort='" + localityShort + '\'' +
                        ", localityLong='" + localityLong + '\'' +
                        ", adminLevel1Short='" + adminLevel1Short + '\'' +
                        ", adminLevel1Long='" + adminLevel1Long + '\'' +
                        ", adminLevel2Short='" + adminLevel2Short + '\'' +
                        ", adminLevel2Long='" + adminLevel2Long + '\'' +
                        ", adminLevel3Short='" + adminLevel3Short + '\'' +
                        ", adminLevel3Long='" + adminLevel3Long + '\'' +
                        ", adminLevel4Short='" + adminLevel4Short + '\'' +
                        ", adminLevel4Long='" + adminLevel4Long + '\'' +
                        ", adminLevel5Short='" + adminLevel5Short + '\'' +
                        ", adminLevel5Long='" + adminLevel5Long + '\'' +
                        ", countryShort='" + countryShort + '\'' +
                        ", countryLong='" + countryLong + '\'' +
                        ", postalCode='" + postalCode + '\'' +
                        '}';
        }
}

