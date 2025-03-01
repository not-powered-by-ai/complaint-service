package com.empik.complaint.repository;

import com.empik.complaint.entity.ComplaintEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<ComplaintEntity, Long> {
  Optional<ComplaintEntity> findByProductIdAndReporter(long productId, String reporter);
}
