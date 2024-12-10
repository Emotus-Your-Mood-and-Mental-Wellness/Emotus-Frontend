package academy.bangkit.emotusproject.ui.home

import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.data.remote.response.MoodResponse
import academy.bangkit.emotusproject.databinding.FragmentHomeBinding
import academy.bangkit.emotusproject.ui.ViewModelFactory
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userId = "user10"
        viewModel.getUser(userId)
        getLiveDataHome()
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


        // Set listener untuk tombol
        binding.saveEntry.setOnClickListener {

        }

        viewModel.userResult.observe(viewLifecycleOwner) { user ->
            user.let {
                if (user.profilePhotoUrl != null) {
                    Glide.with(requireContext())
                        .load(user.profilePhotoUrl)
                        .transform(CircleCrop())
                        .into(binding.photo)
                } else {

                }

            }
            binding.greeting.text = "Hai, ${user.username}!"
        }

        binding.saveEntry.setOnClickListener { button ->
            button.isEnabled = false
            val text: String = binding.input.text.toString()
            val body = MoodResponse(text)
            viewModel.predictMood(userId, body)
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
        binding.a.setOnTouchListener { _, event ->
            // Cek apakah sentuhan berada di luar EditText
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.input.windowToken, 0)
            binding.input.clearFocus()
            true
        }


        viewModel.trendResult.observe(viewLifecycleOwner) { result ->
            result.let {
                binding.angerProgressBar.progress = result.anger.percentage
                binding.angerProgressBarText.text = result.anger.percentage.toString() + "%"
                binding.angerCount.text = result.anger.count.toString()
                binding.sadnessProgressBar.progress = result.sadness.percentage
                binding.sadnessProgressBarText.text = result.sadness.percentage.toString() + "%"
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
                if (entries.data.isEmpty()) {
                    binding.wordSection.text = "Belum Ada Suasana Hati yang Dicatat Hari Ini!"
                    binding.moodStatus.visibility = View.GONE
                } else {
                    binding.wordSection.text ="Mood Hari Ini"
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
        viewModel.summaryResult.observe(viewLifecycleOwner) { summary ->

            if (summary.totalEntries >= 1) {

            }

//            binding.moodRecap.setImageResource(moodIcon(summary.dominantMood))
//            binding.helpfulHint.text = summary.helpfulHint
//            binding.feelInspired.text = summary.feelInspire
        }
        viewModel.isLoadings.observe(viewLifecycleOwner) { its ->
            viewModel.summaryResult.value.let {
                if (viewModel.summaryResult.value!!.totalEntries == 0) {
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
                        binding.helpfulHint.text = "\"${viewModel.summaryResult.value!!.helpfulHint}\""
                        binding.feelInspired.text = "\"${viewModel.summaryResult.value!!.feelInspire}\""
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
        binding.infoButton.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
//                .setPositiveButton("OK") { dialog, which ->
//                    dialog.dismiss()  // Menutup dialog
//                }

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
        viewModel.moodTrends(userId, "daily", "2024-12-06T23:59:59.999Z")
        viewModel.moodSummary(userId, "daily", "2024-12-05T00:00:00.000Z")
        viewModel.moodEntries(userId, "daily", "2024-12-06T23:59:59.999Z")
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