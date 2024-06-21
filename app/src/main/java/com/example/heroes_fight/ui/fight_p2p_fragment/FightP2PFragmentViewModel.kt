package com.example.heroes_fight.ui.fight_p2p_fragment

import com.example.heroes_fight.data.domain.use_case.GetHeroesListUseCase
import com.example.heroes_fight.data.domain.use_case.GetVillainListUseCase
import com.example.heroes_fight.data.utils.BoardManager
import com.example.heroes_fight.ui.fight_fragment.FightFragmentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FightP2PFragmentViewModel @Inject constructor(
    private val getVillainListUseCase: GetVillainListUseCase,
    private val getHeroesListUseCase: GetHeroesListUseCase,
    private val boardManager: BoardManager
) : FightFragmentViewModel(getVillainListUseCase, getHeroesListUseCase, boardManager) {
}