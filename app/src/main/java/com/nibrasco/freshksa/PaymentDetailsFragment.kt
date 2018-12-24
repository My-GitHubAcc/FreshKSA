package com.nibrasco.freshksa


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.Cart
import com.nibrasco.freshksa.Model.Delivery
import com.nibrasco.freshksa.Model.Session
import com.nibrasco.freshksa.Model.User
import kotlinx.android.synthetic.main.fragment_paymentdetails.*

/**
 * A simple [Fragment] subclass.
 */
class PaymentDetailsFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_paymentdetails, container, false)
    }

    override fun onStart() {
        super.onStart()
        LoadContent()
    }

    private fun LoadContent() {
        txtOrderTotal.text = Session.Cart!!.Total().toString()
        txtOrderCount.text = Session.Cart!!.GetCount().toString()

        LinkListeners()
    }

    private fun LinkListeners() {
        btnPaymentConfirm.setOnClickListener { v ->
            val message = resources.getString(R.string.msgPaymentSaving)
            val snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG)
            snackbar.show()
            val Account = edtPaymentAccount.text.toString()

            val cart = Session.Cart
            cart?.bankAccount = Account
            val user = Session.User
            val db = FirebaseDatabase.getInstance()
            val tblCart = db.getReference("Cart")

            tblCart.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(cartSnap: DataSnapshot) {
                    val cartId = user!!.cart
                    cart?.MapToDbRef(tblCart.child(cartId!!))
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            val tblUser = db.getReference("User")

            tblUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnap: DataSnapshot) {
                    val cartId = user!!.cart
                    val usrId = user.phone
                    user.AddOrder(cartId)
                    user.cart = "0"
                    user.MapToDbRef(tblUser.child(usrId!!))
                    Session.User= user
                    Session.Cart= Cart(cart?.address!!)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            snackbar.dismiss()

            val cartFragment = CartFragment()
            activity!!
                    .supportFragmentManager.beginTransaction()
                    .replace(R.id.homeContainer, cartFragment)
                    .commit()
        }
    }
}// Required empty public constructor
