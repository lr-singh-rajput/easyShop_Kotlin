package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.itemModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAdditemBinding
import com.SapnokitPaat.EaseShop.utils.ITEMS
import com.SapnokitPaat.EaseShop.utils.ITEM_IMAGE
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.SapnokitPaat.EaseShop.utils.uploadImage


class additemActivity : AppCompatActivity() {

    private val binding : ActivityAdditemBinding by lazy {
        ActivityAdditemBinding.inflate(layoutInflater)
    }


   private val launcher= registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            uploadImage(uri, ITEM_IMAGE){
                if (it==null){
                    Toast.makeText(this, "not save image url", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "save image url", Toast.LENGTH_SHORT).show()

                    Model.imageitem=it
                    binding.imageitemsave.setImageURI(uri)

                }
            }
        }
    }
    private lateinit var Model:itemModel
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.AIBackbtn.setOnClickListener {
            finish()
        }
        // only show red line
         Model= itemModel()

        binding.imageitemsave.setOnClickListener {
           // launcher.launch("image/*")
            launcher.launch("image/*")
        }


    binding.saveitembtn.setOnClickListener {
    if (binding.saveitemname.text.toString().isEmpty() ||//itemName.isEmpty() || shellPrice == 0 || buyPrice == 0
        binding.saveshellprice.text.toString().isEmpty() ||
        binding.savebuyprice.text.toString().isEmpty()||
        binding.savestockItem.text.toString().isEmpty()

    ){
        Toast.makeText(this, "Pleasse fill the field", Toast.LENGTH_SHORT).show()
        }else{
            Model.itemname=binding.saveitemname.text.toString()
            Model.shellprice=binding.saveshellprice.text.toString().toInt()
            Model.parchassprice=binding.savebuyprice.text.toString().toInt()
            Model.stock=binding.savestockItem.text.toString().toInt()
           // Model.unit=binding.saveUnitI.text.toString()

        /*
        swich button save in databaase
        // Get the stock status based on the switch button state
        val stockStatus = if (binding.switchStock.isChecked) "In Stock" else "Out of Stock"
        Model.stock = stockStatus
        */

        val USER_ID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        // Add a new document with a generated ID
        val newDocumentRef = db
            .collection(USER_DATA)
            .document(ITEMS)
            .collection(USER_ID)
            .document()


// Set the generated document ID to your model
        Model.id = newDocumentRef.id

        // Now save the model to Firestore with the generated document ID
        newDocumentRef.set(Model)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
               // Toast.makeText(this, "please click  reload btn  ", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        finish()




        }


        }
    }
}