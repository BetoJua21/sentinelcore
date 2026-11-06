
package com.robertojuarez.sentinelcore.repository;

import com.robertojuarez.sentinelcore.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AccessLogRepository extends MongoRepository<AccessLog, String> {

    Page<AccessLog> findByGrantedTrue(Pageable pageable);

    Page<AccessLog> findByGrantedFalse(Pageable pageable);

    Page<AccessLog> findByRfidUid(
            String rfidUid,
            Pageable pageable
    );

    Page<AccessLog> findByRfidUidAndGranted(
            String rfidUid,
            boolean granted,
            Pageable pageable
    );

    long countByGrantedTrue();

    long countByGrantedFalse();

    @Query("{ 'timestamp': { '$gte': ?0, '$lt': ?1 } }")
    List<AccessLog> findAccessLogsBetweenDates(
            Instant start,
            Instant end
    );
}
