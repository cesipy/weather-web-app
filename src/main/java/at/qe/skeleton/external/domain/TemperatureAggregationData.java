package at.qe.skeleton.external.domain;
import jakarta.persistence.*;
import java.time.Instant;
/**
 * Represents the temperature aggregation data.
 * <p>
 *     This class is used to store and retrieve the temperature aggregation data
 *     in and from the database.
 * </p>
 */
@Entity
@Table(name = "temp_aggregation")
public class TemperatureAggregationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long temp_aggregation_id;
    private double morningTemperature;
    private double dayTemperature;
    private double eveningTemperature;
    private double nightTemperature;

    public TemperatureAggregationData() {

    }

    public TemperatureAggregationData(double morningTemperature, double dayTemperature, double eveningTemperature, double nightTemperature) {
        this.morningTemperature = morningTemperature;
        this.dayTemperature = dayTemperature;
        this.eveningTemperature = eveningTemperature;
        this.nightTemperature = nightTemperature;
    }

    public Long getTemp_aggregation_id() {
        return temp_aggregation_id;
    }

    public void setTemp_aggregation_id(Long temp_aggregation_id) {
        this.temp_aggregation_id = temp_aggregation_id;
    }

    public double getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(double morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public double getDayTemperature() {
        return dayTemperature;
    }

    public void setDayTemperature( double dayTemperature) {
        this.dayTemperature = dayTemperature;
    }

    public double getEveningTemperature() {
        return eveningTemperature;
    }

    public void setEveningTemperature(double eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }

    public double getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(double nightTemperature) {
        this.nightTemperature = nightTemperature;
    }
}
