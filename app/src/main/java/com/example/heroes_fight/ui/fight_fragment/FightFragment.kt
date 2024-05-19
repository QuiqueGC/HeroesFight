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
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.databinding.FragmentFightBinding
import com.example.heroes_fight.utils.CardsFiller
import com.example.heroes_fight.utils.PlayerChoice
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FightFragment : Fragment() {

    //EL BIDIMENSIONAL HACE [Y][X] Y NO AL REVÉS

    private lateinit var binding: FragmentFightBinding
    private val viewModel: FightFragmentViewModel by viewModels()

    private val board = Array(10) { arrayOfNulls<View>(9) }
    private val heroesList = ArrayList<FighterModel>()
    private val villainsList = ArrayList<FighterModel>()
    private val ivHeroesList = ArrayList<ShapeableImageView>()
    private val ivVillainsList = ArrayList<ShapeableImageView>()

    private var playerChoice = PlayerChoice.WAITING_FOR_ACTION

    private var indexOfActualFighter = -1
    private var actualFighter = FighterModel()
    private var actualFighterIsHero = true
    private var destinationPosition = Position()

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

        setupTilesListeners()

        setupBtnActionsListeners()

        binding.cardIncludedBad.card.setOnClickListener {
            it.visibility = View.GONE
        }
        binding.cardIncludedGood.card.setOnClickListener {
            it.visibility = View.GONE
        }
    }

    private fun setupBtnActionsListeners() {
        with(binding) {
            btnMove.setOnClickListener {
                playerChoice = PlayerChoice.MOVE
                tvInfo.text = "Select the cell you want to move"
            }

            btnAttack.setOnClickListener {
                tvInfo.text = "Select the enemy you want to attack"
                playerChoice = PlayerChoice.ATTACK
            }

            btnDefend.setOnClickListener {
                tvInfo.text = "Select your own hero to confirm"
                playerChoice = PlayerChoice.DEFEND
            }

            btnSupport.setOnClickListener {
                tvInfo.text = "Select the ally you want to support"
                playerChoice = PlayerChoice.SUPPORT
            }

            btnSabotage.setOnClickListener {
                tvInfo.text = "Select the enemy you want to sabotage"
                playerChoice = PlayerChoice.SABOTAGE
            }

            btnPass.setOnClickListener {
                finishTurn()
            }
        }
    }

    private fun setupTilesListeners() {
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                board[i][j]!!.setOnClickListener { _ ->
                    if (playerChoice == PlayerChoice.MOVE) {
                        destinationPosition = Position(i, j)
                        viewModel.tryToMoveFighter(destinationPosition)
                    }
                }
            }
        }
    }

    private fun moveFighterView() {
        // Crear un ConstraintSet
        val constraintSet = ConstraintSet()
        // Clonar las restricciones existentes del ConstraintLayout
        constraintSet.clone(binding.root)

        // Establecer nuevas restricciones
        if (actualFighterIsHero) {
            constraintSet.connect(
                ivHeroesList[indexOfActualFighter].id,
                ConstraintSet.TOP,
                board[destinationPosition.y][destinationPosition.x]!!.id,
                ConstraintSet.TOP
            )
            constraintSet.connect(
                ivHeroesList[indexOfActualFighter].id,
                ConstraintSet.START,
                board[destinationPosition.y][destinationPosition.x]!!.id,
                ConstraintSet.START
            )
        } else {
            constraintSet.connect(
                ivVillainsList[indexOfActualFighter].id,
                ConstraintSet.TOP,
                board[destinationPosition.y][destinationPosition.x]!!.id,
                ConstraintSet.TOP
            )
            constraintSet.connect(
                ivVillainsList[indexOfActualFighter].id,
                ConstraintSet.START,
                board[destinationPosition.y][destinationPosition.x]!!.id,
                ConstraintSet.START
            )
        }

        // Aplicar las nuevas restricciones al ConstraintLayout
        constraintSet.applyTo(binding.root)
    }

    private fun setupFightersListeners() {
        for (i in 0 until ivHeroesList.size) {

            ivVillainsList[i].setOnClickListener { _ ->

                if (villainsList[i].alignment == "bad") {
                    CardsFiller.fillDataIntoVillainFighterCard(
                        binding.cardIncludedBad,
                        villainsList[i],
                        requireContext()
                    )
                    binding.cardIncludedBad.card.visibility = View.VISIBLE
                    binding.cardIncludedBad.btnAppearance.visibility = View.GONE
                    binding.cardIncludedBad.btnBiography.visibility = View.GONE

                } else {
                    CardsFiller.fillDataIntoHeroFighterCard(
                        binding.cardIncludedGood,
                        villainsList[i],
                        requireContext()
                    )
                    binding.cardIncludedGood.card.visibility = View.VISIBLE
                    binding.cardIncludedGood.btnAppearance.visibility = View.GONE
                    binding.cardIncludedGood.btnBiography.visibility = View.GONE

                }
            }

            ivHeroesList[i].setOnClickListener {
                if (heroesList[i].alignment == "good") {
                    CardsFiller.fillDataIntoHeroFighterCard(
                        binding.cardIncludedGood,
                        heroesList[i],
                        requireContext()
                    )
                    binding.cardIncludedGood.card.visibility = View.VISIBLE
                    binding.cardIncludedGood.btnAppearance.visibility = View.GONE
                    binding.cardIncludedGood.btnBiography.visibility = View.GONE

                } else {
                    CardsFiller.fillDataIntoVillainFighterCard(
                        binding.cardIncludedBad,
                        heroesList[i],
                        requireContext()
                    )
                    binding.cardIncludedBad.card.visibility = View.VISIBLE
                    binding.cardIncludedBad.btnAppearance.visibility = View.GONE
                    binding.cardIncludedBad.btnBiography.visibility = View.GONE
                }
            }

            ivVillainsList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttackToVillain(i)
                    PlayerChoice.SABOTAGE -> performSabotageToVillain(i)
                    PlayerChoice.DEFEND -> performDefenseToVillain(i)
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Choice an action",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
            ivHeroesList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttackToHero(i)
                    PlayerChoice.SABOTAGE -> performSabotageToHero(i)
                    PlayerChoice.DEFEND -> performDefenseToHero(i)
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Choice an action",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }

        }
    }

    private fun performDefenseToHero(indexOfFighterSelected: Int) {
        if (heroesList[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else {
            Toast.makeText(
                requireContext(),
                "Only can use in himself",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun performDefenseToVillain(indexOfFighterSelected: Int) {
        if (villainsList[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else {
            Toast.makeText(
                requireContext(),
                "Only can use in himself",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun performSabotageToHero(indexOfFighterSelected: Int) {
        if (!actualFighterIsHero) {
            viewModel.performSabotage(heroesList[indexOfFighterSelected])
        } else {
            Toast.makeText(
                requireContext(),
                "Don't sabotage your own team",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun performSabotageToVillain(indexOfFighterSelected: Int) {
        if (actualFighterIsHero) {
            viewModel.performSabotage(villainsList[indexOfFighterSelected])
        } else {
            Toast.makeText(
                requireContext(),
                "Don't sabotage your own team",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun performAttackToHero(indexOfFighterSelected: Int) {
        if (!actualFighterIsHero) {
            viewModel.performAttack(heroesList[indexOfFighterSelected])
        } else {
            Toast.makeText(
                requireContext(),
                "Don't attack your own team",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun performAttackToVillain(indexOfFighterSelected: Int) {
        if (actualFighterIsHero) {
            viewModel.performAttack(villainsList[indexOfFighterSelected])
        } else {
            Toast.makeText(
                requireContext(),
                "Don't attack your own team",
                Toast.LENGTH_SHORT
            ).show()
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

        lifecycleScope.launch {
            viewModel.actualFighter.collect { actualFighter ->
                if (actualFighter.id != 0) {

                    if (actualFighter.isSabotaged) {
                        binding.tvTurn.text = "${actualFighter.name}'s turn, but was sabotaged"
                        disableActionButtons()
                        binding.btnMove.isEnabled = false
                    } else {
                        binding.tvTurn.text = "${actualFighter.name}'s turn"
                    }
                    this@FightFragment.actualFighter = actualFighter
                    indexOfActualFighter =
                        heroesList.indexOf(heroesList.find { it.id == actualFighter.id })
                    actualFighterIsHero = true

                    if (indexOfActualFighter == -1) {
                        indexOfActualFighter =
                            villainsList.indexOf(villainsList.find { it.id == actualFighter.id })
                        actualFighterIsHero = false
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.fighterMovement.collect { fighterCanMove ->
                if (fighterCanMove) {
                    moveFighterView()
                    binding.tvInfo.text = "Choice your action"
                    binding.btnMove.isEnabled = false
                } else {
                    Toast.makeText(requireContext(), "So far, bastard...", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        lifecycleScope.launch {
            viewModel.actionResult.collect { resultMessage ->
                binding.tvInfo.text = resultMessage
                //Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_LONG).show()
                if (actualFighter.actionPerformed) {
                    disableActionButtons()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.dyingFighter.collect { dyingFighter ->
                val indexOfDyingFighter: Int

                if (heroesList.contains(dyingFighter)) {
                    indexOfDyingFighter = heroesList.indexOf(dyingFighter)
                    heroesList.removeAt(indexOfDyingFighter)
                    ivHeroesList[indexOfDyingFighter].visibility = View.GONE
                    ivHeroesList.removeAt(indexOfDyingFighter)
                } else {
                    indexOfDyingFighter = villainsList.indexOf(dyingFighter)
                    villainsList.removeAt(indexOfDyingFighter)
                    ivVillainsList[indexOfDyingFighter].visibility = View.GONE
                    ivVillainsList.removeAt(indexOfDyingFighter)
                }

                if (heroesList.isEmpty() || villainsList.isEmpty()) {
                    // TODO: Esto es un finish de prueba
                    Toast.makeText(requireContext(), "FINISHHHHHHH", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun addHeroesToLists(
        heroesList: ArrayList<FighterModel>,
        villainsList: ArrayList<FighterModel>
    ) {
        this.heroesList.addAll(heroesList)
        this.villainsList.addAll(villainsList)
    }

    private fun showHeroesMiniatures(
        heroesList: ArrayList<FighterModel>,
        villainsList: ArrayList<FighterModel>
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

    private fun finishTurn() {
        // TODO: están aquí las cosas de final de turno, cuidado
        enableButtons()
        playerChoice = PlayerChoice.WAITING_FOR_ACTION
        indexOfActualFighter = -1
        viewModel.finishTurn()
        binding.tvInfo.text = "Choice your action!"
    }

    private fun disableActionButtons() {
        with(binding) {
            btnSabotage.isEnabled = false
            btnSupport.isEnabled = false
            btnDefend.isEnabled = false
            btnAttack.isEnabled = false
        }
    }

    private fun enableButtons() {
        with(binding) {
            btnMove.isEnabled = true
            btnSabotage.isEnabled = true
            btnSupport.isEnabled = true
            btnDefend.isEnabled = true
            btnAttack.isEnabled = true
        }
    }
}