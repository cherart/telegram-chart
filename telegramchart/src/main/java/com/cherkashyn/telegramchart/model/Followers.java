package com.cherkashyn.telegramchart.model;

import java.util.ArrayList;
import java.util.List;

public class Followers {

    private List<Long> x;
    private List<Line> lines = new ArrayList<>();

    public List<Long> getX() {
        return x;
    }

    public void setX(List<Long> x) {
        this.x = x;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }
}
