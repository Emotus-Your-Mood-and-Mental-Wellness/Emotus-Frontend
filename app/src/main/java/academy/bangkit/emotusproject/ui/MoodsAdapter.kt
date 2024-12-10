package academy.bangkit.emotusproject.ui

import academy.bangkit.emotusproject.R
import academy.bangkit.emotusproject.Utils.Date
import academy.bangkit.emotusproject.data.remote.response.DataItem
import academy.bangkit.emotusproject.databinding.MoodEntriesBinding
import academy.bangkit.emotusproject.di.Injection
import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MoodsAdapter : ListAdapter<DataItem, MoodsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var onDeleteClickListener: ((DataItem, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MoodEntriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onDeleteClickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mood = getItem(position)
        holder.bind(mood)
    }

    class MyViewHolder(
        private val binding: MoodEntriesBinding,
        private val onDeleteClickListener: ((DataItem, position: Int) -> Unit)?
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        private val date: Date = Injection.provideDate()
        fun bind(mood: DataItem) {

            binding.moodHistory.text = moods(mood.predictedMood)
            binding.moodHistory.setTextColor(parseColor(color(mood.predictedMood)))
            binding.moodIconHistory.setImageResource(moodIcon(mood.predictedMood))
            binding.inputHistory.text = mood.diaryEntry
            binding.dateHistory.text = date.formatDatesTime(mood.createdAt).second
            binding.thoughtfulSuggestions.text = mood.thoughtfulSuggestions[0]
            binding.deleteHistory.setOnClickListener {
                onDeleteClickListener?.invoke(mood, adapterPosition)
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

        private fun moods(mood: String): String {
            return when (mood) {
                "Anger" -> "Mood Marah"
                "Sadness" -> "Mood Sedih"
                "Happy" -> "Mood Bahagia"
                "Fear" -> "Mood Takut"
                "Love" -> "Mood Cinta"
                else -> "Mood Kosong"
            }
        }

        private fun color(mood: String): String {
            return when (mood) {
                "Anger" -> "#FF6053"
                "Sadness" -> "#FF796E"
                "Happy" -> "#94E368"
                "Fear" -> "#FFD06A"
                "Love" -> "#ABE68A"
                else -> "#000000"
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DataItem> =
            object : DiffUtil.ItemCallback<DataItem>() {
                override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                    return oldItem.id == newItem.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DataItem,
                    newItem: DataItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    fun removeItem(position: Int) {
        val updatedList = currentList.toMutableList()
        updatedList.removeAt(position)
        submitList(updatedList) // Memperbarui data list
    }
}