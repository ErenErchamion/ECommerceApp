package com.example.e_commerce_app.admin.category

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.CategoryData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File

class CategoryUpdateActivity : AppCompatActivity() {
    lateinit var parrentCategory:CategoryData
    lateinit var subCategory:CategoryData
    private lateinit var fileUri:Uri
    private var imageUrl:String?=null
    lateinit var oldimagename:String
    var isSub=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_update)
        parrentCategory= intent.getSerializableExtra("parrentcategory") as CategoryData
        subCategory= intent.getSerializableExtra("subcategory") as CategoryData


        setResult(RESULT_OK)
        setOldCategory(parrentCategory)

        val updateButton:Button=findViewById(R.id.buttonUpdateCategory)
        val deleteButton:Button=findViewById(R.id.buttonDeleteCategory)
        val updateCatImageView:ImageView=findViewById(R.id.imageViewEditCategoryImage)

        updateButton.setOnClickListener(){
            uploadImageToFirebaseUpdate(fileUri)
        }
        deleteButton.setOnClickListener(){
            deleteOldCategory(parrentCategory)
        }
        updateCatImageView.setOnClickListener(){
            Upload()
        }
    }

    fun setOldCategory(oldCategory:CategoryData){

        val editTextSubCategoryName:TextView=findViewById(R.id.editTextCategoryEditName)
        val imageViewSubCategoryImage:ImageView=findViewById(R.id.imageViewEditCategoryImage)
        val editTextTextSubCategoryImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)

        val imageurl=(""+oldCategory.catImagePath)

        val httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(""+imageurl)
        oldimagename=(""+httpsReference.name).dropLast(4)

        editTextTextSubCategoryImageName.setText(""+oldimagename)
        editTextSubCategoryName.setText(""+oldCategory.catName)

        editTextTextSubCategoryImageName.setText(""+oldimagename)

        val media =(""+oldCategory.catImagePath)
        if (media !== null) {
            Glide.with(this).asBitmap().load(media).into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                    imageViewSubCategoryImage.setImageBitmap(resource)
                    getImageUriFromBitmap(resource)

                }
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })

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
                val updateCatImageView:ImageView=findViewById(R.id.imageViewEditCategoryImage)
                updateCatImageView.setImageURI(fileUri)
            }

        }

    }

    private fun uploadImageToFirebaseAddSub(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextCategoryEditImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Toast.makeText(applicationContext, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show()
                            if(fileNameText.toString()!=oldimagename){
                                deleteOldImage(oldimagename)
                            }

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }

    private fun uploadImageToFirebaseUpdate(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextCategoryEditImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Toast.makeText(applicationContext, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show()
                            if(fileNameText.toString()!=oldimagename){
                                deleteOldImage(oldimagename)
                                updateCategory(parrentCategory)
                            }

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }
    fun deleteOldImage(deleteimagename:String){

    var fileName = deleteimagename +".jpg"

    val storageUrl = ("Categories/"+fileName)
    val storageReference = FirebaseStorage.getInstance().reference.child(storageUrl)
    storageReference.delete().addOnSuccessListener {
        Toast.makeText(applicationContext, "Çöp Dosyalar Başarıyla Silindi", Toast.LENGTH_SHORT).show()

    }
}

    fun deleteOldCategory(oldCategory: CategoryData){
        val db = FirebaseFirestore.getInstance()
        if(isSub==false){
            db.collection("Categories").document(""+oldCategory.catId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Lütfen Bekleyiniz Çöp Dosyalar Siliniyor", Toast.LENGTH_SHORT).show()
                    deleteOldImage(oldimagename)
                }
        }

    }

    fun updateCategory(oldCategory: CategoryData){
        val editTextCategoryEditName:TextView=findViewById(R.id.editTextCategoryEditName)
        val editTextTextCategoryEditImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)
        val db = FirebaseFirestore.getInstance()

        val newCategoryName=editTextCategoryEditName.text.toString()
        val newCategoryImageName=editTextTextCategoryEditImageName.text.toString()

        if(isSub==false){
            val docref = db.collection("Categories").document(""+oldCategory.catId)

            docref
                .update("catName", ""+newCategoryName,"catImagePath",""+imageUrl)


                .addOnSuccessListener {
                    if(oldimagename!=editTextTextCategoryEditImageName.toString()){
                        Toast.makeText(applicationContext, "Çöp Dosyalar Siliniyor", Toast.LENGTH_SHORT).show()
                        deleteOldImage(oldimagename)

                    }

                }
        }


                                            }
    fun addSubCategory(oldCategory: CategoryData){

        val editTextCategoryEditName:TextView=findViewById(R.id.editTextCategoryEditName)
        val editTextTextCategoryEditImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)
        val db = FirebaseFirestore.getInstance()

        val subCategoryName=editTextCategoryEditName.text.toString()
        val subCategoryImageName=editTextTextCategoryEditImageName.text.toString()





            db.collection("Categories").document(""+oldCategory.catId)
                .update(mapOf(


                ))

















    }




    }