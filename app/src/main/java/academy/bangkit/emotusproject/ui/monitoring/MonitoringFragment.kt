package academy.bangkit.emotusproject.ui.monitoring

import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.Utils.Date
import academy.bangkit.emotusproject.databinding.FragmentMonitoringBinding
import academy.bangkit.emotusproject.di.Injection
import academy.bangkit.emotusproject.ui.MoodsAdapter
import academy.bangkit.emotusproject.ui.ViewModelFactory
import android.annotation.SuppressLint
import android.app.ProgressDialog.show
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import okhttp3.internal.notify
import java.time.LocalTime
import java.time.format.DateTimeFormatter

//import kotlin.collections.EmptyMap.entries

//import kotlin.collections.EmptyMap.entries

//import kotlin.collections.EmptyMap.entries

//import kotlin.collections.EmptyMap.entries


class MonitoringFragment : Fragment() {

    private var _binding: FragmentMonitoringBinding? = null
    lateinit var moodsAdapter: MoodsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val date: Date = Injection.provideDate()
    private val viewModel by viewModels<MonitoringViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitoringBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barChart = binding.chart
        val entries = ArrayList<Entry>()
        moodsAdapter = MoodsAdapter()

        moodsAdapter.onDeleteClickListener = { mood, position ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Apakah Anda yakin ingin menghapus pilihan?")
                .setPositiveButton("Ya") { dialog, id ->
                    // Aksi yang dilakukan jika tombol "Ya" ditekan
                    viewModel.deleteMood(mood.id, mood.userId)
                    moodsAdapter.removeItem(position)
                }
                .setNegativeButton("Tidak") { dialog, id ->
                    // Aksi yang dilakukan jika tombol "Tidak" ditekan
                    dialog.dismiss() // Menutup dialog tanpa melakukan apa-apa
                }

            // Menampilkan dialog
            builder.create().show()



//            viewModel.updateEntries(listOf())
////            viewModel.moodEntries("user10", "daily", "")
//            barChart.data = null
//        // Menghapus semua data dan reset chart
////            barChart.data = null   // Pastikan data direset menjadi null
//            barChart.invalidate()  // Refresh chart setelah reset

//            viewModel.moodEntries("user10", "daily", "")
//            viewModel.moodTrends("user10", "daily", "2024-12-06T23:59:59.999Z")
//
//            viewModel.moodSummary("user10", "daily", "")

        }

        binding.infoGraph.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_graph, null)
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
        var dini: MutableList<String> = mutableListOf()
        var awal: MutableList<String> = mutableListOf()
        var pagi: MutableList<String> = mutableListOf()
        var siang: MutableList<String> = mutableListOf()
        var sore: MutableList<String> = mutableListOf()
        var malam: MutableList<String> = mutableListOf()
        viewModel.moodEntries("user10", "daily", "")
        viewModel.entriesResult.observe(viewLifecycleOwner) { entriess ->
            dini= mutableListOf()
            awal= mutableListOf()
            pagi= mutableListOf()
            siang= mutableListOf()
            sore = mutableListOf()
            malam= mutableListOf()
            if (entriess.isNotEmpty()) {
                binding.nullGraph.visibility = View.GONE
                binding.nullHistory.visibility = View.GONE
                binding.nullReflection.visibility = View.GONE
//            Toast.makeText(requireContext(), "entries.toString()" , Toast.LENGTH_SHORT).show()
                if (entries.size != 0) {
//                entries.removeAll(entries)
                    entries.removeAll(entries)
//                barChart.invalidate()
//                val dataSet2 = LineDataSet(entries, "Dataset 2")
////                entries.notify()
//                barChart.data.notifyDataChanged()
//                barChart.data.dataSets.clear()
//                barChart.data.addDataSet(dataSet2)
//                barChart.notifyDataSetChanged()
//                val xAxis = barChart.xAxis
//                xAxis.axisMinimum = entries.first().x // Batas awal sesuai data pertama
//                xAxis.axisMaximum = entries.last().x
//                barChart.data.clearValues()
                    barChart.invalidate()
                }

                if (entries.size == 0) {
                    entriess.let {

                        binding.textView.text = entriess.toString()
                        moodsAdapter.submitList(entriess)
                        binding.recycle.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = moodsAdapter
                        }


                        entriess.asReversed().forEach {
                            val timeParts = date.formatDatesTime(it.createdAt).second
                            val time = LocalTime.parse(
                                timeParts,
                                DateTimeFormatter.ofPattern("HH:mm:ss")
                            )

                            val startTime = LocalTime.of(0, 0)  // 00:00:00
                            val endTime = LocalTime.of(4, 0)
                            val awalS = LocalTime.of(4, 0)  // 00:00:00
                            val awalE = LocalTime.of(8, 0)
                            val pagiS = LocalTime.of(8, 0)  // 00:00:00
                            val pagiE = LocalTime.of(12, 0)
                            val siangS = LocalTime.of(12, 0)  // 00:00:00
                            val siangE = LocalTime.of(16, 0)
                            val soreS = LocalTime.of(16, 0)  // 00:00:00
                            val soreE = LocalTime.of(20, 0)
                            val malamS = LocalTime.of(20, 0)  // 00:00:00
                            val malamE = LocalTime.of(23, 59)
//                    Toast.makeText(requireContext(), "enam.s()", Toast.LENGTH_SHORT).show()
                            if (time in startTime..endTime) {
                                dini.add(it.predictedMood)
                            } else if (time in awalS..awalE) {
                                awal.add(it.predictedMood)
                            } else if (time in pagiS..pagiE) {

                                pagi.add(it.predictedMood)
                            } else if (time in siangS..siangE) {
                                siang.add(it.predictedMood)
                            } else if (time in soreS..soreE) {
                                sore.add(it.predictedMood)
                            } else if (time in malamS..malamE) {
                                malam.add(it.predictedMood)
                            }

                        }
                        val a = dini.size > 1
                        val b = awal.size > 1
                        val c = pagi.size > 1
                        val d = siang.size > 1
                        val e = sore.size > 1
                        val f = malam.size > 1
                        val ee = dini.size == 1
                        val aee = awal.size == 1
                        val aeee = pagi.size == 1
                        val aeeee = siang.size == 1
                        val aeeeee = sore.size == 1
                        val aeeeeee = malam.size == 1
                        val trueCount =
                            listOf(
                                a,
                                b,
                                c,
                                d,
                                e,
                                f,
                                ee,
                                aeeeeee,
                                aeeeee,
                                aeeee,
                                aee,
                                aeee
                            ).count { it }
                        if (dini.size > 1) {

                            val tiga: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    0f,
                                    1f,
                                    dini
                                ) else normalizeDataWithEqualSpacing(0f, 1f, dini)
                            tiga.forEachIndexed { index, value ->
                                val mood =
                                    mood(dini[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (dini.size == 1) {
                            entries.add(
                                Entry(
                                    0f,
                                    mood(dini.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
                        if (awal.size > 1) {

                            val tiga: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    1f,
                                    2f,
                                    awal
                                ) else normalizeDataWithEqualSpacing(1f, 2f, awal)
                            tiga.forEachIndexed { index, value ->
                                val mood =
                                    mood(awal[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (awal.size == 1) {
                            entries.add(
                                Entry(
                                    1f,
                                    mood(awal.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
                        if (pagi.size > 1) {

                            val tiga: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    2f,
                                    3f,
                                    pagi
                                ) else normalizeDataWithEqualSpacing(2f, 3f, pagi)
//                    Toast.makeText(requireContext(), tiga.toString(), Toast.LENGTH_SHORT).show()
                            tiga.forEachIndexed { index, value ->
                                val mood =
                                    mood(pagi[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (pagi.size == 1) {
                            entries.add(
                                Entry(
                                    2f,
                                    mood(pagi.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
                        if (siang.size > 1) {

                            val empat: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    3f,
                                    4f,
                                    siang
                                ) else normalizeDataWithEqualSpacing(3f, 4f, siang)
                            empat.forEachIndexed { index, value ->
                                val mood =
                                    mood(siang[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (siang.size == 1) {
//                        Toast.makeText(requireContext(), "siang", Toast.LENGTH_SHORT).show()
                            entries.add(
                                Entry(
                                    3f,
                                    mood(siang.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
                        if (sore.size > 1) {
                            val lima: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    4f,
                                    5f,
                                    sore
                                ) else normalizeDataWithEqualSpacing(4f, 5f, sore)
                            lima.forEachIndexed { index, value ->
                                val mood =
                                    mood(sore[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (sore.size == 1) {
                            entries.add(
                                Entry(
                                    4f,
                                    mood(sore.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
                        if (malam.size > 1) {
                            val enam: List<Float> =
                                if (trueCount == 1) normalizeDataWithEqualSpacings(
                                    5f,
                                    6f,
                                    malam
                                ) else normalizeDataWithEqualSpacing(5f, 6f, malam)
//                    Toast.makeText(requireContext(), enam.toString(), Toast.LENGTH_SHORT).show()
                            enam.forEachIndexed { index, value ->
                                val mood =
                                    mood(malam[index]) // Ambil elemen dari dini berdasarkan indeks
                                entries.add(Entry(value, mood)) // Tambahkan ke entries
                            }
                        } else if (malam.size == 1) {
                            entries.add(
                                Entry(
                                    5f,
                                    mood(malam.first())
                                )
                            )  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
                        }
//                Toast.makeText(requireContext(), pagi.toString(), Toast.LENGTH_SHORT).show()
//                Toast.makeText(requireContext(), siang.toString(), Toast.LENGTH_SHORT).show()
//                entries.add(Entry(0f, 3f))  // Dini hari: 00:00 - 04:00, Mood: 3 (Sedang)
////        entries.add(Entry(0.5f, 2f))
//                entries.add(Entry(1f, 4f))  // Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
//                entries.add(Entry(2f, 2f))  // Pagi: 08:00 - 12:00, Mood: 2 (Buruk)
//                entries.add(Entry(3f, 5f))  // Siang: 12:00 - 16:00, Mood: 5 (Sangat Baik)
//                entries.add(Entry(4f, 1f))  // Sore: 16:00 - 20:00, Mood: 1 (Sangat Buruk)
//                entries.add(Entry(5f, 3f))  // Malam: 20:00 - 24:00, Mood: 3 (Sedang)
                        val lineDataSet = LineDataSet(entries, "Mood Sepanjang Hari")
                        lineDataSet.colors =
                            ColorTemplate.MATERIAL_COLORS.toList()  // Atur warna garis

                        // Menambahkan titik data ke LineData
                        val lineData = LineData(lineDataSet)

                        // Menampilkan data ke chart
                        barChart.data = lineData

                        // Mengatur sumbu X (Waktu)
                        val xAxis = barChart.xAxis
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.setGranularity(1f)
                        xAxis.valueFormatter =
                            object :
                                com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return when (value.toInt()) {
                                        0 -> "Dini Hari"
                                        1 -> "Pagi Awal"
                                        2 -> "Pagi"
                                        3 -> "Siang"
                                        4 -> "Sore"
                                        5 -> "Malam"
                                        else -> ""
                                    }
                                }
                            }

                        // Mengatur sumbu Y (Mood)
                        val yAxis = barChart.axisLeft
                        yAxis.axisMinimum = 1f  // Mood minimum (Sangat Buruk)
                        yAxis.axisMaximum = 5f  // Mood maksimum (Sangat Baik)
                        yAxis.granularity = 1f  // Interval antar nilai (selalu 1)
                        yAxis.labelCount = 5  // Menampilkan 5 label (1, 2, 3, 4, 5)

// Formatter untuk memastikan label adalah angka bulat
                        yAxis.valueFormatter =
                            object : com.github.mikephil.charting.formatter.ValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return value.toInt()
                                        .toString() // Mengubah label ke bilangan bulat
                                }
                            }
                        // Menambahkan Gridline di sumbu Y
                        barChart.axisRight.isEnabled = false  // Menonaktifkan sumbu kanan

                        // Menampilkan LineChart
                        barChart.invalidate()
                    }
                }

            } else {
                binding.nullGraph.visibility = View.VISIBLE
                binding.nullHistory.visibility = View.VISIBLE
                binding.nullReflection.visibility = View.VISIBLE
                binding.c.visibility = View.GONE
                binding.resultSection.visibility = View.GONE
                binding.recycle.visibility = View.GONE
            }

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

        // programmatically create a LineChart


        // Membuat data untuk BarChart
//        val entries = ArrayList<BarEntry>()
//        entries.add(BarEntry(0f, 3f))  // Dini hari: 00:00 - 04:00, Mood: 3 (misalnya "Sedang")
//        entries.add(BarEntry(1f, 4f))  // Pagi awal: 04:00 - 08:00, Mood: 4 (misalnya "Baik")
//        entries.add(BarEntry(2f, 2f))  // Pagi: 08:00 - 12:00, Mood: 2 (misalnya "Buruk")
//        entries.add(BarEntry(3f, 5f))  // Siang: 12:00 - 16:00, Mood: 5 (misalnya "Sangat Baik")
//        entries.add(BarEntry(4f, 1f))  // Sore: 16:00 - 20:00, Mood: 1 (misalnya "Sangat Buruk")
//        entries.add(BarEntry(5f, 3f))  // Malam: 20:00 - 24:00, Mood: 3 (misalnya "Sedang")
//
//        // Membuat BarDataSet dari data yang sudah ada
//        val barDataSet = BarDataSet(entries, "Mood Sepanjang Hari")
//        barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()  // Atur warna bar
//
//        // Membuat objek BarData
//        val barData = BarData(barDataSet)
//
//        // Menampilkan data ke chart
//        barChart.data = barData
//
//        // Mengatur sumbu X (Waktu)
//        val xAxis = barChart.xAxis
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.setGranularity(1f)
//        xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                return when (value.toInt()) {
//                    0 -> "Dini Hari"
//                    1 -> "Pagi Awal"
//                    2 -> "Pagi"
//                    3 -> "Siang"
//                    4 -> "Sore"
//                    5 -> "Malam"
//                    else -> ""
//                }
//            }
//        }
//
//        // Mengatur sumbu Y (Mood)
//        val yAxis = barChart.axisLeft
//        yAxis.axisMinimum = 0f  // Minimum mood level adalah 0
//        yAxis.axisMaximum = 5f  // Maksimum mood level adalah 5 (Sangat Baik)
//
//        // Menambahkan Gridline di sumbu Y
//        barChart.axisRight.isEnabled = false  // Menonaktifkan sumbu kanan
//
//        // Menampilkan bar chart
//        barChart.invalidate()

//        for (YourData data : dataObjects) {
//            // turn your data into Entry objects
//            entriess.ad add(new Entry(data.getValueX(), data.getValueY()));
//        }

//        val dua = normalizeDataWithEqualSpacing(1, 2, awal)
//        val tiga = normalizeDataWithEqualSpacing(2, 3, pagi)
//        val empat = normalizeDataWithEqualSpacing(3, 4, siang)
//        val lima = normalizeDataWithEqualSpacing(4, 5, sore)
//        val enam = normalizeDataWithEqualSpacing(5, 6, malam)


//        if (malam.size > 1) {


//        }
//
//        dua.forEach {
//            entries.add(Entry(it, 2f))
//        }
//        tiga.forEach {
//            entries.add(Entry(it, 3f))
//        }
//        empat.forEach {
//            entries.add(Entry(it, 4f))
//        }
//        lima.forEach {
//            entries.add(Entry(it, 5f))
//        }
//        enam.forEach {
//            entries.add(Entry(it, 3f))
//        }


        // Membuat LineDataSet dari data yang sudah ada

        viewModel.moodTrends("user10", "daily", "2024-12-06T23:59:59.999Z")

        viewModel.moodSummary("user10", "daily", "")
        viewModel.summaryResult.observe(viewLifecycleOwner) { result ->
            result.let {
                if (result != null) {
                    binding.sympathyMessage.text = result.sympathyMessage
                    binding.thoughtfulSuggestions1.text = result.thoughtfulSuggestions[0]
                    binding.thoughtfulSuggestions2.text = result.thoughtfulSuggestions[1]
                    binding.thingsToDo1.text = result.thingsToDo[0]
                    binding.thingsToDo2.text = result.thingsToDo[1]
                    binding.moodResult.setImageResource(moodIcon(result.dominantMood))
                }
            }
        }
        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    academy.bangkit.emotusproject.R.id.button1 -> {
                        viewModel.moodTrends("user10", "daily", "2024-12-06T23:59:59.999Z")
                        viewModel.moodEntries("user10", "daily", "")
                    }

                    academy.bangkit.emotusproject.R.id.button2 -> {
                        viewModel.moodTrends("404", "weekly", "2024-12-06T23:59:59.999Z")
                        viewModel.moodEntries("404", "weekly", "")
                    }

                    academy.bangkit.emotusproject.R.id.button3 -> {
                        viewModel.moodTrends("404", "monthly", "2024-12-06T23:59:59.999Z")
                        viewModel.moodEntries("404", "monthly", "")
                    }
                }
            } else {
                // Handle button uncheck if needed
                Log.d("ToggleButton", "Button with ID $checkedId was unchecked.")
            }
        }
    }

    private fun normalizeDataWithEqualSpacing(
        min: Float,
        max: Float,
        data: List<String>
    ): List<Float> {
        // Pastikan ada minimal 2 elemen, karena jarak membutuhkan minimal dua titik
        if (data.size < 2) {
            throw IllegalArgumentException("Data harus memiliki minimal 2 elemen untuk perhitungan.")
        }

        // Hitung jarak antar data (step), nilai terakhir akan berada sebelum max
        val step = (max - min) / data.size

        // Buat daftar posisi normalisasi
        return data.indices.map { index -> min + (step * index) }
    }

    fun normalizeDataWithEqualSpacings(min: Float, max: Float, data: List<String>): List<Float> {
        // Pastikan minimal ada 2 data, karena jarak antar data membutuhkan minimal dua titik
        if (data.size < 2) {
            throw IllegalArgumentException("Data harus memiliki minimal 2 elemen untuk perhitungan jarak.")
        }

        // Hitung jarak antar data berdasarkan rentang (max - min) dan jumlah elemen data
        val step = (max - min) / (data.size - 1)

        // Buat daftar untuk menyimpan posisi normalisasi
        return data.indices.map { index -> min + (step * index) }
    }

    fun main() {
        val data = listOf("A", "B", "C", "D") // Contoh data
        val min = 0f
        val max = 1f

        // Hitung posisi normalisasi
        val normalizedPositions = normalizeDataWithEqualSpacing(min, max, data)

        // Cetak hasil
        println("Posisi normalisasi: $normalizedPositions")
    }

    private fun mood(mood: String): Float {
        return when (mood) {
            "Anger" -> 1f
            "Sadness" -> 2f
            "Happy" -> 5f
            "Fear" -> 3f
            "Love" -> 4f
            else -> 0f
        }
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
