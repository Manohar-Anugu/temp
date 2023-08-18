package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.test.ui.main.QrCodeReaderFragment

class MainActivity2 : AppCompatActivity() {

    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.btnLaunchNextScreen).setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateText(){
        findViewById<TextView>(R.id.tvCount).text = "$count"
    }

    override fun onResume() {
        super.onResume()
        count++
        updateText()
    }
}