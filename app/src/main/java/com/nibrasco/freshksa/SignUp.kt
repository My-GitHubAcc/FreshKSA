package com.nibrasco.freshksa

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.Cart
import com.nibrasco.freshksa.Model.Session
import com.nibrasco.freshksa.Model.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    val db = FirebaseDatabase.getInstance()
    val tblUser = db.getReference("User")
    val tblCart = db.getReference("Cart")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnSignUp.setOnClickListener { v ->
            RegisterUser(v)
        }
    }
    private fun RegisterUser(v : View) {

        val name = edtSignUpName.text.toString()
        val pwd = edtSignUpPwd.text.toString()
        val phone = edtSignUpPhone.text.toString()
        if (phone != "" || pwd != "") {
            if (phone.length >= 10) {
                val message = resources.getString(R.string.msgSignUpRegister)
                val snack = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                snack.setActionTextColor(Color.WHITE).show()
                tblUser.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.child(edtSignUpPhone.text.toString()).exists()) {
                            snack.dismiss()

                            Session.User = User(name, pwd)
                            Session.User?.let{
                                it.phone = phone

                                CreateCart(it)

                                snack.setText(resources.getString(R.string.msgSignUpSuccess))
                                        .setActionTextColor(Color.GREEN)
                                        .show()
                                startActivity(Intent(this@SignUp, Home::class.java))
                                snack.dismiss()
                            }
                        } else {
                            snack.dismiss()
                            snack.setText(resources.getString(R.string.msgSignInRegisterFailed))
                                    .setActionTextColor(Color.RED).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            } else {

                Snackbar.make(v, resources.getString(R.string.msgSignUpWrongNumber), Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.YELLOW)
                        .show()
            }

        } else {

            Snackbar.make(v, resources.getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show()
        }
    }
    private fun CreateCart(user : User)
    {
        Session.Cart= Cart()
        tblCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(cartsSnap: DataSnapshot) {
                user.cart = cartsSnap.childrenCount.toString()
                user.MapToDbRef(tblUser.child(user.phone!!))
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
