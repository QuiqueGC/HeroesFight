package com.example.heroes_fight.ui.fight_fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.constants.MyConstants.BOARD_MAX_COLUMNS
import com.example.heroes_fight.data.constants.MyConstants.BOARD_MAX_LINES
import com.example.heroes_fight.data.constants.MyConstants.MAX_ROCKS
import com.example.heroes_fight.data.domain.model.common.ActionResultModel
import com.example.heroes_fight.data.domain.model.common.Position
import com.example.heroes_fight.data.domain.model.common.RockModel
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.utils.CardsFiller
import com.example.heroes_fight.data.utils.PlayerChoice
import com.example.heroes_fight.databinding.FragmentFightBinding
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class FightFragment : Fragment() {

    //EL BIDIMENSIONAL HACE [Y][X] Y NO AL REVÉS

    open lateinit var binding: FragmentFightBinding
    open val viewModel: FightFragmentViewModel by viewModels()

    open val board = List(10) { arrayOfNulls<View>(9) }

    open val heroes = mutableListOf<FighterModel>()
    open val villains = mutableListOf<FighterModel>()
    private var allFighters = mutableListOf<FighterModel>()
    open val ivHeroes = mutableListOf<ShapeableImageView>()
    open val ivVillains = mutableListOf<ShapeableImageView>()
    private val ivAllFighters = mutableListOf<ShapeableImageView>()
    open val actionButtons = mutableListOf<Button>()
    open val rocks = mutableListOf<RockModel>()
    private val ivRocks = mutableListOf<ImageView>()

    private var playerChoice = PlayerChoice.WAITING_FOR_ACTION
    open var indexOfActualFighter = -1
    private var initiativeIndex = 0
    private val middleColumn = 4
    open var actualFighter = FighterModel()
    open var destinationPosition = Position()

    private var isFirstTurn = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFightBinding.inflate(inflater, container, false)

        setupFightersInSameDevice()

        addActionButtonsToList()
        return binding.root
    }

    open fun setupFightersInSameDevice() {
        viewModel.getRandomHeroes()
    }

    private fun addActionButtonsToList() {
        with(binding) {
            actionButtons.addAll(
                listOf(
                    btnAttack, btnShot, btnMove, btnDefend, btnSupport, btnSabotage
                )
            )
        }
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

        with(binding) {
            cardIncludedBad.card.setOnClickListener {
                it.visibility = View.GONE
            }
            cardIncludedGood.card.setOnClickListener {
                it.visibility = View.GONE
            }
            imgReferee.setOnClickListener {
                it.visibility = View.GONE
                ivSpeechBubble.visibility = View.GONE
            }
            ivSpeechBubble.setOnClickListener {
                it.visibility = View.GONE
                imgReferee.visibility = View.GONE
            }
        }
    }

    private fun setupBtnActionsListeners() {
        with(binding) {
            btnMove.setOnClickListener {
                refreshBoard()
                playerChoice = PlayerChoice.MOVE
                tvInfo.text = getString(R.string.selectCellToMove)
                paintAccessibleTiles()
                //setColorActionButtons(btnMove)
            }

            btnAttack.setOnClickListener {
                refreshBoard()
                tvInfo.text = getString(R.string.selectEnemyToAttack)
                playerChoice = PlayerChoice.ATTACK
                paintAccessibleTiles()
                //setColorActionButtons(btnAttack)
            }
            btnDefend.setOnClickListener {
                refreshBoard()
                tvInfo.text = getString(R.string.selectOwnHero)
                playerChoice = PlayerChoice.DEFEND
                //setColorActionButtons(btnDefend)
            }

            btnSupport.setOnClickListener {
                refreshBoard()
                tvInfo.text = getString(R.string.selectAllyToSupport)
                playerChoice = PlayerChoice.SUPPORT
                paintAccessibleTiles()
                //setColorActionButtons(btnSupport)
            }
            btnSabotage.setOnClickListener {
                refreshBoard()
                tvInfo.text = getString(R.string.selectEnemyToSabotage)
                playerChoice = PlayerChoice.SABOTAGE
                paintAccessibleTiles()
                //setColorActionButtons(btnSabotage)
            }

            btnShot.setOnClickListener {
                refreshBoard()
                tvInfo.text = getString(R.string.selectEnemyToShot)
                playerChoice = PlayerChoice.SHOT
                paintAccessibleTiles()
                //setColorActionButtons(btnShot)
            }

            btnPass.setOnClickListener {
                refreshBoard()
                finishTurn()
                //setColorActionButtons(null)
            }

        }
    }


    private fun paintAccessibleTiles() {
        val markedTiles = viewModel.getAccessibleTiles(playerChoice)
        for (i in 0 until BOARD_MAX_LINES) {
            for (j in 0 until BOARD_MAX_COLUMNS) {
                if (markedTiles[i][j]!!) {
                    board[i][j]!!.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.tile_to_move)
                }
            }
        }
    }

    private fun refreshBoard() {
        for (line in board) {
            for (column in line) {
                column!!.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.tile_base2)
            }
        }
    }

    private fun setupTilesListeners() {
        for (i in 0 until BOARD_MAX_LINES) {
            for (j in 0 until BOARD_MAX_COLUMNS) {
                board[i][j]!!.setOnClickListener { _ ->
                    if (playerChoice == PlayerChoice.MOVE) {
                        destinationPosition = Position(i, j)
                        if (viewModel.performMovement(destinationPosition)) {
                            moveFighterView()
                            updateBoardAfterMovement()
                        } else {
                            showToast("So far, bastard...")
                        }
                    }
                }
            }
        }
    }

    open fun moveFighterView() {
        val constraintSet = ConstraintSet()

        constraintSet.clone(binding.root)

        if (actualFighter.isHero) {
            moveHeroOrVillain(constraintSet, ivHeroes)
        } else {
            moveHeroOrVillain(constraintSet, ivVillains)
        }
        constraintSet.applyTo(binding.root)
    }

    private fun moveHeroOrVillain(
        constraintSet: ConstraintSet,
        ivFightersList: List<ShapeableImageView>
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

    open fun moveTvActionResultRight(ivFighter: View) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(
            binding.tvActionResultRight.id,
            ConstraintSet.BOTTOM,
            ivFighter.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            binding.tvActionResultRight.id,
            ConstraintSet.START,
            ivFighter.id,
            ConstraintSet.END
        )
        constraintSet.applyTo(binding.root)
    }

    open fun moveTvActionResultLeft(ivFighter: View) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(
            binding.tvActionResultLeft.id,
            ConstraintSet.BOTTOM,
            ivFighter.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            binding.tvActionResultLeft.id,
            ConstraintSet.END,
            ivFighter.id,
            ConstraintSet.START
        )
        constraintSet.applyTo(binding.root)
    }

    private fun updateBoardAfterMovement() {
        binding.tvInfo.text = getString(R.string.choiceAction)
        binding.btnMove.isEnabled = false
        //binding.btnMove.visibility = View.INVISIBLE
        binding.btnDefend.isEnabled = false
        //binding.btnDefend.visibility = View.INVISIBLE

        refreshBoard()

        if (actualFighter.actionPerformed && actualFighter.movementPerformed) {
            binding.btnPass.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.greenTurn
                )
            )
        }
    }

    private fun setupFightersListeners() {
        for (i in 0 until ivHeroes.size) {

            ivVillains[i].setOnClickListener { _ ->
                showVillainCard(i)
            }
            ivHeroes[i].setOnClickListener {
                showHeroCard(i)
            }

            ivVillains[i].setOnLongClickListener {
                //moveTvActionResultRight(it)
                //moveTvActionResultLeft(it)
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
            ivHeroes[i].setOnLongClickListener {
                //moveTvActionResultRight(it)
                //moveTvActionResultLeft(it)
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
            viewModel.performSupport(heroes[indexOfFighterSelected])
        } else if (!actualFighter.isHero && !isClickOnHero) {
            viewModel.performSupport(villains[indexOfFighterSelected])
        } else {
            showToast("Don't support the enemy")
        }
    }

    private fun performDefense(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (isClickOnHero && heroes[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else if (!isClickOnHero && villains[indexOfFighterSelected] == actualFighter) {
            viewModel.performDefense()
        } else {
            showToast("Only can use in himself")
        }
    }

    private fun performSabotage(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performSabotage(villains[indexOfFighterSelected])
        } else if (!actualFighter.isHero && isClickOnHero)
            viewModel.performSabotage(heroes[indexOfFighterSelected])
        else {
            showToast("Don't sabotage your own team")
        }
    }

    private fun performAttack(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (!actualFighter.isHero && isClickOnHero) {
            viewModel.performAttack(heroes[indexOfFighterSelected])
        } else if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performAttack(villains[indexOfFighterSelected])
        } else {
            showToast("Don't attack your own team")
        }
    }

    private fun performShot(indexOfFighterSelected: Int, isClickOnHero: Boolean) {
        if (!actualFighter.isHero && isClickOnHero) {
            viewModel.performShot(heroes[indexOfFighterSelected], rocks)
        } else if (actualFighter.isHero && !isClickOnHero) {
            viewModel.performShot(villains[indexOfFighterSelected], rocks)
        } else {
            showToast("Don't shot your own team")
        }
    }

    private fun observeViewModel() {

        collectUiState()


        collectActualFighter()


        collectActionResult()


        collectDyingFighter()


        collectFinishBattle()
    }

    open fun collectFinishBattle() {
        lifecycleScope.launch {
            viewModel.finishBattle.collect {
                if (it != null) {
                    findNavController().navigate(
                        FightFragmentDirections.actionFightFragmentToScoreFragment(
                            it
                        )
                    )
                }
            }
        }
    }

    open fun collectDyingFighter() {
        lifecycleScope.launch {
            viewModel.dyingFighter.collect { dyingFighter ->
                val indexOfDyingFighter: Int
                if (heroes.contains(dyingFighter)) {
                    indexOfDyingFighter = heroes.indexOf(dyingFighter)
                    ivHeroes[indexOfDyingFighter].visibility = View.GONE

                    showReferee(
                        "${actualFighter.name} has killed " + heroes[indexOfDyingFighter].name + "!!!"
                    )

                } else {
                    indexOfDyingFighter = villains.indexOf(dyingFighter)
                    ivVillains[indexOfDyingFighter].visibility = View.GONE
                    showReferee(
                        "${actualFighter.name} has killed " + villains[indexOfDyingFighter].name + "!!!"
                    )
                }
                removeOfInitiativeList(dyingFighter)
            }
        }
    }

    open fun collectActionResult() {
        lifecycleScope.launch {
            viewModel.actionResult.collect { actionResultModel ->

                if (actualFighter.actionPerformed) {
                    disableActionButtons(binding.btnMove)
                    refreshBoard()
                    showInfo(actionResultModel)
                    startTimerToHideTvResult()
                } else {
                    showToast(actionResultModel.txtToTvInfo)
                }
                if (actualFighter.movementPerformed) {
                    binding.btnMove.isEnabled = false
                    //binding.btnMove.visibility = View.INVISIBLE
                }
                if (actualFighter.actionPerformed && actualFighter.movementPerformed) {
                    binding.btnPass.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.greenTurn
                        )
                    )
                }
            }
        }
    }

    open fun collectActualFighter() {
        lifecycleScope.launch {
            viewModel.actualFighter.collect { actualFighter ->
                if (actualFighter.id != 0) {

                    binding.tvInfo.text = getString(R.string.choiceAction)

                    this@FightFragment.actualFighter = actualFighter

                    checkIfSabotaged()

                    extractActualFighterIndex()

                    changeBorderOfActualFighter()

                    setBorderAtInitiativeList()

                    if (isFirstTurn) {
                        showAllViews()
                    }
                }
            }
        }
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { fightFragmentUiState ->

                when (fightFragmentUiState) {
                    is FightFragmentUiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is FightFragmentUiState.Error -> {
                        showToast(fightFragmentUiState.errorModel.message)
                        binding.progressBar.visibility = View.GONE
                    }
                    is FightFragmentUiState.Success -> {
                        completeBattlefield(fightFragmentUiState)
                    }
                }
            }
        }
    }

    open fun completeBattlefield(fightFragmentUiState: FightFragmentUiState.Success) {
        showHeroesMiniatures(
            fightFragmentUiState.heroesList,
            fightFragmentUiState.villainsList
        )
        addFightersToLists(
            fightFragmentUiState.heroesList,
            fightFragmentUiState.villainsList
        )
        putFightersInTheInitiativeList(fightFragmentUiState.allFightersSorted)
    }

    open fun showInfo(actionResultModel: ActionResultModel) {
        binding.tvInfo.text = actionResultModel.txtToTvInfo
        binding.tvActionResultRight.text = actionResultModel.txtToTvActionResult
        binding.tvActionResultLeft.text = actionResultModel.txtToTvActionResult
        if (actualFighter.position.x > middleColumn) {
            binding.tvActionResultLeft.visibility = View.VISIBLE
        } else {
            binding.tvActionResultRight.visibility = View.VISIBLE
        }
    }

    open fun startTimerToHideTvResult() {
        val timer = object : CountDownTimer(1500, 1500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                binding.tvActionResultRight.visibility = View.GONE
                binding.tvActionResultLeft.visibility = View.GONE
            }
        }
        timer.start()
    }

    open fun showReferee(sentenceToSay: String) {
        binding.imgReferee.visibility = View.VISIBLE
        binding.ivSpeechBubble.visibility = View.VISIBLE
        binding.tvKilledFighter.text = sentenceToSay
    }

    open fun removeOfInitiativeList(dyingFighter: FighterModel) {
        val dyingFighterFiltered = allFighters.filter { it.id == dyingFighter.id }
        val indexOfDyingFighter = allFighters.indexOf(dyingFighterFiltered.first())
        ivAllFighters[indexOfDyingFighter].visibility = View.GONE
        ivAllFighters.removeAt(indexOfDyingFighter)
        allFighters.removeAt(indexOfDyingFighter)
        initiativeIndex = allFighters.indexOf(actualFighter)
    }

    open fun showAllViews() {
        with(binding) {
            tvTurn.visibility = View.VISIBLE
            tvInfo.visibility = View.VISIBLE
            linearInitiative.root.visibility = View.VISIBLE
            btnPass.visibility = View.VISIBLE
        }
        actionButtons.forEach { it.visibility = View.VISIBLE }
        ivHeroes.forEach { it.visibility = View.VISIBLE }
        ivVillains.forEach { it.visibility = View.VISIBLE }
        isFirstTurn = false
    }

    private fun setBorderAtInitiativeList() {
        ivAllFighters[initiativeIndex].strokeWidth = 20f
        ivAllFighters[initiativeIndex].setStrokeColorResource(R.color.greenTurn)

        if (initiativeIndex != 0) {
            ivAllFighters[initiativeIndex - 1].strokeWidth = 0f
        } else {
            ivAllFighters[ivAllFighters.size - 1].strokeWidth = 0f
        }

    }

    private fun changeBorderOfActualFighter() {
        if (actualFighter.isHero) {
            ivHeroes[indexOfActualFighter].setStrokeColorResource(R.color.greenTurn)
        } else {
            ivVillains[indexOfActualFighter].setStrokeColorResource(R.color.greenTurn)
        }
    }

    private fun putFightersInTheInitiativeList(allFightersSorted: List<FighterModel>) {
        allFighters.addAll(allFightersSorted)

        for (i in allFightersSorted.indices) {
            ivAllFighters.add(binding.linearInitiative.linearLayout.getChildAt(i) as ShapeableImageView)
            showImgWithGlide(
                allFightersSorted[i].image,
                binding.linearInitiative.linearLayout.getChildAt(i) as ShapeableImageView
            )
        }
    }

    private fun extractActualFighterIndex() {
        if (actualFighter.isHero) {
            indexOfActualFighter = heroes.indexOf(heroes.find { it.id == actualFighter.id })
            moveTvActionResultRight(ivHeroes[indexOfActualFighter])
            moveTvActionResultLeft(ivHeroes[indexOfActualFighter])
        } else {
            indexOfActualFighter = villains.indexOf(villains.find { it.id == actualFighter.id })
            moveTvActionResultRight(ivVillains[indexOfActualFighter])
            moveTvActionResultLeft(ivVillains[indexOfActualFighter])
        }
    }

    private fun checkIfSabotaged() {
        if (actualFighter.isSabotaged) {
            binding.tvTurn.text = getString(R.string.sabotagedTurn, actualFighter.name)
            disableActionButtons(null)
        } else {
            binding.tvTurn.text = getString(R.string.newTurn, actualFighter.name)
        }
    }

    private fun addFightersToLists(
        heroesList: List<FighterModel>,
        villainsList: List<FighterModel>
    ) {
        this.heroes.addAll(heroesList)
        this.villains.addAll(villainsList)
    }

    private fun showHeroesMiniatures(
        heroesList: List<FighterModel>,
        villainsList: List<FighterModel>
    ) {
        for (i in heroesList.indices) {
            with(binding) {
                progressBar.visibility = View.GONE
                showImgWithGlide(heroesList[i].image, ivHeroes[i])
                showImgWithGlide(villainsList[i].image, ivVillains[i])
            }
        }
    }

    private fun setupBoard() {
        //empieza por uno porque hay una view antes de los tiles
        var indexOfChild = 1
        for (i in 0 until BOARD_MAX_LINES) {
            for (j in 0 until BOARD_MAX_COLUMNS) {
                board[i][j] = binding.root.getChildAt(indexOfChild)
                indexOfChild++
            }
        }
        with(binding) {
            ivHeroes.addAll(listOf(imgHero0, imgHero1, imgHero2, imgHero3, imgHero4))
            ivVillains.addAll(
                listOf(
                    imgVillain0,
                    imgVillain1,
                    imgVillain2,
                    imgVillain3,
                    imgVillain4
                )
            )

        }
        insertRockViewsAtList()
        setupRocks()
    }

    open fun setupRocks() {
        rocks.addAll(viewModel.getRocks())
        showRocks(rocks)
    }

    private fun insertRockViewsAtList() {
        var indexOfFirstElement = -1
        var loops = 0
        do {
            if (binding.root.getChildAt(loops) == binding.ivRock0) {
                indexOfFirstElement = loops
            }
            loops++
        } while (indexOfFirstElement == -1)

        for (i in 0 until MAX_ROCKS) {
            ivRocks.add(binding.root.getChildAt(indexOfFirstElement) as ImageView)
            indexOfFirstElement++
        }
    }

    open fun showRocks(rocks: MutableList<RockModel>) {
        for (i in rocks.indices) {

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


    open fun finishTurn() {
        if (actualFighter.isHero) {
            ivHeroes[indexOfActualFighter].setStrokeColorResource(R.color.blueGood)
        } else {
            ivVillains[indexOfActualFighter].setStrokeColorResource(R.color.redBad)
        }
        enableButtons()
        playerChoice = PlayerChoice.WAITING_FOR_ACTION
        indexOfActualFighter = -1
        initiativeIndex++
        if (initiativeIndex > ivAllFighters.size - 1) {
            initiativeIndex = 0
        }

        restoreBtnPassColor()

        viewModel.finishTurn()
    }

    private fun restoreBtnPassColor() {
        binding.btnPass.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
    }

    private fun disableActionButtons(noDisableButton: Button?) {
        for (btn in actionButtons) {
            if (btn != noDisableButton) {
                btn.isEnabled = false
                //btn.visibility = View.INVISIBLE
            }
        }
    }

    private fun enableButtons() {
        for (btn in actionButtons) {
            btn.isEnabled = true
            //btn.visibility = View.VISIBLE
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
            heroes[indexOfHero],
            requireContext()
        )
        binding.cardIncludedGood.card.visibility = View.VISIBLE
    }

    private fun showVillainCard(indexOfVillain: Int) {
        CardsFiller.fillDataIntoVillainFighterCard(
            binding.cardIncludedBad,
            villains[indexOfVillain],
            requireContext()
        )
        binding.cardIncludedBad.card.visibility = View.VISIBLE
    }
}