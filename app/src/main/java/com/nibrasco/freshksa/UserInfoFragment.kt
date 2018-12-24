package com.nibrasco.freshksa

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nibrasco.freshksa.Model.Session
import com.nibrasco.freshksa.Model.User
import kotlinx.android.synthetic.main.fragment_userprofil.*


class UserInfoFragment : Fragment() {

    private var txtName: TextView? = null
    private var txtPhone: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_userprofil, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let {
            LoadData(it)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //final View v = getView();

    }

    private fun LinkControls(v: View) {
        txtName = v.findViewById<View>(R.id.txtUserInfoName) as TextView
        txtPhone = v.findViewById<View>(R.id.txtUserInfoPhone) as TextView
    }

    private fun LoadData(v: View) {
        val message = resources.getString(R.string.msgUserInfoLoading)
        val snackbar = Snackbar.make(v, message, Snackbar.LENGTH_INDEFINITE)
        snackbar.show()
        val usr = Session.User
        usr?.let {
            txtUserInfoPhone.text = it.phone
            txtUserInfoName.text = it.name
        }
        txtUserInfoAddress.text = Session.Cart?.address
        snackbar.dismiss()
    }
}// Required empty public constructor
