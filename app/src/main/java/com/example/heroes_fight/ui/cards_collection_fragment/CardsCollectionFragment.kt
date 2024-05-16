package com.example.heroes_fight.ui.cards_collection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes_fight.databinding.FragmentCardsCollectionBinding
import com.example.heroes_fight.utils.CardsFiller
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CardsCollectionFragment : Fragment(), CardsCollectionAdapter.CardListener {


    private val viewModel: CardsCollectionViewModel by viewModels()

    private lateinit var binding: FragmentCardsCollectionBinding
    private lateinit var adapter: CardsCollectionAdapter

    private var idSelectedHero = 0
    private var isGoodCard: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = CardsCollectionAdapter(requireContext(), this, mutableListOf())
        viewModel.getCardsList()
    }

    override fun onResume() {
        super.onResume()

        if (isGoodCard != null) {
            when (isGoodCard!!) {
                true -> binding.cardIncludedGood.card.visibility = View.VISIBLE
                false -> binding.cardIncludedBad.card.visibility = View.VISIBLE
            }
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
    }

    private fun setupListeners() {
        binding.cardIncludedGood.btnBiography.setOnClickListener {
            findNavController().navigate(
                CardsCollectionFragmentDirections.actionCardsCollectionFragmentToCardDetailFragment(
                    idSelectedHero
                )
            )
        }

        binding.cardIncludedGood.btnAppearance.setOnClickListener {
            findNavController().navigate(
                CardsCollectionFragmentDirections.actionCardsCollectionFragmentToAppearanceFragment(
                    idSelectedHero
                )
            )
        }

        binding.cardIncludedBad.btnAppearance.setOnClickListener {
            findNavController().navigate(
                CardsCollectionFragmentDirections.actionCardsCollectionFragmentToAppearanceFragment(
                    idSelectedHero
                )
            )
        }

        binding.cardIncludedBad.btnBiography.setOnClickListener {
            findNavController().navigate(
                CardsCollectionFragmentDirections.actionCardsCollectionFragmentToCardDetailFragment(
                    idSelectedHero
                )
            )
        }

        binding.cardIncludedGood.card.setOnClickListener {
            it.visibility = View.GONE
        }

        binding.cardIncludedBad.card.setOnClickListener {
            it.visibility = View.GONE
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
                            "ERROR ERROR ERROR ERROR",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }


        lifecycleScope.launch {
            viewModel.selectedHero.collect { selectedHero ->

                idSelectedHero = selectedHero.id

                if (selectedHero.alignment == "good") {
                    isGoodCard = true
                    CardsFiller.fillDataIntoGoodCard(
                        binding.cardIncludedGood,
                        selectedHero,
                        requireContext()
                    )
                    binding.cardIncludedGood.card.visibility = View.VISIBLE

                } else if (selectedHero.alignment == "bad") {
                    isGoodCard = false
                    CardsFiller.fillDataIntoBadCard(
                        binding.cardIncludedBad,
                        selectedHero,
                        requireContext()
                    )
                    binding.cardIncludedBad.card.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setupAdapter() {
        val listManager = GridLayoutManager(requireContext(), 2)

        with(binding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = listManager
            recyclerView.adapter = adapter
        }
        setupPagination()
    }

    private fun setupPagination() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager: GridLayoutManager? =
                    recyclerView.layoutManager as GridLayoutManager?
                if (layoutManager?.findLastCompletelyVisibleItemPosition() == recyclerView.adapter?.itemCount?.minus(
                        1
                    )
                ) {
                    viewModel.getCardsList()
                }
            }
        })
    }

    override fun onClick(position: Int) {
        viewModel.getHeroFromList(position)
    }
}