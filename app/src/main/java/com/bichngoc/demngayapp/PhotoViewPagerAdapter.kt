package com.bichngoc.demngayapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bichngoc.demngayapp.databinding.ItemPhotoBinding

class PhotoViewPagerAdapter(private val listPhoto: ArrayList<Photo>) :
    RecyclerView.Adapter<PhotoViewPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPhotoBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = listPhoto[position]
        holder.binding.ivPhoto.setImageResource(photo.imageResoure)
    }

    override fun getItemCount(): Int {
        return listPhoto.size
    }
}
