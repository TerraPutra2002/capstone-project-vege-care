package com.example.vegecare.ui.home.addplant

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.vegecare.R
import com.example.vegecare.databinding.ActivityAddPlantBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddPlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dropdown Jenis Tanaman
        val jenisTanamanArray = resources.getStringArray(R.array.daftar_jenis_tanaman)
        Log.d("AddPlantActivity", "Jenis Tanaman Array: ${jenisTanamanArray.joinToString()}")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jenisTanamanArray)
        Log.d("AddPlantActivity", "Adapter created: ${adapter.count} items")

        binding.etJenisTanaman.setAdapter(adapter)
        binding.etJenisTanaman.setOnClickListener {
            binding.etJenisTanaman.showDropDown()
        }
        binding.etJenisTanaman.inputType = 0

        binding.etJenisTanaman.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.d("AddPlantActivity", "selectedItem: $selectedItem")
        }


        //Dropdown Media Tanam
        val mediaTanamArray = resources.getStringArray(R.array.daftar_media_tanam)
        Log.d("AddPlantActivity", "Media Tanam Array: ${mediaTanamArray.joinToString()}")

        val mediaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mediaTanamArray)
        Log.d("AddPlantActivity", "Adapter created: ${mediaAdapter.count} items")

        binding.etMediaTanaman.setAdapter(mediaAdapter)
        binding.etMediaTanaman.setOnClickListener {
            binding.etMediaTanaman.showDropDown()
        }
        binding.etMediaTanaman.inputType = 0

        binding.etMediaTanaman.setOnItemClickListener { parent, view, position,id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            Log.d("AddPlantActivity", "selectedItem: $selectedItem")
        }


        //Jumlah Tanaman
        binding.apply {
            var jumlahTanaman = 0

            etJumlahTanaman.setText(jumlahTanaman.toString())

            btnKurang.setOnClickListener {
                if (jumlahTanaman > 0) {
                    jumlahTanaman--
                    etJumlahTanaman.setText(jumlahTanaman.toString())
                }
            }

            btnTambah.setOnClickListener {
                jumlahTanaman++
                etJumlahTanaman.setText(jumlahTanaman.toString())
            }

            etJumlahTanaman.addTextChangedListener {
                val input = it.toString().toIntOrNull()
                if (input != null && input >= 0) {
                    jumlahTanaman = input
                } else if (input == null) {
                    etJumlahTanaman.setText(jumlahTanaman.toString())
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Batalkan Penambahan?")
                .setMessage("Apakah Anda yakin ingin membatalkan proses penambahan tanaman?")
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { _, _ ->
                    finish()
                }
                .show()
        }
    }
}