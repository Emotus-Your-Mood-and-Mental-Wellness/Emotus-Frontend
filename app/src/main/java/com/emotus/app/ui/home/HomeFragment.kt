package com.emotus.app.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.emotus.app.R
import com.emotus.app.data.remote.response.MoodResponse
import com.emotus.app.databinding.FragmentHomeBinding
import com.emotus.app.models.UserModel
import com.emotus.app.ui.ViewModelFactory
import com.emotus.app.ui.signin.SignInActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var token: String = ""

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.getUser(userId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Menutup activity hosting Fragment
            } else {
                token = user.token
                viewModel.getUser(token)
                viewModel.userResult.observe(viewLifecycleOwner) {
                    if (it.profilePhotoUrl != null) {
                        Glide.with(requireContext())
                            .load(it.profilePhotoUrl)
                            .transform(CircleCrop())
                            .into(binding.photo)
                    } else {
                        Glide.with(requireContext())
                            .load("https://artikel.rumah123.com/wp-content/uploads/sites/41/2023/09/12160753/gambar-foto-profil-whatsapp-kosong.jpg")
                            .transform(CircleCrop())
                            .into(binding.photo)
                    }

                    binding.greeting.text = "Hai, ${it.username}!"
                }
                viewModel.moodTrends(token, "daily")
                viewModel.moodSummary(token, "daily")
                viewModel.moodEntries(token, "daily")
                viewModel.trendResult.observe(viewLifecycleOwner) {
                }


                viewModel.trendResult.observe(viewLifecycleOwner) { result ->
                    result.let {
                        binding.angerProgressBar.progress = result.anger.percentage
                        binding.angerProgressBarText.text = result.anger.percentage.toString() + "%"
                        binding.angerCount.text = result.anger.count.toString()
                        binding.sadnessProgressBar.progress = result.sadness.percentage
                        binding.sadnessProgressBarText.text =
                            result.sadness.percentage.toString() + "%"
                        binding.sadnessCount.text = result.sadness.count.toString()
                        binding.happyProgressBar.progress = result.happy.percentage
                        binding.happyProgressBarText.text = result.happy.percentage.toString() + "%"
                        binding.happyCount.text = result.happy.count.toString()
                        binding.fearProgressBar.progress = result.fear.percentage
                        binding.fearProgressBarText.text = result.fear.percentage.toString() + "%"
                        binding.fearCount.text = result.fear.count.toString()
                        binding.loveProgressBar.progress = result.love.percentage
                        binding.loveProgressBarText.text = result.love.percentage.toString() + "%"
                        binding.loveCount.text = result.love.count.toString()
                    }
                }
                viewModel.entriesResult.observe(viewLifecycleOwner) { entries ->
                    entries.let {
                        binding.statusSection.visibility = View.VISIBLE
                        if (entries.data.isEmpty()) {

                            binding.wordSection.text =
                                "Belum Ada Mood yang Dicatat Hari Ini!"
                            binding.moodStatus.visibility = View.GONE
                        } else {
                            binding.wordSection.text = "Mood Hari Ini"
                            binding.moodStatus.visibility = View.VISIBLE
                            binding.moodStatus.setImageResource(
                                moodIcon(
                                    (entries.data.first().predictedMood
                                        ?: "R.drawable.round_close_24).toString()")
                                )
                            )
                        }

                    }
                }
                viewModel.isLoading.observe(viewLifecycleOwner) {
                    if (it) {
                        binding.recapLoading.visibility = View.VISIBLE
                        binding.angerProgressBar.visibility = View.GONE
                        binding.angerProgressBarText.visibility = View.GONE
                        binding.angerCount.visibility = View.GONE
                        binding.sadnessProgressBar.visibility = View.GONE
                        binding.sadnessProgressBarText.visibility = View.GONE
                        binding.sadnessCount.visibility = View.GONE
                        binding.happyProgressBar.visibility = View.GONE
                        binding.happyProgressBarText.visibility = View.GONE
                        binding.happyCount.visibility = View.GONE
                        binding.fearProgressBar.visibility = View.GONE
                        binding.fearProgressBarText.visibility = View.GONE
                        binding.fearCount.visibility = View.GONE
                        binding.loveProgressBar.visibility = View.GONE
                        binding.loveProgressBarText.visibility = View.GONE
                        binding.loveCount.visibility = View.GONE
                    } else {
                        binding.recapLoading.visibility = View.GONE
                        binding.angerProgressBar.visibility = View.VISIBLE
                        binding.angerProgressBarText.visibility = View.VISIBLE
                        binding.angerCount.visibility = View.VISIBLE
                        binding.sadnessProgressBar.visibility = View.VISIBLE
                        binding.sadnessProgressBarText.visibility = View.VISIBLE
                        binding.sadnessCount.visibility = View.VISIBLE
                        binding.happyProgressBar.visibility = View.VISIBLE
                        binding.happyProgressBarText.visibility = View.VISIBLE
                        binding.happyCount.visibility = View.VISIBLE
                        binding.fearProgressBar.visibility = View.VISIBLE
                        binding.fearProgressBarText.visibility = View.VISIBLE
                        binding.fearCount.visibility = View.VISIBLE
                        binding.loveProgressBar.visibility = View.VISIBLE
                        binding.loveProgressBarText.visibility = View.VISIBLE
                        binding.loveCount.visibility = View.VISIBLE
                    }
                }
                viewModel.isLoadings.observe(viewLifecycleOwner) { its ->
                    viewModel.summaryResult.observe(viewLifecycleOwner) { summary ->

                        if (summary.totalEntries == 0) {
                            binding.moodRecap.visibility = View.GONE
                            binding.hintSection.visibility = View.GONE
                            binding.recapSection.visibility = View.GONE
                            binding.inspiredSection.visibility = View.GONE
                            binding.helpfulHintLoading.visibility = View.GONE
                            binding.feelInspiredLoading.visibility = View.GONE
                        } else {
                            if (its) {
                                binding.helpfulHintLoading.visibility = View.VISIBLE
                                binding.feelInspiredLoading.visibility = View.VISIBLE
                                binding.moodRecap.visibility = View.GONE
                                binding.helpfulHint.visibility = View.GONE
                                binding.feelInspired.visibility = View.GONE
                            } else {
                                binding.helpfulHintLoading.visibility = View.GONE
                                binding.feelInspiredLoading.visibility = View.GONE
                                binding.moodRecap.setImageResource(
                                    moodIcon(
                                        viewModel.summaryResult.value?.dominantMood
                                            ?: ""
                                    )
                                )
                                binding.helpfulHint.text =
                                    "\"${viewModel.summaryResult.value!!.helpfulHint}\""
                                binding.feelInspired.text =
                                    "\"${viewModel.summaryResult.value!!.feelInspire}\""
                                binding.recapSection.visibility = View.VISIBLE
                                binding.moodRecap.visibility = View.VISIBLE
                                binding.hintSection.visibility = View.VISIBLE
                                binding.inspiredSection.visibility = View.VISIBLE
                                binding.helpfulHint.visibility = View.VISIBLE
                                binding.feelInspired.visibility = View.VISIBLE
                            }
                        }
                    }


                }


            }
        }

        binding.saveEntry.setOnClickListener { button ->
            button.isEnabled = false
            val text: String = binding.input.text.toString()
            val body = MoodResponse(text)
            viewModel.predictMood(token, body)
            viewModel.result.observe(viewLifecycleOwner) { result ->
                result.let {

                    binding.sympathyMessage.text = result.sympathyMessage
                    binding.thoughtfulSuggestions1.text = result.thoughtfulSuggestions[0]
                    binding.thoughtfulSuggestions2.text = result.thoughtfulSuggestions[1]
                    binding.thingsToDo1.text = result.thingsToDo[0]
                    binding.thingsToDo2.text = result.thingsToDo[1]
                    binding.moodResult.setImageResource(moodIcon(result.predictedMood))
                    getLiveDataHome()
                    binding.resultSection.visibility = View.VISIBLE
                    button.isEnabled = true
//                    binding.a.post {
//                        binding.a.smoothScrollTo(0, binding.resultSection.bottom)
//                    }
                }
            }

        }
        binding.a.setOnTouchListener { _, _ ->
            // Cek apakah sentuhan berada di luar EditText
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.input.windowToken, 0)
            binding.input.clearFocus()
            true
        }

        if (token != "") {

        }








        binding.infoButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()  // Menutup dialog
                }

            val alertDialog = dialogBuilder.create()

            alertDialog.window?.setBackgroundDrawableResource(R.drawable.inset)
            alertDialog.window?.setWindowAnimations(R.style.DialogFadeIn)
            alertDialog.show()
        }
//        viewModel.messages.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
//            binding.textView.text = it.toString()
//        }
//        viewModel.messagess.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
//
//        }
    }

    //    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
    private fun getLiveDataHome() {
        viewModel.moodTrends(token, "daily")
        viewModel.moodSummary(token, "daily")
        viewModel.moodEntries(token, "daily")
    }

    private fun moodIcon(mood: String): Int {
        return when (mood) {
            "Anger" -> R.drawable.sentiment_extremely_dissatisfied_40px
            "Sadness" -> R.drawable.sentiment_sad_40px
            "Happy" -> R.drawable.sentiment_excited_40px
            "Fear" -> R.drawable.sentiment_worried_40px
            "Love" -> R.drawable.sentiment_very_satisfied_40px
            else -> R.drawable.round_close_24
        }
    }

}