package com.example.heroes_fight.ui.new_main_menu_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroes_fight.databinding.FragmentNewMainMenuBinding


class NewMainMenuFragment : Fragment() {

    private lateinit var binding: FragmentNewMainMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }


}