package com.example.vegecare.ui.home.addplant

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.vegecare.R
import com.example.vegecare.data.plant.PlantViewModelFactory
import com.example.vegecare.data.plant.database.Plant
import com.example.vegecare.data.plant.database.PlantDatabase
import com.example.vegecare.data.plant.repository.PlantRepository
import com.example.vegecare.databinding.ActivityAddPlantBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddPlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlantBinding
    private val addPlantViewModel: AddPlantViewModel by viewModels {
        PlantViewModelFactory(PlantRepository.getInstance(PlantDatabase.getDatabase(this).plantDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.action_bar)

        setupDropdownMenus()
        setupJumlahTanamanListener()
        setupAddPlantLogic()
        setupCancelAction()
    }

    private fun setupDropdownMenus() {
        // Dropdown untuk Jenis Tanaman
        val jenisTanamanArray = resources.getStringArray(R.array.daftar_jenis_tanaman)
        val jenisAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jenisTanamanArray)
        binding.etJenisTanaman.apply {
            setAdapter(jenisAdapter)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideKeyboard()
                    showDropDown()
                }
            }
            inputType = 0
        }

        val mediaTanamArray = resources.getStringArray(R.array.daftar_media_tanam)
        val mediaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mediaTanamArray)
        binding.etMediaTanaman.apply {
            setAdapter(mediaAdapter)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideKeyboard()
                    showDropDown()
                }
            }
            inputType = 0
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun setupJumlahTanamanListener() {
        var jumlahTanaman = 0
        binding.etJumlahTanaman.setText(jumlahTanaman.toString())

        binding.btnKurang.setOnClickListener {
            if (jumlahTanaman > 0) {
                jumlahTanaman--
                binding.etJumlahTanaman.setText(jumlahTanaman.toString())
            }
        }

        binding.btnTambah.setOnClickListener {
            jumlahTanaman++
            binding.etJumlahTanaman.setText(jumlahTanaman.toString())
        }

        binding.etJumlahTanaman.addTextChangedListener { editable ->
            val input = editable.toString().toIntOrNull()

            if (input != null && input >= 0) {
                jumlahTanaman = input
            } else if (editable.toString().isNotEmpty()) {
                binding.etJumlahTanaman.setText(jumlahTanaman.toString())
            }
        }
    }

    private fun setupAddPlantLogic() {
        binding.btnAddPlant.setOnClickListener {
            val nama = binding.etNamaTanaman.text.toString().trim()
            val jenis = binding.etJenisTanaman.text.toString().trim()
            val media = binding.etMediaTanaman.text.toString().trim()
            val jumlah = binding.etJumlahTanaman.text.toString().toIntOrNull() ?: 0

            if (validateInput(nama, jenis, media, jumlah)) {
                val newPlant = Plant(
                    nama = nama,
                    jenis = jenis,
                    media = media,
                    jumlah = jumlah,
                    hidup = jumlah
                )

                addPlantViewModel.insertPlant(newPlant)
                Toast.makeText(this, "Tanaman berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateInput(nama: String, jenis: String, media: String, jumlah: Int): Boolean {
        return when {
            nama.isEmpty() -> {
                showToast("Nama tanaman tidak boleh kosong!")
                false
            }
            jenis.isEmpty() -> {
                showToast("Jenis tanaman harus dipilih!")
                false
            }
            media.isEmpty() -> {
                showToast("Media tanam harus dipilih!")
                false
            }
            jumlah <= 0 -> {
                showToast("Jumlah tanaman harus lebih dari 0!")
                false
            }
            else -> true
        }
    }

    private fun setupCancelAction() {
        binding.btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Batalkan Penambahan?")
                .setMessage("Apakah Anda yakin ingin membatalkan proses penambahan tanaman?")
                .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Ya") { _, _ -> finish() }
                .show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}