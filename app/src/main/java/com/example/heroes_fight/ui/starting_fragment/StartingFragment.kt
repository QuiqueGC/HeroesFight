package com.example.heroes_fight.ui.starting_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heroes_fight.databinding.FragmentStartingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartingFragment : Fragment() {
    private lateinit var binding: FragmentStartingBinding
    private val viewModel: StartingFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCardsList()

        lifecycleScope.launch {
            viewModel.finnishLoading.collect { finnishLoading ->
                if (finnishLoading) {
                    findNavController().navigate(StartingFragmentDirections.actionStartingFragmentToMainMenuFragment())
                }
            }
        }
    }
}