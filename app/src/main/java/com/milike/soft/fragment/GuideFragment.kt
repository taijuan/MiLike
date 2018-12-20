package com.milike.soft.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.milike.soft.R
import com.milike.soft.activity.HomeActivity
import com.milike.soft.base.BaseFragment
import com.milike.soft.utils.onClick
import kotlinx.android.synthetic.main.fragment_guide.*

class GuideFragment : BaseFragment() {
    override fun loadUrl() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_guide, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageRes = arguments?.getInt("imageRes") ?: -1
        if (imageRes != -1) {
            imageView.setImageResource(imageRes)
        }
        if (arguments?.getBoolean("isShow") == true) {
            start.visibility = View.VISIBLE
            start.onClick {
                startActivity(Intent(activity, HomeActivity::class.java).apply {
                    putExtra("url", activity?.intent?.getStringExtra("url"))
                })
                activity?.finish()
            }
        } else {
            start.visibility = View.GONE
        }
    }
}