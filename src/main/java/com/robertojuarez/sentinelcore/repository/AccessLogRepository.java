package com.robertojuarez.sentinelcore.repository;

import com.robertojuarez.sentinelcore.model.AccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends MongoRepository<AccessLog, String> {
}