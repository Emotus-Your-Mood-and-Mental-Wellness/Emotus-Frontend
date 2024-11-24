package academy.bangkit.emotusproject.ui.monitoring

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.databinding.ActivityMainBinding
import academy.bangkit.emotusproject.databinding.FragmentMonitoringBinding
import android.util.Log
import android.widget.Toast

class MonitoringFragment : Fragment() {

    companion object {
        fun newInstance() = MonitoringFragment()
    }

    private val viewModel: MonitoringViewModel by viewModels()
    private lateinit var binding: FragmentMonitoringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_monitoring, container, false)
    }
}