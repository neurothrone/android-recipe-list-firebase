package tech.neurothrone.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import tech.neurothrone.recipeapp.databinding.FragmentLoginBinding
import tech.neurothrone.recipeapp.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
  
  private var _binding: FragmentRecipeListBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var recipeListAdapter: RecipeListAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    recipeListAdapter = RecipeListAdapter() { navigateToRecipeDetailIn(it) }
    
    binding.recipesRecyclerView.layoutManager = LinearLayoutManager(view.context)
    binding.recipesRecyclerView.adapter = recipeListAdapter
    
    binding.newRecipeButton.setOnClickListener {
      requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_container_view, RecipeDetailFragment())
        .addToBackStack(null)
        .commit()
    }
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
  
  private fun navigateToRecipeDetailIn(position: Int) {
    // TODO: Pass in recipe as argument
    
    println("✅ -> navigate to position: $position")
    
//    requireActivity().supportFragmentManager
//      .beginTransaction()
//      .add(R.id.fragment_container_view, RecipeDetailFragment())
//      .addToBackStack(null)
//      .commit()
  }
}