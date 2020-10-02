package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    private static Sudoku game;
    private static int showcounter = 0;

    public static void main(String[] args) {
        initSudoku();
        newSudokuValues();
        showSudoku();
        solveSudoku();
        showSudoku();

    }

    private static void newSudokuValues2() {
        //row 1
        game.setValueOfField(game.sudokufield[0][1], 5);
        game.setValueOfField(game.sudokufield[0][4], 8);

        game.setValueOfField(game.sudokufield[1][1], 6);
        game.setValueOfField(game.sudokufield[1][5], 4);
        game.setValueOfField(game.sudokufield[1][6], 3);

        game.setValueOfField(game.sudokufield[2][0], 7);
        game.setValueOfField(game.sudokufield[2][3], 3);
        game.setValueOfField(game.sudokufield[2][8], 8);

        //row 2
        game.setValueOfField(game.sudokufield[3][1], 1);
        game.setValueOfField(game.sudokufield[3][5], 7);
        game.setValueOfField(game.sudokufield[3][8], 3);

        game.setValueOfField(game.sudokufield[4][2], 5);
        game.setValueOfField(game.sudokufield[4][4], 1);
        game.setValueOfField(game.sudokufield[4][6], 6);

        game.setValueOfField(game.sudokufield[5][0], 3);
        game.setValueOfField(game.sudokufield[5][3], 2);
        game.setValueOfField(game.sudokufield[5][7], 7);

        //row 3
        game.setValueOfField(game.sudokufield[6][0], 6);
        game.setValueOfField(game.sudokufield[6][5], 5);
        game.setValueOfField(game.sudokufield[6][8], 2);

        game.setValueOfField(game.sudokufield[7][2], 1);
        game.setValueOfField(game.sudokufield[7][3], 7);
        game.setValueOfField(game.sudokufield[7][7], 3);

        game.setValueOfField(game.sudokufield[8][4], 4);
        game.setValueOfField(game.sudokufield[8][7], 6);
    }

    private static void newSudokuValues(){
        //row0
        game.setValueOfField(game.sudokufield[0][7], 4);
        game.setValueOfField(game.sudokufield[0][8], 8);
        //row1
        game.setValueOfField(game.sudokufield[1][1], 9);
        game.setValueOfField(game.sudokufield[1][2], 8);
        game.setValueOfField(game.sudokufield[1][3], 2);
        game.setValueOfField(game.sudokufield[1][8], 7);
        //row2
        game.setValueOfField(game.sudokufield[2][3], 4);
        game.setValueOfField(game.sudokufield[2][4], 3);
        game.setValueOfField(game.sudokufield[2][8], 6);
        //________________________________________
        //row3
        game.setValueOfField(game.sudokufield[3][0], 9);
        game.setValueOfField(game.sudokufield[3][1], 8);
        //row4
        game.setValueOfField(game.sudokufield[4][3], 3);
        game.setValueOfField(game.sudokufield[4][4], 1);
        //row5
        game.setValueOfField(game.sudokufield[5][0], 7);
        game.setValueOfField(game.sudokufield[5][7], 9);
        game.setValueOfField(game.sudokufield[5][8], 4);
        //--------------------------------------------
        //row6
        game.setValueOfField(game.sudokufield[6][2], 9);
        game.setValueOfField(game.sudokufield[6][6], 6);
        //row7
        game.setValueOfField(game.sudokufield[7][3], 7);
        game.setValueOfField(game.sudokufield[7][5], 2);
        //row8
        game.setValueOfField(game.sudokufield[8][0], 5);
        game.setValueOfField(game.sudokufield[8][2], 3);
        game.setValueOfField(game.sudokufield[8][5], 4);
        game.setValueOfField(game.sudokufield[8][6], 8);
    }

    private static void solveSudoku() {
        while (game.isSolved() == false) {
            boolean putNew = putNew();//puts new value in changedList ! ! !
            if (putNew == false) {
                backtrack();
            }
            if (game.changedFields.size() > 0) {
                Field activeField = getLastFromChangedFields();
                if (activeField.usable.size() > 0) {
                    game.setValueOfField(activeField, activeField.usable.get(0));
                } else {
                    backtrack();
                }
            } else {
                System.out.println("Internal error please recall the function: \"solveSudoku() in Main.java\"");
            }
           // showSudoku();
        }
    }

    private static boolean putNew() {
        Field bestOption = bestOption();
        if (bestOption == null) {
            return false;
        } else {
            game.changedFields.add(bestOption);
            return true;
        }
    }

    private static void backtrack() {
        if (doesLastFieldInChangedListHaveUsableValues()) {
            getLastFromChangedFields().backtrack.add(getLastFromChangedFields().value);
            setNextPossibleValueOfChangedList();
        } else {
            addDeletedBacktrackValuesToUsable();
            getLastFromChangedFields().backtrack.removeAll(beginningUsableValues());
            removeLastFromChangedFieldsAndDeleteValue();
            backtrack();
        }
    }

    private static void addDeletedBacktrackValuesToUsable() {
        for(int i : getLastFromChangedFields().backtrack){
            if(game.checkIfNumberIsUsable(getLastFromChangedFields(),i)){
                getLastFromChangedFields().usable.add(i);
            }
        }
    }

    private static void setNextPossibleValueOfChangedList() {
        if(getLastFromChangedFields().value == 0) {
            game.setValueOfField(getLastFromChangedFields(), getLastFromChangedFields().usable.get(0));
        }else{
            int help = getLastFromChangedFields().value;
            game.deleteValueOfField(getLastFromChangedFields());
            getLastFromChangedFields().usable.removeAll(Arrays.asList(help));
            game.setValueOfField(getLastFromChangedFields(), getLastFromChangedFields().usable.get(0));
        }
    }

    private static void removeLastFromChangedFieldsAndDeleteValue() {
        game.deleteValueOfField(getLastFromChangedFields());
        game.changedFields.remove(game.changedFields.size() - 1);
    }

    private static Field getLastFromChangedFields() {
        return game.changedFields.get(game.changedFields.size() - 1);
    }

    private static boolean doesLastFieldInChangedListHaveUsableValues() {
        return getLastFromChangedFields().usable.size() > 0;
    }

    private static Field bestOption() {
        int bestOption = Integer.MAX_VALUE;
        Field bestOptionField = null;
        for (Field[] Y : game.sudokufield) {
            for (Field X : Y) {
                if (X.usable.size() < bestOption && X.value == 0) {
                    bestOption = X.usable.size();
                    bestOptionField = X;
                    if (bestOption == 1) {
                        return bestOptionField;
                    }
                }
            }
        }
        return bestOptionField;
    }

    private static void initSudoku() {
        Field[][] fields = new Field[9][9];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                fields[y][x] = new Field(y, x, 0, beginningUsableValues());
            }
        }
        game = new Sudoku(fields);
    }

    private static ArrayList beginningUsableValues() {
        ArrayList<Integer> output = new ArrayList<>();
        output.add(1);
        output.add(2);
        output.add(3);
        output.add(4);
        output.add(5);
        output.add(6);
        output.add(7);
        output.add(8);
        output.add(9);
        return output;
    }

    private static void showSudoku() {
        showcounter++;
        int counter2 = 0;
        for (Field[] f : game.sudokufield) {
            int counter = 0;
            for (Field field : f) {
                System.out.print(field.value + " ");
                counter++;
                if (counter == 3) {
                    System.out.print("- ");
                    counter = 0;
                }
            }
            System.out.println();
            counter2++;
            if (counter2 == 3) {
                System.out.println("-----------------------");
                counter2 = 0;
            }
        }
        System.out.println("------" + showcounter);
    }
}
