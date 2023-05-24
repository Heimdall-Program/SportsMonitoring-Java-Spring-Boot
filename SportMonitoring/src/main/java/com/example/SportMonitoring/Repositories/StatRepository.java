package com.example.SportMonitoring.Repositories;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {
    List<Stat> findByUser(User user);

    List<Stat> findByTypeIdOrderByValueDesc(Long typeId);
}
