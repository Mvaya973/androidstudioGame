package com.example.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.Math;

public class ScientificFragment extends BaseCalculatorFragment {

    private TextView displayResult;
    private TextView displayExpr;
    private boolean isRadians = true;
    private boolean isInverse = false;
    private TextView btnRadDeg;
    private TextView btnInv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scientific, container, false);

        displayResult = view.findViewById(R.id.sci_display_result);
        displayExpr   = view.findViewById(R.id.sci_display_expr);
        btnRadDeg     = view.findViewById(R.id.btn_rad_deg);
        btnInv        = view.findViewById(R.id.btn_inv);

        // Digits
        int[] digitIds = {
            R.id.sci_btn_0, R.id.sci_btn_1, R.id.sci_btn_2, R.id.sci_btn_3,
            R.id.sci_btn_4, R.id.sci_btn_5, R.id.sci_btn_6, R.id.sci_btn_7,
            R.id.sci_btn_8, R.id.sci_btn_9
        };
        String[] digits = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < digitIds.length; i++) {
            final String d = digits[i];
            view.findViewById(digitIds[i]).setOnClickListener(v -> appendDigit(displayResult, d));
        }

        view.findViewById(R.id.sci_btn_dot).setOnClickListener(v -> appendDecimal(displayResult));
        view.findViewById(R.id.sci_btn_sign).setOnClickListener(v -> toggleSign(displayResult));
        view.findViewById(R.id.sci_btn_percent).setOnClickListener(v -> percentage(displayResult));
        view.findViewById(R.id.sci_btn_clear).setOnClickListener(v -> clear(displayResult, displayExpr));
        view.findViewById(R.id.sci_btn_del).setOnClickListener(v -> deleteLast(displayResult));

        view.findViewById(R.id.sci_btn_add).setOnClickListener(v -> setOperator("+"));
        view.findViewById(R.id.sci_btn_sub).setOnClickListener(v -> setOperator("-"));
        view.findViewById(R.id.sci_btn_mul).setOnClickListener(v -> setOperator("×"));
        view.findViewById(R.id.sci_btn_div).setOnClickListener(v -> setOperator("÷"));
        view.findViewById(R.id.sci_btn_equal).setOnClickListener(v -> calculate());

        // Scientific operations
        view.findViewById(R.id.btn_sin).setOnClickListener(v -> applyTrig("sin"));
        view.findViewById(R.id.btn_cos).setOnClickListener(v -> applyTrig("cos"));
        view.findViewById(R.id.btn_tan).setOnClickListener(v -> applyTrig("tan"));
        view.findViewById(R.id.btn_log).setOnClickListener(v -> applyFunc("log"));
        view.findViewById(R.id.btn_ln).setOnClickListener(v -> applyFunc("ln"));
        view.findViewById(R.id.btn_sqrt).setOnClickListener(v -> applyFunc("√"));
        view.findViewById(R.id.btn_pow2).setOnClickListener(v -> applyFunc("x²"));
        view.findViewById(R.id.btn_pow3).setOnClickListener(v -> applyFunc("x³"));
        view.findViewById(R.id.btn_inv_x).setOnClickListener(v -> applyFunc("1/x"));
        view.findViewById(R.id.btn_exp).setOnClickListener(v -> applyFunc("eˣ"));
        view.findViewById(R.id.btn_pow10).setOnClickListener(v -> applyFunc("10ˣ"));
        view.findViewById(R.id.btn_abs).setOnClickListener(v -> applyFunc("|x|"));
        view.findViewById(R.id.btn_factorial).setOnClickListener(v -> applyFunc("n!"));
        view.findViewById(R.id.btn_pi).setOnClickListener(v -> insertConstant(Math.PI, "π"));
        view.findViewById(R.id.btn_e).setOnClickListener(v -> insertConstant(Math.E, "e"));
        view.findViewById(R.id.btn_pow).setOnClickListener(v -> setOperator("^"));
        view.findViewById(R.id.btn_yroot).setOnClickListener(v -> setOperator("y√"));
        view.findViewById(R.id.btn_open_par).setOnClickListener(v -> appendDigit(displayResult, "("));
        view.findViewById(R.id.btn_close_par).setOnClickListener(v -> appendDigit(displayResult, ")"));

        btnRadDeg.setOnClickListener(v -> {
            isRadians = !isRadians;
            btnRadDeg.setText(isRadians ? "RAD" : "DEG");
        });

        btnInv.setOnClickListener(v -> {
            isInverse = !isInverse;
            updateInverseButtons(view);
        });

        return view;
    }

    private void updateInverseButtons(View view) {
        TextView sinBtn = view.findViewById(R.id.btn_sin);
        TextView cosBtn = view.findViewById(R.id.btn_cos);
        TextView tanBtn = view.findViewById(R.id.btn_tan);
        TextView logBtn = view.findViewById(R.id.btn_log);
        TextView lnBtn  = view.findViewById(R.id.btn_ln);
        TextView sqrtBtn = view.findViewById(R.id.btn_sqrt);

        if (isInverse) {
            sinBtn.setText("asin");
            cosBtn.setText("acos");
            tanBtn.setText("atan");
            logBtn.setText("10ˣ");
            lnBtn.setText("eˣ");
            sqrtBtn.setText("x²");
            btnInv.setAlpha(1.0f);
        } else {
            sinBtn.setText("sin");
            cosBtn.setText("cos");
            tanBtn.setText("tan");
            logBtn.setText("log");
            lnBtn.setText("ln");
            sqrtBtn.setText("√");
            btnInv.setAlpha(0.6f);
        }
    }

    private double toAngle(double val) {
        return isRadians ? val : Math.toRadians(val);
    }
    private double fromAngle(double val) {
        return isRadians ? val : Math.toDegrees(val);
    }

    private void applyTrig(String func) {
        if (currentInput.length() == 0 && result == 0) return;
        double val = getCurrentValue();
        double res;
        String label;
        try {
            if (!isInverse) {
                double angleVal = toAngle(val);
                switch (func) {
                    case "sin": res = Math.sin(angleVal); label = "sin(" + formatResult(val) + ")"; break;
                    case "cos": res = Math.cos(angleVal); label = "cos(" + formatResult(val) + ")"; break;
                    case "tan": res = Math.tan(angleVal); label = "tan(" + formatResult(val) + ")"; break;
                    default: return;
                }
            } else {
                switch (func) {
                    case "sin": res = fromAngle(Math.asin(val)); label = "asin(" + formatResult(val) + ")"; break;
                    case "cos": res = fromAngle(Math.acos(val)); label = "acos(" + formatResult(val) + ")"; break;
                    case "tan": res = fromAngle(Math.atan(val)); label = "atan(" + formatResult(val) + ")"; break;
                    default: return;
                }
            }
            displayExpr.setText(label + " =");
            displayResult.setText(formatResult(res));
            result = res;
            currentInput.setLength(0);
            newInput = true;
        } catch (Exception e) {
            displayResult.setText("Erreur");
        }
    }

    private void applyFunc(String func) {
        if (currentInput.length() == 0 && result == 0) return;
        double val = getCurrentValue();
        double res;
        String label;
        try {
            switch (func) {
                case "log":  res = Math.log10(val); label = "log(" + formatResult(val) + ")"; break;
                case "ln":   res = Math.log(val);   label = "ln(" + formatResult(val) + ")";  break;
                case "√":    res = Math.sqrt(val);  label = "√(" + formatResult(val) + ")";   break;
                case "x²":   res = val * val;        label = formatResult(val) + "²";           break;
                case "x³":   res = val * val * val;  label = formatResult(val) + "³";           break;
                case "1/x":  
                    if (val == 0) { displayResult.setText("Erreur"); return; }
                    res = 1.0 / val; label = "1/(" + formatResult(val) + ")"; break;
                case "eˣ":   res = Math.exp(val);   label = "e^" + formatResult(val);          break;
                case "10ˣ":  res = Math.pow(10, val); label = "10^" + formatResult(val);       break;
                case "|x|":  res = Math.abs(val);   label = "|" + formatResult(val) + "|";     break;
                case "n!":
                    if (val < 0 || val != Math.floor(val) || val > 20) { displayResult.setText("Erreur"); return; }
                    long fact = 1;
                    for (int i = 2; i <= (int)val; i++) fact *= i;
                    res = fact;
                    label = formatResult(val) + "!";
                    break;
                default: return;
            }
            displayExpr.setText(label + " =");
            displayResult.setText(formatResult(res));
            result = res;
            currentInput.setLength(0);
            newInput = true;
        } catch (Exception e) {
            displayResult.setText("Erreur");
        }
    }

    private void insertConstant(double value, String name) {
        currentInput.setLength(0);
        currentInput.append(String.valueOf(value));
        displayResult.setText(name + " = " + formatResult(value));
        result = value;
        newInput = true;
    }

    private void setOperator(String op) {
        if (currentInput.length() > 0 || result != 0) {
            if (currentInput.length() > 0 && !operator.isEmpty()) {
                calculate();
            } else if (currentInput.length() > 0) {
                result = getCurrentValue();
            }
            operator = op;
            displayExpr.setText(formatResult(result) + " " + op);
            newInput = true;
            hasDecimal = false;
        }
    }

    private void calculate() {
        if (operator.isEmpty() || (currentInput.length() == 0 && newInput)) return;
        double second = getCurrentValue();
        String expr = formatResult(result) + " " + operator + " " + formatResult(second) + " =";
        displayExpr.setText(expr);

        switch (operator) {
            case "+": result += second; break;
            case "-": result -= second; break;
            case "×": result *= second; break;
            case "÷":
                if (second == 0) { displayResult.setText("Erreur"); operator = ""; return; }
                result /= second;
                break;
            case "^":    result = Math.pow(result, second); break;
            case "y√":   result = Math.pow(result, 1.0 / second); break;
        }
        displayResult.setText(formatResult(result));
        currentInput.setLength(0);
        operator = "";
        newInput = true;
        hasDecimal = false;
    }
}
