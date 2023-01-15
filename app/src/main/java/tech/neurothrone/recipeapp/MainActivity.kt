package tech.neurothrone.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    val isAuthenticated = false
    
    if (isAuthenticated) {
    // >: -> Recipe List
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container_view, RecipeListFragment())
        .commit()
    } else {
    // >: -> Login Fragment
      supportFragmentManager
        .beginTransaction()
        .replace(R.id.fragment_container_view, LoginFragment())
        .commit()
    }
  }
}

/*

* Login/Register
* List with recipes -> Recipe -> Create recipe
* Recipe detail

+ Recipe
Title
Description
Image, many?
Recipe rows, many

+ Recipe row
Title
Amount
Unit dl/st/osv

 */