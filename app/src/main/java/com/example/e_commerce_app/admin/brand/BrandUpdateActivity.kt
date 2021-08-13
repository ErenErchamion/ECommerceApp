package com.example.e_commerce_app.admin.brand

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
import java.io.ByteArrayOutputStream
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition;
import android.graphics.drawable.Drawable
import androidx.annotation.Nullable
import androidx.core.net.toUri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class BrandUpdateActivity : AppCompatActivity() {

    private lateinit var fileUri:Uri
    private var imageUrl:String?=null
    lateinit var brand:BrandData
    lateinit var oldimagename:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit_brand)

        brand= intent.getSerializableExtra("brand") as BrandData


        val brandnameedittext:EditText=findViewById(R.id.editTextEditBrandName)
        val imageViewBrandEdit:ImageView=findViewById(R.id.imageViewBrandEdit)

        brandnameedittext.setText(""+brand.brandName)
        val imageurl=(""+brand.brandImagePath)

        val httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(""+imageurl)
        oldimagename=(""+httpsReference.name).dropLast(4)


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


    var brandname=brandnameedittext.text.toString()

    val docref = db.collection("Brands").document(""+brand.brandId)

    docref
        .update("brandName", ""+brandname,"brandImagePath",""+imageUrl)


        .addOnSuccessListener {
                Toast.makeText(applicationContext, "markayı başarıyla güncellediniz", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BrandListActivity::class.java)
                startActivity(intent)
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

            val sdf = SimpleDateFormat("dd:MM:yyyy:hh:mm:ss")
            val currentDate = sdf.format(Date())
            var fileName = currentDate +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Brands/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            setBrand()

                            if(oldimagename!=fileName){
                                var oldStorage = FirebaseStorage.getInstance().reference.child("Brands/$oldimagename"+".jpg")
                                oldStorage.delete()
                            }
                        }
                    }
                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }

fun DeleteBrand(view: View){
    val db = FirebaseFirestore.getInstance()

    db.collection("Brands").document(""+brand.brandId)
        .delete()
        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }


    val storageUrl = ("Brands/"+oldimagename+".jpg")
    val storageReference = FirebaseStorage.getInstance().reference.child(storageUrl)
    storageReference.delete().addOnSuccessListener { // File deleted successfully
        Toast.makeText(applicationContext, "markayı başarıyla güncellediniz", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, BrandListActivity::class.java)
        startActivity(intent)
    }.addOnFailureListener { // Uh-oh, an error occurred!
        Log.d(TAG, "onFailure: did not delete file")
    }


}

}

