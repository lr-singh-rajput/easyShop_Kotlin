package com.SapnokitPaat.EaseShop.ActivityBills

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.SapnokitPaat.EaseShop.Model.billCutomerModel
import com.SapnokitPaat.EaseShop.databinding.ActivityUpdatecustomerBillBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class updatecustomerBillActivity : AppCompatActivity() {
    private val binding:ActivityUpdatecustomerBillBinding by lazy {
        ActivityUpdatecustomerBillBinding .inflate(layoutInflater)
    }

    private lateinit var C_Model: billCutomerModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView
    private lateinit var buttonUpdate: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarCUB
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardloadingPrograssBarCUB
        cardProgressBar .visibility = CardView.VISIBLE

        buttonUpdate = binding.updatebtnUcB
        buttonUpdate .visibility =Button.INVISIBLE

        val costomarIdBIL = intent.getStringExtra("CUSTOMER_ID")

        binding.backbtnUcB.setOnClickListener {
            finish()
        }

        binding.cancellbtnUcB.setOnClickListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            finish()
        }
        C_Model= billCutomerModel()


        binding.updatebtnUcB.setOnClickListener {

            val nameU = binding.nameUcB.text.toString()
            val numberU = binding.numberUcB.text.toString()


            C_Model.C_name=binding.nameUcB.text.toString()
            C_Model.C_number=binding.numberUcB.text.toString()

            if (costomarIdBIL != null) {
                C_Model.C_id=costomarIdBIL
            }


            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
            if (costomarIdBIL != null) {
                db.collection(USER_DATA)
                    .document(BILL)
                    .collection(userId)
                    .document(costomarIdBIL)
                    .update("c_name",nameU ,"c_number",numberU)
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

        // show data
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (costomarIdBIL != null) {
            db.collection(USER_DATA)
                .document(BILL)
                .collection(userId)
                .document(costomarIdBIL)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    cardProgressBar .visibility = CardView.INVISIBLE
                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(billCutomerModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.nameUcB.setText( user.C_name)
                            binding.numberUcB.setText (user.C_number.toString())

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