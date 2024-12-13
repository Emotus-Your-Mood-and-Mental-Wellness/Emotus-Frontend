package com.emotus.app.ui.monitoring

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.emotus.app.R
import com.emotus.app.data.remote.response.DataItem
import com.emotus.app.databinding.FragmentMonitoringBinding
import com.emotus.app.di.Injection
import com.emotus.app.ui.MoodsAdapter
import com.emotus.app.ui.ViewModelFactory
import com.emotus.app.utils.Date
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


class MonitoringFragment : Fragment() {

    private var _binding: FragmentMonitoringBinding? = null
    private lateinit var moodsAdapter: MoodsAdapter

    private val binding get() = _binding!!

    private val date: Date = Injection.provideDate()
    private lateinit var period: String
    private val viewModel by viewModels<MonitoringViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        period = "daily"

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
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {

            }
            viewModel.moodTrends(user.token, "daily")
            viewModel.moodEntries(user.token, "daily")
            viewModel.moodSummary(user.token, "daily")
            moodsAdapter.onDeleteClickListener = { mood, position ->
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Apakah Anda yakin ingin menghapus pilihan?")
                    .setPositiveButton("Ya") { _, _ ->
                        viewModel.deleteMood(entryId = mood.id, token = user.token)
                        moodsAdapter.removeItem(position)
                    }
                    .setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create().show()


            }

            binding.infoGraph.setOnClickListener {
                val dialogView = layoutInflater.inflate(R.layout.dialog_graph, null)
                val dialogBuilder = AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .setCancelable(true)


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
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()  // Menutup dialog
                    }

                val alertDialog = dialogBuilder.create()

                alertDialog.window?.setBackgroundDrawableResource(R.drawable.inset)
                alertDialog.window?.setWindowAnimations(R.style.DialogFadeIn)
                alertDialog.show()
            }
            var dini: MutableList<String>
            var awal: MutableList<String>
            var pagi: MutableList<String>
            var siang: MutableList<String>
            var sore: MutableList<String>
            var malam: MutableList<String>

            viewModel.entriesResult.observe(viewLifecycleOwner) { entriess ->
                dini = mutableListOf()
                awal = mutableListOf()
                pagi = mutableListOf()
                siang = mutableListOf()
                sore = mutableListOf()
                malam = mutableListOf()
                if (entriess.isNotEmpty()) {
                    binding.nullGraph.visibility = View.GONE
                    binding.nullHistory.visibility = View.GONE
                    binding.nullReflection.visibility = View.GONE
                    if (entries.size != 0) {
                        entries.removeAll(entries)
                        barChart.invalidate()
                    }

                    if (entries.size == 0) {
                        entriess.let {

                            moodsAdapter.submitList(entriess)
                            binding.recycle.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = moodsAdapter
                            }

                            if (period == "daily") {
                                barChart.visibility = View.VISIBLE
                                binding.bchart.visibility = View.GONE
                                entriess.asReversed().forEach {
                                    val timeParts = formatDatesTime(it.createdAt).second
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
                                    )
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
                                    entries.add(
                                        Entry(
                                            3f,
                                            mood(siang.first())
                                        )
                                    )
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
                                    )
                                }
                                if (malam.size > 1) {
                                    val enam: List<Float> =
                                        if (trueCount == 1) normalizeDataWithEqualSpacings(
                                            5f,
                                            6f,
                                            malam
                                        ) else normalizeDataWithEqualSpacing(5f, 6f, malam)
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
                                    )
                                }
//pagi.toString(), Toast.LENGTH_SHORT).show()
//siang.toString(), Toast.LENGTH_SHORT).show()
// Dini hari: 00:00 - 04:00, Mood: 3 (Sedang)
// Pagi awal: 04:00 - 08:00, Mood: 4 (Baik)
// Pagi: 08:00 - 12:00, Mood: 2 (Buruk)
// Siang: 12:00 - 16:00, Mood: 5 (Sangat Baik)
// Sore: 16:00 - 20:00, Mood: 1 (Sangat Buruk)
// Malam: 20:00 - 24:00, Mood: 3 (Sedang)
                                val lineDataSet = LineDataSet(entries, "Mood Sepanjang Hari")
                                lineDataSet.colors =
                                    ColorTemplate.MATERIAL_COLORS.toList()

                                val lineData = LineData(lineDataSet)

                                barChart.data = lineData

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

                                val yAxis = barChart.axisLeft
                                yAxis.axisMinimum = 0f  // Mood minimum (Sangat Buruk)
                                yAxis.axisMaximum = 5f  // Mood maksimum (Sangat Baik)
                                yAxis.granularity = 1f  // Interval antar nilai (selalu 1)
                                yAxis.labelCount = 5  // Menampilkan 5 label (1, 2, 3, 4, 5)

                                yAxis.valueFormatter =
                                    object :
                                        com.github.mikephil.charting.formatter.ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            return value.toInt()
                                                .toString() // Mengubah label ke bilangan bulat
                                        }
                                    }

                                barChart.axisRight.isEnabled = false  // Menonaktifkan sumbu kanan
                                barChart.description.isEnabled = false
                                barChart.invalidate()

                            } else if (period == "weekly") {
                                val A = getMostFrequentPredictedMoodPerDay(entriess)
                                val a = getDaysOfWeekInIndonesianOrder(entriess)
                                val day = getNextWeekDays(a[0])
                                binding.chart.visibility = View.GONE
                                val bar = binding.bchart
                                bar.visibility = View.VISIBLE
                                val entrie = ArrayList<BarEntry>()
                                A.forEach {
                                    if (it.first == day[0]) {
                                        entrie.add(BarEntry(0f, mood(it.second)))
                                    } else if (it.first == day[1]) {
                                        entrie.add(BarEntry(1f, mood(it.second)))
                                    } else if (it.first == day[2]) {
                                        entrie.add(BarEntry(2f, mood(it.second)))
                                    } else if (it.first == day[3]) {
                                        entrie.add(BarEntry(3f, mood(it.second)))
                                    } else if (it.first == day[4]) {
                                        entrie.add(BarEntry(4f, mood(it.second)))
                                    } else if (it.first == day[5]) {
                                        entrie.add(BarEntry(5f, mood(it.second)))
                                    } else if (it.first == day[6]) {
                                        entrie.add(BarEntry(6f, mood(it.second)))
                                    }
                                }

                                val barDataSet = BarDataSet(entrie, "Mood Mingguan")
                                barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

                                val barDatas = BarData(barDataSet)

                                bar.data = barDatas

                                val xAxis = bar.xAxis
                                xAxis.position = XAxis.XAxisPosition.BOTTOM
                                xAxis.granularity = 1f // Interval antar label (1 hari)
                                xAxis.valueFormatter = object :
                                    com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return when (value.toInt()) {
                                            0 -> day[0]
                                            1 -> day[1]
                                            2 -> day[2]
                                            3 -> day[3]
                                            4 -> day[4]
                                            5 -> day[5]
                                            6 -> day[6]
                                            else -> ""
                                        }
                                    }
                                }

                                val yAxis = bar.axisLeft
                                yAxis.axisMinimum = 0f  // Mood minimum (Sangat Buruk)
                                yAxis.axisMaximum = 5f  // Mood maksimum (Sangat Baik)
                                yAxis.granularity = 1f  // Interval antar nilai (selalu 1)
                                yAxis.labelCount = 5  // Menampilkan 5 label (1, 2, 3, 4, 5)
                                yAxis.valueFormatter = object :
                                    com.github.mikephil.charting.formatter.ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return value.toInt()
                                            .toString()
                                    }
                                }

                                bar.axisRight.isEnabled = false
                                bar.description.isEnabled = false
                                bar.invalidate()
                            } else if (period == "monthly") {
                                val A = getWeeklyPredictedMood(entriess)

                                binding.chart.visibility = View.GONE
                                val bar = binding.bchart
                                bar.visibility = View.VISIBLE
                                val entrie = ArrayList<BarEntry>()
                                A.forEach {
                                    if (it.first == "Minggu 1") {
                                        entrie.add(BarEntry(0f, mood(it.second)))
                                    } else if (it.first == "Minggu 2") {
                                        entrie.add(BarEntry(1f, mood(it.second)))
                                    } else if (it.first == "Minggu 3") {
                                        entrie.add(BarEntry(2f, mood(it.second)))
                                    } else if (it.first == "Minggu 4") {
                                        entrie.add(BarEntry(3f, mood(it.second)))
                                    }
                                }

                                val barDataSet = BarDataSet(entrie, "Mood Bulanan")
                                barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

                                val barDatas = BarData(barDataSet)

                                bar.data = barDatas

                                val xAxis = bar.xAxis
                                xAxis.position = XAxis.XAxisPosition.BOTTOM
                                xAxis.granularity = 1f // Interval antar label (1 hari)
                                xAxis.valueFormatter = object :
                                    com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return when (value.toInt()) {
                                            0 -> "Minggu Ke-1"
                                            1 -> "Minggu Ke-2"
                                            2 -> "Minggu Ke-3"
                                            3 -> "Minggu Ke-4"
                                            else -> ""
                                        }
                                    }
                                }

                                val yAxis = bar.axisLeft
                                yAxis.axisMinimum = 0f  // Mood minimum (Sangat Buruk)
                                yAxis.axisMaximum = 5f  // Mood maksimum (Sangat Baik)
                                yAxis.granularity = 1f  // Interval antar nilai (selalu 1)
                                yAxis.labelCount = 5  // Menampilkan 5 label (1, 2, 3, 4, 5)
                                yAxis.valueFormatter = object :
                                    com.github.mikephil.charting.formatter.ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return value.toInt()
                                            .toString() // Mengubah label ke bilangan bulat
                                    }
                                }

                                bar.axisRight.isEnabled = false
                                bar.description.isEnabled = false
                                bar.invalidate()
                            }
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
                        R.id.button1 -> {
                            period = "daily"
                            viewModel.moodTrends(user.token, "daily")
                            viewModel.moodEntries(user.token, "daily")
                            viewModel.moodSummary(user.token, "daily")
                        }

                        R.id.button2 -> {
                            binding.moo.text = "Minggu Ini Kamu Merasa"
                            period = "weekly"
                            viewModel.moodTrends(user.token, "weekly")
                            viewModel.moodEntries(user.token, period)
                            viewModel.moodSummary(user.token, "weekly")
                        }

                        R.id.button3 -> {
                            binding.moo.text = "Bulan Ini Kamu Merasa"
                            period = "monthly"
                            viewModel.moodTrends(user.token, "monthly")
                            viewModel.moodEntries(user.token, "monthly")
                            viewModel.moodSummary(user.token, "monthly")
                        }
                    }
                } else {
                    // Handle button uncheck if needed
                    Log.d("ToggleButton", "Button with ID $checkedId was unchecked.")
                }
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

    fun getMostFrequentPredictedMoodPerDay(data: List<DataItem>): List<Pair<String, String>> {
        val dateFormatter = DateTimeFormatter.ISO_DATE
        val groupedByDayOfWeek = data.groupBy {
            // Ambil hari dalam minggu berdasarkan createdAt
            val date = LocalDate.parse(it.createdAt.substring(0, 10), dateFormatter)
            date.dayOfWeek
        }

        // Format dayOfWeek to string representation
        return groupedByDayOfWeek.map { (dayOfWeek, entries) ->
            val moodCount = entries.groupingBy { it.predictedMood }.eachCount()
            val mostFrequentMood = moodCount.maxByOrNull { it.value }?.key ?: "Unknown"
            // Convert DayOfWeek to string in full form (e.g., Monday, Tuesday)
            Pair(dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID")), mostFrequentMood)
        }
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

    fun getDayFromDate(dateInput: String): String {
        return try {
            // API 26+ menggunakan DateTimeFormatter
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val date = LocalDate.parse(dateInput, inputFormatter)
            date.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid date"
        }
    }

    fun getWeeklyMoodData(): List<BarEntry> {
        return listOf(
            BarEntry(0f, 3f), // Senin: Mood 3
            BarEntry(1f, 4f), // Selasa: Mood 4
            BarEntry(2f, 2f), // Rabu: Mood 2
            BarEntry(3f, 5f), // Kamis: Mood 5
            BarEntry(4f, 3f), // Jumat: Mood 3
            BarEntry(5f, 1f), // Sabtu: Mood 1
            BarEntry(6f, 4f)  // Minggu: Mood 4
        )
    }

    fun formatDatesTime(input: String): Pair<String, String> {
        val zonedDateTimeUTC = ZonedDateTime.parse(input)
        val zonedDateTimeWIB = zonedDateTimeUTC.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val formattedDate = zonedDateTimeWIB.format(dateFormatter)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val formattedTime = zonedDateTimeWIB.format(timeFormatter)

        return Pair(formattedDate, formattedTime)
    }

    fun getDaysOfWeekInIndonesianOrder(data: List<DataItem>): List<String> {
        val dateFormatter = DateTimeFormatter.ISO_DATE
        val groupedByDate = data.groupBy {
            // Ambil tanggal saja (tanpa waktu)
            LocalDate.parse(it.createdAt.substring(0, 10), dateFormatter)
        }

        // Ambil nama hari berdasarkan tanggal dan urutkan dari yang paling lama hingga terbaru
        return groupedByDate.keys
            .sorted() // Urutkan berdasarkan tanggal
            .map { date ->
                val dayOfWeek = date.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.FULL,
                    Locale("id", "ID")
                )
                dayOfWeek
            }
    }

    fun getNextWeekDays(startDay: String): List<String> {
        // Daftar hari dalam minggu
        val daysOfWeek = listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu")

        // Menemukan indeks hari yang diberikan dalam input
        val startIndex = daysOfWeek.indexOf(startDay)

        // Jika input tidak valid, kembalikan list kosong
        if (startIndex == -1) {
            return emptyList()
        }

        // Menghasilkan array hari dalam seminggu mulai dari input
        val result = mutableListOf<String>()
        for (i in 0 until 7) {
            result.add(daysOfWeek[(startIndex + i) % 7])
        }

        return result
    }

    fun getWeeklyPredictedMood(data: List<DataItem>): List<Pair<String, String>> {
        val dateFormatter = DateTimeFormatter.ISO_DATE
        val today = LocalDate.now()
        val thirtyDaysAgo = today.minusDays(30)

        // Filter data berdasarkan tanggal antara 30 hari ke belakang dan hari ini
        val filteredData = data.filter {
            val entryDate = LocalDate.parse(it.createdAt.substring(0, 10), dateFormatter)
            entryDate.isAfter(thirtyDaysAgo.minusDays(1)) && entryDate.isBefore(today.plusDays(1))
        }

        // Kelompokkan data berdasarkan minggu
        val groupedByWeek = filteredData.groupBy {
            val entryDate = LocalDate.parse(it.createdAt.substring(0, 10), dateFormatter)
            // Tentukan minggu berdasarkan rentang tanggal 7 hari
            when {
                entryDate.isAfter(today.minusDays(7)) -> 1 // Minggu 1
                entryDate.isAfter(today.minusDays(14)) -> 2 // Minggu 2
                entryDate.isAfter(today.minusDays(21)) -> 3 // Minggu 3
                else -> 4 // Minggu 4
            }
        }

        // Mengambil hasil mood yang paling sering per minggu
        return groupedByWeek.map { (week, entries) ->
            val moodCount = entries.groupingBy { it.predictedMood }.eachCount()
            val mostFrequentMood = moodCount.maxByOrNull { it.value }?.key ?: "Unknown"

            // Kembalikan pasangan "Minggu X" dan mood yang paling sering
            Pair("Minggu $week", mostFrequentMood)
        }
    }
}
