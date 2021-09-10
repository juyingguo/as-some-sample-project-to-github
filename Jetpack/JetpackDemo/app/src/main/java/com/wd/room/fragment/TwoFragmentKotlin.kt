package com.wd.room.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wd.room.R
import com.wd.room.viewmodel.SharedViewModel
import com.wd.room.viewmodel.SharedViewModelTwo
import kotlinx.android.synthetic.main.frag_two_kotlin.*

class TwoFragmentKotlin: Fragment() {
    val TAG:String  = "TwoFragmentKotlin";
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_two_kotlin,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(activity!!).get(SharedViewModel::class.java)
        val modelTwo = ViewModelProvider(activity!!,SharedViewModelTwo.SharedViewModelTwoFactory("hello")).get(SharedViewModelTwo::class.java);

        model.sharedName.observe(this, Observer {
            Log.d(TAG, "Observer call,it:$it")
            tv.text = it
        })

        modelTwo.sharedName.observe(this, Observer {
            Log.d(TAG, "Observer call,it:$it")
            tv_view_model_with_param.text = it

        })
    }

    override fun onDestroy() {
        Toast.makeText(activity, "TwoFragment is destroyed", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}