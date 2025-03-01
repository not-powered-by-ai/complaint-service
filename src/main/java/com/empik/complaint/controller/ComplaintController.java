package com.empik.complaint.controller;

import com.empik.complaint.dto.Complaint;
import com.empik.complaint.dto.ComplaintCreateRequest;
import com.empik.complaint.dto.ComplaintUpdateRequest;
import com.empik.complaint.service.ComplaintService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ComplaintController {
  private final ComplaintService complaintService;

  @PostMapping("/complaints")
  public ResponseEntity<Complaint> save(@RequestBody ComplaintCreateRequest request,
      @RequestHeader("X-Forwarded-For") String ip) {
    Complaint savedComplaint = complaintService.save(request, ip);
    return new ResponseEntity<>(savedComplaint, HttpStatus.CREATED);
  }

  @PatchMapping("/complaints/{complaintId}")
  public ResponseEntity<Complaint> update(@PathVariable(name = "complaintId") long complaintId,
      @RequestBody ComplaintUpdateRequest request) {
    Complaint updatedComplaint = complaintService.update(complaintId, request);
    return new ResponseEntity<>(updatedComplaint, HttpStatus.OK);
  }

  @GetMapping("/complaints/{complaintId}")
  public ResponseEntity<Complaint> getById(@PathVariable(name = "complaintId") long complaintId) {
    Complaint complaint = complaintService.getById(complaintId);
    return new ResponseEntity<>(complaint, HttpStatus.OK);
  }

  @GetMapping("/complaints")
  public ResponseEntity<List<Complaint>> getAll() {
    var all = complaintService.getAll();
    return new ResponseEntity<>(all, HttpStatus.OK);
  }
}
