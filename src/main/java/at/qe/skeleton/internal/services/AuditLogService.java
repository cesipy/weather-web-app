package at.qe.skeleton.internal.services;

import at.qe.skeleton.external.controllers.CurrentlyHourlyDailyWeather;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.domain.HourlyWeatherData;
import at.qe.skeleton.external.model.currentandforecast.CurrentAndForecastAnswerDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyTemperatureAggregationDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.DailyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.HourlyWeatherDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HolidayDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.HumidityDTO;
import at.qe.skeleton.external.model.currentandforecast.misc.holiday.PrecipitationDTO;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.external.model.weather.CurrentWeatherData;
import at.qe.skeleton.external.repositories.CurrentWeatherDataRepository;
import at.qe.skeleton.external.services.ApiQueryException;
import at.qe.skeleton.external.services.WeatherApiRequestService;
import at.qe.skeleton.external.services.WeatherDataService;
import at.qe.skeleton.internal.model.AuditLog;
import at.qe.skeleton.internal.model.Userx;
import at.qe.skeleton.internal.repositories.AuditLogRepository;
import at.qe.skeleton.external.repositories.DailyWeatherDataRepository;
import at.qe.skeleton.external.repositories.HourlyWeatherDataRepository;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing audit log entries.
 * This service is responsible for saving audit log entries, specifically
 * user deletion entries, and retrieving all audit log entries.
 *
 * @see Service
 */
@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Saves a generic audit log entry with the specified message and current timestamp.
     *
     * @param message The message to be saved in the audit log.
     */
    public void saveEntry(String message) {
        AuditLog al = new AuditLog();
        al.setMessage(message);
        al.setDate(LocalDateTime.now());
        auditLogRepository.save(al);
    }

    /**
     * Saves an audit log entry for a user deletion with the specified user.
     *
     * @param userx The user whose deletion is being logged.
     */
    public void saveUserDeletedEntry(Userx userx) {
        saveEntry("User with username " + userx.getUsername() + " has been deleted!");
    }

    /**
     * Retrieves all audit log entries from the repository.
     *
     * @return A list of all audit log entries.
     */
    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}
