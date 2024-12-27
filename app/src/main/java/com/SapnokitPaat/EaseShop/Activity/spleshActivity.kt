package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.SapnokitPaat.EaseShop.R

class spleshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splesh)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },3000

        )

    }

}