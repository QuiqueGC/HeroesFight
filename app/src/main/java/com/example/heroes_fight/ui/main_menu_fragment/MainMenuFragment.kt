package com.example.heroes_fight.ui.main_menu_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.FragmentMainMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainMenuFragment : Fragment() {

    private val viewModel: MainMenuViewModel by viewModels()

    private lateinit var binding: FragmentMainMenuBinding

    //variable temporal
    private var idHero = 0
    private var isGoodCard: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHeroById()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setupListeners()
    }


    override fun onResume() {
        super.onResume()

        if (isGoodCard != null) {
            when (isGoodCard!!) {
                true -> binding.cardIncludedGood.card.visibility = View.VISIBLE
                false -> binding.cardIncludedBad.card.visibility = View.VISIBLE
            }
        }
    }

    private fun setupListeners() {
        binding.btnRandomCard.setOnClickListener {
            viewModel.getHeroById()
        }

        binding.cardSmallIncludedBad.card.setOnClickListener {
            binding.cardIncludedBad.card.visibility = View.VISIBLE
        }

        binding.cardSmallIncludedGood.card.setOnClickListener {
            binding.cardIncludedGood.card.visibility = View.VISIBLE
        }

        binding.cardIncludedBad.card.setOnClickListener {
            it.visibility = View.GONE
        }

        binding.cardIncludedGood.card.setOnClickListener {
            it.visibility = View.GONE
        }

        binding.cardIncludedGood.btnBiography.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToCardDetailFragment(
                    idHero
                )
            )
        }

        binding.cardIncludedBad.btnBiography.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToCardDetailFragment(
                    idHero
                )
            )
        }


        binding.cardIncludedGood.btnAppearance.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToAppearanceFragment(
                    idHero
                )
            )
        }

        binding.cardIncludedBad.btnAppearance.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToAppearanceFragment(
                    idHero
                )
            )
        }

        binding.btnGrid.setOnClickListener {
            findNavController().navigate(
                MainMenuFragmentDirections.actionMainMenuFragmentToCardsCollectionFragment()
            )
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
            cardSmallIncludedGood.card.visibility = View.GONE
            cardSmallIncludedBad.card.visibility = View.GONE
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "ERROR ERROR ERROR ERROR", Toast.LENGTH_LONG).show()
    }

    private fun showCard(heroModel: HeroModel) {
        idHero = heroModel.id
        binding.progressBar.visibility = View.GONE

        if (heroModel.alignment == "bad") {

            isGoodCard = false

            binding.cardSmallIncludedGood.card.visibility = View.GONE
            binding.cardSmallIncludedBad.card.visibility = View.VISIBLE
            with(binding.cardSmallIncludedBad) {
                tvId.text = heroModel.serialNum
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

            with(binding.cardIncludedBad) {
                tvId.text = heroModel.serialNum
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

        } else {

            isGoodCard = true

            binding.cardSmallIncludedBad.card.visibility = View.GONE
            binding.cardSmallIncludedGood.card.visibility = View.VISIBLE
            with(binding.cardSmallIncludedGood) {
                tvId.text = heroModel.serialNum
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

            with(binding.cardIncludedGood) {
                tvId.text = heroModel.serialNum
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