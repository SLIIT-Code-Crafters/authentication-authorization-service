package com.sep.authenticationauthorization.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.authenticationauthorization.configuration.entity.mastertoken.MasterToken;

@Repository
public interface MasterTokenRepository extends JpaRepository<MasterToken, Long>{

}
