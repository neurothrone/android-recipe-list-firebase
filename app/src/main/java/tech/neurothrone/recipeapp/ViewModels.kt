package tech.neurothrone.recipeapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecipeViewModel : ViewModel() {
  
  private lateinit var auth: FirebaseAuth
  
//  val recipes: MutableLiveData<List<Recipe>> by lazy {
//    MutableLiveData<List<Recipe>>()
//  }
//
//  fun fetchRecipes() {
////    recipes.value = ...fetch from firebase
//  }
  
//  fun addRecipe() {}
//  fun updateRecipe() {}
//  fun deleteRecipe() {}
  
  val isAuthenticated: MutableLiveData<Boolean> by lazy {
    MutableLiveData<Boolean>()
  }
  
  fun setUp() {
    auth = Firebase.auth
    isAuthenticated.value = auth.currentUser != null
  }
  
  fun logIn(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { authResult ->
        if (authResult.isSuccessful) {
          isAuthenticated.value = true
          
          Log.d(TAG, "signInWithEmail:success")
        } else {
          Log.w(TAG, "signInWithEmail:failure", authResult.exception)
        }
      }
  }
  
  fun signUp(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { authResult ->
        if (authResult.isSuccessful) {
          isAuthenticated.value = true
          
          Log.d(TAG, "createUserWithEmail:success")
        } else {
          Log.w(TAG, "createUserWithEmail:failure", authResult.exception)
        }
      }
  }
  
  fun logOut() {
    auth.signOut()
    isAuthenticated.value = false
    
    Log.d(TAG, "signOut:success")
  }
  
  companion object {
    const val TAG = "firebase-logcat"
  }
}