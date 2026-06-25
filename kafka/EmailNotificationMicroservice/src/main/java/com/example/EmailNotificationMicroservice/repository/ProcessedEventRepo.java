package com.example.EmailNotificationMicroservice.repository;

import com.example.EmailNotificationMicroservice.model.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepo extends JpaRepository<ProcessedEventEntity,Long> {

    ProcessedEventEntity findByMessageId(String messageId);
}
