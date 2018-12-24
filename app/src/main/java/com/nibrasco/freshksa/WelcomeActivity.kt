package com.nibrasco.freshksa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_welcome.*
import org.w3c.dom.Text

class WelcomeActivity : AppCompatActivity() {

    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dots: ArrayList<TextView>? = null
    private var layouts: IntArray? = null
    private var prefManager: PreferenceManager? = null

    //  viewpager change listener
    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts!!.size - 1) {
                // last page. make button text to GOT IT
                btn_next.text = getString(R.string.start)
                btn_skip.visibility = View.GONE
            } else {
                // still pages are left
                btn_next.text = getString(R.string.next)
                btn_skip.visibility = View.VISIBLE
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}

        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (!isTaskRoot
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.action != null
                && intent.action == Intent.ACTION_MAIN) {
            finish()
            return
        }
        // Checking for first time launch - before calling setContentView()
        prefManager = PreferenceManager(this)
        /* if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
*/
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_welcome)
        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(R.layout.slide_screen1, R.layout.slide_screen2, R.layout.slide_screen3, R.layout.slide_screen4, R.layout.slide_screen5)

        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()

        myViewPagerAdapter = MyViewPagerAdapter()
        view_pager.adapter = myViewPagerAdapter
        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)

        btn_skip.setOnClickListener { launchHomeScreen() }

        btn_next!!.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts!!.size) {
                // move to next screen
                view_pager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {


        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        layoutDots.removeAllViews()
        dots = ArrayList()

        for( i in 0..layouts!!.size){
            var t = TextView(this)
            t.text = Html.fromHtml("&#8226;")
            t.textSize = 35f
            t.setTextColor(colorsInactive[1])
            dots!!.add(t)
        }

        if (dots!!.isNotEmpty())
            dots!![currentPage].setTextColor(colorsActive[1])
    }

    private fun getItem(i: Int): Int {
        return view_pager.currentItem + i
    }

    private fun launchHomeScreen() {
        prefManager!!.isFirstTimeLaunch = false
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts!![position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts!!.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}