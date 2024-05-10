package com.example.heroes_fight.ui.main_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import com.example.heroes_fight.databinding.FragmentMainMenuBinding
import kotlinx.coroutines.launch

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding
    private lateinit var viewModel: MainMenuViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = MainMenuViewModel(GetHeroByIdUseCase())
        viewModel.getHeroById()
        observeViewModel()

        binding.btnRandomCard.setOnClickListener {
            viewModel.getHeroById()
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { mainMenuUiState ->
                when (mainMenuUiState) {
                    is MainMenuUiState.Success -> showCard(mainMenuUiState.heroModel)
                    is MainMenuUiState.Loading -> setLoading()
                    is MainMenuUiState.Error -> showError()
                }

            }
        }
    }

    private fun setLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            cardIncludedGood.card.visibility = View.GONE
            cardIncludedBad.card.visibility = View.GONE
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "ERROR ERROR ERROR ERROR", Toast.LENGTH_LONG).show()
    }

    private fun showCard(heroModel: HeroModel) {
        binding.progressBar.visibility = View.GONE

        if (heroModel.alignment == "bad") {
            binding.cardIncludedGood.card.visibility = View.GONE
            binding.cardIncludedBad.card.visibility = View.VISIBLE
            with(binding.cardIncludedBad) {
                tvId.text = heroModel.id
                tvName.text = heroModel.name
                tvStrengthContent.text = heroModel.strength
                tvCombatContent.text = heroModel.combat
                tvIntelligenceContent.text = heroModel.intelligence
                tvSpeedContent.text = heroModel.speed
                tvDurabilityContent.text = heroModel.durability
                tvPowerContent.text = heroModel.power
                Glide.with(requireContext())
                    .load(heroModel.image)
                    .apply(RequestOptions().centerCrop())
                    .into(imgHero)
            }
        } else {
            binding.cardIncludedBad.card.visibility = View.GONE
            binding.cardIncludedGood.card.visibility = View.VISIBLE
            with(binding.cardIncludedGood) {
                tvId.text = heroModel.id
                tvName.text = heroModel.name
                tvStrengthContent.text = heroModel.strength
                tvCombatContent.text = heroModel.combat
                tvIntelligenceContent.text = heroModel.intelligence
                tvSpeedContent.text = heroModel.speed
                tvDurabilityContent.text = heroModel.durability
                tvPowerContent.text = heroModel.power
                Glide.with(requireContext())
                    .load(heroModel.image)
                    .error(R.drawable.fight)
                    .apply(RequestOptions().centerCrop())
                    .into(imgHero)
            }
        }
    }
}