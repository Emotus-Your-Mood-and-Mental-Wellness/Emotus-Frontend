package academy.bangkit.emotusproject.ui.tips

import academy.bangkit.emotusproject.databinding.FragmentTipsBinding
import academy.bangkit.emotusproject.ui.ViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<TipsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dailyTips("user10", "daily")
        viewModel.userResult.observe(viewLifecycleOwner) { tips ->
            binding.satu.text = tips.takeAMomentForYourself
            binding.dua.text = tips.kindReminder
            binding.tiga.text = tips.quickActivity
            binding.empat.text = tips.relaxationExercise
        }
    }
}