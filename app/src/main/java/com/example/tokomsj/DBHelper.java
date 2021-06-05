package com.example.tokomsj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "products", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, stock INTEGER)";
        String sql_2 = "CREATE TABLE IF NOT EXISTS orders(id INTEGER PRIMARY KEY AUTOINCREMENT, total_order INTEGER)";
        db.execSQL(sql);
        db.execSQL(sql_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table jika tersedia
        String sql = "DROP TABLE IF EXISTS products";
        db.execSQL(sql);
        onCreate(db);

        String sql_2 = "DROP TABLE IF EXISTS orders";
        db.execSQL(sql_2);
        // Buat kembali table setelah di drop
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id",product.getId());
        values.put("name", product.getName()); // Nama Produk
        values.put("price", product.getPrice()); // Harga Produk
        values.put("stock", product.getStock()); // Stock Produk

        // Inserting Row
        db.insert("products", null, values);
        db.close(); // Closing database connection
    }

    // List Produk
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM products";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                product.setPrice(cursor.getInt(2));
                product.setStock(cursor.getInt(3));

                // Adding student to list
                productList.add(product);
            } while (cursor.moveToNext());
        }

        // return student list
        return productList;
    }

    // Updating a student
    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", product.getName()); //These Fields should be your String values of actual column names
        cv.put("price", product.getPrice());
        cv.put("stock", product.getStock());
        db.update("products", cv, "id="+product.getId(), null);
    }

    // Deleting a student
    public void deleteProduct(int value) {
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("delete from products where id="+value);
        db.close();
    }

}
