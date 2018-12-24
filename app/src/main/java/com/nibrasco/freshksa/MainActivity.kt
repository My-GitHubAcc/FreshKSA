package com.nibrasco.freshksa

import android.content.Intent
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.Cart
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }

        btnMainSignIn.setOnClickListener {
            val signIn = Intent(this@MainActivity, SignIn::class.java)
            startActivity(signIn)
        }
        btnMainSignUp.setOnClickListener {
            val signUp = Intent(this@MainActivity, SignUp::class.java)
            startActivity(signUp)
        }
        //OutputToDB();
    }
}

