package com.example.heroes_fight.ui.fight_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.databinding.FragmentFightBinding
import com.example.heroes_fight.utils.CardsFiller
import com.example.heroes_fight.utils.PlayerChoice
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class FightFragment : Fragment() {

    //EL BIDIMENSIONAL HACE [Y][X] Y NO AL REVÉS

    private lateinit var binding: FragmentFightBinding
    private val viewModel: FightFragmentViewModel by viewModels()

    private val board = Array(10) { arrayOfNulls<View>(9) }

    private val heroesList = ArrayList<FighterModel>()
    private val villainsList = ArrayList<FighterModel>()
    private var allFightersList = ArrayList<FighterModel>()
    private val ivHeroesList = ArrayList<ShapeableImageView>()
    private val ivVillainsList = ArrayList<ShapeableImageView>()
    private val ivAllFightersList = ArrayList<ShapeableImageView>()

    // TODO: prueba disparos
    private val rocks = ArrayList<RockModel>()
    private val ivRocks = ArrayList<ImageView>()

    private var playerChoice = PlayerChoice.WAITING_FOR_ACTION
    private var indexOfActualFighter = -1
    private var initiativeIndex = 0
    private var actualFighter = FighterModel()
    private var destinationPosition = Position()

    private var isFirstTurn = true

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
            binding.btnPass.isEnabled = true
        }
        binding.cardIncludedGood.card.setOnClickListener {
            it.visibility = View.GONE
            binding.btnPass.isEnabled = true
        }
    }

    private fun setupBtnActionsListeners() {
        with(binding) {
            btnMove.setOnClickListener {
                repaintBoard()
                playerChoice = PlayerChoice.MOVE
                tvInfo.text = getString(R.string.selectCellToMove)
                paintAccessibleTiles(actualFighter.movementCapacity)
            }
            btnAttack.setOnClickListener {
                repaintBoard()
                tvInfo.text = getString(R.string.selectEnemyToAttack)
                playerChoice = PlayerChoice.ATTACK
                paintMeleeDistance()
            }
            btnDefend.setOnClickListener {
                repaintBoard()
                tvInfo.text = getString(R.string.selectOwnHero)
                playerChoice = PlayerChoice.DEFEND
                board[actualFighter.position.y][actualFighter.position.x]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
            }
            btnSupport.setOnClickListener {
                repaintBoard()
                tvInfo.text = getString(R.string.selectAllyToSupport)
                playerChoice = PlayerChoice.SUPPORT
                paintMeleeDistance()
            }
            btnSabotage.setOnClickListener {
                repaintBoard()
                tvInfo.text = getString(R.string.selectEnemyToSabotage)
                playerChoice = PlayerChoice.SABOTAGE
                paintMeleeDistance()
            }
            btnPass.setOnClickListener {
                repaintBoard()
                finishTurn()
            }

            btnShot.setOnClickListener {
                repaintBoard()
                tvInfo.text = getString(R.string.selectEnemyToShot)
                playerChoice = PlayerChoice.SHOT
                paintAccessibleTiles(actualFighter.distanceToShot)
            }
        }
    }

    private fun paintMeleeDistance() {
        with(actualFighter.position) {
            if (y > 0) {
                board[y - 1][x]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
            }
            if (x > 0) {
                board[y][x - 1]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
            }
            if (y < 9) {
                board[y + 1][x]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
            }
            if (x < 8) {
                board[y][x + 1]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
            }
        }
    }

    private fun paintAccessibleTiles(statOfFighterToUse: Int) {
        Log.i("quique", "NUEVA RONDA")
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                var difference: Int
                var result: Int
                with(actualFighter.position) {
                    if (x < j && y < i) {
                        if (x + statOfFighterToUse >= j) {
                            difference = j - x
                            result = statOfFighterToUse - difference

                            if (y + result >= i) {
                                board[i][j]!!.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.tile_to_move
                                    )
                            }
                        }
                    } else if (x > j && y > i) {
                        if (x - statOfFighterToUse <= j) {
                            difference = x - j
                            result = statOfFighterToUse - difference
                            if (y - result <= i) {
                                board[i][j]!!.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.tile_to_move
                                    )
                            }
                        }
                    } else if (x < j && y > i) {
                        if (x + statOfFighterToUse >= j) {
                            difference = j - x
                            result = statOfFighterToUse - difference
                            if (y - result <= i) {
                                board[i][j]!!.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.tile_to_move
                                    )
                            }
                        }
                    } else if (x > j && y < i) {
                        if (x - statOfFighterToUse <= j) {
                            difference = x - j
                            result = statOfFighterToUse - difference
                            if (y + result >= i) {
                                board[i][j]!!.background =
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.tile_to_move
                                    )
                            }
                        }
                    } else if (x == j && y < i) {
                        if (y + statOfFighterToUse >= i) {
                            board[i][j]!!.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
                        }
                    } else if (x == j && y > i) {
                        if (y - statOfFighterToUse <= i) {
                            board[i][j]!!.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
                        }
                    } else if (x < j && y == i) {
                        if (x + statOfFighterToUse >= j) {
                            board[i][j]!!.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
                        }
                    } else if (x > j && y == i) {
                        if (x - statOfFighterToUse <= j) {
                            board[i][j]!!.background =
                                ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
                        }
                    }
                }
            }
        }
    }

    private fun repaintBoard() {
        for (i in 0 until 10) {
            for (j in 0 until 9) {
                board[i][j]!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_base)
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
                binding.btnPass.isEnabled = false

            }
            ivHeroesList[i].setOnClickListener {
                showHeroCard(i)
                binding.btnPass.isEnabled = false
            }

            ivVillainsList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttack(i, false)
                    PlayerChoice.SABOTAGE -> performSabotage(i, false)
                    PlayerChoice.DEFEND -> performDefense(i, false)
                    PlayerChoice.SUPPORT -> performSupport(i, false)
                    PlayerChoice.SHOT -> performShot(i, false)
                    else -> showToast("Choice an action")
                }
                true
            }
            ivHeroesList[i].setOnLongClickListener {
                when (playerChoice) {
                    PlayerChoice.ATTACK -> performAttack(i, true)
                    PlayerChoice.SABOTAGE -> performSabotage(i, true)
                    PlayerChoice.DEFEND -> performDefense(i, true)
                    PlayerChoice.SUPPORT -> performSupport(i, true)
                    PlayerChoice.SHOT -> performShot(i, true)
                    else -> showToast("Choice an action")
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

    private fun performShot(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (!actualFighter.isHero && isClickOnHero) {
            viewModel.performShot(heroesList[indexOfFighterSelected], rocks)
        } else if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performShot(villainsList[indexOfFighterSelected], rocks)
        } else {
            showToast("Don't shot your own team")
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
                        putHeroesInTheInitiativeList(fightFragmentUiState.allFightersSorted)

                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.actualFighter.collect { actualFighter ->
                if (actualFighter.id != 0) {
                    binding.ivSpeechBubble.visibility = View.VISIBLE
                    binding.tvInfo.text = getString(R.string.choiceAction)

                    this@FightFragment.actualFighter = actualFighter

                    checkIfSabotaged()

                    extractActualFighterIndex()

                    changeBorderOfActualFighter()

                    setBorderAtInitiativeList()

                    showAllViews()
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
                    repaintBoard()
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
                    repaintBoard()
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

                removeOfInitiativeList(dyingFighter)
            }
        }
    }

    private fun removeOfInitiativeList(dyingFighter: FighterModel) {
        val dyingFighterFiltered = allFightersList.filter { it.id == dyingFighter.id }
        val indexOfDyingFighter = allFightersList.indexOf(dyingFighterFiltered[0])
        ivAllFightersList[indexOfDyingFighter].visibility = View.GONE
        ivAllFightersList.removeAt(indexOfDyingFighter)
        allFightersList.removeAt(indexOfDyingFighter)
        initiativeIndex = allFightersList.indexOf(actualFighter)
    }

    private fun showAllViews() {
        if (isFirstTurn) {
            binding.linearInitiative.root.visibility = View.VISIBLE
            ivHeroesList.forEach { it.visibility = View.VISIBLE }
            ivVillainsList.forEach { it.visibility = View.VISIBLE }
            isFirstTurn = false
        }
    }

    private fun setBorderAtInitiativeList() {
        ivAllFightersList[initiativeIndex].strokeWidth = 20f
        ivAllFightersList[initiativeIndex].setStrokeColorResource(R.color.greenTurn)

        if (initiativeIndex != 0) {
            ivAllFightersList[initiativeIndex - 1].strokeWidth = 0f
        } else {
            ivAllFightersList[ivAllFightersList.size - 1].strokeWidth = 0f
        }

    }

    private fun changeBorderOfActualFighter() {
        if (actualFighter.isHero) {
            ivHeroesList[indexOfActualFighter].setStrokeColorResource(R.color.greenTurn)
        } else {
            ivVillainsList[indexOfActualFighter].setStrokeColorResource(R.color.greenTurn)
        }
    }

    private fun putHeroesInTheInitiativeList(allFightersSorted: ArrayList<FighterModel>) {
        allFightersList.addAll(allFightersSorted)

        for (i in 0 until allFightersSorted.size) {
            ivAllFightersList.add(binding.linearInitiative.linearLayout.getChildAt(i) as ShapeableImageView)
            showImgWithGlide(
                allFightersSorted[i].image,
                binding.linearInitiative.linearLayout.getChildAt(i) as ShapeableImageView
            )
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
            ivRocks.addAll(
                listOf(
                    ivRock0,
                    ivRock1,
                    ivRock2,
                    ivRock3,
                    ivRock4,
                    ivRock5,
                    ivRock6,
                    ivRock7,
                    ivRock8,
                    ivRock9,
                    ivRock10,
                    ivRock11,
                    ivRock12,
                    ivRock13,
                    ivRock14
                )
            )
        }

        // TODO: esta función no debería estar aquí
        //  tengo que refactorizar el código antes
        //  de seguir avanzando con otra cosa
        getRocks()

        putRocks()
    }

    private fun putRocks() {
        for (i in 0 until rocks.size) {

            ivRocks[i].visibility = View.VISIBLE

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)

            constraintSet.connect(
                ivRocks[i].id,
                ConstraintSet.TOP,
                board[rocks[i].position.y][rocks[i].position.x]!!.id,
                ConstraintSet.TOP
            )
            constraintSet.connect(
                ivRocks[i].id,
                ConstraintSet.START,
                board[rocks[i].position.y][rocks[i].position.x]!!.id,
                ConstraintSet.START
            )
            Log.i(
                "quique",
                "Posición de la roca nº $i, y = ${rocks[i].position.y}, x = ${rocks[i].position.x}"
            )
            constraintSet.applyTo(binding.root)
        }
    }

    private fun getRocks() {
        val randomQuantityRocks = Random.nextInt(8, 16)
        for (i in 1..randomQuantityRocks) {
            val randomY = Random.nextInt(1, 9)
            val randomX = Random.nextInt(0, 9)
            rocks.add(RockModel(Position(randomY, randomX)))
        }
    }

    private fun finishTurn() {
        if (actualFighter.isHero) {
            ivHeroesList[indexOfActualFighter].setStrokeColorResource(R.color.blueGood)
        } else {
            ivVillainsList[indexOfActualFighter].setStrokeColorResource(R.color.redBad)
        }
        enableButtons()
        playerChoice = PlayerChoice.WAITING_FOR_ACTION
        indexOfActualFighter = -1
        initiativeIndex++
        if (initiativeIndex > ivAllFightersList.size - 1) {
            initiativeIndex = 0
        }
        viewModel.finishTurn()
    }

    private fun disableActionButtons() {
        with(binding) {
            btnSabotage.isEnabled = false
            btnSupport.isEnabled = false
            btnDefend.isEnabled = false
            btnAttack.isEnabled = false
            btnShot.isEnabled = false
        }
    }

    private fun enableButtons() {
        with(binding) {
            btnMove.isEnabled = true
            btnSabotage.isEnabled = true
            btnSupport.isEnabled = true
            btnDefend.isEnabled = true
            btnAttack.isEnabled = true
            btnShot.isEnabled = true
        }
    }

    private fun showToast(textToShow: String) {
        Toast.makeText(requireContext(), textToShow, Toast.LENGTH_SHORT).show()
    }

    private fun showImgWithGlide(url: String, imageView: ShapeableImageView) {
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.question_mark)
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
    }

    private fun showVillainCard(indexOfVillain: Int) {
        CardsFiller.fillDataIntoVillainFighterCard(
            binding.cardIncludedBad,
            villainsList[indexOfVillain],
            requireContext()
        )
        binding.cardIncludedBad.card.visibility = View.VISIBLE
    }
}