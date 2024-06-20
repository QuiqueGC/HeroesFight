package com.example.heroes_fight.ui.mode_selection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroes_fight.databinding.FragmentModeSelectionBinding


class ModeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentModeSelectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModeSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
}