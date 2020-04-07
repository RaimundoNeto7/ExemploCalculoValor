package com.example.exemplorecalcularvalor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RadioButton rb02;
    private RadioButton rb04;
    private RadioButton rb06;
    private RadioButton rb12;
    private RadioGroup rg;

    private EditText edtValueUnitario;

    private TextView txtValueDe;

    private int multiplicador;
    private double unitario;
    private double valor;

    Locale locale = new Locale("pt", "BR");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rb02 = findViewById(R.id.rbPack02);
        rb04 = findViewById(R.id.rbPack04);
        rb06 = findViewById(R.id.rbPack06);
        rb12 = findViewById(R.id.rbPack12);
        rg = findViewById(R.id.rg);

        edtValueUnitario = findViewById(R.id.edtValueUnitario);
        txtValueDe = findViewById(R.id.txtValueDe);

        rb02.setChecked(true);
        multiplicador = 2;
        unitario = 0.0;

        calcularValor();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb02.getId()){
                    multiplicador = 2;
                }
                else if (checkedId == rb04.getId()){
                    multiplicador = 4;
                }
                else if (checkedId == rb06.getId()){
                    multiplicador = 6;
                }
                else if (checkedId == rb12.getId()){
                    multiplicador = 12;
                }
                calcularValor();
            }
        });

        edtValueUnitario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtValueUnitario.removeTextChangedListener(this);
                BigDecimal parsed = parseToBigDecimal(s.toString(), locale);
                String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

                String stringValue = NumberFormat.getNumberInstance(locale).format(parsed); // sem o simbolo de moeda
                unitario = Double.parseDouble(stringValue.replace(".", "").replace(",", "."));

                edtValueUnitario.setText(formatted);
                edtValueUnitario.setSelection(formatted.length());
                edtValueUnitario.addTextChangedListener(this);

                calcularValor();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void calcularValor() {
        valor = unitario * multiplicador;
        DecimalFormat df = new DecimalFormat("####0.00");

        txtValueDe.setText(df.format(valor));
    }

    private BigDecimal parseToBigDecimal(String value, Locale locale) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());

        String cleanString = value.replaceAll(replaceable, "");

        return new BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR
        );
    }
}
