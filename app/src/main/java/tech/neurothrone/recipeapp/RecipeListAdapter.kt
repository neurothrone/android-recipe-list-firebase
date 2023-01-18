package tech.neurothrone.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import tech.neurothrone.recipeapp.databinding.RecipeListItemRowBinding

class RecipeListAdapter(
  private val recipes: MutableLiveData<List<Recipe>>,
  val onRowClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder>() {
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
    val binding = RecipeListItemRowBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return RecipeListViewHolder(binding)
  }
  
  override fun getItemCount(): Int = recipes.value?.size ?: 0
  
  override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
    val recipe = recipes.value?.get(position)
    
    recipe?.let {
      holder.binding.titleTextView.text = it.title
      holder.itemView.setOnClickListener { onRowClicked(position) }
    }
  }
  
  inner class RecipeListViewHolder(
    val binding: RecipeListItemRowBinding
  ) : RecyclerView.ViewHolder(binding.root)
  
}

