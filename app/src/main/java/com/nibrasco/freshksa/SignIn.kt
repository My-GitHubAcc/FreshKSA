package com.nibrasco.freshksa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.Cart
import com.nibrasco.freshksa.Model.Session
import com.nibrasco.freshksa.Model.User
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btnSignIn.setOnClickListener { v -> Authenticate(v) }
    }

    private fun Authenticate(v : View) {
        val db = FirebaseDatabase.getInstance()
        val tblUser = db.getReference("User")
        val phone = edtPhone.text!!.toString()
        val pwd = edtPwd.text!!.toString()
        if (phone != "" && pwd != "") {
            val message = resources.getString(R.string.msgSignInLoadingProfile)
            val snack = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE)
            snack.show()
            tblUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.child(edtPhone!!.text!!.toString()).exists()) {
                        snack.dismiss()

                        Session.User = User(dataSnapshot.child(phone))
                        if (Session.User?.password == pwd) {
                            val message = resources.getString(R.string.msgSignInSuccess)
                            (snack.setText(message)
                                    .view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView)
                                    .setTextColor(Color.GREEN)
                            snack.show()
                            val tblCart = db.getReference("Cart")
                            tblCart.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(cartsSnap: DataSnapshot) {
                                    val id = Session.User!!.cart
                                    if (id != "0")
                                        Session.Cart=Cart(cartsSnap.child(id!!))
                                    else {
                                        Session.Cart=Cart()
                                        Session.User!!.cart = cartsSnap.childrenCount.toString()
                                        Session.User!!.MapToDbRef(tblUser.child(Session.User!!.phone!!))
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {

                                }
                            })
                            startActivity(Intent(this@SignIn, Home::class.java))

                        } else {
                            val message = resources.getString(R.string.msgSignInFailed)
                            (snack.setText(message)
                                    .view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView)
                                    .setTextColor(Color.YELLOW)
                            snack.show()
                        }
                    } else {
                        val message = resources.getString(R.string.msgSignInInexistant)
                        (snack.setText(message)
                                .view.findViewById<View>(android.support.design.R.id.snackbar_text) as TextView)
                                .setTextColor(Color.RED)
                        snack.show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        } else {

            Snackbar.make(v, resources.getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG).show()
        }
    }
}
