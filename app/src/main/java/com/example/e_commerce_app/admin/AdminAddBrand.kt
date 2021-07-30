package com.example.e_commerce_app.admin

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AdminAddBrand : AppCompatActivity() {
    lateinit var fileUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_brand)

    }


    fun addCategory(view: View){
        val db = FirebaseFirestore.getInstance()

        val brandnameEditText:EditText=findViewById(R.id.editTextBrandName)
        val brandimagepathEditText:EditText=findViewById(R.id.editTextBrandImagePath)

        var brandname=brandnameEditText.text.toString()
        var brandimagepath=brandimagepathEditText.text.toString()

        var newBrand=BrandData()

        newBrand.brandName=brandname
        newBrand.brandImagePath=brandimagepath
        newBrand.brandId=""

        db.collection("Brands").document()
            .set(newBrand)

        db.collection("Brands")
            .whereEqualTo("brandName", ""+brandname)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var id=document.getString("brandId")

                    id=document.id
                    newBrand.brandName=brandname
                    newBrand.brandImagePath=brandimagepath
                    newBrand.brandId=id

                    db.collection("Brands").document(""+id)
                        .set(newBrand)

                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        uploadImageToFirebase(fileUri)



    }



    fun Upload(view: View){
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            100
        )
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == 100
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
                if (data!=null&&data.data!=null){
                    fileUri = data.data!!
                    val brandImageView:ImageView=findViewById(R.id.brandImageView)
                    brandImageView.setImageURI(fileUri)
                }

        }

    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {

            val FileNameText = findViewById(R.id.editTextBrandImagePath) as EditText
            var filename=FileNameText.text.toString()
            var fileName = filename +".jpg"


            val refStorage = FirebaseStorage.getInstance().reference.child("Brands/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }

}

