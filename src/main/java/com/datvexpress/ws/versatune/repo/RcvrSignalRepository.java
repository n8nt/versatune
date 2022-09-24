package com.datvexpress.ws.versatune.repo;

import com.datvexpress.ws.versatune.model.RcvrSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface RcvrSignalRepository extends JpaRepository<RcvrSignal, Long> {

}
