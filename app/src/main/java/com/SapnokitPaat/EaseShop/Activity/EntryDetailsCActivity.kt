package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.databinding.ActivityEntryCDetailsBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntryDetailsCActivity : AppCompatActivity() {
    private val binding:ActivityEntryCDetailsBinding by lazy {
        ActivityEntryCDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var buttonUpdate: Button

    private lateinit var textgave: TextView
    private lateinit var textgot: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        binding.EDCRestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        loadingProgressBar = binding.loadingPrograssBarCE
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        buttonUpdate = binding.deletbtnEc
        buttonUpdate .visibility =Button.INVISIBLE

        textgave = binding.textView40
        textgave .visibility =TextView.INVISIBLE

        textgot = binding.textView38
        textgot.visibility =TextView.INVISIBLE

        val costomarname = intent.getStringExtra("CUSTOMER_NAME")
        val transactionaId = intent.getStringExtra("TRANSACTION_ID")
        val costomarId = intent.getStringExtra("CUSTOMER_ID")


        binding.shownameEd.text="$costomarname"

        binding.backEd.setOnClickListener {
            finish()
        }

        binding.updatebtnEd.setOnClickListener {
            var intent = Intent(this, updateTrancationCActivity::class.java)
            intent.putExtra("CUSTOMER_ID", costomarId)
            intent.putExtra("TRANSACTION_ID", transactionaId)
            startActivity(intent)
        }


        setup_data_show()

        binding.deletbtnEc.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("delete")
            dialog.setMessage("This will delete the transaction  ")

            dialog.setPositiveButton("Yas") {
                    dialogInterface, which ->
                if (transactionaId != null) {
                    db.collection(USER_DATA)
                        .document(CUSTOMERS)
                        .collection(userId)
                        .document(costomarId!!)
                        .collection(CUSTOMERS)
                        .document(transactionaId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "transaction deleted successfully", Toast.LENGTH_SHORT).show()


                            // Optionally, you can update UI or perform any other actions after deletion
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete customer: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "No customer ID available", Toast.LENGTH_SHORT).show()
                }
           //     startActivity(Intent(this,showcustomarTransactionActivity::class.java))
            //    finish()
              //  val intent = Intent(this, showcustomarTransactionActivity::class.java)
              //  startActivity(intent)
                finish()


            }
            dialog.setNegativeButton("No"){
                    dialogInterface,which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            }
            var alertDialog:AlertDialog= dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()



        }
    }

    override fun onStart() {
        super.onStart()
        setup_data_show()
    }

    private fun setup_data_show() {
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val transactionaId = intent.getStringExtra("TRANSACTION_ID")
        val costomarId = intent.getStringExtra("CUSTOMER_ID")


        /*/ Configure Firestore settings for offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
*/
        if (transactionaId != null) {
            db.collection(USER_DATA)
                .document(CUSTOMERS)
                .collection(userId)
                .document(costomarId!!)
                .collection(CUSTOMERS)
                .document(transactionaId)
                .get()
                .addOnSuccessListener {documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE
                    if (documentSnapshot.exists()){
                        val user = documentSnapshot.toObject(transactionModel::class.java)
                        if (user != null){


                            val give = user.T_give
                            val got = user.T_got
                            val strgive = give.toString()
                            val strgot = got.toString()

                            if ( got >= 0.toString() && give >= 0.toString()){
                                textgave.visibility =TextView.VISIBLE
                                textgot.visibility =TextView.VISIBLE
                                binding.showgoveEd.text ="₹$strgive"
                                binding.showgotEd.text = "₹$strgot"
                                //  Toast.makeText(this, "show gor $strgot , $strgive", Toast.LENGTH_SHORT).show()

                            }
                            else if (got >= 0.toString()){

                                binding.showgotEd.text = "₹$strgot"
                                textgot.visibility =TextView.VISIBLE
                                textgave.visibility =TextView.INVISIBLE
                                //   Toast.makeText(this, "show got $strgot ", Toast.LENGTH_SHORT).show()
                            } else  if (give >= 0.toString()){
                                binding.showgoveEd.text ="₹$strgive"
                                textgot.visibility =TextView.INVISIBLE
                                textgave.visibility =TextView.VISIBLE
                                //  Toast.makeText(this, "show give, $strgive", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, "empty field ", Toast.LENGTH_SHORT).show()
                            }

                            // binding.showgoveEd.text ="₹$"




                            binding.showbillEd.text = user.T_billNo
                            binding.showdetailEd.text = user.T_detail
                            // Convert milliseconds to formatted date and time string
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy \n hh:mm a", Locale.getDefault())
                            val formattedDateTime = dateFormat.format(Date(user.mfgDt))
                            binding.showtimeEd.text = formattedDateTime



                        } else {
                            // Handle the case where the user conversion is null
                            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                        }
                    } else{
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