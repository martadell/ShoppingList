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

    private ArrayList<ShoppingItem> itemlist;
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
        itemlist.add(new ShoppingItem ("Patatas", true));
        itemlist.add(new ShoppingItem ("Papel de vater", true));
        itemlist.add(new ShoppingItem ("Zanahorias"));
        itemlist.add(new ShoppingItem ("Copas danone"));

        adapter = new ShoppingListAdapter(this, R.layout.shopping_item, itemlist);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }}
        );

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                itemlist.get(pos).toggleChecked();
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> list, View item, int position, long id) {
                maybeRemoveitem(position);
                return true;
            }
        });



    }

    private void maybeRemoveitem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        String message = getText(R.string.confirm_message) + " " + "'" + itemlist.get(position).getText() + "'" + "?";
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
            itemlist.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");}

        list.smoothScrollToPosition(itemlist.size()-1);
    }
}