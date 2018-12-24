package com.nibrasco.freshksa


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.Cart
import com.nibrasco.freshksa.Model.Session
import kotlinx.android.synthetic.main.fragment_hachiorder.*

/**
 * A simple [Fragment] subclass.
 */
class CamelFragment : Fragment() {

    private val currentItem: Cart.Item? = Session.Item

    init {
        currentItem!!.Quantity = 1
        currentItem.Weight = 0
        currentItem.Intestine = false
        currentItem.setSlicing(Cart.eSlicing.Fridge.Value)
        currentItem.setPackaging(Cart.ePackaging.None.Value)
        currentItem.Total = Session.Item!!.DefaultPrice
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_hachiorder, container, false)
    }

    override fun onStart() {
        super.onStart()
        LoadContent()
    }

    private fun LinkListeners() {
        spWeightCamel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentItem!!.Weight = parent.getPositionForView(view)
                txtTotalItem.text = currentItem.Total.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        edtQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val qte = Integer.parseInt(if (s.toString() == "") "1" else s.toString())
                currentItem!!.Quantity = qte
                txtTotalItem.text = currentItem.Total.toString()
            }
        })
        edtNotes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                Session.Item!!.Notes = s.toString()
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
        Session.Cart?.AddItem(Session.Item!!)
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

    private fun LoadContent() {

        val list = Cart.Lists.GetWeightNames(Session.Item!!.Category)

        context?.let {
            val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spWeightCamel.adapter = adapter
        }
        txtTotalItem.text = Session.Item!!.Total.toString()

        LinkListeners()
    }
}

