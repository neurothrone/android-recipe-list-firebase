package tech.neurothrone.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeListAdapter(
  val onRowClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecipeListViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
    val inflater =
      LayoutInflater.from(parent.context).inflate(R.layout.recipe_list_item_row, parent, false)
    return RecipeListViewHolder(inflater)
  }
  
  override fun getItemCount(): Int = 5
  
  override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
    holder.titleTextView.text = "Row $position"
    
    holder.itemView.setOnClickListener { onRowClicked(position) }
  }
}

class RecipeListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
  var titleTextView: TextView = view.findViewById(R.id.title_text_view)
}