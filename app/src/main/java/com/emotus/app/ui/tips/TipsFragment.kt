package com.emotus.app.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.emotus.app.databinding.FragmentTipsBinding
import com.emotus.app.ui.ViewModelFactory

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
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {

            } else {
                viewModel.dailyTips(user.token, "daily")

                viewModel.userResult.observe(viewLifecycleOwner) { tips ->
                    if (tips.quickActivity != null) {
                        binding.satu.visibility = View.VISIBLE
                        binding.dua.visibility = View.VISIBLE
                        binding.tiga.visibility = View.VISIBLE
                        binding.empat.visibility = View.VISIBLE
                        binding.subtitle1.visibility = View.VISIBLE
                        binding.subtitle2.visibility = View.VISIBLE
                        binding.subtitle3.visibility = View.VISIBLE
                        binding.subtitle4.visibility = View.VISIBLE
                        binding.satu.text = tips.takeAMomentForYourself
                        binding.dua.text = tips.kindReminder
                        binding.tiga.text = tips.quickActivity
                        binding.empat.text = tips.relaxationExercise
                        binding.nulls.visibility = View.GONE
                    } else {
                        binding.nulls.visibility = View.VISIBLE
                        binding.satu.visibility = View.GONE
                        binding.dua.visibility = View.GONE
                        binding.tiga.visibility = View.GONE
                        binding.empat.visibility = View.GONE
                        binding.subtitle1.visibility = View.GONE
                        binding.subtitle2.visibility = View.GONE
                        binding.subtitle3.visibility = View.GONE
                        binding.subtitle4.visibility = View.GONE
                    }

                }
            }
        }

    }
}