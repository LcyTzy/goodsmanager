package com.example.goodsmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.example.goodsmanagerapp.database.GoodsDatabase;
import com.example.goodsmanagerapp.entity.Goods;

import java.util.concurrent.Executors;

public class GoodsDetailActivity extends AppCompatActivity {
    private TextView tvDetailName, tvDetailSeries, tvDetailQuality, tvDetailViscosity,
            tvDetailSpec, tvDetailUnit, tvDetailModel, tvDetailPrice, tvDetailStock;
    private GoodsDatabase db;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int selectedCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        initViews();
        db = GoodsDatabase.getInstance(this);

        // 获取选中的客户
        selectedCustomer = getIntent().getIntExtra("SELECTED_CUSTOMER", 0);

        long goodsId = getIntent().getLongExtra("GOODS_ID", -1);
        if (goodsId == -1) {
            finish();
            return;
        }

        queryGoodsDetail(goodsId);
    }

    private void initViews() {
        tvDetailName = findViewById(R.id.tv_detail_name);
        tvDetailSeries = findViewById(R.id.tv_detail_series);
        tvDetailQuality = findViewById(R.id.tv_detail_quality);
        tvDetailViscosity = findViewById(R.id.tv_detail_viscosity);
        tvDetailSpec = findViewById(R.id.tv_detail_spec);
        tvDetailUnit = findViewById(R.id.tv_detail_unit);
        tvDetailModel = findViewById(R.id.tv_detail_model);
        tvDetailPrice = findViewById(R.id.tv_detail_price);
        tvDetailStock = findViewById(R.id.tv_detail_stock);
    }

    private void queryGoodsDetail(long goodsId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = db.goodsDao().getGoodsById(goodsId);
            if (goods == null) {
                handler.post(() -> finish());
                return;
            }

            handler.post(() -> {
                tvDetailName.setText("品名：" + goods.getName());
                tvDetailSeries.setText("系列：" + (goods.getSeries().isEmpty() ? "无" : goods.getSeries()));
                tvDetailQuality.setText("质量级别：" + (goods.getQualityLevel().isEmpty() ? "无" : goods.getQualityLevel()));
                tvDetailViscosity.setText("粘度等级：" + (goods.getViscosityLevel().isEmpty() ? "无" : goods.getViscosityLevel()));
                tvDetailSpec.setText("规格：" + (goods.getSpec().isEmpty() ? "无" : goods.getSpec()));
                tvDetailUnit.setText("单位：" + goods.getUnit());
                tvDetailModel.setText("适用车型：" + (goods.getApplicableModel().isEmpty() ? "无" : goods.getApplicableModel()));

                // 根据选中的客户显示对应价格
                double displayPrice;
                String customerSuffix = "";
                switch (selectedCustomer) {
                    case 1:
                        displayPrice = goods.getPriceCustomer1();
                        customerSuffix = "(客户1)";
                        break;
                    case 2:
                        displayPrice = goods.getPriceCustomer2();
                        customerSuffix = "(客户2)";
                        break;
                    case 3:
                        displayPrice = goods.getPriceCustomer3();
                        customerSuffix = "(客户3)";
                        break;
                    default:
                        displayPrice = goods.getPrice();
                }
                tvDetailPrice.setText("价格" + customerSuffix + "：¥" + displayPrice);

                tvDetailStock.setText("库存：" + goods.getStock() + goods.getUnit());
            });
        });
    }
}