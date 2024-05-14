package com.example.heroes_fight.ui.card_appearance_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroes_fight.databinding.FragmentAppearanceBinding

class AppearanceFragment : Fragment() {

    private lateinit var binding: FragmentAppearanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAppearanceBinding.inflate(inflater, container, false)
        return binding.root
    }
}