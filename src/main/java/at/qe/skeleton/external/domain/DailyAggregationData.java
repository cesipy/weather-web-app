package at.qe.skeleton.external.domain;


import jakarta.persistence.*;


/**
 * Represents the daily temperature aggregation data.
 * <p>
 *     This class is used to store and retrieve the daily temperature aggregation data
 *     in and from the database.
 * </p>
 */
@Entity
@Table(name = "daily_aggregation")
public class DailyAggregationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyAggregationId;
    private double morningTemperature;
    private double dayTemperature;
    private double eveningTemperature;
    private double nightTemperature;
    private double minimumDailyTemperature;
    private double maximumDailyTemperature;

    public DailyAggregationData() {
    }

    public DailyAggregationData(double morningTemperature, double dayTemperature, double eveningTemperature, double nightTemperature, double minimumDailyTemperature, double maximumDailyTemperature) {
        this.morningTemperature = morningTemperature;
        this.dayTemperature = dayTemperature;
        this.eveningTemperature = eveningTemperature;
        this.nightTemperature = nightTemperature;
        this.minimumDailyTemperature = minimumDailyTemperature;
        this.maximumDailyTemperature = maximumDailyTemperature;
    }

    public Long getDailyAggregationId() {
        return dailyAggregationId;
    }

    public void setDailyAggregationId(Long dailyAggregationId) {
        this.dailyAggregationId = dailyAggregationId;
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

    public void setDayTemperature(double dayTemperature) {
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

    public double getMinimumDailyTemperature() {
        return minimumDailyTemperature;
    }

    public void setMinimumDailyTemperature(double minimumDailyTemperature) {
        this.minimumDailyTemperature = minimumDailyTemperature;
    }

    public double getMaximumDailyTemperature() {
        return maximumDailyTemperature;
    }

    public void setMaximumDailyTemperature(double maximumDailyTemperature) {
        this.maximumDailyTemperature = maximumDailyTemperature;
    }
}
