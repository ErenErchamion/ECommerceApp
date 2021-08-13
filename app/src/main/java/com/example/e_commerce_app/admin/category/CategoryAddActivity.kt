package com.example.e_commerce_app.admin.category

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.CategoryData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CategoryAddActivity : AppCompatActivity() {
    private lateinit var fileUri:Uri
    private var imageUrl:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_add)

        val buttonAddCategory:Button=findViewById(R.id.buttonAddSubCategory)
        val imageViewCategoryImage:ImageView=findViewById(R.id.imageViewEditCategoryImage)
        buttonAddCategory.setOnClickListener(){
            uploadImageToFirebase(fileUri)
        }
        imageViewCategoryImage.setOnClickListener(){
            upload()
        }
    }


fun addCategory(){
    val list:ArrayList<CategoryData> = ArrayList()
    
    val db = FirebaseFirestore.getInstance()

    val catnameEditText:EditText=findViewById(R.id.editTextCategoryEditName)
    var catname=catnameEditText.text.toString()

    var newCategory=CategoryData()

    newCategory.catName=catname
    newCategory.catId=""
    newCategory.catImagePath=imageUrl
    newCategory.catParrentId=""




    db.collection("Categories").document()
        .set(newCategory)


    db.collection("Categories")
        .whereEqualTo("catName", ""+catname)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                var id=document.getString("catId")

                id=document.id
                newCategory.catId=id



                db.collection("Categories").document(""+id)
                    .set(newCategory)

                Toast.makeText(applicationContext, "başarıyla yeni kategori eklediniz", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }





}







    fun upload(){
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
                val categoryImageView: ImageView =findViewById(R.id.imageViewEditCategoryImage)
                categoryImageView.setImageURI(fileUri)
            }

        }

    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {

            val sdf = SimpleDateFormat("dd:MM:yyyy:hh:mm:ss")
            val currentDate = sdf.format(Date())
            var fileName = currentDate +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {

                            imageUrl = it.toString()
                            addCategory()



                        }
                    })

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }





}