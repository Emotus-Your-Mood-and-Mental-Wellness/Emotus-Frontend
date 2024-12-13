package com.emotus.app.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.emotus.app.R
import com.emotus.app.data.remote.response.Update
import com.emotus.app.databinding.FragmentSettingsBinding
import com.emotus.app.ui.ViewModelFactory
import com.emotus.app.ui.signin.SignInActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var currentImageUri: Uri? = null
    private var token: String? = null

    var isIconClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                token = user.token
                viewModel.getUser(user.token)
                viewModel.userResult.observe(viewLifecycleOwner) { users ->
                    binding.name.setText(users.username)
                    binding.email.setText(users.email)
//                    binding.userid.setText(users.userId)
                    if (users.profilePhotoUrl != null) {
                        Glide.with(requireContext())
                            .load(users.profilePhotoUrl)
                            .transform(CircleCrop())
                            .into(binding.imageView8)
                    } else {
                        Glide.with(requireContext())
                            .load("https://artikel.rumah123.com/wp-content/uploads/sites/41/2023/09/12160753/gambar-foto-profil-whatsapp-kosong.jpg")
                            .transform(CircleCrop())
                            .into(binding.imageView8)
                    }

                }
                binding.button.setOnClickListener {
                    if (!isIconClicked) {
                        startGallery()
                        isIconClicked = true
                        binding.button.setImageResource(R.drawable.round_save_24)
                    } else {
                        isIconClicked = false
                        binding.button.setImageResource(R.drawable.edit_24px)
                        val fileImage: File = uriToFile(currentImageUri, requireContext())
                        val requestImageFile =
                            fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "photo",
                            getFileName(currentImageUri, requireContext()),
                            requestImageFile
                        )
                        viewModel.photo(user.token, imageMultipart)
                        val builder = AlertDialog.Builder(requireContext())
                            .setMessage("Foto Profil Berhasil Diganti!")
                            .setCancelable(true)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alertDialog = builder.create()
                        alertDialog.show()
                    }


                }
                binding.delete.setOnClickListener {
                    val fileImage: File = File.createTempFile("empty", ".jpg")
                    val requestImageFile =
                        fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        "null.jpg",
                        requestImageFile
                    )
                        viewModel.photo(user.token, imageMultipart)
//                        val builder = AlertDialog.Builder(requireContext())
//                        builder.setMessage("Apakah Anda yakin ingin menghapus pilihan?")
//                            .setPositiveButton("Ya") { _, _ ->
//                            }
//                            .setNegativeButton("Tidak") { dialog, _ ->
//                                dialog.dismiss()
//                            }
//                        builder.create().show()
//                    }


                }
            }
        }


        binding.nameLayout.setEndIconOnClickListener {
            if (!binding.name.isEnabled) {
                binding.nameLayout.setEndIconDrawable(R.drawable.round_save_24)
                binding.name.isEnabled = true
            } else {
                viewModel.putAccount(
                    token!!, Update(binding.email.text.toString(), binding.name.text.toString())
                )
                binding.nameLayout.setEndIconDrawable(R.drawable.edit_24px)
                binding.name.isEnabled = false
                val builder = AlertDialog.Builder(requireContext())
                    .setMessage("Nama Berhasil Diganti!")
                    .setCancelable(true)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }

        binding.emailLayout.setEndIconOnClickListener {
            if (!binding.email.isEnabled) {
                binding.emailLayout.setEndIconDrawable(R.drawable.round_save_24)
                binding.email.isEnabled = true
            } else {
                viewModel.putAccount(
                    token!!, Update(binding.email.text.toString(), binding.name.text.toString())
                )
                binding.emailLayout.setEndIconDrawable(R.drawable.edit_24px)
                binding.email.isEnabled = false
                val builder = AlertDialog.Builder(requireContext())
                    .setMessage("Alamat Email Berhasil Diganti!")
                    .setCancelable(true)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        }
        binding.logout.setOnClickListener {
            viewModel.logout()
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            isIconClicked = false
            binding.button.setImageResource(R.drawable.edit_24px)
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            Glide.with(requireContext())
                .load(it)
                .transform(CircleCrop())
                .into(binding.imageView8)

        }
    }

    fun uriToFile(uri: Uri?, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp", null, context.cacheDir)
        contentResolver.openInputStream(uri!!)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }

    fun getFileName(uri: Uri?, context: Context): String? {
        var fileName: String? = null
        if (uri!!.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (fileName == null) {
            fileName = uri.path?.let {
                val cut = it.lastIndexOf('/')
                if (cut != -1) it.substring(cut + 1) else it
            }
        }
        return fileName
    }
}