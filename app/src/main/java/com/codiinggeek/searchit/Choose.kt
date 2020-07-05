package com.codiinggeek.searchit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Choose : AppCompatActivity() {

    lateinit var btnBinarySearch: Button
    lateinit var btnLinearSearch: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        sharedPreferences = getSharedPreferences("Preference", Context.MODE_PRIVATE)
        val intent = Intent(this@Choose, MainActivity::class.java)

        btnLinearSearch = findViewById(R.id.btnLinearSearch)
        btnBinarySearch = findViewById(R.id.btnBinarySearch)

        btnLinearSearch.setOnClickListener {
            sharedPreferences.edit().putInt("Option", 1).apply()
            startActivity(intent)
        }
        btnBinarySearch.setOnClickListener {
            sharedPreferences.edit().putInt("Option", 2).apply()
            startActivity(intent)
        }
    }
}