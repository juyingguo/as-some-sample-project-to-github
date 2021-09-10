package com.wd.room.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wd.room.R
import com.wd.room.fragment.OneFragmentKotlin
import com.wd.room.fragment.TwoFragmentKotlin
import kotlinx.android.synthetic.main.activity_view_model.*

class ViewModelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)

        replaceFragment(TwoFragmentKotlin())
        
        bt_one.setOnClickListener{
            replaceFragment(OneFragmentKotlin())
        }
        bt_two.setOnClickListener{
            replaceFragment(TwoFragmentKotlin())
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fl,fragment)
        transaction.commit()
    }

    override fun onDestroy() {
        Log.e("ViewModelActivity", "onDestroy")
        super.onDestroy()
    }
}