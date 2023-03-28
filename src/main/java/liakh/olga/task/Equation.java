package liakh.olga.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Equation {
    private int id;
    private String equation;

    public Equation() {
    }

    public Equation(int id, String equation) {
        this.id = id;
        this.equation = equation;
    }

    public Equation(String equation) {
        this.equation = equation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }

    public String getEquationFromScanner(Scanner sc) {
        int counter = 0;
        String equation = sc.nextLine();
        while (counter != 2) {
            if (equation.isEmpty()) {   //check if line empty
                System.out.println("Please try again! The line is empty");
                equation = sc.nextLine();
            } else if (equation.contains("x")) {    //check is line contains "x"
                if (checkBrackets(equation)) {      //check is line contains pairs of brackets
                    if (checkTheOperations(equation)) return equation; //check is all arithmetical signs are correct
                } else {
                    break;
                }
            } else {
                System.out.println("Your equation has no X! Try again!");
                equation = sc.nextLine();
            }
            counter++; //3 attempts
        }
        return "";
    }

    public boolean checkBrackets(String input) {
        char[] chr = input.toCharArray();
        int ctr1 = 0;
        int ctr2 = 0;
        for (char c : chr) {
            if (c == '(') {
                ctr1++;
            }
            if (c == ')') {
                ctr2++;
            }
        }
        if (ctr1 != ctr2) {
            throw new IllegalArgumentException("You made mistake! Check the brackets please!");
        }
        return true;
    }

    private boolean checkTheOperations(String input) {
        if (input.contains("+*") || input.contains("+-") ||
                input.contains("*+") || input.contains("-*") || input.contains("/*") || input.contains("*/")) {
            System.out.println("You made mistake! Check the signs!");
            return false;
        }
        return true;
    }

    public int amountOfNumbers(String input) {
        int counter = 0;
        Pattern pattern = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group());
            counter++;
        }
        return counter;
    }

    @Override
    public String toString() {
        return "Equation{" +
                "id=" + id +
                ", equation='" + equation + '\'' +
                '}';
    }
}
