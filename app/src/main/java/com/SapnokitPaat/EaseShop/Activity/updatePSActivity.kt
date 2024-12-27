package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.databinding.ActivityUpdatePsBinding
import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class updatePSActivity : AppCompatActivity() {
    private val binding: ActivityUpdatePsBinding by lazy {
        ActivityUpdatePsBinding.inflate(layoutInflater)
    }
    private lateinit var S_Model: supplierModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarSU
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardloadingPrograssBarSU
        cardProgressBar .visibility = CardView.VISIBLE

        buttonUpdate = binding.updatebtnUs
        buttonUpdate .visibility =Button.INVISIBLE

        val supplierId = intent.getStringExtra("SUPPLIER_ID")

        binding.backbtnUs.setOnClickListener {
            finish()
        }
        S_Model= supplierModel()

        binding.updatebtnUs.setOnClickListener {
            S_Model.S_name=binding.NameUs.text.toString()
            S_Model.S_number=binding.numberUs.text.toString()
            S_Model.S_area=binding.areaUs.text.toString()
            S_Model.S_pincode=binding.pincodeUs.text.toString()
            S_Model.S_city=binding.cityUs.text.toString()

            if (supplierId != null) {
                S_Model.S_id=supplierId
            }


            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
            if (supplierId != null) {
                db.collection(USER_DATA)
                    .document(SUPPLIERS)
                    .collection(userId)
                    .document(supplierId)
                    .set(S_Model)
                    .addOnSuccessListener { documentSnapshot ->
                        Toast.makeText(this, "update customer", Toast.LENGTH_SHORT).show()

                    }

                    .addOnFailureListener { exception ->
                        // Handle any errors
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }

            }
            finish()
        }


        binding.cancellbtnUs.setOnClickListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            finish()
        }


        // show data
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (supplierId != null) {
            db.collection(USER_DATA)
                .document(SUPPLIERS)
                .collection(userId)
                .document(supplierId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    cardProgressBar .visibility = CardView.INVISIBLE
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(supplierModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.NameUs.setText( user.S_name)
                            binding.numberUs.setText (user.S_number.toString())
                            binding.areaUs.setText ( user.S_area)
                            binding.pincodeUs.setText ( user.S_pincode)
                            binding.cityUs.setText(user.S_city)
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