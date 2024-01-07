package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.DailyWeatherData;
import at.qe.skeleton.external.model.location.Location;
import at.qe.skeleton.internal.model.Userx;

/**
 * Repository interface for managing {@link DailyAggregationData} entities in the database.
 */
public interface DailyAggregationDataRepository extends AbstractRepository<DailyAggregationData, Long>{
}
