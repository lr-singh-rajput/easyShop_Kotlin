package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.databinding.ActivityProfileCactivityBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class profileCActivity : AppCompatActivity() {
   private val bining:ActivityProfileCactivityBinding by lazy {
       ActivityProfileCactivityBinding.inflate(layoutInflater)
   }
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bining.root)

        //restart activity
        bining.PCrestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        loadingProgressBar = bining.loadingPrograssBarCPT
        loadingProgressBar .visibility = ProgressBar.VISIBLE


        buttonUpdate = bining.deletebtnCd
        buttonUpdate .visibility =Button.INVISIBLE

        // Retrieve the ID passed from MainActivity
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        bining.backbtnUC.setOnClickListener {

            finish()

        }

       bining.updatebtnCd.setOnClickListener {
            var intent = Intent(this, updatePCActivity::class.java)
         //   intent.putExtra("TRANSACTION_ID", transactionModel.T_id)
            intent.putExtra("CUSTOMER_ID", costomarId)
           // intent.putExtra("CUSTOMER_NAME", costomarname)
            startActivity(intent)
        }


        bining.deletebtnCd.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("delete")
            dialog.setMessage("This will delete the customer and transaction  ")

            dialog.setPositiveButton("Yas") {
                    dialogInterface, which ->
                if (costomarId != null) {
                    db.collection(USER_DATA)
                        .document(CUSTOMERS)
                        .collection(userId)
                        .document(costomarId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT).show()


                            // Optionally, you can update UI or perform any other actions after deletion
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete Customer: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    setResult(RESULT_OK)
                    finish()


                } else {
                    Toast.makeText(this, "No Customer ID available", Toast.LENGTH_SHORT).show()
                }

            }
            dialog.setNegativeButton("No"){
                    dialogInterface,which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            }
            var alertDialog:AlertDialog= dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


        //delete check doc


        setepRecyclerview()


    }

    override fun onStart() {
        super.onStart()
        setepRecyclerview()
    }

    private fun setepRecyclerview() {
        // Access Firestore instance
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        /*
                // Configure Firestore settings for offline persistence
                val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
                db.firestoreSettings = settings

                */

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

                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(customerModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            bining.shownameCD.text = user.C_name
                            //bining.shownumberCd.text = user.C_number.toString()
                            val number= user.C_number.toString()
                            val code= user.C_country
                            bining.shownumberCd.text = "$code$number"
                            //bining.showgsrcp.text = user.C_gstNo
                            bining.showareaCd.text = user.C_area
                            bining.showpinCd.text = user.C_pincode
                            bining.showcityCd.text = user.C_city
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

                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }
}