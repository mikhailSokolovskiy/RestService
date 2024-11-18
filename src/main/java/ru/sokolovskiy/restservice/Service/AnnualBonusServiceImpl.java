package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import ru.sokolovskiy.restservice.Model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysPerYear = 365;
        if ((Year.now().getValue() % 4 == 0 && Year.now().getValue() % 100 != 0) || (Year.now().getValue() % 400 == 0)) {
            daysPerYear = 366;
        }
        return salary * bonus * daysPerYear * positions.getPositionCoefficient() / workDays;
    }
}
