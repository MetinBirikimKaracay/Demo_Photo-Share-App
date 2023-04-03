package com.example.authuygulama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authuygulama.databinding.ActivityFeedBinding
import com.example.authuygulama.databinding.ActivityFotografPaylasBinding
import com.example.authuygulama.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Feed : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth :FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter : FeedRecyclerAdapter

    var postList = ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriCek()

        var layoutManager = LinearLayoutManager(this)
        binding.recylerView.layoutManager = layoutManager
        recyclerViewAdapter = FeedRecyclerAdapter(postList)
        binding.recylerView.adapter = recyclerViewAdapter

    }

    fun verileriCek(){

        database.collection("Post").orderBy("Tarih",Query.Direction.DESCENDING).addSnapshotListener { value, exeption ->

            if(exeption != null){

                Toast.makeText(this,exeption.localizedMessage,Toast.LENGTH_LONG).show()

            }else{
                //value boş mu diye kontrol ediyoruz
                if(value != null){
                    //value boş olmayabilir ama içinde veri olmaya da bilir. Ders -> 4.15
                    if(value.isEmpty == false){

                        val documentList = value.documents

                        //Daha önceden post listesine kaydettiğimiz verileri temizledik
                        postList.clear()

                        for(post in documentList){

                            //ActivityFotoğrafPaylas'ta postHasMap'e parametreleri any olarak eklediğimiz için
                            //burada o parametreyi çekerken any olarak geliyor. as String diyerek string olduğunu belirtiyoruz.
                            val userEmail = post.get("Email") as String
                            val aciklama = post.get("Aciklama") as String
                            val gorselURL = post.get("GorselURL") as String

                            val cekilenPost = Post(userEmail,aciklama,gorselURL)
                            postList.add(cekilenPost)

                        }

                        recyclerViewAdapter.notifyDataSetChanged()

                    }
                }
            }
        }
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