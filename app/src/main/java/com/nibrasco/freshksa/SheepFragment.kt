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
import kotlinx.android.synthetic.main.fragment_naemiorder.*

/**
 * A simple [Fragment] subclass.
 */
class SheepFragment : Fragment() {

    internal var currentItem: Cart.Item? = null

    init {
        currentItem = Session.Item
        currentItem!!.Quantity = 1
        currentItem!!.Weight = 0
        currentItem!!.Intestine = false
        currentItem!!.Slicing = Cart.eSlicing.Fridge
        currentItem!!.Packaging = Cart.ePackaging.None
        currentItem!!.Total = Session.Item!!.DefaultPrice
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_naemiorder, container, false)
    }

    override fun onStart() {
        super.onStart()
        LoadContent()
    }

    private fun LinkListeners() {
        rdGrpIntestine.setOnCheckedChangeListener { group, checkedId ->
            when (group.checkedRadioButtonId) {
                R.id.rdInt_Yes -> currentItem!!.Intestine = true
                R.id.rdInt_No -> currentItem!!.Intestine = false
            }
        }
        spPackaging.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //if(position != spPackaging.getSelectedItemPosition()) {
                currentItem!!.setPackaging(parent.getPositionForView(view))
                txtTotalItem.text = currentItem!!.Total.toString()
                //}
                //No specific pricing
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        spWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //if(position != parent.getSelectedItemPosition()) {
                currentItem!!.Weight = parent.getPositionForView(view)
                txtTotalItem.text = currentItem!!.Total.toString()
                //}
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        spSlicing.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // if(position != spSlicing.getSelectedItemPosition()) {
                currentItem!!.setSlicing(parent.getPositionForView(view))
                //}
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
                currentItem!!.Quantity = if (qte >= 1) qte else 1
                txtTotalItem.text = currentItem!!.Total.toString()

            }
        })
        edtNotes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                currentItem!!.Notes = s.toString()
            }
        })
        btnItemOrder.setOnClickListener { v ->
            if (Session.Item!!.Category != Cart.eCategory.None) {
                //Add the item to the cart at this point
                if (SaveChanges()!!) {
                    val f = CartFragment()
                    val fragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.homeContainer, f)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
        }
    }

    private fun LoadContent() {

        context?.let {
            var list = Cart.Lists.GetWeightNames(Session.Item!!.Category)
            var adapter: ArrayAdapter<*> = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spWeight!!.adapter = adapter
            //spWeight.setSelection(0);

            list = Cart.Lists.GetSlicingNames()
            adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spSlicing!!.adapter = adapter
            //spSlicing.setSelection(0);

            list = Cart.Lists.GetPackagingNames()
            adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spPackaging!!.adapter = adapter
        }

        //spPackaging.setSelection(0);
        txtTotalItem.text = Session.Item!!.Total.toString()

        LinkListeners()
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
}
