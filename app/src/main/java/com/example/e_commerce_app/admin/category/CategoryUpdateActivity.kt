package com.example.e_commerce_app.admin.category

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.CategoryData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class CategoryUpdateActivity : AppCompatActivity() {
    lateinit var parrentCategory:CategoryData
    private lateinit var fileUri:Uri
    private var imageUrl:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_update)
        parrentCategory= intent.getSerializableExtra("parrentcategory") as CategoryData
        setResult(RESULT_OK)
    }








    fun Upload(){
        val intent = Intent()
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
                val brandImageView: ImageView =findViewById(R.id.imageViewBrandEdit)
                brandImageView.setImageURI(fileUri)
            }

        }

    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextEditBrandImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Brands/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }

}