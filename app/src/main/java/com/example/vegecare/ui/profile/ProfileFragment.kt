package com.example.vegecare.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vegecare.R
import com.example.vegecare.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val achievementList = mutableListOf<Achievement>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val achievementTitles = resources.getStringArray(R.array.achievement_names).filterNotNull()
        val achievementDescriptions = resources.getStringArray(R.array.achievement_descriptions).filterNotNull()

        for (i in achievementTitles.indices) {
            achievementList.add(
                Achievement(
                    title = achievementTitles[i],
                    description = achievementDescriptions[i]
                )
            )
        }

        val achievementAdapter = AchievementAdapter(achievementList)
        binding.rvAchievement.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvAchievement.adapter = achievementAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}