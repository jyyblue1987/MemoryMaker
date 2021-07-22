package com.example.memorymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity(), Callbacks {
    var fragmentNum : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if( savedInstanceState == null )
        {
            showMemoryListFramgment()

        }

    }

    fun showMemoryListFramgment() {
        fragmentNum = 1
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MemoryListFragment.newInstance())
            .commitAllowingStateLoss()

    }

    override fun onMemorySelected(Memory: UUID) {
        fragmentNum = 2

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MemoryFragment.newInstance(Memory))
            .commit()
    }

    override fun onBackPressed() {
        if( fragmentNum == 2 ) {
            showMemoryListFramgment()
            return
        }

        super.onBackPressed()
    }
}