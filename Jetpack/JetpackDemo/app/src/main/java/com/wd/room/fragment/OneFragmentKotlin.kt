package com.wd.room.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wd.room.R
import com.wd.room.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.frag_one_kotlin.*

class OneFragmentKotlin: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_one_kotlin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(activity!!).get(SharedViewModel::class.java)

        bt_fragment_one_change_value.setOnClickListener{
            model.sharedName.value = "anrikuwen"
        }
    }

    override fun onDestroy() {
        Toast.makeText(activity,"OneFragment is destroyed", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}