package com.example.heroes_fight.ui.score_p2p_fragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.heroes_fight.ui.score_fragment.ScoreAdapter
import com.example.heroes_fight.ui.score_fragment.ScoreFragment

class ScoreP2PFragment : ScoreFragment() {

    private val args: ScoreP2PFragmentArgs by navArgs()
    override fun backToMenu() {
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(ScoreP2PFragmentDirections.actionScoreP2PFragmentToNewMainMenuFragment())
        }
    }

    override fun setTitle() {
        if (args.playerWon) {
            binding.tvWin.text = "You won!"
        } else {
            binding.tvWin.text = "Enemy won!"
        }

        binding.tvScoreTitle.text = "Yor stats:"
    }

    override fun createAdapter() {
        adapter = ScoreAdapter(
            args.scoreListModel.scores,
            requireContext()
        )
    }
}