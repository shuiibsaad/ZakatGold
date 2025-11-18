package com.example.zakatgold;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etPrice;
    RadioButton rbKeep, rbWear;
    Button btnCalculate;
    TextView tvTotalValue, tvPayableValue, tvZakat;

    private static final double NISAB_KEEP = 85.0; // grams
    private static final double NISAB_WEAR = 200.0; // grams
    private static final double ZAKAT_RATE = 0.025; // 2.5%

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        rbKeep = findViewById(R.id.rbKeep);
        rbWear = findViewById(R.id.rbWear);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvPayableValue = findViewById(R.id.tvPayableValue);
        tvZakat = findViewById(R.id.tvZakat);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });
    }

    private void calculateZakat() {

        String weightStr = etWeight.getText().toString();
        String priceStr = etPrice.getText().toString();

        if (weightStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter both gold weight and price.", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight;
        double price;
        try {
            weight = Double.parseDouble(weightStr);
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format. Please enter numerical values.", Toast.LENGTH_SHORT).show();
            return;
        }

        double nisab;
        if (rbKeep.isChecked()) {
            nisab = NISAB_KEEP;
        } else if (rbWear.isChecked()) {
            nisab = NISAB_WEAR;
        } else {
            Toast.makeText(this, "Please select the gold type.", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalValue = weight * price;
        double payableWeight = weight - nisab;

        double zakatPayableValue;
        if (payableWeight <= 0) {
            zakatPayableValue = 0.0;
        } else {
            zakatPayableValue = payableWeight * price;
        }

        double totalZakat = zakatPayableValue * ZAKAT_RATE;

        tvTotalValue.setText(String.format("Total value: RM %.2f", totalValue));

        tvPayableValue.setText(String.format("Payable value (after threshold): RM %.2f", zakatPayableValue));

        tvZakat.setText(String.format("Zakat (2.5%%): RM %.2f", totalZakat));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appUrl = "Check out this Zakat Estimator App: https://github.com/shuiibsaad/ZakatGold";

            shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl);
            startActivity(Intent.createChooser(shareIntent, "Share App Link via"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}