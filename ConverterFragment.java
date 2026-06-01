package com.example.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ConverterFragment extends Fragment {

    private EditText inputDecimal;
    private EditText inputBinary;
    private EditText inputHex;
    private EditText inputOctal;

    private boolean isUpdating = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        inputDecimal = view.findViewById(R.id.input_decimal);
        inputBinary  = view.findViewById(R.id.input_binary);
        inputHex     = view.findViewById(R.id.input_hex);
        inputOctal   = view.findViewById(R.id.input_octal);

        // Copy buttons
        view.findViewById(R.id.btn_copy_dec).setOnClickListener(v -> copyToClipboard(inputDecimal.getText().toString(), "Décimal"));
        view.findViewById(R.id.btn_copy_bin).setOnClickListener(v -> copyToClipboard(inputBinary.getText().toString(), "Binaire"));
        view.findViewById(R.id.btn_copy_hex).setOnClickListener(v -> copyToClipboard(inputHex.getText().toString(), "Hexadécimal"));
        view.findViewById(R.id.btn_copy_oct).setOnClickListener(v -> copyToClipboard(inputOctal.getText().toString(), "Octal"));

        view.findViewById(R.id.btn_clear_conv).setOnClickListener(v -> clearAll());

        setupDecimalWatcher();
        setupBinaryWatcher();
        setupHexWatcher();
        setupOctalWatcher();

        return view;
    }

    private void setupDecimalWatcher() {
        inputDecimal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;
                String text = s.toString().trim();
                if (text.isEmpty() || text.equals("-")) {
                    clearOthers(inputDecimal);
                } else {
                    try {
                        long val = Long.parseLong(text);
                        updateFromDecimal(val);
                    } catch (NumberFormatException e) {
                        inputBinary.setText("Invalide");
                        inputHex.setText("Invalide");
                        inputOctal.setText("Invalide");
                    }
                }
                isUpdating = false;
            }
        });
    }

    private void setupBinaryWatcher() {
        inputBinary.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;
                String text = s.toString().trim();
                if (text.isEmpty()) {
                    clearOthers(inputBinary);
                } else {
                    try {
                        long val = Long.parseLong(text, 2);
                        inputDecimal.setText(String.valueOf(val));
                        inputHex.setText(Long.toHexString(val).toUpperCase());
                        inputOctal.setText(Long.toOctalString(val));
                    } catch (NumberFormatException e) {
                        inputDecimal.setText("Invalide");
                        inputHex.setText("Invalide");
                        inputOctal.setText("Invalide");
                    }
                }
                isUpdating = false;
            }
        });
    }

    private void setupHexWatcher() {
        inputHex.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;
                String text = s.toString().trim();
                if (text.isEmpty()) {
                    clearOthers(inputHex);
                } else {
                    try {
                        long val = Long.parseLong(text, 16);
                        inputDecimal.setText(String.valueOf(val));
                        inputBinary.setText(Long.toBinaryString(val));
                        inputOctal.setText(Long.toOctalString(val));
                    } catch (NumberFormatException e) {
                        inputDecimal.setText("Invalide");
                        inputBinary.setText("Invalide");
                        inputOctal.setText("Invalide");
                    }
                }
                isUpdating = false;
            }
        });
    }

    private void setupOctalWatcher() {
        inputOctal.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                isUpdating = true;
                String text = s.toString().trim();
                if (text.isEmpty()) {
                    clearOthers(inputOctal);
                } else {
                    try {
                        long val = Long.parseLong(text, 8);
                        inputDecimal.setText(String.valueOf(val));
                        inputBinary.setText(Long.toBinaryString(val));
                        inputHex.setText(Long.toHexString(val).toUpperCase());
                    } catch (NumberFormatException e) {
                        inputDecimal.setText("Invalide");
                        inputBinary.setText("Invalide");
                        inputHex.setText("Invalide");
                    }
                }
                isUpdating = false;
            }
        });
    }

    private void updateFromDecimal(long val) {
        inputBinary.setText(Long.toBinaryString(val));
        inputHex.setText(Long.toHexString(val).toUpperCase());
        inputOctal.setText(Long.toOctalString(val));
    }

    private void clearOthers(EditText source) {
        if (source != inputDecimal) inputDecimal.setText("");
        if (source != inputBinary)  inputBinary.setText("");
        if (source != inputHex)     inputHex.setText("");
        if (source != inputOctal)   inputOctal.setText("");
    }

    private void clearAll() {
        isUpdating = true;
        inputDecimal.setText("");
        inputBinary.setText("");
        inputHex.setText("");
        inputOctal.setText("");
        isUpdating = false;
    }

    private void copyToClipboard(String text, String label) {
        if (text.isEmpty() || text.equals("Invalide")) return;
        android.content.ClipboardManager clipboard =
            (android.content.ClipboardManager) requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), label + " copié : " + text, Toast.LENGTH_SHORT).show();
    }
}
