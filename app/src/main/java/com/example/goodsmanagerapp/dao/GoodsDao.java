//package com.example.goodsmanagerapp.dao;
//
//import androidx.room.Dao;
//import androidx.room.Delete;
//import androidx.room.Insert;
//import androidx.room.Query;
//import androidx.room.Update;
//
//import com.example.goodsmanagerapp.entity.Goods;
//
//import java.util.List;
//
//@Dao
//public interface GoodsDao {
//    @Insert
//    void insert(Goods goods);
//
//    @Insert
//    void insertAll(Goods... goods);
//
//    @Delete
//    void delete(Goods goods);
//
//    @Update
//    void update(Goods goods);
//
//    @Query("SELECT * FROM goods ORDER BY id ASC")
//    List<Goods> getAllGoods();
//
//    @Query("SELECT * FROM goods WHERE id = :goodsId")
//    Goods getGoodsById(long goodsId);
//
//    @Query("SELECT * FROM goods WHERE name LIKE '%' || :keyword || '%' OR series LIKE '%' || :keyword || '%'")
//    List<Goods> searchGoods(String keyword);
//
//    // 新增：按分类查询
//    @Query("SELECT * FROM goods WHERE category = :category ORDER BY id ASC")
//    List<Goods> getGoodsByCategory(String category);
//
//    // 新增：按关键词+分类查询
//    @Query("SELECT * FROM goods WHERE (name LIKE '%' || :keyword || '%' OR series LIKE '%' || :keyword || '%') AND category = :category ORDER BY id ASC")
//    List<Goods> searchGoodsByCategory(String keyword, String category);
//}

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
    @Insert
    void insert(Goods goods);

    @Insert
    void insertAll(Goods... goods);

    @Delete
    void delete(Goods goods);

    @Update
    void update(Goods goods);

    @Query("SELECT * FROM goods ORDER BY id ASC")
    List<Goods> getAllGoods();

    @Query("SELECT * FROM goods WHERE id = :goodsId")
    Goods getGoodsById(long goodsId);

    /**
     * 模糊搜索：匹配「产品名称、系列、适用车型」含关键词的货物
     * 支持场景：
     * 1. 搜“228” → 匹配名称含“ZTB-228”的货物
     * 2. 搜“丰田” → 匹配适用车型含“丰田”的货物
     * 3. 搜“汽机油” → 匹配名称/系列含“汽机油”的货物
     */
    @Query("SELECT * FROM goods " +
            "WHERE name LIKE '%' || :keyword || '%' " +          // 匹配产品名称（如“战途变滤 ZTB-228”）
            "OR series LIKE '%' || :keyword || '%' " +         // 匹配产品系列（如“精品塑料桶汽机油”）
            "OR applicableModel LIKE '%' || :keyword || '%'") // 匹配适用车型（如“丰田-佳美2.4”）
    List<Goods> searchGoods(String keyword);

    // 新增：按分类查询
    @Query("SELECT * FROM goods WHERE category = :category ORDER BY id ASC")
    List<Goods> getGoodsByCategory(String category);

    /**
     * 模糊搜索+分类筛选：指定分类下，匹配「名称/系列/适用车型」含关键词的货物
     * 支持场景：搜“丰田”+分类“滤清器” → 仅显示滤清器中适用丰田的货物
     */
    @Query("SELECT * FROM goods " +
            "WHERE category = :category " +
            "AND (" +
            "name LIKE '%' || :keyword || '%' " +
            "OR series LIKE '%' || :keyword || '%' " +
            "OR applicableModel LIKE '%' || :keyword || '%') " +
            "ORDER BY id ASC")
    List<Goods> searchGoodsByCategory(String keyword, String category);
}