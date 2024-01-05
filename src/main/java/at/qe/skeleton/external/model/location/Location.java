package at.qe.skeleton.external.model.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.io.Serializable;


/**
 * Represents a geographical location with associated details such as name, latitude, longitude, and country.
 * This class is used for mapping location information retrieved from a JSON data source to Java objects.
 *
 */
@Entity
@Table(name = "location")
@JsonIgnoreProperties(ignoreUnknown = true) // ignores other attributes in .json (like adminLevel1Short)
public class Location implements Serializable {
        @Id
        @JsonProperty("owm_city_id") @Column(name = "id") Long id;
        @JsonProperty("owm_city_name") @Column(name = "name") String name;
        @JsonProperty("owm_latitude") @Column(name = "latitude") double latitude;
        @JsonProperty("owm_longitude") @Column(name = "longitude") double longitude;
        @JsonProperty("owm_country") @Column(name = "abbreviatedCountry") String abbreviatedCountry;
        @JsonProperty("country_long") @Column(name = "country") String country;

        public Location() {
        }

        public Location(Long id, String name, double latitude, double longitude, String abbreviatedCountry, String country, String postalCode) {
                this.id = id;
                this.name = name;
                this.latitude = latitude;
                this.longitude = longitude;
                this.abbreviatedCountry = abbreviatedCountry;
                this.country = country;
                this.postalCode = postalCode;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long owmCityId) {
                this.id = owmCityId;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public double getLatitude() {
                return latitude;
        }

        public void setLatitude(double latitude) {
                this.latitude = latitude;
        }

        public double getLongitude() {
                return longitude;
        }

        public void setLongitude(double longitude) {
                this.longitude = longitude;
        }

        public String getAbbreviatedCountry() {
                return abbreviatedCountry;
        }

        public void setAbbreviatedCountry(String abbreviatedCountry) {
                this.abbreviatedCountry = abbreviatedCountry;
        }

        public String getCountry() {
                return this.country;
        }
        public void setCountry(String country) {
                this.country = country;
        }
        public String getPostalCode() {
                return postalCode;
        }

        public void setPostalCode(String postalCode) {
                this.postalCode = postalCode;
        }

        @JsonProperty("postal_code") @Column(name = "postal_code") String postalCode;

        /**
         * Generates a debug-friendly string representation of the {@code Location} object.
         *
         * @return A string containing key details of the location.
         */
        public String toDebugString() {
                return "Location{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", latitude='" + latitude + '\'' +
                        ", longitude='" + longitude + '\'' +
                        ", abbreviatedCountry='" + abbreviatedCountry + '\'' +
                        ", country='" + country + '\'' +
                        ", postalCode='" + postalCode + '\'' +
                        '}';
        }

        @Override
        public String toString() {
                return name + ", " + country;
        }
}

