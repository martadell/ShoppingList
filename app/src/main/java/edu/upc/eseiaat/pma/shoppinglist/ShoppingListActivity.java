package edu.upc.eseiaat.pma.shoppinglist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<String> itemlist;
    private ShoppingListAdapter adapter;

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

        adapter = new ShoppingListAdapter(this, R.layout.shopping_item, itemlist);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }}
        );

        edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                addItem();
                return true;
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int position, long id) {
                maybeRemoveitem(position);
                return true;
            }
        });

        list.setAdapter(adapter);

    }

    private void maybeRemoveitem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String message = getText(R.string.confirm_message) + " " + "'" + itemlist.get(position) + "'" + "?";
        builder.setMessage(message);
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemlist.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void addItem() {
        String item_text = edit_item.getText().toString();
        if (!item_text.isEmpty()) {
            itemlist.add(item_text);
            adapter.notifyDataSetChanged();
            edit_item.setText("");}
    }
}