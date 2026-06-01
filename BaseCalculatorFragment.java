package com.example.calculator;

import androidx.fragment.app.Fragment;
import android.widget.TextView;

public abstract class BaseCalculatorFragment extends Fragment {

    protected StringBuilder currentInput = new StringBuilder();
    protected double result = 0;
    protected String operator = "";
    protected boolean newInput = true;
    protected boolean hasDecimal = false;

    protected void appendDigit(TextView display, String digit) {
        if (newInput) {
            currentInput.setLength(0);
            hasDecimal = false;
            newInput = false;
        }
        if (currentInput.length() < 15) {
            currentInput.append(digit);
            display.setText(currentInput.toString());
        }
    }

    protected void appendDecimal(TextView display) {
        if (newInput) {
            currentInput.setLength(0);
            currentInput.append("0");
            newInput = false;
        }
        if (!hasDecimal) {
            hasDecimal = true;
            currentInput.append(".");
            display.setText(currentInput.toString());
        }
    }

    protected void toggleSign(TextView display) {
        if (currentInput.length() == 0) return;
        String text = currentInput.toString();
        if (text.startsWith("-")) {
            currentInput.deleteCharAt(0);
        } else {
            currentInput.insert(0, "-");
        }
        display.setText(currentInput.toString());
    }

    protected void percentage(TextView display) {
        if (currentInput.length() == 0) return;
        try {
            double val = Double.parseDouble(currentInput.toString());
            val = val / 100.0;
            currentInput = new StringBuilder(formatResult(val));
            display.setText(currentInput.toString());
            newInput = true;
        } catch (NumberFormatException e) {
            display.setText("Erreur");
        }
    }

    protected void clear(TextView display, TextView displayExpr) {
        currentInput.setLength(0);
        result = 0;
        operator = "";
        newInput = true;
        hasDecimal = false;
        display.setText("0");
        if (displayExpr != null) displayExpr.setText("");
    }

    protected void deleteLast(TextView display) {
        if (newInput || currentInput.length() == 0) return;
        char last = currentInput.charAt(currentInput.length() - 1);
        if (last == '.') hasDecimal = false;
        currentInput.deleteCharAt(currentInput.length() - 1);
        if (currentInput.length() == 0) {
            display.setText("0");
        } else {
            display.setText(currentInput.toString());
        }
    }

    protected double getCurrentValue() {
        if (currentInput.length() == 0) return result;
        return Double.parseDouble(currentInput.toString());
    }

    protected String formatResult(double val) {
        if (Double.isNaN(val)) return "Indéfini";
        if (Double.isInfinite(val)) return "Infini";
        if (val == Math.floor(val) && Math.abs(val) < 1e15) {
            return String.valueOf((long) val);
        }
        // Up to 10 significant digits, trimming trailing zeros
        String formatted = String.format("%.10f", val).replaceAll("0+$", "").replaceAll("\\.$", "");
        return formatted;
    }
}
