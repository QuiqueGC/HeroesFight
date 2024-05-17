package com.example.heroes_fight.ui.fight_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.FragmentFightBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FightFragment : Fragment() {

    private lateinit var binding: FragmentFightBinding
    private val viewModel: FightFragmentViewModel by viewModels()

    private val board = Array(10) { arrayOfNulls<View>(9) }

    private var indexOfChild = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRandomHeroes()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFightBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBoard()

        observeViewModel()

        setupListeners()

    }

    private fun setupListeners() {

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { fightFragmentUiState ->

                when (fightFragmentUiState) {
                    is FightFragmentUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is FightFragmentUiState.Error -> Toast.makeText(
                        requireContext(),
                        "ERROR ERROR ERROR",
                        Toast.LENGTH_SHORT
                    ).show()

                    is FightFragmentUiState.Success -> showHeroes(
                        fightFragmentUiState.hero,
                        fightFragmentUiState.villain
                    )
                }
            }
        }
    }

    private fun showHeroes(hero: HeroModel, villain: HeroModel) {
        with(binding) {

            progressBar.visibility = View.GONE

            Glide.with(requireContext())
                .load(hero.image)
                .error(R.drawable.fight)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)

            Glide.with(requireContext())
                .load(villain.image)
                .error(R.drawable.fight)
                .apply(RequestOptions().centerCrop())
                .into(imgVillain)
        }
    }

    private fun setupBoard() {
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                board[i][j] = binding.board.root.getChildAt(indexOfChild)
                indexOfChild++
            }
        }

        /*board[0][0]!!.setOnClickListener {
            Toast.makeText(requireContext(),"asdasd",Toast.LENGTH_SHORT).show()
        }*/
    }
}