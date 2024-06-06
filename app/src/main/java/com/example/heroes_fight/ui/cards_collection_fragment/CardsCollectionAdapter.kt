package com.example.heroes_fight.ui.cards_collection_fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.ItemCardHeroSmallBadBinding
import com.example.heroes_fight.databinding.ItemCardHeroSmallBinding

class CardsCollectionAdapter(
    private val context: Context,
    private val listener: CardListener,
    private var cardsList: MutableList<HeroModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface CardListener {
        fun onClick(position: Int)
    }

    private val HERO_BAD = 0
    private val HERO_GOOD = 1


    inner class HeroGoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCardHeroSmallBinding.bind(view)
    }

    inner class HeroBadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCardHeroSmallBadBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HERO_BAD -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_card_hero_small_bad, parent, false)
                HeroBadViewHolder(view)
            }

            HERO_GOOD -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_card_hero_small, parent, false)
                HeroGoodViewHolder(view)
            }

            else -> throw IllegalArgumentException("Tipo de elemento desconocido")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeroGoodViewHolder -> {
                with(holder.binding) {
                    tvId.text = cardsList[position].serialNum
                    tvName.text = cardsList[position].name
                    tvStrengthContent.text = cardsList[position].strength.toString()
                    tvCombatContent.text = cardsList[position].combat.toString()
                    tvIntelligenceContent.text = cardsList[position].intelligence.toString()
                    tvSpeedContent.text = cardsList[position].speed.toString()
                    tvDurabilityContent.text = cardsList[position].durability.toString()
                    tvPowerContent.text = cardsList[position].power.toString()
                    Glide.with(context)
                        .load(cardsList[position].image)
                        .error(R.drawable.question_mark)
                        .apply(RequestOptions().centerCrop())
                        .into(imgHero)
                }
                holder.binding.root.setOnClickListener {
                    listener.onClick(position)
                    Log.i("clic", "se hace clic en un héroe")
                }
            }

            is HeroBadViewHolder -> {
                with(holder.binding) {
                    tvId.text = cardsList[position].serialNum
                    tvName.text = cardsList[position].name
                    tvStrengthContent.text = cardsList[position].strength.toString()
                    tvCombatContent.text = cardsList[position].combat.toString()
                    tvIntelligenceContent.text = cardsList[position].intelligence.toString()
                    tvSpeedContent.text = cardsList[position].speed.toString()
                    tvDurabilityContent.text = cardsList[position].durability.toString()
                    tvPowerContent.text = cardsList[position].power.toString()
                    Glide.with(context)
                        .load(cardsList[position].image)
                        .error(R.drawable.question_mark)
                        .apply(RequestOptions().centerCrop())
                        .into(imgHero)
                }
                holder.binding.root.setOnClickListener {
                    listener.onClick(position)
                    Log.i("clic", "se hace clic en un villano")
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (cardsList[position].alignment) {
            "bad" -> HERO_BAD
            "good" -> HERO_GOOD
            else -> HERO_BAD//throw IllegalArgumentException("Tipo de elemento desconocido")
        }
    }

    override fun getItemCount(): Int = cardsList.count()

    fun refreshList(newList: MutableList<HeroModel>) {
        cardsList = newList
        notifyDataSetChanged()
    }
}