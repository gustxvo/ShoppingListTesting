package com.gustxvo.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.gustxvo.shoppinglisttesting.databinding.FragmentAddShoppingItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment() {

    private var _binding: FragmentAddShoppingItemBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ShoppingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShoppingItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )

            val callback = object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    viewModel.setCurrentImageUrl("")
                    findNavController().popBackStack()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(callback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}