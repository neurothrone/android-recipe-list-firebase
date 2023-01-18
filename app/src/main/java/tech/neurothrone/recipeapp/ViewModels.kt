package tech.neurothrone.recipeapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

enum class LoginResult {
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  REGISTER_SUCCESS,
  REGISTER_FAIL
}

class RecipeViewModel : ViewModel() {
  
  private lateinit var auth: FirebaseAuth
  private val database: DatabaseReference =
    Firebase.database("https://sandbox-17072-default-rtdb.europe-west1.firebasedatabase.app/").reference
  
  private val storage = Firebase.storage
  
  // TODO: currentRecipe
//  private var currentRecipe: Recipe? = null
  
  val imageResult: MutableLiveData<Bitmap> by lazy {
    MutableLiveData<Bitmap>()
  }
  
  val currentUser: MutableLiveData<FirebaseUser?> by lazy {
    MutableLiveData<FirebaseUser?>()
  }
  
  val loginStatus: MutableLiveData<LoginResult> by lazy {
    MutableLiveData<LoginResult>()
  }
  
  val recipes: MutableLiveData<List<Recipe>> by lazy {
    MutableLiveData<List<Recipe>>()
  }
  
  fun setUp() {
    auth = Firebase.auth
    getCurrentUser()
  }
  
  private fun getCurrentUser() {
    currentUser.value = auth.currentUser
  }
  
  fun logIn(email: String, password: String) {
    auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener { authResult ->
        if (authResult.isSuccessful) {
          getCurrentUser()
          
          loginStatus.value = LoginResult.LOGIN_SUCCESS
          Log.d(TAG, "signInWithEmail:success")
        } else {
          loginStatus.value = LoginResult.LOGIN_FAIL
          Log.w(TAG, "signInWithEmail:failure", authResult.exception)
        }
      }
  }
  
  fun signUp(email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener { authResult ->
        if (authResult.isSuccessful) {
          getCurrentUser()
  
          loginStatus.value = LoginResult.REGISTER_SUCCESS
          Log.d(TAG, "createUserWithEmail:success")
        } else {
          loginStatus.value = LoginResult.REGISTER_FAIL
          Log.w(TAG, "createUserWithEmail:failure", authResult.exception)
        }
      }
  }
  
  fun logOut() {
    auth.signOut()
    getCurrentUser()
    
    Log.d(TAG, "signOut:success")
  }
  
  fun fetchRecipes() {
    val currentUser = currentUser.value ?: return
    
    database
      .child("recipe-app")
      .child("users")
      .child(currentUser.uid)
      .child("recipes")
      .get()
      .addOnSuccessListener {
        val tempRecipes = mutableListOf<Recipe>()
        
        for (recipeSnapshot in it.children) {
          recipeSnapshot.getValue<Recipe>()?.let { recipe: Recipe ->
            recipe.id = recipeSnapshot.key
            tempRecipes.add(recipe)
          }
        }
        
        recipes.value = tempRecipes
        
        Log.d("firebase-logcat", "✅ -> Successfully fetched recipes")
      }
      .addOnFailureListener {
        Log.w("firebase-logcat", "❌ -> Failed to fetch recipes. Error: ${it.message}")
      }
  }
  
  fun saveRecipe(recipe: Recipe) {
    val currentUser = auth.currentUser ?: return
    
    val recipePath = database
      .child("recipe-app")
      .child("users")
      .child(currentUser.uid)
      .child("recipes")
  
    // Update
    recipe.id?.let { recipeId ->
      recipePath
        .child(recipeId)
        .updateChildren(recipe.toMap())
      
      return
    }
    
    // Create
    recipePath
      .push()
      .setValue(recipe)
    
    fetchRecipes()
  }
  
  fun deleteRecipe() {}
  
  fun uploadImage(bitmapImage: Bitmap, recipe: Recipe) {
    // Save to Firebase
    val storageRef = storage.reference
      .child("recipe-app")
      .child(recipe.id!!)
//      .child("my-image")
  
    val baos = ByteArrayOutputStream()
//    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 25, baos)
    val scaledDownBitmap = resizeBitmap(bitmapImage, 800)
    scaledDownBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
    val data = baos.toByteArray()
  
    storageRef.putBytes(data)
      .addOnSuccessListener { taskSnapshot ->
        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
        imageResult.value = scaledDownBitmap
      }
      .addOnFailureListener {
        // Handle unsuccessful uploads
      }
  }
  
  fun downloadImage(recipe: Recipe) {
    val recipeId = recipe.id ?: return
    
    // !: Download image
    val imageRef = storage.reference
      .child("recipe-app")
      .child(recipeId)
  
    val ONE_MEGABYTE: Long = 1024 * 1024
    imageRef.getBytes(ONE_MEGABYTE)
      .addOnSuccessListener { data ->
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        imageResult.value = bitmap
      }.addOnFailureListener {
        // Handle any errors
      }
  }
  
  private fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap {
    try {
      if (source.height >= source.width) {
        if (source.height <= maxLength) { // if image height already smaller than the required height
          return source
        }
        
        val aspectRatio = source.width.toDouble() / source.height.toDouble()
        val targetWidth = (maxLength * aspectRatio).toInt()
        val result = Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
        return result
      } else {
        if (source.width <= maxLength) { // if image width already smaller than the required width
          return source
        }
        
        val aspectRatio = source.height.toDouble() / source.width.toDouble()
        val targetHeight = (maxLength * aspectRatio).toInt()
        
        val result = Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
        return result
      }
    } catch (e: Exception) {
      return source
    }
  }
  
  companion object {
    const val TAG = "firebase-logcat"
  }
}