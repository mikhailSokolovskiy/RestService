package ru.sokolovskiy.restservice.Service;

import org.junit.jupiter.api.Test;
import ru.sokolovskiy.restservice.Model.Positions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {

        //given
        Positions positions = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;


        //when
        double result = new AnnualBonusServiceImpl().calculate(positions, salary, bonus, workDays);


        //then
        double expected = 361481.48148148146;
        assertThat(result).isEqualTo(expected);

    }
}