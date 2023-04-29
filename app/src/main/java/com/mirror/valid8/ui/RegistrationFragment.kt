package com.mirror.valid8.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mirror.valid8.databinding.FragmentRegistrationBinding
import com.mirror.valid8.presentation.RegistrationFormEvent
import com.mirror.valid8.presentation.RegistrationViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private val viewModel: RegistrationViewModel by activityViewModels()

    private lateinit var binding: FragmentRegistrationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentRegistrationBinding.inflate(inflater, container, false).also {
            binding = it
            lifecycleScope.launch {
                lifecycle.currentState.let { state ->
                    when (state) {
                        Lifecycle.State.STARTED -> {
                            viewModel.validationEventChannel.collectLatest { event ->
                                when (event) {
                                    is RegistrationViewModel.ValidationEvent.Success -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "Success tu",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                }
                            }
                        }

                        else -> {}
                    }
                }
            }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etEmail.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        viewModel.onEvent(RegistrationFormEvent.EmailChanged(text.toString()))
                    }
                }

            }
        }

    }

}