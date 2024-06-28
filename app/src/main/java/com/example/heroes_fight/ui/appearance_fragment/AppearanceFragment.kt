package com.example.heroes_fight.ui.appearance_fragment

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
import com.example.heroes_fight.data.domain.model.hero.AppearanceModel
import com.example.heroes_fight.databinding.FragmentAppearanceBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppearanceFragment : Fragment() {

    private val viewModel: AppearanceViewModel by viewModels()

    private val args: AppearanceFragmentArgs by navArgs()
    private lateinit var binding: FragmentAppearanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppearanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHeroData(args.idHero)
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is AppearanceUiState.Loading -> setLoading()
                    is AppearanceUiState.Success -> showData(uiState.appearanceModel)
                }
            }
        }
    }


    private fun setLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showData(appearanceModel: AppearanceModel) {
        with(binding) {
            progressBar.visibility = View.GONE
            tvNameHeroContent.text = appearanceModel.name
            tvGenderHeroContent.text = appearanceModel.gender
            tvHeightHeroContent.text =
                "${appearanceModel.heightCm} / ${appearanceModel.heightFeet} feet"
            tvWeightHeroContent.text = "${appearanceModel.weightKg} / ${appearanceModel.weightLb}"
            tvRaceHeroContent.text = appearanceModel.race
            tvEyeColorHeroContent.text = appearanceModel.eyeColor
            tvHairColorHeroContent.text = appearanceModel.hairColor

            Log.i("img", "Url de la imagen -> ${appearanceModel.image}")
            Glide.with(requireContext())
                .load(appearanceModel.image)
                .apply(RequestOptions().centerCrop())
                .error(R.drawable.question_mark)
                .into(imgHero)
        }
    }
}