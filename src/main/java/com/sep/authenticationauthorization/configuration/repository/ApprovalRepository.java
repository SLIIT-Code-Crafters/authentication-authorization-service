package com.sep.authenticationauthorization.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.authenticationauthorization.configuration.entity.approval.Approval;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long>{

}
