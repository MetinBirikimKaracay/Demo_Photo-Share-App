package com.example.authuygulama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.authuygulama.databinding.ActivityFeedBinding
import com.example.authuygulama.databinding.ActivityFotografPaylasBinding
import com.example.authuygulama.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class Feed : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

    }
    //Yaptığımız menüyü uygulamaya bağlamak için
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        //,'den sonraki menü fonksiyon parametresi olarak tanımlanan menü
        menuInflater.inflate(R.menu.ayarlar,menu)

        return super.onCreateOptionsMenu(menu)
    }

    //Menüde seçilen seçeneğin onclick metodları diyebiliriz.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.fotografPaylas){

            val intent = Intent(this,ActivityFotografPaylas::class.java)
            startActivity(intent)

        }else if(item.itemId == R.id.cikis){

            //Çıkış yap
            auth.signOut()

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}