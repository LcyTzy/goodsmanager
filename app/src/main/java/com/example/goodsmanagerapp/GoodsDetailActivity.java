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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        // 绑定控件
        initViews();

        // 初始化数据库
        db = GoodsDatabase.getInstance(this);

        // 获取传递的货物ID
        long goodsId = getIntent().getLongExtra("GOODS_ID", -1);
        if (goodsId == -1) { // 异常情况：无ID则返回
            finish();
            return;
        }

        // 查询货物详情（子线程）
        queryGoodsDetail(goodsId);
    }

    // 绑定详情页控件
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

    // 子线程查询货物详情并更新UI
    private void queryGoodsDetail(long goodsId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Goods goods = db.goodsDao().getGoodsById(goodsId);
            if (goods == null) { // 货物不存在
                handler.post(() -> finish());
                return;
            }

            // 主线程更新UI
            handler.post(() -> {
                tvDetailName.setText("品名：" + goods.getName());
                tvDetailSeries.setText("系列：" + (goods.getSeries().isEmpty() ? "无" : goods.getSeries()));
                tvDetailQuality.setText("质量级别：" + (goods.getQualityLevel().isEmpty() ? "无" : goods.getQualityLevel()));
                tvDetailViscosity.setText("粘度等级：" + (goods.getViscosityLevel().isEmpty() ? "无" : goods.getViscosityLevel()));
                tvDetailSpec.setText("规格：" + (goods.getSpec().isEmpty() ? "无" : goods.getSpec()));
                tvDetailUnit.setText("单位：" + goods.getUnit());
                tvDetailModel.setText("适用车型：" + (goods.getApplicableModel().isEmpty() ? "无" : goods.getApplicableModel()));
                tvDetailPrice.setText("价格：¥" + goods.getPrice());
                tvDetailStock.setText("库存：" + goods.getStock() + goods.getUnit());
            });
        });
    }
}