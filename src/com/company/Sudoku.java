package com.company;

import java.util.ArrayList;
import java.util.Arrays;

public class Sudoku {
    public Field[][] sudokufield;
    public boolean solved = false;
    public ArrayList<Field> changedFields = new ArrayList<Field>();

    public Sudoku(Field[][] sudokufield) {
        this.sudokufield = sudokufield;
    }

    private Field[][] getAllRows() {
        return sudokufield;
    }

    private Field[][] getAllColumns() {
        Field[][] columns = new Field[9][9];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                columns[y][x] = sudokufield[x][y];
            }
        }
        return columns;
    }

    private Field[][] getAllQuadrants() {
        Field[][] output = {{sudokufield[0][0], sudokufield[0][1], sudokufield[0][2], sudokufield[1][0], sudokufield[1][1], sudokufield[1][2], sudokufield[2][0], sudokufield[2][1], sudokufield[2][2]},
                            {sudokufield[0][3], sudokufield[0][4], sudokufield[0][5], sudokufield[1][3], sudokufield[1][4], sudokufield[1][5], sudokufield[2][3], sudokufield[2][4], sudokufield[2][5]},
                            {sudokufield[0][6], sudokufield[0][7], sudokufield[0][8], sudokufield[1][6], sudokufield[1][7], sudokufield[1][8], sudokufield[2][6], sudokufield[2][7], sudokufield[2][8]},

                            {sudokufield[3][0], sudokufield[3][1], sudokufield[3][2], sudokufield[4][0], sudokufield[4][1], sudokufield[4][2], sudokufield[5][0], sudokufield[5][1], sudokufield[5][2]},
                            {sudokufield[3][3], sudokufield[3][4], sudokufield[3][5], sudokufield[4][3], sudokufield[4][4], sudokufield[4][5], sudokufield[5][3], sudokufield[5][4], sudokufield[5][5]},
                            {sudokufield[3][6], sudokufield[3][7], sudokufield[3][8], sudokufield[4][6], sudokufield[4][7], sudokufield[4][8], sudokufield[5][6], sudokufield[5][7], sudokufield[5][8]},

                            {sudokufield[6][0], sudokufield[6][1], sudokufield[6][2], sudokufield[7][0], sudokufield[7][1], sudokufield[7][2], sudokufield[8][0], sudokufield[8][1], sudokufield[8][2]},
                            {sudokufield[6][3], sudokufield[6][4], sudokufield[6][5], sudokufield[7][3], sudokufield[7][4], sudokufield[7][5], sudokufield[8][3], sudokufield[8][4], sudokufield[8][5]},
                            {sudokufield[6][6], sudokufield[6][7], sudokufield[6][8], sudokufield[7][6], sudokufield[7][7], sudokufield[7][8], sudokufield[8][6], sudokufield[8][7], sudokufield[8][8]},
        };
        return output;
    }

    public boolean isSolved() {
        if (checkGridForErrors() && checkForZero()) {
            return true;
        }
        return false;
    }

    private Field[][][] allVariations() {
        Field[][][] allVariations = {getAllRows(), getAllColumns(), getAllQuadrants()};
        return allVariations;
    }

    public boolean checkGridForErrors() {
        Field[][][] allVariations = allVariations();
        for (Field[][] grid : allVariations) {
            for (Field[] line : grid) {
                if (checkArrayForDouble(line)) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkArrayForDouble(Field array[]) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (array[y].value != array[x].value || x == y || array[y].value == 0 || array[x].value == 0) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkForZero() {
        for (Field[] y : sudokufield) {
            for (Field x : y) {
                if (x.value == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Field[][] allCoherentFieldsOfExactOnePosition(Field field) {
        Field[][] output = new Field[3][9];
        for (int i = 0; i < 3; i++) {
            output[i] = findsLineOfAFieldInAGrid(allVariations()[i], field);
            //der Output ist ein Array von jeweils dem Quadranten, der Reihe und der Spalten. Das Array von den dreien
            //wird ausgewählt anhand der einstimmigkeit mit dem Mitgegebenen Field.
            //Dies ist wichtig für die Angleichung der MöglichkeitenListe
        }
        return output;
    }

    private Field[] findsLineOfAFieldInAGrid(Field[][] grid, Field initial) {
        for (Field[] y : grid) {
            for (Field x : y) {
                if (x == initial) {
                    return y;
                }
            }
        }
        System.out.println("Error in: findsLineOfAFieldInAGrid(...)");
        return null;
    }

    public void deleteANumberFromAllConnectedUsableDueToFieldValueChange(Field field, int number) {//changed removedAll without knowing what it would do
        for (Field[] line : allCoherentFieldsOfExactOnePosition(field)) {
            for (Field lineField : line) {
                //lineField.usable.remove(Integer.valueOf(number));
                lineField.usable.removeAll(Arrays.asList(number));
            }
        }
    }

    public void addANumberFromAllConnectedUsableDueToFieldValueChangeIfNotExistsAnyways(Field field, int number) {
        for (Field[] line : allCoherentFieldsOfExactOnePosition(field)) {
            for (Field lineField : line) {
                if (numberExistsInLine(lineField.usable, number)) {
                    lineField.usable.add(number);
                }
            }
        }
    }

    private boolean numberExistsInLine(ArrayList<Integer> usable, int number) {
        for (int numFromUsable : usable) {
            if (number == numFromUsable) {
                return false;
            }
        }
        return true;
    }

    public void setValueOfField(Field field, int number) {
        field.value = number;
        deleteANumberFromAllConnectedUsableDueToFieldValueChange(field, number);
    }

    public void deleteValueOfField(Field field) {
        int deletedValue = field.value;
        field.value = 0;
        //hier kommt das problem, was ist wenn es durch andere negativ beeinflusst ist und somit nicht eifach hinzugefügt werden kann (die Zahl)????
        for (Field[] line : allCoherentFieldsOfExactOnePosition(field)) {
            for (Field lineField : line) {
                if (checkIfNumberIsUsable(lineField, deletedValue)) {
                    lineField.usable.add(deletedValue);
                }
            }
        }
    }

    private boolean checkIfNumberIsUsable(Field field, int deletedValue) {
        for (Field[] line : allCoherentFieldsOfExactOnePosition(field)) {
            for (Field lineField : line) {
                if(lineField.value == deletedValue){
                    return false;
                }
            }
        }
        return true;
    }
}
