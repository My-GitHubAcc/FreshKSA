package com.nibrasco.freshksa

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.support.v7.widget.Toolbar
import com.google.firebase.database.*
import com.nibrasco.freshksa.Model.*
import kotlinx.android.synthetic.main.fragment_orderitem.*

import java.util.ArrayList

class OrderItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderitem, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Session.Item = Cart.Item()
        LoadContent()
    }
    private fun LoadContent() {
        val list = ArrayList<ItemCategory>()
        for (c in Cart.eCategory.values()) {
            if (c != Cart.eCategory.None)
                list.add(ItemCategory(resources, c))
        }
        recyclerItems.addOnItemTouchListener(
                RecyclerItemTouchListener(activity!!.applicationContext, recyclerItems, object : RecyclerItemTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {
                        val item = list[position]
                        val category = Cart.eCategory.Get(item.image_drawable)
                        var fragment: Fragment? = null
                        when (category) {
                            Cart.eCategory.Goat -> fragment = SheepFragment()
                            Cart.eCategory.Sheep -> fragment = SheepFragment()
                            Cart.eCategory.GroundMeat -> fragment = CamelFragment()
                            Cart.eCategory.Camel -> fragment = CamelFragment()
                            Cart.eCategory.HalfSheep -> fragment = HalfFragment()
                        }
                        fragment?.let {
                            Session.Item!!.Category = category
                            val ft = activity!!.supportFragmentManager.beginTransaction()
                            ft.replace(R.id.orderItemContentFrame, it)
                            ft.commit()
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                }))

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        recyclerItems.adapter = RecyclerItemAdapter(context!!, list)
        recyclerItems.layoutManager = layoutManager

        Session.Item!!.Category = Cart.eCategory.Sheep
        val fragment = SheepFragment()
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.orderItemContentFrame, fragment)
        ft.commit()
    }
}
