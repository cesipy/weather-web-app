package at.qe.skeleton.external.repositories;

import at.qe.skeleton.external.domain.DailyAggregationData;
import at.qe.skeleton.internal.repositories.AbstractRepository;


/**
 * Repository interface for managing {@link DailyAggregationData} entities in the database.
 */
public interface DailyAggregationDataRepository extends AbstractRepository<DailyAggregationData, Long> {
}
