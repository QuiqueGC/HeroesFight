package com.example.heroes_fight.ui.biography_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.BiographyModel
import com.example.heroes_fight.databinding.FragmentBiographyDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BiographyFragment : Fragment() {

    private val viewModel: BiographyViewModel by viewModels()

    private val args: BiographyFragmentArgs by navArgs()
    private lateinit var binding: FragmentBiographyDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBiographyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHeroData(args.idHero)

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { cardDetailUiState ->
                when (cardDetailUiState) {
                    is BiographyUiState.Loading -> setLoading()
                    is BiographyUiState.Success -> showData(cardDetailUiState.biographyModel)
                }
            }
        }
    }


    private fun showData(biographyModel: BiographyModel) {
        with(binding) {
            progressBar.visibility = View.GONE
            tvNameHeroContent.text = biographyModel.name
            tvPublisherHeroContent.text = biographyModel.publisher
            tvAlterEgoHeroContent.text = biographyModel.alterEgos
            tvFullNameHeroContent.text = biographyModel.fullName
            tvFirstAppearanceHeroContent.text = biographyModel.firstAppearance
            tvPlaceOfBirthHeroContent.text = biographyModel.placeOfBirth
            tvAliasesHeroContent.text = biographyModel.aliases

            Log.i("img", "Url de la imagen -> ${biographyModel.image}")
            Glide.with(requireContext())
                .load(biographyModel.image)
                .apply(RequestOptions().centerCrop())
                .error(R.drawable.question_mark)
                .into(imgHero)
        }
    }

    private fun setLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }
}