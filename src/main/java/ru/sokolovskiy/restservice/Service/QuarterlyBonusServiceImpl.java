package ru.sokolovskiy.restservice.Service;

import org.springframework.stereotype.Service;
import ru.sokolovskiy.restservice.Model.Positions;

import java.time.Year;

@Service
public class QuarterlyBonusServiceImpl implements QuarterlyBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays){
        double daysPerQuarter = 365 / 4.0;

        // Проверяем, является ли текущий год високосным
        int currentYear = java.time.Year.now().getValue();
        if ((currentYear % 4 == 0 && currentYear % 100 != 0) || (currentYear % 400 == 0)) {
            daysPerQuarter = 366 / 4.0;
        }

        // Рассчитываем квартальную премию
        return salary * bonus * daysPerQuarter * positions.getPositionCoefficient() / workDays;
    }

}
