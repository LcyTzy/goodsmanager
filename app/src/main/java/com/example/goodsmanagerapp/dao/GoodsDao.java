package com.example.goodsmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.goodsmanagerapp.entity.Goods;

import java.util.List;

@Dao
public interface GoodsDao {
    @Query("SELECT * FROM goods ORDER BY name ASC")
    List<Goods> getAllGoods();

    @Query("SELECT * FROM goods WHERE id = :id")
    Goods getGoodsById(long id);

    @Query("SELECT * FROM goods WHERE category = :category ORDER BY name ASC")
    List<Goods> getGoodsByCategory(String category);

    @Query("SELECT * FROM goods WHERE name LIKE '%' || :keyword || '%' OR series LIKE '%' || :keyword || '%' ORDER BY name ASC")
    List<Goods> searchGoods(String keyword);

    @Query("SELECT * FROM goods WHERE category = :category AND (name LIKE '%' || :keyword || '%' OR series LIKE '%' || :keyword || '%') ORDER BY name ASC")
    List<Goods> searchGoodsByCategory(String keyword, String category);

    @Insert
    void insert(Goods goods);

    @Insert
    void insertAll(Goods... goods);

    @Update
    void update(Goods goods);

    @Delete
    void delete(Goods goods);
}