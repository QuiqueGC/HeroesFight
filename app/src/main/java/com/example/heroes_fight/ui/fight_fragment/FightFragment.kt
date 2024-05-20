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
                tvInfo.text = getString(R.string.selectCellToMove)
            }
            btnAttack.setOnClickListener {
                tvInfo.text = getString(R.string.selectEnemyToAttack)
                playerChoice = PlayerChoice.ATTACK
            }
            btnDefend.setOnClickListener {
                tvInfo.text = getString(R.string.selectOwnHero)
                playerChoice = PlayerChoice.DEFEND
            }
            btnSupport.setOnClickListener {
                tvInfo.text = getString(R.string.selectAllyToSupport)
                playerChoice = PlayerChoice.SUPPORT
            }
            btnSabotage.setOnClickListener {
                tvInfo.text = getString(R.string.selectEnemyToSabotage)
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
        val constraintSet = ConstraintSet()

        constraintSet.clone(binding.root)

        if (actualFighter.isHero) {
            moveHeroOrVillain(constraintSet, ivHeroesList)
        } else {
            moveHeroOrVillain(constraintSet, ivVillainsList)
        }
        constraintSet.applyTo(binding.root)
    }

    private fun moveHeroOrVillain(
        constraintSet: ConstraintSet,
        ivFightersList: java.util.ArrayList<ShapeableImageView>
    ) {
        constraintSet.connect(
            ivFightersList[indexOfActualFighter].id,
            ConstraintSet.TOP,
            board[destinationPosition.y][destinationPosition.x]!!.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            ivFightersList[indexOfActualFighter].id,
            ConstraintSet.START,
            board[destinationPosition.y][destinationPosition.x]!!.id,
            ConstraintSet.START
        )
    }

    private fun setupFightersListeners() {
        for (i in 0 until ivHeroesList.size) {

            ivVillainsList[i].setOnClickListener { _ ->
                showVillainCard(i)
            }
            ivHeroesList[i].setOnClickListener {
                showHeroCard(i)
            }

            ivVillainsList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttack(i, false)
                    PlayerChoice.SABOTAGE -> performSabotage(i, false)
                    PlayerChoice.DEFEND -> performDefense(i, false)
                    PlayerChoice.SUPPORT -> performSupport(i, false)
                    else -> {
                        showToast("Choice an action")
                    }
                }
                true
            }
            ivHeroesList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttack(i, true)
                    PlayerChoice.SABOTAGE -> performSabotage(i, true)
                    PlayerChoice.DEFEND -> performDefense(i, true)
                    PlayerChoice.SUPPORT -> performSupport(i, true)
                    else -> {
                        showToast("Choice an action")
                    }
                }
                true
            }

        }
    }

    private fun performSupport(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (actualFighter.isHero && isClickOnHero) {
            viewModel.performSupport(heroesList[indexOfFighterSelected])
        } else if (!actualFighter.isHero && !isClickOnHero) {
            viewModel.performSupport(villainsList[indexOfFighterSelected])
        } else {
            showToast("Don't support the enemy")
        }
    }

    private fun performDefense(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (isClickOnHero && heroesList[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else if (!isClickOnHero && villainsList[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else {
            showToast("Only can use in himself")
        }
    }

    private fun performSabotage(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performSabotage(villainsList[indexOfFighterSelected])
        } else if (!actualFighter.isHero && isClickOnHero)
            viewModel.performSabotage(heroesList[indexOfFighterSelected])
        else {
            showToast("Don't sabotage your own team")
        }
    }

    private fun performAttack(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (!actualFighter.isHero && isClickOnHero) {
            viewModel.performAttack(heroesList[indexOfFighterSelected])
        } else if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performAttack(villainsList[indexOfFighterSelected])
        } else {
            showToast("Don't attack your own team")
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { fightFragmentUiState ->

                when (fightFragmentUiState) {
                    is FightFragmentUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is FightFragmentUiState.Error -> showToast("ERROR ERROR ERROR")

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
                    binding.tvInfo.text = getString(R.string.choiceAction)
                    showImgWithGlide(actualFighter.image, binding.imgActualFighter)
                    this@FightFragment.actualFighter = actualFighter

                    checkIfSabotaged()

                    extractActualFighterIndex()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.fighterMovement.collect { fighterCanMove ->
                if (fighterCanMove) {
                    moveFighterView()
                    binding.tvInfo.text = getString(R.string.choiceAction)
                    binding.btnMove.isEnabled = false
                    binding.btnDefend.isEnabled = false
                } else {
                    showToast("So far, bastard...")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.actionResult.collect { resultMessage ->
                binding.tvInfo.text = resultMessage

                if (actualFighter.actionPerformed) {
                    disableActionButtons()
                }
                if (actualFighter.movementPerformed) {
                    binding.btnMove.isEnabled = false
                }
            }
        }

        lifecycleScope.launch {
            viewModel.dyingFighter.collect { dyingFighter ->
                val indexOfDyingFighter: Int

                if (heroesList.contains(dyingFighter)) {
                    indexOfDyingFighter = heroesList.indexOf(dyingFighter)
                    ivHeroesList[indexOfDyingFighter].visibility = View.GONE
                } else {
                    indexOfDyingFighter = villainsList.indexOf(dyingFighter)
                    ivVillainsList[indexOfDyingFighter].visibility = View.GONE
                }
            }
        }
    }

    private fun extractActualFighterIndex() {
        indexOfActualFighter = if (actualFighter.isHero) {
            heroesList.indexOf(heroesList.find { it.id == actualFighter.id })
        } else {
            villainsList.indexOf(villainsList.find { it.id == actualFighter.id })
        }
    }

    private fun checkIfSabotaged() {
        if (actualFighter.isSabotaged) {
            binding.tvTurn.text = getString(R.string.sabotagedTurn, actualFighter.name)
            disableActionButtons()
            binding.btnMove.isEnabled = false
        } else {
            binding.tvTurn.text = getString(R.string.newTurn, actualFighter.name)
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
                showImgWithGlide(heroesList[i].image, ivHeroesList[i])
                showImgWithGlide(villainsList[i].image, ivVillainsList[i])
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
        with(binding) {
            ivHeroesList.addAll(listOf(imgHero0, imgHero1, imgHero2, imgHero3, imgHero4))
            ivVillainsList.addAll(
                listOf(
                    imgVillain0,
                    imgVillain1,
                    imgVillain2,
                    imgVillain3,
                    imgVillain4
                )
            )
        }
    }

    private fun finishTurn() {
        enableButtons()
        playerChoice = PlayerChoice.WAITING_FOR_ACTION
        indexOfActualFighter = -1
        viewModel.finishTurn()
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

    private fun showToast(textToShow: String) {
        Toast.makeText(requireContext(), textToShow, Toast.LENGTH_SHORT).show()
    }

    private fun showImgWithGlide(url: String, imageView: ShapeableImageView) {
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.fight)
            .apply(RequestOptions().centerCrop())
            .into(imageView)
    }

    private fun showHeroCard(indexOfHero: Int) {
        CardsFiller.fillDataIntoHeroFighterCard(
            binding.cardIncludedGood,
            heroesList[indexOfHero],
            requireContext()
        )
        binding.cardIncludedGood.card.visibility = View.VISIBLE
        binding.cardIncludedGood.btnAppearance.visibility = View.GONE
        binding.cardIncludedGood.btnBiography.visibility = View.GONE
    }

    private fun showVillainCard(indexOfVillain: Int) {
        CardsFiller.fillDataIntoVillainFighterCard(
            binding.cardIncludedBad,
            villainsList[indexOfVillain],
            requireContext()
        )
        binding.cardIncludedBad.card.visibility = View.VISIBLE
        binding.cardIncludedBad.btnAppearance.visibility = View.GONE
        binding.cardIncludedBad.btnBiography.visibility = View.GONE
    }
}