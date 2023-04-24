package com.clevertapTestAll

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.log


class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_new)

        val ss:String = intent.getStringExtra("webURL").toString()
        Log.d(ss, "onCreate: ")

        Toast.makeText(applicationContext, ss, Toast.LENGTH_SHORT).show()

    }

    override fun onBackPressed() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}