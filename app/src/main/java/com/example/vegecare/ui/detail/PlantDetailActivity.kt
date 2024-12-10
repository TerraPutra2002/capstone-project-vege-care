package com.example.vegecare.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.vegecare.R
import com.example.vegecare.data.plant.database.PlantDatabase
import com.example.vegecare.data.plant.repository.PlantRepository
import com.example.vegecare.databinding.ActivityPlantDetailBinding
import com.example.vegecare.ui.detect.PlantDetectionActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class PlantDetailActivity : AppCompatActivity() {

    // ViewBinding instance
    private lateinit var binding: ActivityPlantDetailBinding

    // SharedPreferences to store quest completion status
    private val sharedPreferences by lazy {
        getSharedPreferences("QuestPreferences", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityPlantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

        val plantId = intent.getIntExtra("plant_id", -1)
        if (plantId == -1) {
            Toast.makeText(this, "ID tanaman tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchPlantDetails(plantId)

        // Initialize CheckBoxes and Buttons using ViewBinding
        initQuestCheckBoxes()

        // Button click listener for disease detection
        binding.btnToDisease.setOnClickListener {
            val intent = Intent(this, PlantDetectionActivity::class.java)
            startActivity(intent)
        }

        initReportDeadPlantFeature()
    }

    private fun fetchPlantDetails(plantId: Int) {
        lifecycleScope.launch {
            val plant = withContext(Dispatchers.IO) {
                val plantDao = PlantDatabase.getDatabase(this@PlantDetailActivity).plantDao()
                PlantRepository.getInstance(plantDao).getPlantById(plantId)
            }

            if (plant != null) {
                displayPlantDetails(plant)
            } else {
                Toast.makeText(this@PlantDetailActivity, "Tanaman tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayPlantDetails(plant: com.example.vegecare.data.plant.database.Plant) {
        binding.apply {
            tvJenisTanaman.text = plant.jenis
            tvJumlahTanaman.text = plant.hidup.toString()
        }
    }

    private fun initQuestCheckBoxes() {
        // Get current date
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Set listeners for quest checkboxes
        val checkBoxes = listOf(binding.checkBox1, binding.checkBox2, binding.checkBox3, binding.checkBox4)

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Save the completion status in SharedPreferences
                    markQuestAsCompleted(checkBox)
                    // Show Toast for quest completion
                    Toast.makeText(this, "Anda telah menyelesaikan quest ini dan mendapat 15 leaf point", Toast.LENGTH_SHORT).show()
                } else {
                    // Prevent unchecking if quest is already completed
                    if (isQuestCompleted(checkBox)) {
                        checkBox.isChecked = true // Keep it checked
                    }
                }
            }
        }

        // Disable Checkboxes 3 and 4 if it's not the 15th day of the month
        if (currentDay != 15) {
            binding.checkBox3.isEnabled = false
            binding.checkBox4.isEnabled = false
        } else {
            binding.checkBox3.isEnabled = true
            binding.checkBox4.isEnabled = true
        }
    }

    // This function ensures that the quest status is stored
    private fun markQuestAsCompleted(checkBox: CheckBox) {
        val editor = sharedPreferences.edit()
        when (checkBox.id) {
            R.id.checkBox1 -> editor.putBoolean("quest_harian_done", true)
            R.id.checkBox2 -> editor.putBoolean("quest_harian_done", true)
            R.id.checkBox3 -> editor.putBoolean("quest_bulanan_done", true)
            R.id.checkBox4 -> editor.putBoolean("quest_bulanan_done", true)
        }
        editor.apply()
    }

    // This function checks if the quest has already been completed
    private fun isQuestCompleted(checkBox: CheckBox): Boolean {
        return when (checkBox.id) {
            R.id.checkBox1, R.id.checkBox2 -> sharedPreferences.getBoolean("quest_harian_done", false)
            R.id.checkBox3, R.id.checkBox4 -> sharedPreferences.getBoolean("quest_bulanan_done", false)
            else -> false
        }
    }

    private fun initReportDeadPlantFeature() {
        binding.btnTanamanMati.setOnClickListener {
            val tanamanHidup = binding.tvJumlahTanaman.text.toString().toIntOrNull() ?: 0
            var tanamanMati = 0

            val dialogView = layoutInflater.inflate(R.layout.dialog_input_tanaman_mati, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Laporkan Tanaman Mati")
                .setView(dialogView)
                .setPositiveButton("Submit") { _, _ ->
                    showConfirmationDialog(tanamanHidup, tanamanMati)
                }
                .setNegativeButton("Batal", null)
                .create()

            val btnPlus = dialogView.findViewById<ImageButton>(R.id.btn_plus)
            val btnMin = dialogView.findViewById<ImageButton>(R.id.btn_minus)
            val tvInputCount = dialogView.findViewById<TextView>(R.id.tv_input_count)

            btnPlus.setOnClickListener {
                tanamanMati ++
                tvInputCount.text = tanamanMati.toString()
            }

            btnMin.setOnClickListener {
                if (tanamanMati > 0) {
                    tanamanMati --
                    tvInputCount.text = tanamanMati.toString()
                }
            }

            dialog.show()
        }
    }

    private fun showConfirmationDialog(tanamanHidup: Int, tanamanMati: Int) {
        if (tanamanMati > tanamanHidup) {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Kesalahan Input")
                .setMessage("Jumlah tanaman mati tidak boleh melebihi jumlah tanaman!")
                .setPositiveButton("Ulangi") { _, _ -> binding.btnTanamanMati.performClick() }
                .setNegativeButton("Batal", null)
                .show()
        } else {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin akan melaporkan $tanamanMati tanaman mati?")
                .setPositiveButton("Ya") { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        updateDeadPlantCount(tanamanMati)
                    }
                    Toast.makeText(this, "$tanamanMati tanaman telah dilaporkan mati", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }

    private suspend fun updateDeadPlantCount(tanamanMati: Int) {
        withContext(Dispatchers.IO) {
            val plantId = intent.getIntExtra("plant_id", -1)
            val plantDao = PlantDatabase.getDatabase(this@PlantDetailActivity).plantDao()
            val repository = PlantRepository.getInstance(plantDao)
            val plant = repository.getPlantById(plantId)
            plant?.let {
                val newJumlahHidup = it.hidup - tanamanMati
                if (newJumlahHidup >= 0) {
                    repository.updateJumlahHidup(plantId, newJumlahHidup)
                    withContext(Dispatchers.Main){
                        binding.tvJumlahTanaman.text = newJumlahHidup.toString()
                    }
                }
            }
        }
    }
}
