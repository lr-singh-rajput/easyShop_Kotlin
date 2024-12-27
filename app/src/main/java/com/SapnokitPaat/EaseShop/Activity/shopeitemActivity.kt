package com.SapnokitPaat.EaseShop.Activity


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.SapnokitPaat.EaseShop.Model.itemModel
import com.SapnokitPaat.EaseShop.databinding.ActivityShopeitemBinding
import com.SapnokitPaat.EaseShop.Adaptor.itemAdaptor
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.utils.ITEMS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source


class shopeitemActivity : AppCompatActivity() {
    private val binding :ActivityShopeitemBinding by lazy {
        ActivityShopeitemBinding.inflate(layoutInflater)
    }

    private lateinit var loadingProgressBar: ProgressBar
    private var db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarIT

        //restart activity
        binding.restartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        binding.closesibtn.setOnClickListener {
            finish()
        }
        binding.additemactivitybtn.setOnClickListener {
           var intent = Intent(this, additemActivity::class.java)
            startActivity(intent)
        }

        setupI_RecyclerView()

    }

    override fun onStart() {
        super.onStart()
        setupI_RecyclerView()

    }
    private fun setupI_RecyclerView() {
        loadingProgressBar .visibility = ProgressBar.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return


        // Configure Firestore settings for offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings



        db.collection(USER_DATA)
            .document(ITEMS)
            .collection(userId)
          // .orderBy("itemname")
           .orderBy("itemname")
            .get(getSource())
            .addOnSuccessListener { result ->
                handleResult(result)
            }
            .addOnFailureListener { e ->
                loadingProgressBar .visibility = ProgressBar.INVISIBLE
                if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    // Handle permission denied error
                    Toast.makeText(this, "Permission denied. .", Toast.LENGTH_LONG).show()
                } else {
                    // Handle other errors
                    Toast.makeText(this, "Error loading customers: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
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

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            val network =connectivityManager.activeNetwork ?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(network) ?:return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }

    private fun handleResult(result: QuerySnapshot) {
        loadingProgressBar .visibility = ProgressBar.INVISIBLE

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
            val item = result.toObjects(itemModel::class.java)
            binding.rvShowitem.layoutManager = LinearLayoutManager(this)
            binding.rvShowitem.adapter = itemAdaptor(item) { itemModel ->
                // Handle click event
                val intent = Intent(this@shopeitemActivity, profileitemActivity::class.java)
                intent.putExtra("ITEM_ID", itemModel.id)
                // intent.putExtra("CUSTOMER_NAME", itemModel.itemname)
                startActivity(intent)
            }



    }

}