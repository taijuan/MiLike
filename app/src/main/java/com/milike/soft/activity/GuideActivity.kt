package com.milike.soft.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentPagerAdapter
import com.milike.soft.R
import com.milike.soft.base.BaseActivity
import com.milike.soft.fragment.GuideFragment
import com.milike.soft.utils.onPageScrolled
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        val images = arrayOf(R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4)
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int) = GuideFragment().apply {
                arguments = Bundle().apply {
                    putInt("imageRes", images[position])
                    putBoolean("isShow", position == images.size - 1)
                }
            }

            override fun getCount() = images.size
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.removeAllTabs()
        for (it in images) {
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.dot_guide))
        }
        viewPager.onPageScrolled { position, positionOffset ->
            if (position == images.size - 2 && positionOffset >= .5 || position > images.size - 2) {
                tabLayout.visibility = View.GONE
            } else {
                tabLayout.visibility = View.VISIBLE
            }
        }
    }
}
