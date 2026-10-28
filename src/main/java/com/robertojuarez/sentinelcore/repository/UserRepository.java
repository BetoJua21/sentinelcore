package com.robertojuarez.sentinelcore.repository;

import com.robertojuarez.sentinelcore.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByRfidUid(String rfidUid);
}
