package com.nibrasco.freshksa


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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
import com.nibrasco.freshksa.Model.Session
import kotlinx.android.synthetic.main.fragment_nesfnaemiorder.*

/**
 * A simple [Fragment] subclass.
 */
class HalfFragment : Fragment() {

    var currentItem: Cart.Item?

    init {
        // Required empty public constructor
        currentItem = Session.Item
        currentItem!!.Quantity = 1
        currentItem!!.Intestine = false
        currentItem!!.Weight = 0
        currentItem!!.setPackaging(Cart.ePackaging.Bags.Value)
        currentItem!!.setSlicing(Cart.eSlicing.Fridge.Value)
        currentItem!!.Notes = ""
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nesfnaemiorder, container, false)
    }

    override fun onStart() {
        super.onStart()
            LoadContent()

    }

    private fun LoadContent() {
        edtQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val qte = Integer.parseInt(if (s.toString() == "") "0" else s.toString())
                currentItem!!.Quantity = qte
                txtTotalItem.text = currentItem!!.Total.toString()
            }
        })
        btnItemOrder.setOnClickListener { v ->
            if (Session.Item!!.Category != Cart.eCategory.None) {
                //Add the item to the cart at this point
                if (SaveChanges()) {
                    val f = CartFragment()
                    val fragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.homeContainer, f)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
        }
    }

    private fun SaveChanges(): Boolean {
        var snack : Snackbar? = null
        view?.let {
            snack = Snackbar.make(it, "Saving Your Order", Snackbar.LENGTH_LONG)
            snack?.show()
        }

        var success = true
        Session.Cart!!.AddItem(Session.Item!!)
        //if(Session.getInstance().User().getCart().equals("0"))
        //{
        val db = FirebaseDatabase.getInstance()
        val tblCart = db.getReference("Cart")
        tblCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(cartsSnap: DataSnapshot) {

                val cartRef = tblCart.child(Session.User!!.cart!!)
                Session.Item!!.MapToDbRef(cartRef.child("Items"))

                view?.let {
                    snack?.dismiss()
                }
                success = true
            }

            override fun onCancelled(databaseError: DatabaseError) {
                success = false
            }
        })
        //}
        return success
    }
}
