package com.example.heroes_fight.ui.cards_collection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.data.utils.CardsFiller
import com.example.heroes_fight.databinding.FragmentCardsCollectionBinding
import com.example.heroes_fight.ui.cards_collection_fragment.adapter.CardsCollectionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CardsCollectionFragment : Fragment(), CardsCollectionAdapter.CardListener {


    private val viewModel: CardsCollectionViewModel by viewModels()

    private lateinit var binding: FragmentCardsCollectionBinding
    private lateinit var adapter: CardsCollectionAdapter

    private var selectedHero = HeroModel()
    private var isFullCollection = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = CardsCollectionAdapter(requireContext(), this, mutableListOf())
        //viewModel.getCardsList()
    }

    override fun onResume() {
        super.onResume()
        if (selectedHero.id > 0) {
            setInfoInBigCardAndShow()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCardsCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        observeViewModel()

        setupListeners()

        //setupSearchView()

        //setupSwipeToRefresh()
    }

    /*private fun setupSwipeToRefresh() {
        with(binding.swipeToRefresh) {
            setOnRefreshListener {
                viewModel.restartList()
                viewModel.getCardsList()
                isRefreshing = false
            }
        }
    }*/

    /*private fun setupSearchView() {
        with(binding.searchView) {

            setOnClickListener {
                isIconified = false
            }

            setOnQueryTextFocusChangeListener { _, hasFocus ->
                isIconified = !hasFocus
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        isFullCollection = false
                        viewModel.searchHero(query)
                        clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })
        }
    }*/

    private fun setupListeners() {
        with(binding) {

            cardIncludedGood.btnBiography.setOnClickListener {
                findNavController().navigate(
                    CardsCollectionFragmentDirections.actionCardsCollectionFragmentToCardDetailFragment(
                        selectedHero.id
                    )
                )
            }

            cardIncludedGood.btnAppearance.setOnClickListener {
                findNavController().navigate(
                    CardsCollectionFragmentDirections.actionCardsCollectionFragmentToAppearanceFragment(
                        selectedHero.id
                    )
                )
            }

            cardIncludedBad.btnAppearance.setOnClickListener {
                findNavController().navigate(
                    CardsCollectionFragmentDirections.actionCardsCollectionFragmentToAppearanceFragment(
                        selectedHero.id
                    )
                )
            }

            cardIncludedBad.btnBiography.setOnClickListener {
                findNavController().navigate(
                    CardsCollectionFragmentDirections.actionCardsCollectionFragmentToCardDetailFragment(
                        selectedHero.id
                    )
                )
            }

            cardIncludedGood.card.setOnClickListener {
                searchView.visibility = View.VISIBLE
                it.visibility = View.GONE
                selectedHero = HeroModel()
            }

            cardIncludedBad.card.setOnClickListener {
                searchView.visibility = View.VISIBLE
                it.visibility = View.GONE
                selectedHero = HeroModel()
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { cardsCollectionUiState ->
                when (cardsCollectionUiState) {
                    is CardsCollectionUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.refreshList(cardsCollectionUiState.cardsList)
                    }

                    is CardsCollectionUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is CardsCollectionUiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            cardsCollectionUiState.errorModel.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun setupAdapter() {
        val listManager = GridLayoutManager(requireContext(), 4)

        with(binding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = adapter
        }
        //setupPagination()
    }

    /*private fun setupPagination() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager: GridLayoutManager? =
                    recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager?.findLastCompletelyVisibleItemPosition() == recyclerView.adapter?.itemCount?.minus(
                        1
                    )
                ) {
                    if (isFullCollection) {
                        viewModel.getCardsList()
                        Log.i("quique", "Ha entrado en el if de la paginación")
                    }
                }
            }
        })
    }*/

    override fun onClick(position: Int) {
        binding.searchView.visibility = View.GONE
        with(binding) {
            if (cardIncludedBad.card.isVisible || cardIncludedGood.card.isVisible) {
                cardIncludedBad.card.visibility = View.GONE
                cardIncludedGood.card.visibility = View.GONE
            }
            selectedHero = viewModel.getHeroFromList(position)
            setInfoInBigCardAndShow()

        }

    }

    private fun setInfoInBigCardAndShow() {
        if (selectedHero.alignment == "good") {

            CardsFiller.fillDataIntoGoodCard(
                binding.cardIncludedGood,
                selectedHero,
                requireContext()
            )
            binding.cardIncludedGood.card.visibility = View.VISIBLE

        } else if (selectedHero.alignment == "bad") {

            CardsFiller.fillDataIntoBadCard(
                binding.cardIncludedBad,
                selectedHero,
                requireContext()
            )
            binding.cardIncludedBad.card.visibility = View.VISIBLE
        }
    }
}