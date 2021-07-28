package com.example.e_commerce_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerce_app.R

class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }
    fun openBrandActivity(){
        val intent = Intent(this, AdminAddBrand::class.java)
        startActivity(intent)

    }


}