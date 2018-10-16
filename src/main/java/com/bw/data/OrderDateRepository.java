package com.bw.data;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.bw.DTO.*;

@Repository
public interface OrderDateRepository extends JpaRepository<OrderDate, Integer>{


}
