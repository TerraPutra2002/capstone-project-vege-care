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
            binding.tvItemName.text = achievement.title
            binding.tvItemDescription.text = achievement.description

            val iconRes = when {
                position % 15 == 0 -> R.drawable.achievement_uncompleted
                position % 5 == 0 -> R.drawable.achievement_completed
                else -> R.drawable.achievement_completed
            }
            binding.itemIcon.setImageResource(iconRes)
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