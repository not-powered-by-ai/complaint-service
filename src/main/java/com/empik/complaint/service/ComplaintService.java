package com.empik.complaint.service;

import com.empik.complaint.dto.Complaint;
import com.empik.complaint.dto.ComplaintCreateRequest;
import com.empik.complaint.dto.ComplaintUpdateRequest;
import com.empik.complaint.entity.ComplaintEntity;
import com.empik.complaint.exception.ComplaintNotFoundException;
import com.empik.complaint.exception.InvalidRequestException;
import com.empik.complaint.repository.ComplaintRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplaintService {
  private final ComplaintRepository repository;
  private final CountryService countryService;

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Complaint save(final ComplaintCreateRequest request, final String ip) {
    validate(request);
    var existingEntity = repository.findByProductIdAndReporter(request.productId(), request.reporter());
    if (existingEntity.isEmpty()) {
      return createNewComplaint(request, ip);
    }
    return incrementCounter(existingEntity.get());
  }

  @Transactional
  public Complaint update(final long complaintId, final ComplaintUpdateRequest request) {
    var entity = getOrNotFound(complaintId);
    if (request.content() == null || request.content().isBlank()) {
      log.error("Attempted to update complaint {} with blank content", complaintId);
      throw new InvalidRequestException("The content must be non-blank");
    }
    entity.setContent(request.content());
    var saved = repository.save(entity);
    return toDto(saved);
  }

  public Complaint getById(final long complaintId) {
    var entity = getOrNotFound(complaintId);
    return toDto(entity);
  }

  public List<Complaint> getAll() {
    return repository.findAll().stream().map(ComplaintService::toDto).toList();
  }

  private static void validate(final ComplaintCreateRequest request) {
    if (request.content() == null || request.content().isBlank()) {
      throw new InvalidRequestException("The content must be non-blank");
    }
    if (request.reporter() == null || request.reporter().isBlank()) {
      throw new InvalidRequestException("The reporter must be non-blank");
    }
    if (request.productId() == null) {
      throw new InvalidRequestException("Product id must be provided");
    }
  }

  private Complaint createNewComplaint(final ComplaintCreateRequest request, final String ip) {
    var country = countryService.findCountryForIp(ip).country();
    var entity = new ComplaintEntity();
    entity.setProductId(request.productId());
    entity.setContent(request.content());
    entity.setReporter(request.reporter());
    entity.setCountry(country);
    entity.setCounter(1L);
    var saved = repository.save(entity);
    return toDto(saved);
  }

  private Complaint incrementCounter(final ComplaintEntity entity) {
    long counter = entity.getCounter() + 1;
    entity.setCounter(counter);
    var saved = repository.save(entity);
    return toDto(saved);
  }

  private ComplaintEntity getOrNotFound(final long complaintId) {
    var entity = repository.findById(complaintId);
    if (entity.isEmpty()) {
      log.error("Complaint {} does not exist", complaintId);
      throw new ComplaintNotFoundException(complaintId);
    }
    return entity.get();
  }

  private static Complaint toDto(final ComplaintEntity entity) {
    return Complaint.builder()
        .id(entity.getId())
        .productId(entity.getProductId())
        .content(entity.getContent())
        .createdAt(entity.getCreatedAt())
        .reporter(entity.getReporter())
        .country(entity.getCountry())
        .counter(entity.getCounter())
        .build();
  }
}
