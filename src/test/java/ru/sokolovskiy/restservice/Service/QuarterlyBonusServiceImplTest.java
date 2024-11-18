package ru.sokolovskiy.restservice.Service;

import org.junit.jupiter.api.Test;
import ru.sokolovskiy.restservice.Model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuarterlyBonusServiceImplTest {

    @Test
    void calculate() {


        //given
        Positions positions = Positions.MANAGER;
        double bonus = 1.3;
        int workDays = 243;
        double salary = 100000.00;


        //when
        double result = new QuarterlyBonusServiceImpl().calculate(positions, salary, bonus, workDays);


        //then
        double expected = 58740.74074074074;
        assertThat(result).isEqualTo(expected);

    }
}