package com.SapnokitPaat.EaseShop.ActivityBills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.databinding.ActivityProfileBillBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class profileBillActivity : AppCompatActivity() {
    private val binding: ActivityProfileBillBinding by lazy {
        ActivityProfileBillBinding.inflate(layoutInflater)
    }

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardloadingProgressBar: CardView
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarCPTB
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardloadingProgressBar = binding.cardloadingPrograssBarCPTB
        cardloadingProgressBar.visibility = CardView.VISIBLE


        buttonUpdate = binding.deletebtnCdB
        buttonUpdate .visibility = Button.INVISIBLE

        // Retrieve the ID passed from MainActivity
        val costomarIdBIL = intent.getStringExtra("CUSTOMER_ID")

        binding.backbtnUCB.setOnClickListener {
            finish()
        }
        //restart activity
        binding.PCrestartbtnB.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid



        binding.updatebtnCdB.setOnClickListener {
            var intent = Intent(this, updatecustomerBillActivity::class.java)
            //   intent.putExtra("TRANSACTION_ID", transactionModel.T_id)
            intent.putExtra("CUSTOMER_ID", costomarIdBIL)
            // intent.putExtra("CUSTOMER_NAME", costomarname)
            startActivity(intent)
        }

        binding.deletebtnCdB.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("delete")
            dialog.setMessage("This will delete the customer and transaction  ")

            dialog.setPositiveButton("Yas") {
                    dialogInterface, which ->
                if (costomarIdBIL != null) {
                    db.collection(USER_DATA)
                        .document(BILL)
                        .collection(userId)
                        .document(costomarIdBIL)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT).show()

                            // Optionally, you can update UI or perform any other actions after deletion
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete customer: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                  //  Toast.makeText(this, "Please back manage Bill page, then delete Customer", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()


                } else {
                    Toast.makeText(this, "No customer ID available", Toast.LENGTH_SHORT).show()
                }

            }
            dialog.setNegativeButton("No"){
                    dialogInterface,which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            }
            var alertDialog: AlertDialog = dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

    setupRecyclerview()


    }
    override fun onStart() {
        super.onStart()
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        val costomarIdBIL = intent.getStringExtra("CUSTOMER_ID")
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        /*
 // Configure Firestore settings for offline persistence
 val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(true)
      .build()
     db.firestoreSettings = settings
*/
        // val firestore = Firebase.firestore

        //firestore.firestoreSettings = firestoreSettings {
        //  isPersistenceEnabled = true
        //}

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
                    cardloadingProgressBar .visibility = CardView.INVISIBLE

                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(customerModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.shownameCDB.text = user.C_name
                            val number= user.C_number.toString()
                            val code= user.C_country

                            binding.shownumberCdB.text = "$code$number"
                            binding.showareaCdB.text = user.C_area
                            binding.showpinCdB.text = user.C_pincode
                            binding.showcityCdB.text = user.C_city
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
                    cardloadingProgressBar .visibility = CardView.INVISIBLE
                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }



    }
}