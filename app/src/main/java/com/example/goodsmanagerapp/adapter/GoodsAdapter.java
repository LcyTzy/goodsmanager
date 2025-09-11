package com.example.goodsmanagerapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodsmanagerapp.GoodsDetailActivity;
import com.example.goodsmanagerapp.GoodsEditActivity;
import com.example.goodsmanagerapp.database.GoodsDatabase;
import com.example.goodsmanagerapp.databinding.ItemGoodsBinding;
import com.example.goodsmanagerapp.entity.Goods;

import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private final Context context;
    private List<Goods> goodsList; // 货物列表数据源
    private final GoodsDatabase db;

    public GoodsAdapter(Context context, List<Goods> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
        this.db = GoodsDatabase.getInstance(context);
    }

    // 新增：更新数据源的方法（MainActivity 中调用此方法刷新列表）
    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
        notifyDataSetChanged(); // 通知 RecyclerView 刷新界面
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用 ViewBinding 加载 item 布局
        ItemGoodsBinding binding = ItemGoodsBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods goods = goodsList.get(position);
        ItemGoodsBinding binding = holder.binding;

        // 绑定货物信息到布局控件
        binding.tvName.setText(goods.getName());
        binding.tvPrice.setText("¥" + goods.getPrice());
        binding.tvSeriesSpec.setText("系列：" + goods.getSeries() + " | 规格：" + goods.getSpec());
        binding.tvCategory.setText("分类：" + goods.getCategory());
        binding.tvStock.setText(String.valueOf(goods.getStock()));

        // 库存减 1 逻辑
        binding.btnMinus.setOnClickListener(v -> updateStock(goods, -1, binding.tvStock));
        // 库存加 1 逻辑
        binding.btnPlus.setOnClickListener(v -> updateStock(goods, 1, binding.tvStock));

        // 跳转“详情页”
        binding.btnDetail.setOnClickListener(v -> openDetail(goods.getId()));
        // 跳转“编辑页”
        binding.btnEdit.setOnClickListener(v -> openEdit(goods.getId()));
        // 删除货物
        binding.btnDelete.setOnClickListener(v -> deleteGoods(goods, position));
    }

    // 库存更新逻辑（抽离为独立方法，代码更简洁）
    private void updateStock(Goods goods, int delta, TextView tvStock) {
        int newStock = goods.getStock() + delta;
        if (newStock < 0) {
            Toast.makeText(context, "库存不能为负！", Toast.LENGTH_SHORT).show();
            return;
        }
        goods.setStock(newStock);
        db.goodsDao().update(goods); // 数据库更新
        tvStock.setText(String.valueOf(newStock)); // UI 刷新
    }

    // 打开“货物详情页”
    private void openDetail(long goodsId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("GOODS_ID", goodsId);
        context.startActivity(intent);
    }

    // 打开“货物编辑页”
    private void openEdit(long goodsId) {
        Intent intent = new Intent(context, GoodsEditActivity.class);
        intent.putExtra("GOODS_ID", goodsId);
        context.startActivity(intent);
    }

    // 删除货物（数据库 + 列表同步更新）
    private void deleteGoods(Goods goods, int position) {
        db.goodsDao().delete(goods); // 数据库删除
        goodsList.remove(position); // 列表删除
        notifyItemRemoved(position); // 局部刷新 RecyclerView
        Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        // 避免 goodsList 为 null 时崩溃
        return goodsList != null ? goodsList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemGoodsBinding binding;

        ViewHolder(ItemGoodsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}