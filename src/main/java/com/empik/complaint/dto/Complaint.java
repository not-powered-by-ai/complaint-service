package com.empik.complaint.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record Complaint(
  Long id,
  Long productId,
  String content,
  Instant createdAt,
  String reporter,
  String country,
  Long counter
){ }
