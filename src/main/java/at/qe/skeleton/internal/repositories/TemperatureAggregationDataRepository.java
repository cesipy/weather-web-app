package at.qe.skeleton.internal.repositories;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.external.domain.TemperatureAggregationData;
/**
 * Repository interface for managing {@link DailyAggregationData} entities in the database.
 */
public interface TemperatureAggregationDataRepository extends AbstractRepository<TemperatureAggregationData, Long> {
}
