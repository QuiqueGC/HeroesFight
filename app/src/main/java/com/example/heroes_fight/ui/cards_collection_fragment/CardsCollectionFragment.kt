package com.example.heroes_fight.ui.cards_collection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.FragmentCardsCollectionBinding


class CardsCollectionFragment : Fragment() {

    private lateinit var binding: FragmentCardsCollectionBinding
    private lateinit var adapter: CardsCollectionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardsCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = CardsCollectionAdapter(requireContext(), getListCards())
        val listManager = GridLayoutManager(requireContext(), 2)

        with(binding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = adapter
        }
    }


    private fun getListCards(): MutableList<HeroModel> {
        return mutableListOf(
            HeroModel(name = "manolo", alignment = "bad"),
            HeroModel(name = "otro", alignment = "good"),
            HeroModel(name = "lalala", alignment = "good"),
            HeroModel(name = "asdasfsdf", alignment = "bad"),
        )
    }
}