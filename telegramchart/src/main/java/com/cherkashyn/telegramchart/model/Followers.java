package com.cherkashyn.telegramchart.model;

import java.util.ArrayList;
import java.util.List;

public class Followers {

    private List<Long> listOfX;
    private List<Line> lines = new ArrayList<>();

    public List<Long> getListOfX() {
        return listOfX;
    }

    public void setListOfX(List<Long> listOfX) {
        this.listOfX = listOfX;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }
}
