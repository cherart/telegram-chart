package com.cherkashyn.telegramchart.model;

import java.util.List;

public class Line {

    private List<Integer> listOfY;
    private String name;
    private String color;

    public List<Integer> getListOfY() {
        return listOfY;
    }

    public void setListOfY(List<Integer> listOfY) {
        this.listOfY = listOfY;
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
