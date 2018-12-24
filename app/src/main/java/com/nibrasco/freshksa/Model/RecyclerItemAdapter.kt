package com.nibrasco.freshksa.Model

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nibrasco.freshksa.R

import android.view.LayoutInflater.*

class RecyclerItemAdapter(//private final List<Fragment> fragments = new ArrayList<>();
        //private final List<String> titles = new ArrayList<>();
        //public ViewPagerItemAdapter(FragmentManager fm) {
        //    super(fm);
        //}
        //public void addFragment(Fragment fragment, String title) {
        //    fragments.add(fragment);
        //    titles.add(title);
        //}
        //@Override
        //public Fragment getItem(int i) {
        //    return fragments.get(i);
        //}
        //
        //@Override
        //public int getCount() {
        //    return fragments.size();
        //}
        private val ctx: Context, private val items: List<ItemCategory>) : RecyclerView.Adapter<RecyclerItemAdapter.ItemHolder>() {
    inner class ItemHolder//
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView
        var resource: ImageView
        //
        val resourceId: Int
            get() = resource.id

        init {
            title = itemView.findViewById<View>(R.id.txtCategory) as TextView
            resource = itemView.findViewById<View>(R.id.CategoryResource) as ImageView
        }

        fun bind(item: ItemCategory) {
            resource.setImageResource(item.image_drawable)
            title.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ItemHolder {
        val view = from(ctx)
                .inflate(R.layout.recycler_item, parent, false)
        return ItemHolder(view)
    }

    //
    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        itemHolder.bind(items[position])

    }

    //
    override fun getItemCount(): Int {
        return items.size
    }

    //
    override fun getItemViewType(position: Int): Int {
        return R.layout.recycler_item
    }
}

