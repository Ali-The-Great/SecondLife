package com.example.secondlife
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.database.sqlite.SQLiteDatabase
import android.widget.SimpleCursorAdapter



class MainActivity : AppCompatActivity() {

    private lateinit var itemNameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var addButton: Button
    private lateinit var itemsListView: ListView
    private lateinit var myDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the database
        myDatabase = openOrCreateDatabase("Items", MODE_PRIVATE, null)

        // Create the table
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS items (name VARCHAR, quantity INT(3))")

        // Set up the layout
        setContentView(R.layout.activity_main)

        itemNameEditText = findViewById(R.id.itemNameEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        addButton = findViewById(R.id.addButton)
        itemsListView = findViewById(R.id.itemsListView)

        // Set up the add button listener
        addButton.setOnClickListener {
            // Insert a new row into the database
            val itemName = itemNameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toInt()

            myDatabase.execSQL("INSERT INTO items (name, quantity) VALUES ('$itemName', $quantity)")

            // Update the ListView
            val cursor = myDatabase.rawQuery("SELECT * FROM items", null)
            (itemsListView.adapter as SimpleCursorAdapter).changeCursor(cursor)
        }

        // Set up the adapter
        val columns = arrayOf("name", "quantity")
        val ids = intArrayOf(R.id.itemNameTextView, R.id.quantityTextView)

        val adapter = SimpleCursorAdapter(
            this,
            R.layout.item_layout,
            myDatabase.rawQuery("SELECT * FROM items", null),
            columns,
            ids,
            0
        )

        itemsListView.adapter = adapter
    }
}
