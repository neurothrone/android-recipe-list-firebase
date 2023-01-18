package tech.neurothrone.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import tech.neurothrone.recipeapp.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
  
  private var _binding: FragmentRecipeListBinding? = null
  private val binding get() = _binding!!
  
  private lateinit var recipeListAdapter: RecipeListAdapter
  
  private val viewModel: RecipeViewModel by activityViewModels()
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    setUp(view)
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
  
  private fun setUp(view: View) {
    setUpAdapter(view)
    registerListeners()
    
    val recipesObserver = Observer<List<Recipe>> {
      recipeListAdapter.notifyDataSetChanged()
    }

    viewModel.recipes.observe(viewLifecycleOwner, recipesObserver)
    
    viewModel.fetchRecipes()
  }
  
  private fun setUpAdapter(view: View) {
    recipeListAdapter = RecipeListAdapter(viewModel.recipes) { navigateToRecipeDetailAt(it) }
    
    binding.recipesRecyclerView.layoutManager = LinearLayoutManager(view.context)
    binding.recipesRecyclerView.adapter = recipeListAdapter
  }
  
  private fun registerListeners() {
    binding.newRecipeButton.setOnClickListener {
      requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_container_view, RecipeDetailFragment())
        .addToBackStack(null)
        .commit()
    }
    
    binding.logOutButton.setOnClickListener { viewModel.logOut() }
  }
  
  private fun navigateToRecipeDetailAt(position: Int) {
    // TODO: Pass in recipe as argument
    
    println("✅ -> navigate to position: $position")
    
    val recipe = viewModel.recipes.value?.get(position)
    
    recipe?.let {
      val fragment = RecipeDetailFragment()
      fragment.recipe = it
      fragment.onRecipeUpdated = {
        recipeListAdapter.notifyItemChanged(position)
      }
      
      requireActivity().supportFragmentManager
        .beginTransaction()
        .add(R.id.fragment_container_view, fragment)
        .addToBackStack(null)
        .commit()
    }
    
  }
}