package edu.upc.eseiaat.pma.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<String> itemlist;
    private ArrayAdapter<String> adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = (ListView) findViewById(R.id.list);
        btn_add = (Button) findViewById(R.id.btn_ad);
        edit_item = (EditText) findViewById(R.id.edit_item);

        itemlist = new ArrayList<>();
        itemlist.add("Patatas");
        itemlist.add("Papel de vater");
        itemlist.add("Zanahorias");
        itemlist.add("Copas danone");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemlist);


        list.setAdapter(adapter);

    }
}