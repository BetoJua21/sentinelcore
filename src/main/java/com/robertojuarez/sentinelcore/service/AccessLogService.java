
package com.robertojuarez.sentinelcore.service;

import com.robertojuarez.sentinelcore.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class AccessLogService {

    private final MongoTemplate mongoTemplate;

    public AccessLogService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<AccessLog> searchLogs(
            LocalDate date,
            String rfidUid,
            Boolean granted,
            int page,
            int size
    ) {
        ZoneId zone = ZoneId.of("America/Mexico_City");
        Query query = new Query();

        if (date != null) {
            Instant start = date.atStartOfDay(zone).toInstant();
            Instant end = date.plusDays(1).atStartOfDay(zone).toInstant();

            query.addCriteria(
                    Criteria.where("timestamp").gte(start).lt(end)
            );
        }

        if (rfidUid != null && !rfidUid.isBlank()) {
            query.addCriteria(
                    Criteria.where("rfidUid").is(rfidUid.trim())
            );
        }

        if (granted != null) {
            query.addCriteria(
                    Criteria.where("granted").is(granted)
            );
        }

        long total = mongoTemplate.count(query, AccessLog.class);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        query.with(pageable);

        List<AccessLog> logs = mongoTemplate.find(query, AccessLog.class);

        return new PageImpl<>(logs, pageable, total);
    }
}
