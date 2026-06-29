package com.example.TransferMicroservice.repository;

import com.example.TransferMicroservice.model.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends JpaRepository<TransferEntity,String> {
}
