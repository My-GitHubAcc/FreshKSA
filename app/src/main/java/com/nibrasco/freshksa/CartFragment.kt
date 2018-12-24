package com.nibrasco.freshksa


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.*
import kotlinx.android.synthetic.main.fragment_cart.*

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {

    private val cart: Cart?
    private var items: ArrayList<CartItemCategory> = ArrayList()

    init {
        // Required empty public constructor
        cart = Session.Cart
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val v = view
        DisplayValues()
        val flipListeners = cart != null && cart.Items()?.isNotEmpty()!!
        if (flipListeners) {
            FillRecyclerView(v)
            AssignListeners(flipListeners)
        } else {
            btnItemOrder.visibility = View.GONE
            btnConfirmCart.text = btnItemOrder.text
            AssignListeners(flipListeners)
        }
    }

    private fun DisplayValues() {
        txtCartTotal.text = cart?.Total().toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    private fun FillRecyclerView(v: View?) {
        if (cart?.Items()!!.isNotEmpty()) {
            cart.Items()!!.forEach {
                items.add(CartItemCategory(it))
            }
            recyclerCartItems.adapter = RecyclerCartItemAdapter(v!!.context, items)
            val layoutManager = LinearLayoutManager(v.context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerCartItems.layoutManager = layoutManager
        }
    }

    private fun AssignListeners(flipped: Boolean) {
        val orderListener = View.OnClickListener {
            val f = OrderItemFragment()
            assert(fragmentManager != null)
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.homeContainer, f)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val shippingListener = View.OnClickListener {
            val f = ShippingDetailsFragment()
            assert(fragmentManager != null)
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.homeContainer, f)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        recyclerCartItems.addOnItemTouchListener(RecyclerItemTouchListener(activity!!.applicationContext,
                recyclerCartItems,
                object : RecyclerItemTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        RemoveItem(position)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                }))
        if ((!flipped)) {
            btnConfirmCart!!.setOnClickListener(orderListener)
        } else {
            btnConfirmCart.setOnClickListener(shippingListener)
            btnItemOrder.setOnClickListener(orderListener)
        }
    }

    internal fun RemoveItem(index: Int) {
        try {
            if (index >= 0 && cart!!.Items()!!.isNotEmpty()) {
                items.removeAt(index)
                recyclerCartItems.adapter!!.notifyDataSetChanged()
                DisplayValues()
                Session.Cart = cart
                val db = FirebaseDatabase.getInstance()
                val tblCart = db.getReference("Cart")
                tblCart.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(cartsSnap: DataSnapshot) {
                        val id = Session.User!!.cart
                        tblCart.child(id!!).child("Items").child(Integer.toString(cart.GetItem(index).Category.Value)).removeValue()
                        cart.RemoveItem(index)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                val flipListeners = cart.Items()!!.isNotEmpty()
                if (flipListeners) {
                    FillRecyclerView(view)
                    AssignListeners(flipListeners)
                } else {
                    btnItemOrder.visibility = View.GONE
                    btnConfirmCart!!.text = btnItemOrder.text
                    AssignListeners(flipListeners)
                }
            }
        } catch (e: Exception) {
        }

    }
}
