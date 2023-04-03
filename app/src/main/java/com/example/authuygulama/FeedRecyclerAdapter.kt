package com.example.authuygulama

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.authuygulama.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

private lateinit var binding : RecyclerRowBinding

class FeedRecyclerAdapter(val postList : ArrayList<Post>) : RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder>() {

    class PostHolder (binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerRowEmail = binding.recyclerRowEmail
        val recyclerRowDescription = binding.recyclerRowDescription
        val recyclerRowImage = binding.recyclerRowImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {

        holder.recyclerRowEmail.text = postList[position].kullaniciEmail
        holder.recyclerRowDescription.text = postList[position].aciklama
        Picasso.get().load(postList[position].postURL).into(holder.recyclerRowImage)

    }


}