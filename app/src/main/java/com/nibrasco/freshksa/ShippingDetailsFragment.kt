package com.nibrasco.freshksa


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nibrasco.freshksa.Model.Cart
import com.nibrasco.freshksa.Model.GPSTracker
import com.nibrasco.freshksa.Model.Session
import com.nibrasco.freshksa.Model.User
import kotlinx.android.synthetic.main.fragment_shippingdetails.*

import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class ShippingDetailsFragment : Fragment() {

    var cart: Cart? = Session.Cart

    init {
        // Required empty public constructor
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        LoadContent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shippingdetails, container, false)
    }

    private fun LoadContent() {
        val user = Session.User
        txtDetailsUserName.text=user!!.name
        txtDetailsPhone.text=user.phone
        InitGps()
        LinkListeners()
    }

    private fun LinkListeners() {
        rdGrpTime.setOnCheckedChangeListener { group, checkedId ->
            when (group.checkedRadioButtonId) {
                R.id.rdTimeNoon -> cart?.setTimeOfDelivery(0)
                R.id.rdTimeAfterNoon -> cart?.setTimeOfDelivery(1)
                R.id.rdTimeEvening -> cart?.setTimeOfDelivery(2)
            }
        }
        btnConfirmShipping.setOnClickListener(View.OnClickListener { v ->
            if (edtOrderAddress.text.toString() == "") {
                Snackbar.make(v, resources.getString(R.string.msgSignInEmpty), Snackbar.LENGTH_LONG)
                return@OnClickListener
            }
            cart?.address = edtOrderAddress.text.toString()
            Session.Cart=cart
            val fragment = PaymentDetailsFragment()
            activity!!
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.homeContainer, fragment)
                    .commit()
        })
    }

    private fun InitGps() {
        activity?.let {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1037)
            val snackbar = Snackbar.make(view!!, "Getting Location", Snackbar.LENGTH_INDEFINITE)
            (snackbar.view
                    .findViewById<View>(android.support.design.R.id.snackbar_text) as TextView)
                    .setTextColor(Color.YELLOW)
            snackbar.show()

            val gps = GPSTracker(it)
            GetLocation(gps)
            snackbar.dismiss()
        }

    }

    private fun GetLocation(gps: GPSTracker) {
        val geocoder = Geocoder(activity!!.applicationContext)
        try {
            val addressList = geocoder.getFromLocation(gps.latitude, gps.longitude, 1)
            val str = addressList[0].getAddressLine(0)
            cart?.address = str
            edtOrderAddress.text?.append(cart?.address)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
