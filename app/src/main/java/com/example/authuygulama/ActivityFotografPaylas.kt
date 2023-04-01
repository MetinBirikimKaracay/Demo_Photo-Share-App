package com.example.authuygulama

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.authuygulama.databinding.ActivityFotografPaylasBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ActivityFotografPaylas : AppCompatActivity() {

    private lateinit var binding : ActivityFotografPaylasBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseFirestore
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotografPaylasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun gorselSec(view: View){

        //İzinin daha önceden alınıp alınmadığını kontrol eden if bloğu
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Alınmamış. İlk defa izin istiyoruz
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else {
            //İzin daha önceden verilmiş
            val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)

        }

    }

    fun paylas(view: View){

        //UUID -> Universal Unique ID
        //Storage'a kaydedilen resmin ismini uuid ile veriyoruz ki her seferinde random bir isim gelip başka bir görsel ismiyle çakışmasın
        val uuid = UUID.randomUUID()
        val secilenIsim = "${uuid}.jpg"

        //Firebase arayüzündeki Storage sekmesini referans veriyor.
        val referans = storage.reference
        //üsttekinin child'ı storagetaki dosyayı referans verir. Onun da child'ı o dosyanın içindeki görseli referans verir.
        val gorselReferans = referans.child("images").child(secilenIsim)


        if(secilenGorsel != null){

            gorselReferans.putFile(secilenGorsel!!).addOnSuccessListener {
                //Toast.makeText(this,"Paylaşım Başarılı",Toast.LENGTH_LONG).show()

                //Az önce Storage'a attığımız görselin URL'sini alıyoruz. Başarılı olursa aşağıdaki işlemler gerçekleşiyor
                referans.child("images").child(secilenIsim).downloadUrl.addOnSuccessListener {
                    //Database'e kaydetmek için gereken parametreleri alıyoruz

                    val gorselUri = it.toString()
                    val kullaniciEmail = auth.currentUser!!.email.toString()
                    val aciklama = binding.etAciklama.text.toString()
                    //Güncel tarihi almak için Firebase'in kendi fonksiyonunu seçiyoruz yani com.google.firebase seçilmeli!
                    val tarih = Timestamp.now()

                    //Database işlemleri

                    //Burayı hatırlamak için 4.14 videosunu 6.dkdan itibaren izle
                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("GorselURL",gorselUri)
                    postHashMap.put("Email",kullaniciEmail)
                    postHashMap.put("Aciklama",aciklama)
                    postHashMap.put("Tarih",tarih)

                    //Post adında bir veritabanı tablosu oluşturup içine verileri gönderdik.
                    database.collection("Post").add(postHashMap).addOnSuccessListener {

                        //Feed'ten intent ile buraya gelirken finish yapmadığımız için orası hala açık
                        //o yüzden tekrar intent ile sayfa geçişi vermeye gerek yok
                        Toast.makeText(this,"Paylaşım Başarılı",Toast.LENGTH_LONG).show()
                        finish()

                    }.addOnFailureListener {

                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                        println(it.localizedMessage)

                    }


                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }



            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }


    }

    //Request koda göre izinlerin işlevleri
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //ilk defa izin veriliyor
        if(requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galeriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //data != null => geriye bir veri geldi ise
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            secilenGorsel = data.data

            if(secilenGorsel != null){

                if(Build.VERSION.SDK_INT >= 28){

                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageView.setImageBitmap(secilenBitmap)

                }else {

                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    binding.imageView.setImageBitmap(secilenBitmap)

                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}