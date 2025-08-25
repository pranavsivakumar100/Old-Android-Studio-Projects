package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import org.mariuszgromada.math.mxparser.*;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.lang.Math.*;

public class CalculatorApp extends AppCompatActivity {

    private EditText display;
    private ArrayList<String> integers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        integers = new ArrayList<String>();
        display = findViewById(R.id.input);

        // OnClickListener to display TextView
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getString(R.string.display).equals(display.getText().toString())) {
                    display.setText("");
                }
            }
        });
    }

    /**
     * Update TextView when a button is clicked
     * @param strToAdd The String value to add in the TextView display
     */
    private void updateText(String strToAdd) {
        String oldStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(strToAdd);
        } else {
            display.setText(String.format("%s%s%s", leftStr, strToAdd, rightStr));
        }
        display.setSelection(cursorPos + 1);
    }

    /**
     * Determines whether an open or closed parentheses is displayed in the TextView
     * @param view Accesses the View class
     */
    public void parenthesesButton(View view) {
        int cursorPos = display.getSelectionStart();
        int textLength = display.getText().length();
        int openPar = 0, closedPar = 0;

        for (int i = 0; i < cursorPos; i++) {
            if (display.getText().toString().substring(i, i + 1).equals("(")) {
                openPar += 1;
            }
            if (display.getText().toString().substring(i, i + 1).equals(")")) {
                closedPar += 1;
            }
        }

        if (openPar == closedPar || display.getText().toString().substring(textLength - 1, textLength).equals("(")) {
            updateText("(");
            display.setSelection(cursorPos + 1);
        } else if (closedPar < openPar || !display.getText().toString().substring(textLength - 1, textLength).equals("(")) {
            updateText(")");
        }
        display.setSelection(cursorPos + 1);
    }

    /**
     * Calculates the expression in the TextView and displays the result.
     * @param view Accesses the View class
     */
    public void equalButton(View view) {
        try {

            String userExpression = display.getText().toString();

            userExpression = userExpression.replaceAll("÷", "/");
            userExpression = userExpression.replaceAll("×", "*");

            Expression exp = new Expression(userExpression);
            String result = String.valueOf(exp.calculate());

            add(integers);
            subtract(integers);
            multiply(integers);
            divide(integers);

            display.setText(result);
            display.setSelection(result.length());

        } catch(Exception arithmeticException) {
            display.setText("NaN");
        }
    }

    /**
     * Delete last character in TextView when button is clicked
     * @param view Accesses the View class
     */
    public void backspaceButton(View view) {
        int cursorPos = display.getSelectionStart();
        int textLength = display.getText().length();

        if (cursorPos != 0 && textLength != 0) {
            SpannableStringBuilder selection = (SpannableStringBuilder) display.getText();
            selection.replace(cursorPos - 1, cursorPos, "");
            display.setText(selection);
            display.setSelection(cursorPos - 1);
        }
    }

    /**
     * Add integers
     * @param list List containing all integers
     */
    public void add(ArrayList<String> list) {
        int sum = 0;
        for (String element : list){
            try {
                int num = Integer.parseInt(element);
                sum += num;
            } catch (Exception e){
                System.out.println ("Element " + element + " in the array is not an integer");
            }
        }
        returnNum(sum);
    }

    /**
     * Subtract Integers
     * @param list List containing all integers
     */
    public void subtract(ArrayList<String> list) {
        int result = 0;
        for (String element : list) {
            try {
                int num = Integer.parseInt(element);
                result -= num;
            } catch (Exception e) {
                System.out.println ("Element " + element + " in the array is not an integer");
            }
        }
        returnNum(result);
    }

    /**
     * Multiply Integers
     * @param list List containing all integers
     */
    public void multiply(ArrayList<String> list) {
        int product = 0;
        for (String element : list) {
            try {
                int num = Integer.parseInt(element);
                product *= num;
            } catch (Exception e) {
                System.out.println(" Element " + element + " in the array is not an integer");
            }
        }
        returnNum(product);
    }

    /**
     * Dividing Integers
     * @param list List containing all integers
     */
    public void divide(ArrayList<String> list) {
        int result = 0, count = 0;
        for (String element : list) {
            try {
                int num = Integer.parseInt(element);
                result /= num;
            } catch(ArithmeticException e) {
                System.out.println("ArithmeticException Error");
            } catch(Exception e) {
                System.out.println(" Element " + element + " in the array is not an integer");
            }
        }
        returnNum(result);
    }

    /**
     * onClick function to set TextView to a String value when a button is clicked
     * @param view Accesses the View class
     */
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button0:
                integers.add("0");
                updateText("0");
                break;

            case R.id.button1:
                integers.add("1");
                updateText("1");
                break;

            case R.id.button2:
                integers.add("2");
                updateText("2");
                break;

            case R.id.button3:
                integers.add("3");
                updateText("3");
                break;

            case R.id.button4:
                integers.add("4");
                updateText("4");
                break;
            case R.id.button5:
                integers.add("5");
                updateText("5");
                break;
            case R.id.button6:
                integers.add("6");
                updateText("6");
                break;
            case R.id.button7:
                integers.add("7");
                updateText("7");
                break;
            case R.id.button8:
                integers.add("8");
                updateText("8");
                break;

            case R.id.button9:
                integers.add("9");
                updateText("9");
                break;

            case R.id.decimalButton:
                updateText(".");
                break;

            case R.id.divideButton:
                updateText("÷");
                break;

            case R.id.multiplyButton:
                updateText("×");
                break;
            case R.id.subtractButton:
                updateText("-");
                break;
            case R.id.addButton:
                updateText("+");
                break;

            case R.id.plusMinusButton:
                updateText("-");
                break;

            case R.id.exponentButton:
                updateText("^");
                break;
        }
    }

    /**
     * Button to clear TextView
     * @param view Accesses the View class
     */
    public void clearButton(View view) {
        display.setText("");
    }

    /**
     * returns an integer inputed
     * @param num the integer inputed
     * @return the integer parameter
     */
    public int returnNum(int num) {
        return num;
    }

}