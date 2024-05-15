package com.example.heroes_fight.ui.cards_collection_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes_fight.data.domain.repository.DataProvider
import com.example.heroes_fight.data.domain.repository.remote.RemoteDataSource
import com.example.heroes_fight.data.domain.use_case.GetHeroByIdUseCase
import com.example.heroes_fight.databinding.FragmentCardsCollectionBinding
import kotlinx.coroutines.launch
import javax.inject.Inject


class CardsCollectionFragment @Inject constructor(private var viewModel: CardsCollectionViewModel) :
    Fragment() {

    constructor() : this(CardsCollectionViewModel(GetHeroByIdUseCase(DataProvider(RemoteDataSource()))))

    private lateinit var binding: FragmentCardsCollectionBinding
    private lateinit var adapter: CardsCollectionAdapter

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

        viewModel.getCardsList()

        observeViewModel()
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
    }

    private fun setupAdapter() {
        adapter = CardsCollectionAdapter(requireContext(), mutableListOf())
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
}