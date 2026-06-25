package com.example.EmailNotificationMicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="processed-events")
public class ProcessedEventEntity implements Serializable {
    @Serial
    private static final long serialVersionUID=20000000000000000L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,unique = true)
    private String messageId;

    @Column(nullable = false)
    private String productId;

    public ProcessedEventEntity(String messageId, String productId) {
        this.messageId = messageId;
        this.productId = productId;
    }
}
