package com.example.vegecare.ui.detect

import android.graphics.BitmapFactory
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.vegecare.BuildConfig
import com.example.vegecare.databinding.ActivityPlantDetectionBinding
import com.example.vegecare.ui.detect.data.response.PlantDeseaseResponse
import com.example.vegecare.ui.detect.data.retrofit.PlantApiConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class PlantDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantDetectionBinding
    private var selectedImageFile: File? = null
    private var currentPhotoPath: String? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToCamera.setOnClickListener { openCamera() }
        binding.btnToGalery.setOnClickListener { openGallery() }

        // Upload
        binding.btnUploadPhoto.setOnClickListener {
            selectedImageFile?.let { file ->
                uploadImage(file)
            } ?: Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val photoFile: File = createCustomTempFile(this)
        photoUri = FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            photoFile
        )
        currentPhotoPath = photoFile.absolutePath

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        launcherCamera.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcherGallery.launch(intent)
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let {
                val file = File(it)
                val rotatedBitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
                selectedImageFile = file.reduceFileImage() // Reduksi ukuran gambar jika diperlukan
                // Tampilkan gambar di ImageView
                binding.imgPhotoPlant.setImageBitmap(rotatedBitmap)
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImageUri, this)
            val bitmap = BitmapFactory.decodeFile(file.path)
            selectedImageFile = file.reduceFileImage() // Reduksi ukuran gambar jika diperlukan
            // Tampilkan gambar di ImageView
            binding.imgPhotoPlant.setImageBitmap(bitmap)
        }
    }

    private fun uploadImage(file: File) {
        // Buat request body untuk file gambar
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData("image", file.name, requestImageFile)

        // Panggil API untuk mendeteksi penyakit tanaman
        val apiService = PlantApiConfig.getApiService()
        apiService.getPlantDiseasePrediction(imageMultipart)
            .enqueue(object : retrofit2.Callback<PlantDeseaseResponse> {
                override fun onResponse(
                    call: Call<PlantDeseaseResponse>,
                    response: Response<PlantDeseaseResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        binding.classs.text =
                            result?.diseasePrediction?.jsonMemberClass ?: "Tidak diketahui"
                        binding.confidence.text = result?.diseasePrediction?.confidence.toString()
                        binding.generalCare.text = result?.plantCare?.generalCare ?: "-"
                        binding.treatment.text = result?.plantCare?.treatment ?: "-"
                        binding.prevention.text = result?.plantCare?.prevention ?: "-"
                    } else {
                        Toast.makeText(
                            this@PlantDetectionActivity,
                            "Gagal: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PlantDeseaseResponse>, t: Throwable) {
                    Toast.makeText(
                        this@PlantDetectionActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
