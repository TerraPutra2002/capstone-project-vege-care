package com.example.vegecare.ui.home.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.vegecare.R
import androidx.recyclerview.widget.RecyclerView
import com.example.vegecare.data.plant.database.Plant
import com.example.vegecare.databinding.ItemPlantBinding

class PlantAdapter(private val onItemClick: (Plant) -> Unit) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    private val plantList = mutableListOf<Plant>()

    fun submitList(list: List<Plant>) {
        plantList.clear()
        plantList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlantAdapter.PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantAdapter.PlantViewHolder, position: Int) {
        holder.bind(plantList[position])
    }

    override fun getItemCount(): Int = plantList.size

    inner class PlantViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant) {
            binding.tvNamaTanaman.text = plant.nama
            binding.tvJenisTanaman.text = plant.jenis
            binding.tvJumlahTanaman.text = "Jumlah: ${plant.jumlah}"
            binding.tvTanamanMati.text = "Mati: ${plant.jumlah - plant.hidup}"

            val persentase = if (plant.jumlah>0) {
                (plant.hidup.toDouble() / plant.jumlah * 100).toInt()
            } else {
                0
            }
            binding.tvPersentaseHidup.text = "Persentase: $persentase%"

            val drawableRes = when (plant.jenis) {
                "Cabai (Chilli)" -> R.drawable.cabai_v2
                "Kembang Kol (Cauliflower)" -> R.drawable.kembang_kol_v2
                "Selada (Lettuce)" -> R.drawable.selada_v2
                "Sawi (Mustard Greens)" -> R.drawable.sawi_v2
                "Terong (Eggplant)" -> R.drawable.terong_v2
                "Timun (Cucumber)" -> R.drawable.timun_v2
                "Tomat (Tomato)" -> R.drawable.tomat_v2
                else -> R.drawable.daun
            }

            val context = itemView.context
            val compressedBitmap = getCompressedBitmap(context, drawableRes, 90, 90)
            binding.ivItem.setImageBitmap(compressedBitmap)
            binding.root.setOnClickListener {
                onItemClick(plant)
            }
        }
    }

    private fun getCompressedBitmap(context: Context, drawableRes: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        BitmapFactory.decodeResource(context.resources, drawableRes, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(context.resources, drawableRes, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}