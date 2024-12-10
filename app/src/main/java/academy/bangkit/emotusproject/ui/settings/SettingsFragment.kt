package academy.bangkit.emotusproject.ui.settings

import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.data.remote.response.UserAccountBody
import academy.bangkit.emotusproject.databinding.FragmentSettingsBinding
import academy.bangkit.emotusproject.ui.ViewModelFactory
import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
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
    private var userId: String? = null
    private var body: UserAccountBody? = null
    var pass: String? = null
    var isIconClicked = false
    var isIconClickeds = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = FragmentSettingsBinding.inflate(layoutInflater)

        // TODO: Use the ViewModel
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
        viewModel.getUser("user10")

        viewModel.userResult.observe(viewLifecycleOwner) { user ->
            binding.name.setText(user.username)
            binding.userid.setText(user.userId)
            Glide.with(requireContext())
                .load(user.profilePhotoUrl)
                .transform(CircleCrop())
                .into(binding.imageView8)
            pass = user.password
            body = UserAccountBody(user.username, user.password)
            userId = user.userId
        }
        viewModel.messages.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//            binding.textView3.text = it
        }

        binding.textInputLayout3.setEndIconOnClickListener {
            if (!binding.name.isEnabled) {
                binding.textInputLayout3.setEndIconDrawable(R.drawable.round_save_24)
                binding.name.isEnabled = true
            } else {
                Toast.makeText(requireContext(), binding.name.text.toString(), Toast.LENGTH_SHORT)
                    .show()
                viewModel.putAccountt(
                    "user10",
                    UserAccountBody(binding.name.text.toString(), "passsssss")
                )
                binding.textInputLayout3.setEndIconDrawable(R.drawable.edit_24px)
                binding.name.isEnabled = false
            }

        }
//        viewModel.bool.observe(viewLifecycleOwner) {
//
//        }
//        viewModel.updateBool(false)

//        binding.textInputLayout3.setEndIconOnClickListener {
//            if (!binding.userid.isEnabled) {
//                binding.textInputLayout2.setEndIconDrawable(R.drawable.round_save_24)
//                binding.userid.isEnabled = true
//            } else {
//                binding.textInputLayout2.setEndIconDrawable(R.drawable.edit_24px)
//                binding.userid.isEnabled = false
//            }
//
//        }

        binding.button.setOnClickListener {
            if (!isIconClicked) {
                startGallery()
                isIconClicked = true
               binding.button.setImageResource(R.drawable.round_save_24)
            } else {
                isIconClicked = false
                binding.button.setImageResource(R.drawable.edit_24px)
                val fileImage: File = uriToFile(currentImageUri, requireContext())
                val requestImageFile = fileImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    getFileName(currentImageUri, requireContext()),
                    requestImageFile
                )
                viewModel.photo("user10", imageMultipart)
            }


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