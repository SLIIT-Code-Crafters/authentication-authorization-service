package com.sep.authenticationauthorization.configuration.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sep.authenticationauthorization.configuration.enums.ApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Approvals")
public class Approval {
	
	@Id
	private Long id;
	
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "content", nullable = false)
	private String content;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "approval_status", nullable = false)
	private ApprovalStatus approvalStatus;
	
	@Column(name = "reason", nullable = false)
	private String reason;
	
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	
	@Column(name = "approved_by")
	private String approvedBy;
	
	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

}
