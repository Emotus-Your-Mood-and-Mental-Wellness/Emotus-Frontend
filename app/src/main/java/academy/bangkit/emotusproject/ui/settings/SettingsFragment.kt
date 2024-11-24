package academy.bangkit.emotusproject.ui.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.databinding.ActivityMainBinding
import academy.bangkit.emotusproject.databinding.FragmentSettingsBinding
import android.util.TypedValue
import android.widget.Button

class SettingsFragment : Fragment() {
    private lateinit var rectangleView: View

    private lateinit var binding: FragmentSettingsBinding

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


}