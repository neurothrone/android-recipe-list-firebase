package tech.neurothrone.recipeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import tech.neurothrone.recipeapp.databinding.FragmentRecipeDetailBinding

class RecipeDetailFragment : Fragment() {
  
  private var _binding: FragmentRecipeDetailBinding? = null
  private val binding get() = _binding!!
  
  private val viewModel: RecipeViewModel by activityViewModels()
  
  var recipe: Recipe? = null
  var onRecipeUpdated: (() -> Unit)? = null
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val imageObserver = Observer<Bitmap> {
      binding.recipeImageView.setImageBitmap(it)
    }
    viewModel.imageResult.observe(viewLifecycleOwner, imageObserver)
    
    recipe?.let {
      viewModel.downloadImage(it)
      
      binding.recipeTitleEditText.setText(it.title)
      binding.recipeDescriptionEditTextMultiLine.setText(it.description)
    }
  
    registerListeners()
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
  
  private fun registerListeners() {
    val image = registerForActivityResult(
      ActivityResultContracts.GetContent(),
      ActivityResultCallback {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
          requireContext().contentResolver,
          it
        )
        
        binding.recipeImageView.setImageBitmap(bitmap)
        viewModel.uploadImage(bitmap, recipe!!)
      })
    
    binding.galleryButton.setOnClickListener {
      image.launch("image/*")
    }
    binding.cameraButton.setOnClickListener { goToCameraFragment() }
    binding.recipeSaveButton.setOnClickListener { saveRecipe() }
  }
  
  private fun goToCameraFragment() {
    val cameraFragment = CameraFragment()
    cameraFragment.recipe = recipe
    
    requireActivity().supportFragmentManager
      .beginTransaction()
      .add(R.id.fragment_container_view, cameraFragment)
      .addToBackStack(null)
      .commit()
  }
  
  private fun saveRecipe() {
    val recipeToSave = recipe ?: Recipe()
    
    recipeToSave.title = binding.recipeTitleEditText.text.toString()
    recipeToSave.description = binding.recipeDescriptionEditTextMultiLine.text.toString()
    
    viewModel.saveRecipe(recipeToSave)
    onRecipeUpdated?.let { it() }
  
    dismissDetailView()
  }
  
  private fun dismissDetailView() {
    requireActivity().supportFragmentManager
      .popBackStack()
  }
}