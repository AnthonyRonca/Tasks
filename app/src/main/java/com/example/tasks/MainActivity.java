/*
 *  Anthony Ronca
 *  8/31/19
 *  Over 2 days consisting of 3 hours
 *
 *  This application serves as a to-do list that allows users
 *  to add and remove tasks.
 *
 *      - Utilizes logging for troubleshooting experience
 *
 *
 *
 *
 *
 */


package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items; // An array of tasks as strings
    ArrayAdapter<String> itemsAdapter; // Adapts list items to string
    ListView lvItems; // ListView Items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        // Attach objects to code with R.id.objectName
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        // mock data
        //items.add("First Task");

        // Method call for removal of listItems
        setupListViewListener();


    }

    public void onAddItem(View v) {
        EditText newItem = (EditText) findViewById(R.id.newItem); // User's input
        String itemText = newItem.getText().toString(); // Text from plaintext field
        itemsAdapter.add(itemText); // Add new item to list
        newItem.setText(""); // Clear text field
        Toast.makeText(getApplicationContext(), " Item successfully added",
                Toast.LENGTH_SHORT).show(); // Show confirmation message
        writeItems();

    }

    private void setupListViewListener() {
        // Helps user debug without disrupting the UI
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            // Listens for prolonged click which then removes that task from the list
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            items.remove(position); // Removes clicked item
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            return true;

            }
        });
    }

    // Allows for data persistance
    private File getDataFile( ) {
        return new File(getFilesDir(), "todo.txt");
        // returns the file directory with that name

    }

    private void readItems( ){
        // Protected with a try - catch block
        try {
            // IDE defaults to Android.os FileUtils so apache commons is specified
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset()));

        } catch( IOException e ) {
            //
            Log.e("MainActivity", "Error reading file. ", e);
            items = new ArrayList<>();

        }
    }

    private void writeItems( ){
        // Protected with try/catch
        try {
            // IDE defaults to Android.os FileUtils so apache commons is specified
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), items);


        } catch( IOException e ) {
            Log.e("MainActivity", "Error writing to file. ", e);
            items = new ArrayList<>();
        }
    }
}
