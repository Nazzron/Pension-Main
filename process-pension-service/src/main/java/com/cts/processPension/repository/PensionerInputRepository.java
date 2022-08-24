package com.cts.processPension.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.processPension.entity.PensionerInput;


@Repository
public interface PensionerInputRepository extends JpaRepository<PensionerInput, String> {

}
