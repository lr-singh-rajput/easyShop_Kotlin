package com.SapnokitPaat.EaseShop.ActivityBills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.SapnokitPaat.EaseShop.Model.BillModel

import com.SapnokitPaat.EaseShop.databinding.ActivityCreateBillBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.ParseException

class createBillActivity : AppCompatActivity() {
    private val binding: ActivityCreateBillBinding by lazy {
        ActivityCreateBillBinding .inflate(layoutInflater)
    }
    private val db = Firebase.firestore
    private lateinit var Model:BillModel
    private lateinit var button:Button
    private lateinit var button2:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val customerBillId = intent.getStringExtra("CUSTOMERBILL_ID")
        button = binding.culaterbtnbillA
        button2 = binding.removegstbtnbillA2

        //set default country code
        binding.quntitybillA.setText("1").toString()
            //   binding.iddhow.text ="$customerBillId"

        //val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).apply {
       //     timeZone = TimeZone.getDefault() // Use the device's default timezone
        //}

        binding.backbillA.setOnClickListener {
            finish()
        }
        binding.removegstbtnbillA2.setOnClickListener {
            button.visibility = Button.INVISIBLE
            val rateStr = binding.RatebillA.text.toString()
            val quantityStr = binding.quntitybillA.text.toString()
            val gstStr= binding.gstbillA.text.toString()


            if (rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toDouble()
                val amount = rate * quantity
                val gst = gstStr.toDouble()
                val removegstrate  = rate - (rate*100 /(100+gst))
                val remove= rate - removegstrate

                // add rate * quntity
                val newAmountQu = remove * quantity

                val gstAmount = amount - (amount*100 /(100+gst))

                val netvalue= amount


                binding.RatebillA.setText ("${String.format("%.2f",remove)}")

                binding.AmountbillA.setText ("${String.format("%.2f",newAmountQu)}")
                binding.totalgstbillA.setText ("${String.format("%.2f",gstAmount)}")
                binding.allwithgstAmountbillA.setText ("${String.format("%.2f",netvalue)}")

            }
            else if(rateStr.isNotEmpty() && quantityStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toInt()
                val amount = rate * quantity
                binding.AmountbillA.setText ("$amount").toString()
                binding.allwithgstAmountbillA.setText ("$amount").toString()

            } else{
                Toast.makeText(this, "all field required", Toast.LENGTH_SHORT).show()
            }
        }
    binding.culaterbtnbillA.setOnClickListener {
        button2.visibility = Button.INVISIBLE
        val rateStr = binding.RatebillA.text.toString()
        val quantityStr = binding.quntitybillA.text.toString()
        val gstStr= binding.gstbillA.text.toString()


        if (rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty()) {
            val rate = rateStr.toDouble()
            val quantity = quantityStr.toDouble()
            val amount = rate * quantity
            val gst = gstStr.toDouble()
            val withGstAmount = amount * gst / 100
            val allwithGstAmount = amount + withGstAmount
            binding.AmountbillA.setText ("$amount").toString()
            binding.totalgstbillA.setText( "$withGstAmount").toString()
            binding.allwithgstAmountbillA.setText ("$allwithGstAmount").toString()

        }
        else if(rateStr.isNotEmpty() && quantityStr.isNotEmpty()) {
            val rate = rateStr.toDouble()
            val quantity = quantityStr.toInt()
            val amount = rate * quantity
            binding.AmountbillA.setText ("$amount").toString()
            binding.allwithgstAmountbillA.setText ("$amount").toString()

        }else {
            Toast.makeText(this, "Invalied input", Toast.LENGTH_SHORT).show()

            }
        }







        binding.addnewitembillA.setOnClickListener {
            val itemName1 = binding.itemnamebillA.text.toString()
            val rateStr = binding.RatebillA.text.toString()
            val quantityStr = binding.quntitybillA.text.toString()
            val gstStr = binding.gstbillA.text.toString()
            val unitStr = binding.unitbillA.text.toString()
            val amountStr = binding.AmountbillA.text.toString()
            val withGstAmountStr = binding.totalgstbillA.text.toString()
            val allwithGstAmountStr = binding.allwithgstAmountbillA.text.toString()

            if (itemName1.isNotEmpty() && rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty() && unitStr.isNotEmpty()
                && amountStr.isNotEmpty() && withGstAmountStr.isNotEmpty()  && allwithGstAmountStr.isNotEmpty() ) {

                try {


                    val AB_model = BillModel().apply {
                        itemName = binding.itemnamebillA.text.toString()
                        detail = binding.detailbillA.text.toString()
                        unit = binding.unitbillA.text.toString()
                        rate = binding.RatebillA.text.toString().toDouble()
                        quantity = binding.quntitybillA.text.toString().toInt()
                        gst = binding.gstbillA.text.toString().toInt()

                        amount = binding.AmountbillA.text.toString().toDouble()
                        totalgst = binding.totalgstbillA.text.toString().toDouble()
                        amountwithgst = binding.allwithgstAmountbillA.text.toString().toDouble()
                        date= System.currentTimeMillis()// Automatically detect current date and time

                    }
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val newDocumentRef = db.collection(USER_DATA)
                        .document(BILL)
                        .collection(userId)
                        .document(customerBillId!!)
                        .collection(BILL)
                        .document()



                    if (newDocumentRef != null) {
                        AB_model.id = newDocumentRef.id
                    }

                    // Now save the model to Firestore with the generated document ID
                    if (newDocumentRef != null) {
                        newDocumentRef.set(AB_model)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()


                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        val intent=intent
                        finish()
                        startActivity(intent)
                    }

                } catch (e: ParseException) {
                    val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                    Toast.makeText(
                        this,
                        "Invalid date format. Please enter a date in the format: $expectedFormat",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }  else if (itemName1.isNotEmpty() && rateStr.isNotEmpty() && quantityStr.isNotEmpty() && unitStr.isNotEmpty()
            && amountStr.isNotEmpty() &&  allwithGstAmountStr.isNotEmpty()) {

            try {


                val AB_model = BillModel().apply {
                    itemName = binding.itemnamebillA.text.toString()
                    detail = binding.detailbillA.text.toString()
                    unit = binding.unitbillA.text.toString()
                    rate = binding.RatebillA.text.toString().toDouble()
                    quantity = binding.quntitybillA.text.toString().toInt()
                   // gst = binding.gstbillA.text.toString().toInt()

                    amount = binding.AmountbillA.text.toString().toDouble()
                   // totalgst = binding.totalgstbillA.text.toString().toDouble()
                    amountwithgst = binding.allwithgstAmountbillA.text.toString().toDouble()
                    date= System.currentTimeMillis()// Automatically detect current date and time

                }
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val newDocumentRef = db.collection(USER_DATA)
                    .document(BILL)
                    .collection(userId)
                    .document(customerBillId!!)
                    .collection(BILL)
                    .document()



                if (newDocumentRef != null) {
                    AB_model.id = newDocumentRef.id
                }

                // Now save the model to Firestore with the generated document ID
                if (newDocumentRef != null) {
                    newDocumentRef.set(AB_model)
                        .addOnSuccessListener {

                            Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()


                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    val intent=intent
                    finish()
                    startActivity(intent)
                }

            } catch (e: ParseException) {
                val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                Toast.makeText(
                    this,
                    "Invalid date format. Please enter a date in the format: $expectedFormat",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        else{
                Toast.makeText(this, "all field enter", Toast.LENGTH_SHORT).show()
            }

        }

        binding.finishbillA.setOnClickListener {
            val itemName1 = binding.itemnamebillA.text.toString()
            val rateStr = binding.RatebillA.text.toString()
            val unitStr = binding.unitbillA.text.toString()

            val quantityStr = binding.quntitybillA.text.toString()
            val gstStr = binding.gstbillA.text.toString()
            val amountStr = binding.AmountbillA.text.toString()
            val withGstAmountStr = binding.totalgstbillA.text.toString()
            val allwithGstAmountStr = binding.allwithgstAmountbillA.text.toString()

            if (itemName1.isNotEmpty() && rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty() && unitStr.isNotEmpty()
                && amountStr.isNotEmpty() && withGstAmountStr.isNotEmpty() && allwithGstAmountStr.isNotEmpty()) {

                try {


                    val AB_model = BillModel().apply {
                        itemName = binding.itemnamebillA.text.toString()
                        detail = binding.detailbillA.text.toString()
                        rate = binding.RatebillA.text.toString().toDouble()
                        unit = binding.unitbillA.text.toString()
                        quantity = binding.quntitybillA.text.toString().toInt()
                        gst = binding.gstbillA.text.toString().toInt()
                        amount = binding.AmountbillA.text.toString().toDouble()
                        totalgst = binding.totalgstbillA.text.toString().toDouble()
                        amountwithgst = binding.allwithgstAmountbillA.text.toString().toDouble()
                        date= System.currentTimeMillis()// Automatically detect current date and time

                    }
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val newDocumentRef = db.collection(USER_DATA)
                        .document(BILL)
                        .collection(userId)
                        .document(customerBillId!!)
                        .collection(BILL)
                        .document()



                    if (newDocumentRef != null) {
                        AB_model.id = newDocumentRef.id
                    }

                    // Now save the model to Firestore with the generated document ID
                    if (newDocumentRef != null) {
                        newDocumentRef.set(AB_model)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Successfully saved & create bill", Toast.LENGTH_SHORT).show()


                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        val intent = Intent(this, billingActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                } catch (e: ParseException) {
                    val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                    Toast.makeText(
                        this,
                        "Invalid date format. Please enter a date in the format: $expectedFormat",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }else if (itemName1.isNotEmpty() && rateStr.isNotEmpty() && quantityStr.isNotEmpty()&& amountStr.isNotEmpty()
                && unitStr.isNotEmpty() &&  allwithGstAmountStr.isNotEmpty()){

                try {


                    val AB_model = BillModel().apply {
                        itemName = binding.itemnamebillA.text.toString()
                        detail = binding.detailbillA.text.toString()
                        rate = binding.RatebillA.text.toString().toDouble()
                        unit = binding.unitbillA.text.toString()
                        quantity = binding.quntitybillA.text.toString().toInt()
                      //  gst = binding.gstbillA.text.toString().toInt()
                        amount = binding.AmountbillA.text.toString().toDouble()
                      //  totalgst = binding.totalgstbillA.text.toString().toDouble()
                       amountwithgst = binding.allwithgstAmountbillA.text.toString().toDouble()
                        date= System.currentTimeMillis()// Automatically detect current date and time

                    }
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val newDocumentRef = db.collection(USER_DATA)
                        .document(BILL)
                        .collection(userId)
                        .document(customerBillId!!)
                        .collection(BILL)
                        .document()



                    if (newDocumentRef != null) {
                        AB_model.id = newDocumentRef.id
                    }

                    // Now save the model to Firestore with the generated document ID
                    if (newDocumentRef != null) {
                        newDocumentRef.set(AB_model)
                            .addOnSuccessListener {

                                Toast.makeText(this, "Successfully saved & create bill", Toast.LENGTH_SHORT).show()


                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        val intent = Intent(this, billingActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                } catch (e: ParseException) {
                    val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                    Toast.makeText(
                        this,
                        "Invalid date format. Please enter a date in the format: $expectedFormat",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        else{
                Toast.makeText(this, "all field enter", Toast.LENGTH_SHORT).show()
            }
        }


        /*

        binding.finishbillA.setOnClickListener {



                val calculatot = calculatotModel().apply {
                    itemName = binding.itemnamebillA.text.toString()
                    rate = binding.RatebillA.text.toString().toDouble()
                    quantity = binding.quntitybillA.text.toString().toInt()
                    gst = binding.gstbillA.text.toString().toInt()
                    amount = binding.AmountbillA.text.toString().toInt()
                    detail = binding.detailbillA.text.toString()
                    totalgst = binding.withgstAmountbillA.text.toString().toDouble()
                    amountwithgst = binding.allwithgstAmountbillA.text.toString().toDouble()
                    //date = System.currentTimeMillis()// Automatically detect current date and time
                }
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                val newDocumentRef = customerBillId?.let { it1 ->
                    db.collection(USER_DATA)
                        .document(BILL)
                        .collection(userId)
                        .document(it1)
                        .collection(BILL)
                        .document()
                }


                if (newDocumentRef != null) {
                    calculatot.id = newDocumentRef.id
                }

                // Now save the model to Firestore with the generated document ID
                if (newDocumentRef != null) {
                    newDocumentRef.set(calculatot)
                        .addOnSuccessListener {

                            Toast.makeText(this, "Add item", Toast.LENGTH_SHORT).show()
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }

                }


        }
        */

    }
}