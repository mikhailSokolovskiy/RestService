package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import ru.sokolovskiy.restservice.Model.Positions;

@Service
public interface QuarterlyBonusService {

    double calculate(Positions positions, double salary, double bonus, int workDays);


}