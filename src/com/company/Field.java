package com.company;

import java.util.ArrayList;

public class Field{
    public int Y;
    public int X;
    public int value;
    public ArrayList<Integer> usable;
    public ArrayList<Integer> backtrack = new ArrayList<>();

    public Field(int Y, int X, int value, ArrayList usable) {
        this.X = X;
        this.Y = Y;
        this.value = value;
        this.usable = new ArrayList<>(usable);
    }
}
