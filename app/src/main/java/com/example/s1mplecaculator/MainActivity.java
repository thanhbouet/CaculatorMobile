package com.example.s1mplecaculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    EditText rsl;
    EditText pre;
    boolean equalPressed;

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else if (eat('%')) x %= parseFactor(); //mod
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rsl = findViewById(R.id.edttxt_rsl);
        pre = findViewById(R.id.edttxt_preExp);
    }

    public void keyEvent(View view) {
        String number = rsl.getText().toString();

        switch (view.getId()) {
            case R.id.btn_C:
                equalPressed = false;
                number = "0";
                break;
            case R.id.btn_del:
                equalPressed = false;
                StringBuilder temp = new StringBuilder(number);
                temp.deleteCharAt(temp.length() - 1);
                number = temp.toString();
                break;
            case R.id.btn_0:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                if (!number.equals("0") && !number.equals("0000")) {
                    number += "0";
                    break;
                }
                break;
            case R.id.btn_1:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "1";
                break;
            case R.id.btn_2:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "2";
                break;
            case R.id.btn_3:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "3";
                break;
            case R.id.btn_4:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "4";
                break;
            case R.id.btn_5:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "5";
                break;
            case R.id.btn_6:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "6";
                break;
            case R.id.btn_7:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "7";
                break;
            case R.id.btn_8:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "8";
                break;
            case R.id.btn_9:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                number += "9";
                break;
            case R.id.btn_add:
                equalPressed = false;
                if (number.charAt(number.length() - 1) != '+' && !number.equals("")) {
                    number += "+";
                    break;
                } break;
            case R.id.btn_div:
                equalPressed = false;
                if (!number.equals("0") && !number.equals("")) {
                    number += "/";
                    break;
                }break;
            case R.id.btn_mod:
                equalPressed = false;
                if (!number.equals("0") && !number.equals("")) {
                    number += "%";
                    break;
                }break;
            case R.id.btn_dot:
                if (equalPressed) {
                    number = "";
                    equalPressed = false;
                }
                if (number.equals("")) {
                    number += "0.";
                } else {
                    number += ".";
                }
                break;
            case R.id.btn_mul:
                equalPressed = false;
                if (!number.equals("0")) {
                    number += "*";
                    break;
                }break;
            case R.id.btn_sub:
                equalPressed = false;
                number += "-";
                break;

            case R.id.btn_eql:
                if (equalPressed) {
                    equalPressed = false;
                    break;
                }
                String exp = number;
                try {
                    number = Double.toString(eval(exp));
                    double tempDb = Double.parseDouble(number);
                    long tempInt = Math.round(tempDb);
                    if (tempDb == (double) tempInt) {
                        number = Long.toString(tempInt);
                    }
                    pre.setText(exp);
                } catch (Exception e) {
                    number = "Illegal Expression";
                } finally {

                    equalPressed = true;
                }
                break;
        }
        if (number.length() >= 2) {
            if (number.charAt(0) == '0' && number.charAt(1) != '.') {
                StringBuilder temp = new StringBuilder(number);
                temp.deleteCharAt(0);
                number = temp.toString();
            }
        }
        //equalPressed = false;
        rsl.setText(number);
    }
}