package com.example.tokomsj;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private DBHelper db;
    private EditText id,name,price, stock;
    private Button create,update,delete,showAll, showTotalOrder;
    private static final String ORDER_FILE="order_total.txt", STOCK_FILE="stock_total.txt";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#6c4fff"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        create = (Button) findViewById(R.id.create);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        showAll=(Button) findViewById(R.id.show_product_all);
        showTotalOrder=(Button) findViewById(R.id.show_total_order);

        textView= (TextView) findViewById(R.id.information);
        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        price = (EditText) findViewById(R.id.price);
        stock = (EditText) findViewById(R.id.stock);

        db = new DBHelper(this);

        create.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        showAll.setOnClickListener(this);
        showTotalOrder.setOnClickListener(this);
    }

    public void updateProduct(){
        try {
        Product product = new Product();
        product.setName(name.getText().toString());
        product.setId(Integer.parseInt(id.getText().toString()));
        product.setPrice(Integer.parseInt(price.getText().toString()));
        product.setStock(Integer.parseInt(stock.getText().toString()));
        db.updateProduct(product);
        Toast.makeText(this, " updated", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this, "Exception occurred", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProduct(){
        try {
            db.deleteProduct(Integer.parseInt(id.getText().toString()));
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            Toast.makeText(this, "Exception occurred", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createProduct(){
        try {
            Product product = new Product();
            product.setName(name.getText().toString());
            product.setId(Integer.parseInt(id.getText().toString()));
            product.setPrice(Integer.parseInt(price.getText().toString()));
            product.setStock(Integer.parseInt(stock.getText().toString()));
            db.addProduct(product);

            Object path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS);
            String s = "1";
            File readFirst = new File(path +"/"+ STOCK_FILE);
            if(readFirst.exists()){
                BufferedReader br = new BufferedReader(
                        new FileReader(path + "/" + STOCK_FILE));
                String readLine = br.readLine();
                s = readLine;

                BufferedWriter bw = new BufferedWriter(
                        new FileWriter(path + "/" + STOCK_FILE));
                int stock_field = Integer.parseInt(stock.getText().toString());
                for(int j = 0; j<s.length(); j++) {
                    bw.write("1");
                }
                for(int i = 0; i<Integer.parseInt(String.valueOf(stock_field)); i++) {
                    bw.write("1");
                }
                bw.close();
                br.close();
            } else {
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter(path + "/" + STOCK_FILE));
                int stock_field = Integer.parseInt(stock.getText().toString());
                for(int i = 0; i<Integer.parseInt(String.valueOf(stock_field)); i++) {
                    bw.write("1");

                }
                bw.close();
            }

            Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
            id.setText("");
            name.setText("");
            price.setText("");
            stock.setText("");
            id.requestFocus();
        }catch (Exception e){
            Toast.makeText(this, "Exception occurred", Toast.LENGTH_SHORT).show();
        }
    }

    //        textView.setText("");
//        List<Product> products = db.getAllProducts();
//        for (Product product : products) {
//            String log = ">> Id: " + product.getId() + " | Nama Produk: " + product.getName() + " | Harga Produk: " + product.getPrice() + "  | Stok: " + product.getStock();
//            // Writing students  to log
//            Log.d("Product: : ", log);
//            textView.append(log + "\n");
//        }

    public void showAll(){
        ListView listView = (ListView) findViewById(R.id.list);
        List<Product> products = db.getAllProducts();
        List<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
        for (Product product : products) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("name",""+product.getName());
            datum.put("price", ""+product.getPrice()+" (Klik disini untuk membeli)");
            arrayList.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, arrayList,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "price"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Object path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS);
                    String s_stock = "1";
                    File readStockFirst = new File(path+"/"+STOCK_FILE);
                    if(readStockFirst.exists()) {
                        BufferedReader br_stock = new BufferedReader(
                                new FileReader(path + "/" + STOCK_FILE));
                        String readStockLine = br_stock.readLine();
                        s_stock = readStockLine;
                        int s_stock_length = s_stock.length()-1;
                        BufferedWriter bw_stock = new BufferedWriter(
                                new FileWriter(path + "/" + STOCK_FILE));
                        for(int j=0; j<s_stock_length; j++) {
                            bw_stock.write("1");
                        }
                        bw_stock.close();
                        br_stock.close();
                    }

                    String s = "1";
                    File readFirst = new File(path +"/"+ ORDER_FILE);
                    if(readFirst.exists()){
                        BufferedReader br = new BufferedReader(
                                new FileReader(path + "/" + ORDER_FILE));
                        String readLine = br.readLine();
                        s = readLine;

                        BufferedWriter bw = new BufferedWriter(
                                new FileWriter(path + "/" + ORDER_FILE));
                        bw.write("1"+s);
                        bw.close();
                        br.close();
                    } else {
                        BufferedWriter bw = new BufferedWriter(
                                new FileWriter(path + "/" + ORDER_FILE));
                        bw.write("1");
                        bw.close();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showTotalOrder() throws IOException {
        String s, s2;
        int count_stock = 0;
        int count = 0;
        Object path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);

        File readStockFirst = new File(path +"/"+ STOCK_FILE);
        if(readStockFirst.exists()) {
            // Baca Sisa Stok
            BufferedReader br_stock = new BufferedReader(
                    new FileReader(path + "/" + STOCK_FILE));
            String readStockLine = br_stock.readLine();
            s2 = readStockLine;
            for(int i=0; i<s2.length(); i++) {
                count_stock++;
            }
        }

        File readFirst = new File(path +"/"+ ORDER_FILE);
        if(readFirst.exists()){
            // Baca Total Pesanan (Order)
            BufferedReader br = new BufferedReader(
                    new FileReader(path + "/" + ORDER_FILE));
            String readLine = br.readLine();
            s = readLine;
            for(int i=0; i<s.length(); i++) {
                count++;
            }
            textView.setText("Total Order : "+count+" | Stok Tersisa :"+count_stock);
            br.close();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create:
                createProduct();
                break;
            case R.id.update:
                updateProduct();
                break;
            case R.id.delete:
                deleteProduct();
                break;
            case R.id.show_product_all:
                showAll();
                break;
            case R.id.show_total_order:
                try {
                    showTotalOrder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }
}