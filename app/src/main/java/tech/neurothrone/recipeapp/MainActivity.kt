package tech.neurothrone.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
  
  private val viewModel: RecipeViewModel by viewModels()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    viewModel.setUp()
    
    val isAuthenticatedObserver = Observer<Boolean> { isAuthenticated ->
      if (isAuthenticated) {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_container_view, RecipeListFragment())
          .commit()
      } else {
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.fragment_container_view, LoginFragment())
          .commit()
      }
    }
    
    viewModel.isAuthenticated.observe(this, isAuthenticatedObserver)
  }
}
