package com.example.heroes_fight.ui.score_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heroes_fight.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private val args: ScoreFragmentArgs by navArgs()
    private lateinit var adapter: ScoreAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setTitle()

        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToNewMainMenuFragment())
        }
    }

    private fun setTitle() {
        binding.tvWin.text = if (args.scoreListModel.areVillains) {
            "Villains won"
        } else {
            "Heroes won"
        }
    }

    private fun setupAdapter() {
        adapter = ScoreAdapter(
            args.scoreListModel.scores,
            requireContext(),
            args.scoreListModel.areVillains
        )
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
}