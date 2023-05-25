package com.example.SportMonitoring.Controllers;

import com.example.SportMonitoring.Models.Stat;
import com.example.SportMonitoring.Models.User;
import com.example.SportMonitoring.Models.StatType;
import com.example.SportMonitoring.Repositories.UserRepository;
import com.example.SportMonitoring.Repositories.StatTypeRepository;
import com.example.SportMonitoring.Repositories.StatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;

@Service
public class AnalysisService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatTypeRepository statTypeRepository;

    @Autowired
    private StatRepository statRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<StatType> getAllStatTypes() {
        return statTypeRepository.findAll();
    }

    public List<Stat> getStats(User user, StatType statType) {
        return statRepository.findByUserAndType(user, statType);
    }

    public double calculateProgress(List<Stat> stats) {
        stats.sort(Comparator.comparing(Stat::getAssignedDate));
        if (stats.size() > 1) {
            double earliestValue = stats.get(0).getValue();
            double latestValue = stats.get(stats.size() - 1).getValue();
            return latestValue - earliestValue;
        }
        return 0;
    }

    public double calculateProjection(List<Stat> stats) {
        stats.sort(Comparator.comparing(Stat::getAssignedDate));
        if (stats.size() > 1) {
            double earliestValue = stats.get(0).getValue();
            double latestValue = stats.get(stats.size() - 1).getValue();
            double progress = latestValue - earliestValue;
            return latestValue + progress;
        }
        return 0;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public StatType findStatTypeById(Long statTypeId) {
        return statTypeRepository.findById(statTypeId).orElse(null);
    }

}
