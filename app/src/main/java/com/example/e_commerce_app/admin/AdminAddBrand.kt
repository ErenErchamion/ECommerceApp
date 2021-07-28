package com.example.e_commerce_app.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.e_commerce_app.R
import com.google.firebase.firestore.FirebaseFirestore

class AdminAddBrand : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_brand)
    }
    fun addCategory(){
        val db = FirebaseFirestore.getInstance()
        val brandsSpiner:Spinner=findViewById(R.id.spnBrands)
        val brandImageView:ImageView=findViewById(R.id.imageView)
        val brandnameEditText:EditText=findViewById(R.id.editTextBrandName)
        val brandimagepathEditText:EditText=findViewById(R.id.editTextBrandImagePath)

        var brandname=brandnameEditText.text.toString()
        var brandimagepath=brandimagepathEditText.text.toString()



    }









}