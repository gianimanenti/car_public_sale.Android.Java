package com.moondu.leilao.commons.watcher;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class PhoneWhatcher implements TextWatcher {

    private EditText input;

    private boolean isUpdating;
    private String old = "";

    public PhoneWhatcher(EditText input) {
        this.input = input;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        String str = unmask(charSequence.toString());
        String mask = "(##) #####-####";

        String mascara = "";

        if (isUpdating) {
            old = str;
            isUpdating = false;
            return;
        }
        int i = 0;

        for (char m : mask.toCharArray()) {
            if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                mascara += m;
                continue;
            }

            try {
                mascara += str.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }

        isUpdating = true;

        input.setText(mascara);
        input.setSelection(mascara.length());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }
}