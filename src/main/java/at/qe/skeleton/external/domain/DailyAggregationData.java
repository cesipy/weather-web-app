package at.qe.skeleton.external.domain;

import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.TemperatureAggregationDTO;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "daily_aggregation")
public class DailyAggregationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long daily_aggregation_id;
    private double morningTemperature;
    private double dayTemperature;
    private double eveningTemperature;
    private double nightTemperature;
    private double minimumDailyTemperature;
    private double maximumDailyTemperature;

    public DailyAggregationData() {
    }

    public Long getDaily_aggregation_id() {
        return daily_aggregation_id;
    }

    public void setDaily_aggregation_id(Long daily_aggregation_id) {
        this.daily_aggregation_id = daily_aggregation_id;
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
