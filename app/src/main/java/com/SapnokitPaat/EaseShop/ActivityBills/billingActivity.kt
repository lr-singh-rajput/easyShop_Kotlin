package com.SapnokitPaat.EaseShop.ActivityBills

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.SapnokitPaat.EaseShop.Adaptor.CustBillAdaptor
import com.SapnokitPaat.EaseShop.Model.billCutomerModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityBillingBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source

class billingActivity : AppCompatActivity() {
    private val binding:ActivityBillingBinding by lazy {
        ActivityBillingBinding.inflate(layoutInflater)
    }
    private var db = FirebaseFirestore.getInstance()
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardView: CardView

    private lateinit var searchView: SearchView
    private lateinit var adaptor: CustBillAdaptor
    private  var customerList: List<billCutomerModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar=binding.loadingPrograssBaBILLr
        cardView=binding.cardloadingPrograssBaBILLr

        binding.restartbtnbr.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        binding.closesibtnbr.setOnClickListener {
            finish()
        }

        binding.creatbillbtnactivity.setOnClickListener {
            var intent = Intent(this, addCustomerBillActivity::class.java)
            startActivity(intent)
        }
        binding.calculategstbbtnactivity.setOnClickListener {
            var intent = Intent(this, calculateGstActivity::class.java)
            startActivity(intent)
        }


        setupCB_RecyclerView()

        // Handle item click
        adaptor = CustBillAdaptor(emptyList()){ billCutomerModel ->
            // Here you handle the click event
            val intent = Intent(this, showbillActivity::class.java)
            intent.putExtra("CUSTOMER_ID", billCutomerModel.C_id)
            intent.putExtra("CUSTOMER_NAME", billCutomerModel.C_name)
            intent.putExtra("CUSTOMER_NO", billCutomerModel.C_number.toString())
            startActivity(intent)
        }
        binding.rvShowbill.layoutManager = LinearLayoutManager(this)
        binding.rvShowbill.adapter = adaptor

        // Set up search functionality

        searchView = binding.searchBill

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return true
            }
        })

    }

    private fun filter(query: String) {
        val filteredList = customerList.filter { billCutomerModel ->
            billCutomerModel.C_name.contains(query, ignoreCase = true )
        }
        adaptor.setData(filteredList)
    }
    override fun onStart() {
        super.onStart()
        setupCB_RecyclerView()

    }

    private fun setupCB_RecyclerView() {
        loadingProgressBar .visibility = ProgressBar.VISIBLE
        cardView .visibility = CardView.VISIBLE

        // Configure Firestore settings for offline persistence
      //  val settings = FirebaseFirestoreSettings.Builder()
      //     .setPersistenceEnabled(true)
      //      .build()
      //  db.firestoreSettings = settings

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection(USER_DATA)
            .document(BILL)
            .collection(userId)
            .orderBy("c_name")
            .get(getSource())
            .addOnSuccessListener { result ->
                handleResult(result)
            }
            .addOnFailureListener { e ->
                loadingProgressBar .visibility = ProgressBar.INVISIBLE
                cardView .visibility = CardView.INVISIBLE


                Toast.makeText(this, "Error loading suppliers: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }


    }
    private fun getSource(): Source{
        return if (isNetworkAvailable()){
            Source.DEFAULT
        }else{
            Source.CACHE
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network =connectivityManager.activeNetwork ?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(network) ?:return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
    private fun handleResult(result: QuerySnapshot) {
        cardView .visibility = CardView.VISIBLE
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        if (result.isEmpty && getSource() == Source.CACHE){
            //show error
            val view = layoutInflater.inflate(R.layout.dealog_warning_datanot_show,null)

            val builder =  AlertDialog.Builder(this)
            builder.setView(view)

            val alertDialog = builder.create()
            //set the background to transparent
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            return
        }

        if (result.isEmpty){
            if (isNetworkAvailable()){
                // Toast.makeText(this, "Add new data, please", Toast.LENGTH_LONG).show()
                val view = layoutInflater.inflate(R.layout.dialog_adddata_box,null)

                val builder =  AlertDialog.Builder(this)
                builder.setView(view)

                val alertDialog = builder.create()
                //set the background to transparent
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()

            }

            else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                // Show alert to check internet connection if user is offline
                // Function to show warning dialog
                val view = layoutInflater.inflate(R.layout.dealog_warning_datanot_show,null)

                val builder =  AlertDialog.Builder(this)
                builder.setView(view)

                val alertDialog = builder.create()
                //set the background to transparent
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()


            }

            return
        }



            loadingProgressBar .visibility = ProgressBar.INVISIBLE
            cardView .visibility = CardView.INVISIBLE

            val costomar = result.toObjects(billCutomerModel::class.java)
            customerList = costomar
        adaptor.setData(customerList)
        /*
            binding.rvShowbill.layoutManager = LinearLayoutManager(this)
            binding.rvShowbill.adapter = CustBillAdaptor(costomar) { customerModel ->


                //   Here you handle the click event
                val intent = Intent(this, showbillActivity::class.java)
                intent.putExtra("CUSTOMER_ID", customerModel.C_id)
                intent.putExtra("CUSTOMER_NAME", customerModel.C_name)
                intent.putExtra("CUSTOMER_NO", customerModel.C_number.toString())
                startActivity(intent)
            }

*/

    }

}