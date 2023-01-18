package tech.neurothrone.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import tech.neurothrone.recipeapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
  
  private var _binding: FragmentLoginBinding? = null
  private val binding get() = _binding!!
  
  private val viewModel: RecipeViewModel by activityViewModels()
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = FragmentLoginBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    registerListeners(view)
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    
    _binding = null
  }
  
  private fun registerListeners(view: View) {
    binding.signUpButton.setOnClickListener {
      val email = binding.emailEditText.text.toString()
      val password = binding.passwordEditText.text.toString()
      
      if (email.isEmpty() || password.isEmpty()) return@setOnClickListener
    
      viewModel.signUp(email, password)
    }
  
    binding.logInButton.setOnClickListener {
      val email = binding.emailEditText.text.toString()
      val password = binding.passwordEditText.text.toString()
  
      if (email.isEmpty() || password.isEmpty()) return@setOnClickListener
  
      viewModel.logIn(email, password)
    }
  
    val loginStatusObserver = Observer<LoginResult> { loginStatus ->
      if (loginStatus == LoginResult.LOGIN_FAIL) {
        Snackbar.make(
          view,
          "❌ -> Invalid credentials.",
          Snackbar.LENGTH_LONG
        ).show()
      } else if (loginStatus == LoginResult.REGISTER_FAIL) {
        Snackbar.make(
          view,
          "❌ -> Type in a valid email and/or a password of minimum 6 letters.",
          Snackbar.LENGTH_LONG
        ).show()
      }
    }
  
    viewModel.loginStatus.observe(viewLifecycleOwner, loginStatusObserver)
  }
}