package com.example.goodsmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodsmanagerapp.database.GoodsDatabase;
import com.example.goodsmanagerapp.entity.Goods;

import java.util.concurrent.Executors;

public class GoodsEditActivity extends AppCompatActivity {
    private EditText etName, etSeries, etQuality, etViscosity, etSpec, etUnit, etModel,
            etPrice, etPriceCustomer1, etPriceCustomer2, etPriceCustomer3, etStock;
    private Spinner spinnerCategory;
    private Button btnSubmit;
    private GoodsDatabase db;
    private long editGoodsId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_goods);

        initViews();
        db = GoodsDatabase.getInstance(this);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"汽机油", "变速箱油", "柴机油", "辅助油品", "滤清器", "其他"}
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        editGoodsId = getIntent().getLongExtra("GOODS_ID", -1);
        if (editGoodsId != -1) {
            loadEditGoodsData();
        }

        btnSubmit.setOnClickListener(v -> submitGoods());
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etSeries = findViewById(R.id.et_series);
        etQuality = findViewById(R.id.et_quality);
        etViscosity = findViewById(R.id.et_viscosity);
        etSpec = findViewById(R.id.et_spec);
        etUnit = findViewById(R.id.et_unit);
        etModel = findViewById(R.id.et_model);
        etPrice = findViewById(R.id.et_price);
        etPriceCustomer1 = findViewById(R.id.et_price_customer1);
        etPriceCustomer2 = findViewById(R.id.et_price_customer2);
        etPriceCustomer3 = findViewById(R.id.et_price_customer3);
        etStock = findViewById(R.id.et_stock);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void loadEditGoodsData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = db.goodsDao().getGoodsById(editGoodsId);
            if (goods == null) {
                runOnUiThread(this::finish);
                return;
            }

            runOnUiThread(() -> {
                etName.setText(goods.getName());
                etSeries.setText(goods.getSeries());
                etQuality.setText(goods.getQualityLevel());
                etViscosity.setText(goods.getViscosityLevel());
                etSpec.setText(goods.getSpec());
                etUnit.setText(goods.getUnit());
                etModel.setText(goods.getApplicableModel());
                etPrice.setText(String.valueOf(goods.getPrice()));
                etPriceCustomer1.setText(String.valueOf(goods.getPriceCustomer1()));
                etPriceCustomer2.setText(String.valueOf(goods.getPriceCustomer2()));
                etPriceCustomer3.setText(String.valueOf(goods.getPriceCustomer3()));
                etStock.setText(String.valueOf(goods.getStock()));

                int categoryPos = ((ArrayAdapter<String>) spinnerCategory.getAdapter())
                        .getPosition(goods.getCategory());
                if (categoryPos != -1) {
                    spinnerCategory.setSelection(categoryPos);
                }
                btnSubmit.setText("更新货物");
            });
        });
    }

    private void submitGoods() {
        String name = etName.getText().toString().trim();
        String series = etSeries.getText().toString().trim();
        String quality = etQuality.getText().toString().trim();
        String viscosity = etViscosity.getText().toString().trim();
        String spec = etSpec.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String price1Str = etPriceCustomer1.getText().toString().trim();
        String price2Str = etPriceCustomer2.getText().toString().trim();
        String price3Str = etPriceCustomer3.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "品名、价格、库存为必填项！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 如果客户价格未填写，使用默认价格
        double price, price1, price2, price3;
        try {
            price = Double.parseDouble(priceStr);
            price1 = price1Str.isEmpty() ? price : Double.parseDouble(price1Str);
            price2 = price2Str.isEmpty() ? price : Double.parseDouble(price2Str);
            price3 = price3Str.isEmpty() ? price : Double.parseDouble(price3Str);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "价格格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "库存格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = new Goods(name, series, quality, viscosity, spec, unit, model,
                    price, price1, price2, price3, stock, category);
            if (editGoodsId == -1) {
                db.goodsDao().insert(goods);
                runOnUiThread(() -> {
                    Toast.makeText(GoodsEditActivity.this, "新增货物成功！", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                goods.setId(editGoodsId);
                db.goodsDao().update(goods);
                runOnUiThread(() -> {
                    Toast.makeText(GoodsEditActivity.this, "更新货物成功！", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}