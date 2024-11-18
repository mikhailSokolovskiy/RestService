package ru.sokolovskiy.restservice.Model;

import lombok.Getter;

@Getter
public enum Positions {

    DEV(2.2, false),
    HR(1.2, false),
    TL(2.6, false),
    TESTER(1.4, false),
    SECURITY(1.2, false),
    MANAGER(1.2,true);




    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

}
