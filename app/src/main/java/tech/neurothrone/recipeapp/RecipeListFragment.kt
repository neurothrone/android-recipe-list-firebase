package tech.neurothrone.recipeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import tech.neurothrone.recipeapp.databinding.FragmentLoginBinding
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
    
    setUpAdapter(view)
    registerListeners()
    
    // TODO: Remove
    Firebase.auth.currentUser?.let {
      Log.d(RecipeViewModel.TAG, "✅ -> email: ${it.email}")
      Log.d(RecipeViewModel.TAG, "✅ -> uid: ${it.uid}")
    }
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
  
  private fun setUpAdapter(view: View) {
    recipeListAdapter = RecipeListAdapter() { navigateToRecipeDetailIn(it) }
    
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