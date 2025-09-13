package com.example.goodsmanagerapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
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
    private List<Goods> goodsList;
    private final GoodsDatabase db;
    private int selectedCustomer; // 新增：选中的客户

    public GoodsAdapter(Context context, List<Goods> goodsList, int selectedCustomer) {
        this.context = context;
        this.goodsList = goodsList;
        this.db = GoodsDatabase.getInstance(context);
        this.selectedCustomer = selectedCustomer;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
        notifyDataSetChanged();
    }

    // 新增：设置选中的客户
    public void setSelectedCustomer(int selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        binding.tvName.setText(goods.getName());

        // 根据选中的客户显示不同价格
        double displayPrice;
        switch (selectedCustomer) {
            case 1:
                displayPrice = goods.getPriceCustomer1();
                break;
            case 2:
                displayPrice = goods.getPriceCustomer2();
                break;
            case 3:
                displayPrice = goods.getPriceCustomer3();
                break;
            default:
                displayPrice = goods.getPrice();
        }
        binding.tvPrice.setText("¥" + displayPrice);

        binding.tvSeriesSpec.setText("系列：" + goods.getSeries() + " | 规格：" + goods.getSpec());
        binding.tvCategory.setText("分类：" + goods.getCategory());
        binding.tvStock.setText(String.valueOf(goods.getStock()));

        binding.btnMinus.setOnClickListener(v -> updateStock(goods, -1, binding.tvStock));
        binding.btnPlus.setOnClickListener(v -> updateStock(goods, 1, binding.tvStock));

        binding.btnDetail.setOnClickListener(v -> openDetail(goods.getId()));
        binding.btnEdit.setOnClickListener(v -> openEdit(goods.getId()));
        binding.btnDelete.setOnClickListener(v -> deleteGoods(goods, position));
    }

    private void updateStock(Goods goods, int delta, TextView tvStock) {
        int newStock = goods.getStock() + delta;
        if (newStock < 0) {
            Toast.makeText(context, "库存不能为负！", Toast.LENGTH_SHORT).show();
            return;
        }
        goods.setStock(newStock);
        db.goodsDao().update(goods);
        tvStock.setText(String.valueOf(newStock));
    }

    private void openDetail(long goodsId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("GOODS_ID", goodsId);
        intent.putExtra("SELECTED_CUSTOMER", selectedCustomer); // 传递选中的客户
        context.startActivity(intent);
    }

    private void openEdit(long goodsId) {
        Intent intent = new Intent(context, GoodsEditActivity.class);
        intent.putExtra("GOODS_ID", goodsId);
        context.startActivity(intent);
    }

    private void deleteGoods(Goods goods, int position) {
        db.goodsDao().delete(goods);
        goodsList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
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