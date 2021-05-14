package com.tournoux.ws.btsocket.repo;

import com.tournoux.ws.btsocket.model.RcvrSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface RcvrSignalRepository extends JpaRepository<RcvrSignal, Long> {

}
