package com.example.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StandardFragment extends BaseCalculatorFragment {

    private TextView displayResult;
    private TextView displayExpr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standard, container, false);

        displayResult = view.findViewById(R.id.display_result);
        displayExpr   = view.findViewById(R.id.display_expr);

        // Digit buttons
        int[] digitIds = {
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9
        };
        String[] digits = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < digitIds.length; i++) {
            final String d = digits[i];
            view.findViewById(digitIds[i]).setOnClickListener(v -> appendDigit(displayResult, d));
        }

        view.findViewById(R.id.btn_dot).setOnClickListener(v -> appendDecimal(displayResult));
        view.findViewById(R.id.btn_sign).setOnClickListener(v -> toggleSign(displayResult));
        view.findViewById(R.id.btn_percent).setOnClickListener(v -> percentage(displayResult));
        view.findViewById(R.id.btn_clear).setOnClickListener(v -> clear(displayResult, displayExpr));
        view.findViewById(R.id.btn_del).setOnClickListener(v -> deleteLast(displayResult));

        view.findViewById(R.id.btn_add).setOnClickListener(v -> setOperator("+"));
        view.findViewById(R.id.btn_sub).setOnClickListener(v -> setOperator("-"));
        view.findViewById(R.id.btn_mul).setOnClickListener(v -> setOperator("×"));
        view.findViewById(R.id.btn_div).setOnClickListener(v -> setOperator("÷"));

        view.findViewById(R.id.btn_equal).setOnClickListener(v -> calculate());

        return view;
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
        }
        displayResult.setText(formatResult(result));
        currentInput.setLength(0);
        operator = "";
        newInput = true;
        hasDecimal = false;
    }
}
