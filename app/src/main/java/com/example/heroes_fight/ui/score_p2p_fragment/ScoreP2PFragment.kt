package com.example.heroes_fight.ui.score_p2p_fragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.heroes_fight.R
import com.example.heroes_fight.ui.score_fragment.ScoreAdapter
import com.example.heroes_fight.ui.score_fragment.ScoreFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScoreP2PFragment : ScoreFragment() {

    private val args: ScoreP2PFragmentArgs by navArgs()
    override fun backToMenu() {
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(ScoreP2PFragmentDirections.actionScoreP2PFragmentToMainMenuFragment())
        }
    }

    override fun setTitle() {
        if (args.playerWon) {
            binding.tvWin.text = getString(R.string.you_won)
        } else {
            binding.tvWin.text = getString(R.string.enemy_won)
        }

        binding.tvScoreTitle.text = getString(R.string.yor_stats)
    }

    override fun createAdapter() {
        adapter = ScoreAdapter(
            args.scoreListModel.scores,
            requireContext()
        )
    }
}