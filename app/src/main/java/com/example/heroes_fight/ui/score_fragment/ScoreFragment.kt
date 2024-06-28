package com.example.heroes_fight.ui.score_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heroes_fight.R
import com.example.heroes_fight.databinding.FragmentScoreBinding


open class ScoreFragment : Fragment() {

    open lateinit var binding: FragmentScoreBinding
    private val args: ScoreFragmentArgs by navArgs()
    open lateinit var adapter: ScoreAdapter
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
        backToMenu()

    }

    open fun backToMenu() {
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToMainMenuFragment())
        }
    }

    open fun setTitle() {
        binding.tvWin.text = if (args.scoreListModel.areVillains) {
            getString(R.string.villains_won)
        } else {
            getString(R.string.heroes_won)
        }
    }

    private fun setupAdapter() {

        createAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    open fun createAdapter() {
        adapter = ScoreAdapter(
            args.scoreListModel.scores,
            requireContext()
        )
    }
}