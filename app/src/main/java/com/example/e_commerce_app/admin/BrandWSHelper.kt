package com.example.e_commerce_app.admin

import android.content.ContentValues
import android.util.Log
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData
import com.google.firebase.firestore.FirebaseFirestore

class BrandWSHelper {

    companion object {
        fun getBrandsFromFireStore(brandCallBack: BrandCallBack){
            val brandList:ArrayList<BrandData> = ArrayList()
            val db = FirebaseFirestore.getInstance()
            db.collection("Brands")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val brand= BrandData()

                        brand.brandId= document.getString("brandId")!!
                        brand.brandName=document.getString("brandName")!!
                        brand.brandImagePath=document.getString("brandImagePath")!!
                        brandList.add(brand)

                    }
                    // burda response callback tetiklenir
                    brandCallBack.brandListResponse(brandList)
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }
}