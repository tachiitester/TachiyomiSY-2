package eu.kanade.tachiyomi.ui.browse.source.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import eu.kanade.tachiyomi.databinding.SourceFilterSheetSavedSearchesBinding
import eu.kanade.tachiyomi.util.view.gone
import eu.kanade.tachiyomi.util.view.visible

class SavedSearchesAdapter(var chips: List<Chip> = emptyList()) :
    RecyclerView.Adapter<SavedSearchesAdapter.SavedSearchesViewHolder>() {

    private lateinit var binding: SourceFilterSheetSavedSearchesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchesViewHolder {
        binding = SourceFilterSheetSavedSearchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSearchesViewHolder(binding.root)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: SavedSearchesViewHolder, position: Int) {
        holder.bind(chips)
    }

    inner class SavedSearchesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(chips: List<Chip> = emptyList()) {
            binding.savedSearches.removeAllViews()
            if (chips.isEmpty()) {
                binding.savedSearchesTitle.gone()
            } else {
                binding.savedSearchesTitle.visible()
                chips.forEach {
                    binding.savedSearches.addView(it)
                }
            }
        }
    }
}
