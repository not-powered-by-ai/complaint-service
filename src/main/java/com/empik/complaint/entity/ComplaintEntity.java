package com.empik.complaint.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.Instant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "complaints", uniqueConstraints={
    @UniqueConstraint(columnNames = {"product_id", "reporter"})})
@Data
public class ComplaintEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "content", nullable = false)
  private String content;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "reporter", nullable = false)
  private String reporter;

  @Column(name = "country", nullable = false)
  private String country;

  @Column(name = "counter", nullable = false)
  private Long counter;

  @Version
  private Long version;
}
