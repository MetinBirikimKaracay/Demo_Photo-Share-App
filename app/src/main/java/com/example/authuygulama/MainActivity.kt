package com.example.authuygulama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.authuygulama.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val intent = Intent(this,Feed::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun girisYap(view:View){

        auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString()).addOnSuccessListener {

            val guncelKullanici = auth.currentUser?.email.toString()
            Toast.makeText(this,"Hoşgeldin ${guncelKullanici}", Toast.LENGTH_LONG).show()

            val intent = Intent(this,Feed::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

    fun kayitOl(view: View) {

        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString()).addOnSuccessListener {

            val intent = Intent(this, Feed::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener {
            Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
