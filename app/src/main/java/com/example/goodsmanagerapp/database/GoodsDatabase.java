package com.example.goodsmanagerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.goodsmanagerapp.dao.GoodsDao;
import com.example.goodsmanagerapp.entity.Goods;

// 数据库版本1，关联实体类Goods
@Database(entities = {Goods.class}, version = 2, exportSchema = false)
public abstract class GoodsDatabase extends RoomDatabase {
    // 单例实例（防止多实例占用资源）
    private static volatile GoodsDatabase INSTANCE;

    // 提供Dao接口实例
    public abstract GoodsDao goodsDao();

    // 获取数据库实例（线程安全）
    public static GoodsDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (GoodsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    GoodsDatabase.class,
                                    "goods_database" // 数据库文件名
                            )
                            .allowMainThreadQueries() // 临时允许主线程操作（简化示例，正式项目用子线程）
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}