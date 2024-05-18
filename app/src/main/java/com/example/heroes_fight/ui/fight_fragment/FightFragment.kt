package com.example.heroes_fight.ui.fight_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.FragmentFightBinding
import com.example.heroes_fight.utils.CardsFiller
import com.example.heroes_fight.utils.FighterAction
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FightFragment : Fragment() {

    private lateinit var binding: FragmentFightBinding
    private val viewModel: FightFragmentViewModel by viewModels()

    private val board = Array(10) { arrayOfNulls<View>(9) }
    private val heroesList = ArrayList<HeroModel>()
    private val villainsList = ArrayList<HeroModel>()
    private val ivHeroesList = ArrayList<ShapeableImageView>()
    private val ivVillainsList = ArrayList<ShapeableImageView>()

    private var fighterAction = FighterAction.WaitingForAction


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

        setupFightersListeners()

        setupBoardListeners()

        binding.cardIncludedBad.card.setOnClickListener {
            it.visibility = View.GONE
        }
        binding.cardIncludedGood.card.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    private fun setupBoardListeners() {
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                board[i][j]!!.setOnClickListener { imgTile ->

                    // Crear un ConstraintSet
                    val constraintSet = ConstraintSet()
                    // Clonar las restricciones existentes del ConstraintLayout
                    constraintSet.clone(binding.root)

                    // Establecer nuevas restricciones
                    constraintSet.connect(
                        binding.imgHero.id,
                        ConstraintSet.TOP,
                        imgTile.id,
                        ConstraintSet.TOP
                    )
                    constraintSet.connect(
                        binding.imgHero.id,
                        ConstraintSet.START,
                        imgTile.id,
                        ConstraintSet.START
                    )

                    // Aplicar las nuevas restricciones al ConstraintLayout
                    constraintSet.applyTo(binding.root)
                }
            }
        }
    }

    private fun setupFightersListeners() {
        for (i in 0 until ivHeroesList.size) {
            ivHeroesList[i].setOnClickListener {
                CardsFiller.fillDataIntoGoodCard(
                    binding.cardIncludedGood,
                    heroesList[i],
                    requireContext()
                )
                binding.cardIncludedGood.card.visibility = View.VISIBLE
                binding.cardIncludedGood.btnAppearance.visibility = View.GONE
                binding.cardIncludedGood.btnBiography.visibility = View.GONE

                ivVillainsList[i].setOnClickListener {
                    CardsFiller.fillDataIntoBadCard(
                        binding.cardIncludedBad,
                        villainsList[0],
                        requireContext()
                    )
                    binding.cardIncludedBad.card.visibility = View.VISIBLE
                    binding.cardIncludedBad.btnAppearance.visibility = View.GONE
                    binding.cardIncludedBad.btnBiography.visibility = View.GONE
                }
            }
        }
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

                    is FightFragmentUiState.Success -> {
                        showHeroesMiniatures(
                            fightFragmentUiState.heroesList,
                            fightFragmentUiState.villainsList
                        )
                        addHeroesToLists(
                            fightFragmentUiState.heroesList,
                            fightFragmentUiState.villainsList
                        )
                    }
                }
            }
        }
    }

    private fun addHeroesToLists(
        heroesList: ArrayList<HeroModel>,
        villainsList: ArrayList<HeroModel>
    ) {
        this.heroesList.addAll(heroesList)
        this.villainsList.addAll(villainsList)
    }

    private fun showHeroesMiniatures(
        heroesList: ArrayList<HeroModel>,
        villainsList: ArrayList<HeroModel>
    ) {

        for (i in 0 until heroesList.size) {
            with(binding) {
                progressBar.visibility = View.GONE
                Glide.with(requireContext())
                    .load(heroesList[i].image)
                    .error(R.drawable.fight)
                    .apply(RequestOptions().centerCrop())
                    .into(ivHeroesList[i])

                Glide.with(requireContext())
                    .load(villainsList[i].image)
                    .error(R.drawable.fight)
                    .apply(RequestOptions().centerCrop())
                    .into(ivVillainsList[i])
            }
        }
    }

    private fun setupBoard() {
        //empieza por uno porque hay una view antes de los tiles
        var indexOfChild = 1
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                board[i][j] = binding.root.getChildAt(indexOfChild)
                indexOfChild++
            }
        }
        // TODO: Esto tendrán que ser dos bucles para rellenar los array con todas las views
        ivHeroesList.add(binding.imgHero)
        ivVillainsList.add(binding.imgVillain)
    }
}