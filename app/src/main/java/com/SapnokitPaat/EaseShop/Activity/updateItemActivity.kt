package com.SapnokitPaat.EaseShop.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar

import android.widget.Toast
import androidx.cardview.widget.CardView

import com.SapnokitPaat.EaseShop.Model.itemModel

import com.SapnokitPaat.EaseShop.databinding.ActivityUpdateItemBinding

import com.SapnokitPaat.EaseShop.utils.ITEMS

import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class updateItemActivity : AppCompatActivity() {
    private val binding: ActivityUpdateItemBinding by lazy {
        ActivityUpdateItemBinding.inflate(layoutInflater)
    }
    private lateinit var Model: itemModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView
    private lateinit var buttonUpdate: Button
   // private lateinit var genderSwitch: Switch


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarIU
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardPrograssBarIU
        cardProgressBar .visibility = CardView.VISIBLE

        buttonUpdate = binding.updatebtnItu
        buttonUpdate .visibility =Button.INVISIBLE

      /*  genderSwitch = binding.genderSwitch

        
        binding.showimageIPUPi.setOnClickListener {
            Toast.makeText(this, "image not update", Toast.LENGTH_SHORT).show()
        }

        this code swich button on off to database and colore
        // Set a listener to detect changes in the gender Switch state
        genderSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Change the thumb tint color based on the state of the gender Switch
            if (isChecked) {
                genderSwitch.thumbTintList = resources.getColorStateList(R.color.blue)
            } else {
                genderSwitch.thumbTintList = resources.getColorStateList(R.color.black)
            }

            // Set the value of the model property based on the state of the gender Switch
            val genderValue = if (isChecked) {
                "In Stock" // Set to "Male" if the gender Switch is checked
            } else {
                "Out of Stock" // Set to "Female" if the gender Switch is not checked
            }

            // Assign the gender value to your model property
            // For example, if your model is named Model, and the gender property is named gender:
            Model.stock = genderValue
        }
        */




        val itemId = intent.getStringExtra("ITEM_ID")


        Model= itemModel()

        //update code

        binding.updatebtnItu.setOnClickListener {

           // Model.imageitem=binding.SHOWIDI.text.toString()
           // Model.itemname=binding.nameitemUp.text.toString()
            //Model.parchassprice=binding.buyitemUp.text.toString().toInt()
           // Model.shellprice=binding.sellitemUp.text.toString().toInt()
          //  Model.stock=binding.stockitemUp.text.toString().toInt()
            //Model.unit=binding.unititemUp.text.toString()

            if (itemId != null) {
                Model.id=itemId
            }




            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            val nameitemss=binding.nameitemUp.text.toString()
            val buys=binding.buyitemUp.text.toString()
            val sells=binding.sellitemUp.text.toString()
            val stocksU=binding.stockitemUp.text.toString()

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)

                db.collection(USER_DATA)
                    .document(ITEMS)
                      .collection(userId)
                     .document(itemId!!)
                    .update("itemname" ,nameitemss,
                        "parchassprice" , buys,
                        "shellprice" , sells,
                        "stock" , stocksU
                    )
                    .addOnSuccessListener { documentSnapshot ->
                        Toast.makeText(this, "update customer", Toast.LENGTH_SHORT).show()

                    }

                    .addOnFailureListener { exception ->
                        // Handle any errors
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                finish()


        }

        binding.cancelbtnItu.setOnClickListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            finish()
        }

        // show data in update activity

        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (itemId != null) {
            db.collection(USER_DATA)
                .document(ITEMS)
                .collection(userId)
                .document(itemId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    cardProgressBar .visibility = CardView.INVISIBLE
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(itemModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                           // binding.SHOWIDI.setText( user.imageitem)
                            binding.nameitemUp.setText( user.itemname)
                            binding.buyitemUp.setText (user.parchassprice.toString())
                            binding.sellitemUp.setText ( user.shellprice.toString())
                            binding.stockitemUp.setText ( user.stock.toString())
                         //   binding.unititemUp.setText ( user.unit)

                            // Set the state of the gender Switch based on the user's gender
                         //   genderSwitch.isChecked = user.stock == "In Stock"

                        } else {
                            // Handle the case where the user conversion is null
                            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle the case where the document does not exist
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    cardProgressBar .visibility = CardView.INVISIBLE
                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }


    }
}