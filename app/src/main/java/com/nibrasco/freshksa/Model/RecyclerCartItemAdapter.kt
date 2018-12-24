package com.nibrasco.freshksa.Model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nibrasco.freshksa.R


class RecyclerCartItemAdapter(private val ctx: Context, private val items: List<CartItemCategory>) : RecyclerView.Adapter<RecyclerCartItemAdapter.CartItemHolder>() {
    inner class CartItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var Category: TextView
        internal var Weight: TextView
        internal var Packaging: TextView
        internal var Qte: TextView
        internal var Slicing: TextView
        internal var Intestine: TextView
        internal var resource: ImageView

        init {
            Category = itemView.findViewById<View>(R.id.txtCartItemCategory) as TextView
            Qte = itemView.findViewById<View>(R.id.txtCartItemQte) as TextView
            Weight = itemView.findViewById<View>(R.id.txtCartItemWeight) as TextView
            Packaging = itemView.findViewById<View>(R.id.txtCartItemPackaging) as TextView
            Intestine = itemView.findViewById<View>(R.id.txtCartItemIntestine) as TextView
            Slicing = itemView.findViewById<View>(R.id.txtCartItemSlicing) as TextView
            resource = itemView.findViewById<View>(R.id.resource) as ImageView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): CartItemHolder {
        val view = LayoutInflater
                .from(ctx)
                .inflate(R.layout.recycler_cartitem, parent, false)

        return CartItemHolder(view)
    }

    override fun onBindViewHolder(itemHolder: CartItemHolder, position: Int) {
        val category = items[position]
        itemHolder.Category.text = category.category
        itemHolder.Packaging.text = category.packaging
        itemHolder.Intestine.text = category.intestine
        itemHolder.Weight.text = category.weight
        itemHolder.Slicing.text = category.slicing
        itemHolder.Qte.text = category.quantity
        itemHolder.resource.setImageResource(category.image_drawable)
    }

    override fun getItemCount(): Int {
        return items.size
    }


}


