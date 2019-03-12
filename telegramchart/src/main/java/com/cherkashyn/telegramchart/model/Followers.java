package com.cherkashyn.telegramchart.model;

import java.util.List;

public class Followers {

    private List<Long> x;
    private Line lineYZero;
    private Line lineYOne;

    public List<Long> getX() {
        return x;
    }

    public void setX(List<Long> x) {
        this.x = x;
    }

    public Line getLineYZero() {
        return lineYZero;
    }

    public void setLineYZero(Line lineYZero) {
        this.lineYZero = lineYZero;
    }

    public Line getLineYOne() {
        return lineYOne;
    }

    public void setLineYOne(Line lineYOne) {
        this.lineYOne = lineYOne;
    }
}
