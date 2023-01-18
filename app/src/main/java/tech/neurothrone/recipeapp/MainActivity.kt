package tech.neurothrone.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/*
BUGS:
+ Can press through recipe detail on recipe
- New Recipe: Choosing image will crash app
- After loading image for one Recipe, it will show for all other Recipes

TODOS:
+ Scale down image before upload

 */
class MainActivity : AppCompatActivity() {
  
  private val viewModel: RecipeViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    registerObservers()
    viewModel.setUp()
  }
  
  private fun registerObservers() {
    val currentUserObserver = Observer<FirebaseUser?> { currentUser ->
      if (currentUser == null) {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_container_view, LoginFragment())
          .commit()
      } else {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_container_view, RecipeListFragment())
          .commit()
        
      }
    }
    
    viewModel.currentUser.observe(this, currentUserObserver)
  }
}
