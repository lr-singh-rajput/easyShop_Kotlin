package com.SapnokitPaat.EaseShop.Activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.SapnokitPaat.EaseShop.Model.customerModel

import com.SapnokitPaat.EaseShop.databinding.ActivityUpdatePcactivityBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class updatePCActivity : AppCompatActivity() {
    private val binding : ActivityUpdatePcactivityBinding by lazy {
        ActivityUpdatePcactivityBinding.inflate(layoutInflater)
    }
    private lateinit var C_Model: customerModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarCU
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardloadingPrograssBarCU
        cardProgressBar .visibility = CardView.VISIBLE

        buttonUpdate = binding.updatebtnUc
        buttonUpdate .visibility =Button.INVISIBLE

        val costomarId = intent.getStringExtra("CUSTOMER_ID")

        binding.backbtnUc.setOnClickListener {
            finish()
        }

        binding.cancellbtnUc.setOnClickListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            finish()
        }
        C_Model= customerModel()

        binding.updatebtnUc.setOnClickListener {

            C_Model.C_name=binding.nameUc.text.toString()
            C_Model.C_number=binding.numberUc.text.toString()
            C_Model.C_area=binding.areaUc.text.toString()
            C_Model.C_pincode=binding.pincodeUc.text.toString()
            C_Model.C_city=binding.cityUc.text.toString()
            if (costomarId != null) {
                C_Model.C_id=costomarId
            }


            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
            if (costomarId != null) {
                db.collection(USER_DATA)
                    .document(CUSTOMERS)
                    .collection(userId)
                    .document(costomarId)
                    .set(C_Model)
                    .addOnSuccessListener { documentSnapshot ->
                        Toast.makeText(this, "update customer", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                finish()
            }
        }
        //update data code end

        // show data
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (costomarId != null) {
            db.collection(USER_DATA)
                .document(CUSTOMERS)
                .collection(userId)
                .document(costomarId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    cardProgressBar .visibility = CardView.INVISIBLE
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(customerModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.nameUc.setText( user.C_name)
                            binding.numberUc.setText (user.C_number.toString())
                            binding.areaUc.setText ( user.C_area)
                            binding.pincodeUc.setText ( user.C_pincode)
                            binding.cityUc.setText(user.C_city)
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





        /*
        binding.updatebtnUc.setOnClickListener {
            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid


            if (costomarId != null) {
                val name = binding.NameUc.text.toString()
                val number = binding.numberUc.text.toString().toLong()
                val area = binding.areaUc.text.toString()
                val pincode = binding.pincodeUc.text.toString()
                val city = binding.cityUc.text.toString()

                // Create a Customer object with updated data
                val updatedCustomer = customerModel(name, number, area, pincode, city)
                db.collection(USER_DATA)
                    .document(CUSTOMERS)
                    .collection(userId)
                    .document(costomarId)
                    .set(updatedCustomer) // Use set() to update the entire document
                    .addOnSuccessListener {
                        Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                        var intent = Intent(this, customarDetailActivity::class.java)
                        finish()
                        startActivity(intent)

                        // Optionally, you can update UI or perform any other actions after deletion
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Failed to delete customer: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(this, "No customer ID available", Toast.LENGTH_SHORT).show()
            }
        }
        */

    }
}