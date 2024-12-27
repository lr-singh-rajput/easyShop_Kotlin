package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.SapnokitPaat.EaseShop.Model.userModel
import com.SapnokitPaat.EaseShop.ActivityBills.billingActivity



import com.SapnokitPaat.EaseShop.databinding.ActivityHomeBinding
import com.SapnokitPaat.EaseShop.utils.LOGINDETAILS
import com.SapnokitPaat.EaseShop.utils.USER_DATA


class HomeActivity : AppCompatActivity() {

   private val binding : ActivityHomeBinding by lazy {
       ActivityHomeBinding.inflate(layoutInflater)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // Assuming 'users' is the collection name
       val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection("user")
           .document(userId)
        // Assuming 'users' is the name of your collection
        userRef.get()
            .addOnSuccessListener { querySnapshot ->


                    val user =querySnapshot.toObject(userModel::class.java)
                    if (user != null) {
                        // Assuming you want to display information from the first document found
                        // Update UI
                        binding.shownameU.text = user.U_name
                        binding.showusername.text = user.U_shopName
                        // Remove this if you want to handle all documents in the collection
                    } else {
                        // Handle the case where the user conversion is null
                    }

            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }




        binding.customersBTN.setOnClickListener{
            var intent = Intent(this@HomeActivity, customarDetailActivity::class.java)

            startActivity(intent)
        }
        binding.suppliersBTN.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        binding.shopBTN.setOnClickListener{
            var intent = Intent(this, shopeitemActivity::class.java)
            startActivity(intent)
        }
        binding.billBTNh.setOnClickListener{
            var intent = Intent(this, billingActivity::class.java)
            startActivity(intent)
        }




    }
}