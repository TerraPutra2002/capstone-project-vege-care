package com.example.vegecare.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vegecare.R
import com.example.vegecare.databinding.ItemAchievementBinding

class AchievementAdapter(
    private val achievementList: List<Achievement>
) : RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    inner class AchievementViewHolder(private val binding: ItemAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: Achievement, position: Int) {
            binding.tvItemName.text = "Segera hadir..."
            binding.tvItemDescription.text = "Segera hadir..."

            binding.itemIcon.setImageResource(R.drawable.achievement_uncompleted)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding =
            ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievementList[position]
        holder.bind(achievement, position)
    }

    override fun getItemCount(): Int = achievementList.size
}