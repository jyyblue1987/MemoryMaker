package com.example.memorymaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity(), Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if( savedInstanceState == null )
        {
            showMemoryListFramgment()

        }

    }

    fun showMemoryListFramgment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MemoryListFragment.newInstance())
            .commitAllowingStateLoss()

    }

    override fun onMemorySelected(Memory: UUID) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MemoryFragment.newInstance(Memory))
            .commit()
    }
}