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
    private EditText etName, etSeries, etQuality, etViscosity, etSpec, etUnit, etModel, etPrice, etStock;
    private Spinner spinnerCategory; // 新增：分类Spinner
    private Button btnSubmit;
    private GoodsDatabase db;
    private long editGoodsId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_goods);

        // 1. 绑定控件
        initViews();

        // 2. 初始化数据库
        db = GoodsDatabase.getInstance(this);

        // 3. 初始化“分类Spinner”的选项（示例分类：可根据实际业务调整）
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"汽机油", "变速箱油", "柴机油", "辅助油品", "滤清器", "其他"}
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // 4. 判断是否为“编辑模式”，加载已有货物数据
        editGoodsId = getIntent().getLongExtra("GOODS_ID", -1);
        if (editGoodsId != -1) {
            loadEditGoodsData();
        }

        // 5. 提交按钮点击事件
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
        etStock = findViewById(R.id.et_stock);
        spinnerCategory = findViewById(R.id.spinner_category); // 绑定分类Spinner
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void loadEditGoodsData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = db.goodsDao().getGoodsById(editGoodsId);
            if (goods == null) {
                runOnUiThread(this::finish);
                return;
            }
            // 主线程填充表单（含分类）
            runOnUiThread(() -> {
                etName.setText(goods.getName());
                etSeries.setText(goods.getSeries());
                etQuality.setText(goods.getQualityLevel());
                etViscosity.setText(goods.getViscosityLevel());
                etSpec.setText(goods.getSpec());
                etUnit.setText(goods.getUnit());
                etModel.setText(goods.getApplicableModel());
                etPrice.setText(String.valueOf(goods.getPrice()));
                etStock.setText(String.valueOf(goods.getStock()));
                // 选中当前货物的分类
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
        // 1. 获取输入（含分类）
        String name = etName.getText().toString().trim();
        String series = etSeries.getText().toString().trim();
        String quality = etQuality.getText().toString().trim();
        String viscosity = etViscosity.getText().toString().trim();
        String spec = etSpec.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString(); // 获取分类

        // 2. 验证输入
        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "品名、价格、库存为必填项！", Toast.LENGTH_SHORT).show();
            return;
        }
        double price;
        int stock;
        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "价格/库存格式错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. 数据库操作（新增/更新，含category）
        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = new Goods(name, series, quality, viscosity, spec, unit, model, price, stock, category);
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