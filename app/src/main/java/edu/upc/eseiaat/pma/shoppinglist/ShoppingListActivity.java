package edu.upc.eseiaat.pma.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String FILENAME = "string_list.txt";
    private static final int MAX_BYTES = 8000;
    private ArrayList<ShoppingItem> itemlist;
    private ShoppingListAdapter adapter;

    private ListView list;
    private Button btn_add;
    private EditText edit_item;

    private void writeItemList() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            for (int i =0; i < itemlist.size(); i++) {
                ShoppingItem it = itemlist.get(i);
                String line = String.format("%s;%b\n", it.getText(), it.isChecked());
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("pauek", "writeItemList: FileNotFoundException");
            Toast.makeText(this, R.string.cannotwrite, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("pauek", "writeItemList: IOException");
            Toast.makeText(this, R.string.cannotwrite, Toast.LENGTH_SHORT).show();
        }
    }

    private void readItemlist() {
        itemlist = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[MAX_BYTES];
            int nread = fis.read(buffer);
            String content = new String (buffer, 0, nread);
            String[] lines = content.split("\n"); //separar les línies
            for (String line : lines) { //passa per les linies
                String[] parts = line.split(";"); //separar els noms dels booleans
                itemlist.add(new ShoppingItem(parts[0], parts[1].equals("true")));
            }
            fis.close();

        } catch (FileNotFoundException e) {
            Log.i("pauek", "readItemList: FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("pauek", "readItemList: IOException");
            Toast.makeText(this, R.string.cannotread, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeItemList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        list = (ListView) findViewById(R.id.list);
        btn_add = (Button) findViewById(R.id.btn_ad);
        edit_item = (EditText) findViewById(R.id.edit_item);

        readItemlist();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.clear_checked:
                clearChecked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearChecked() {
        int i =0;
        while (i < itemlist.size()) {
            if (itemlist.get(i).isChecked()){
                itemlist.remove(i);}
            else {
                i++;}
        }
        adapter.notifyDataSetChanged();
    }
}