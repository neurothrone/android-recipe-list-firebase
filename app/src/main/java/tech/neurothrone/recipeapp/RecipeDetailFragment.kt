package tech.neurothrone.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.neurothrone.recipeapp.databinding.FragmentRecipeDetailBinding
import tech.neurothrone.recipeapp.databinding.FragmentRecipeListBinding

class RecipeDetailFragment : Fragment() {
  
  private var _binding: FragmentRecipeDetailBinding? = null
  private val binding get() = _binding!!
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
}