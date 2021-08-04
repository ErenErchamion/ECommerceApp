package com.example.e_commerce_app.admin

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition;
import android.graphics.drawable.Drawable
import androidx.annotation.Nullable
import androidx.core.net.toUri
import java.io.File



class AdminEditBrandActivity : AppCompatActivity() {

    private lateinit var fileUri:Uri
    private var imageUrl:String?=null
    lateinit var brand:BrandData
    lateinit var oldimagename:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_brand)

        brand= intent.getSerializableExtra("brand") as BrandData
        brand.brandId

        val brandnameedittext:EditText=findViewById(R.id.editTextEditBrandName)
        val brandImageNameedittext:EditText=findViewById(R.id.editTextEditBrandImageName)
        val imageViewBrandEdit:ImageView=findViewById(R.id.imageViewBrandEdit)

        brandnameedittext.setText(""+brand.brandName)
        val imageurl=(""+brand.brandImagePath)

        val httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(""+imageurl)
        oldimagename=(""+httpsReference.name).dropLast(4)

        brandImageNameedittext.setText(""+oldimagename)

        val media =(""+brand.brandImagePath)
        if (media !== null) {
            Glide.with(this).asBitmap().load(media).into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                    imageViewBrandEdit.setImageBitmap(resource)
                    getImageUriFromBitmap(resource)

                }
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })

        }


        imageViewBrandEdit.setOnClickListener {
            Upload()
        }

    }


    fun getImageUriFromBitmap(photo: Bitmap){



        val file = File(this.cacheDir,"CUSTOM NAME") //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

        fileUri= file.toUri()
    }



fun setBrand(){
    val db = FirebaseFirestore.getInstance()

    val brandnameedittext:EditText=findViewById(R.id.editTextEditBrandName)
    val brandImageNameedittext:EditText=findViewById(R.id.editTextEditBrandImageName)


    var brandname=brandnameedittext.text.toString()
    var brandimagename=brandImageNameedittext.text.toString()

    val docref = db.collection("Brands").document(""+brand.brandId)

    docref
        .update("brandName", ""+brandname,"brandImagePath",""+imageUrl)


        .addOnSuccessListener {
            if(oldimagename!=brandimagename){
                var oldStorage = FirebaseStorage.getInstance().reference.child("Brands/$oldimagename"+".jpg")
                    oldStorage.delete()

            }
        }

        .addOnFailureListener { e ->
            Log.w(TAG, "Error updating document", e)
        }




}

fun updateBrand(view: View){

    uploadImageToFirebase(fileUri)

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
                val brandImageView:ImageView=findViewById(R.id.imageViewBrandEdit)
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
                            setBrand()
                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }



}

