package com.example.authuygulama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authuygulama.databinding.ActivityFotografPaylasBinding

class ActivityFotografPaylas : AppCompatActivity() {

    private lateinit var binding : ActivityFotografPaylasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotografPaylasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}