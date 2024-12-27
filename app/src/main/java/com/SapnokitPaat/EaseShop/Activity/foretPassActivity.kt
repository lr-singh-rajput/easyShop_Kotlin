package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.SapnokitPaat.EaseShop.databinding.ActivityForetPassBinding
import com.google.firebase.auth.FirebaseAuth


class foretPassActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val binding : ActivityForetPassBinding by lazy {
        ActivityForetPassBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.forgetBack.setOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()

        binding.forgateBtn.setOnClickListener{
           val email= binding.emailForget.text.toString().trim()
        if (email.isEmpty()){
            Toast.makeText(this, " Email Required ", Toast.LENGTH_SHORT).show()
            }else{
                resetPass(email)
            }
        }

    }

    private fun resetPass(email: String) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Reset email send ", Toast.LENGTH_SHORT).show()
                finish()

            }
            else{
                Toast.makeText(this, "error Try Again ", Toast.LENGTH_SHORT).show()

            }
        }
    }
}