package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.userModel
import com.SapnokitPaat.EaseShop.databinding.ActivityRegisterBinding
import com.SapnokitPaat.EaseShop.utils.LOGINDETAILS
import com.SapnokitPaat.EaseShop.utils.USER_DATA

class registerActivity : AppCompatActivity() {
    private val binding : ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val countryCodes= arrayOf("+91","+1","+44","+61")

    private lateinit var auth: FirebaseAuth

    private lateinit var Model: userModel
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //set default country code
        binding.registarcountry.setText("91").toString()
/*
        val pass= binding.registarpass
        val minLength = 5
        pass.filters = arrayOf(InputFilter.LengthFilter(minLength))
*/
        binding.loginPage.setOnClickListener {


           var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Auth
        //auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        Model= userModel()

        binding.registerBTN.setOnClickListener {
            val registername: String = binding.registarName.text.toString()
            val registershop: String = binding.registarshopName.text.toString()
            val registernumber: String = binding.registarNumber.text.toString()

            val registeremail: String = binding.registaremail.text.toString()
            val registerpass: String = binding.registarpass.text.toString()
            val registerRepass: String = binding.registarrepass2.text.toString()

            if (registername.isEmpty() || registershop.isEmpty() ||   registeremail.isEmpty() || registerpass.isEmpty() || registerRepass.isEmpty()) {
                Toast.makeText(this, "please all field enter", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(registeremail, registerpass)
                    .addOnCompleteListener(this) { task ->
                   //     Toast.makeText(this, "don", Toast.LENGTH_SHORT).show()
                        if (task.isSuccessful) {
                            val USER_ID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                         //   val userId: FirebaseUser? = auth.currentUser
                            Model.U_name=binding.registarName.text.toString()
                            Model.U_shopName=binding.registarshopName.text.toString()
                            Model.U_country=binding.registarcountry.text.toString()
                            Model.U_number=binding.registarNumber.text.toString()
                            Model.email=binding.registaremail.text.toString()
                            Model.password=binding.registarpass.text.toString()


                            if (!isValidPhoneNumber(registernumber)){
                                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                                    .show()
                                return@addOnCompleteListener

                            }
                            if (!isValidEmail(registeremail)){
                                Toast.makeText(this, "Invalid email ", Toast.LENGTH_SHORT)
                                    .show()
                                return@addOnCompleteListener
                            }

                        if (registerpass != registerRepass){
                            Toast.makeText(this, "chack password and Re-password", Toast.LENGTH_SHORT).show()
                            return@addOnCompleteListener
                        }


                            // Add a new document with a generated ID
                            val newDocumentRef = db.collection(USER_DATA)
                                .document(LOGINDETAILS)
                                .collection("user")
                                .document(USER_ID)
                            //use this
                           // val newDocumentRef = db.collection(USER_DATA)
                            //    .document(USER_ID)

                    // Set the generated document ID to your model
                            Model.U_id = newDocumentRef.id

                            // Now save the model to Firestore with the generated document ID
                            newDocumentRef.set(Model)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show()
                                    var intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                                }


                        }
                    }
                     .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun isValidEmail(registeremail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(registeremail).matches()

    }

    private fun isValidPhoneNumber(registernumber: String): Boolean {
        val phonePattern = "^[+]?[0-9]{10,13}\$".toRegex()
        return phonePattern.matches(registernumber)

    }


}