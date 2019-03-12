package com.cherkashyn.telegramchart.model;

import java.util.List;

public class Line {

    private List<Integer> y;
    private String name;
    private String color;

    public List<Integer> getY() {
        return y;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
