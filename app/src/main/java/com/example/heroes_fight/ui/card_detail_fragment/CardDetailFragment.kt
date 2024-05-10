package com.example.heroes_fight.ui.card_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.data.domain.model.hero.ImgModel
import com.example.heroes_fight.data.domain.use_case.GetHeroBiographyByIdUseCase
import com.example.heroes_fight.data.domain.use_case.GetHeroImgById
import com.example.heroes_fight.databinding.FragmentCardDetailBinding
import kotlinx.coroutines.launch


class CardDetailFragment : Fragment() {

    private val args: CardDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentCardDetailBinding
    private lateinit var viewModel: CardDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = CardDetailViewModel(GetHeroBiographyByIdUseCase(), GetHeroImgById())
        viewModel.getHeroData(args.idHero)

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { cardDetailUiState ->
                when (cardDetailUiState) {
                    is CardDetailUiState.Loading -> setLoading()
                    is CardDetailUiState.Success -> showData(
                        cardDetailUiState.biographyModel,
                        cardDetailUiState.imgModel
                    )

                    is CardDetailUiState.Error -> showError()
                }
            }
        }
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), "ERROR ERROR ERROR ERROR", Toast.LENGTH_LONG).show()
    }

    private fun showData(biographyModel: BiographyModel, imgModel: ImgModel) {
        with(binding) {
            progressBar.visibility = View.GONE
            tvNameHeroContent.text = biographyModel.name
            tvPublisherHeroContent.text = biographyModel.publisher
            tvAlterEgoHeroContent.text = biographyModel.alterEgos
            tvFullNameHeroContent.text = biographyModel.fullName
            tvFirstAppearanceHeroContent.text = biographyModel.firstAppearance
            tvPlaceOfBirthHeroContent.text = biographyModel.placeOfBirth

            Glide.with(requireContext())
                .load(imgModel.url)
                .apply(RequestOptions().centerCrop())
                .error(R.drawable.fight)
                .into(imgHero)
        }
    }
    
    private fun setLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }
}