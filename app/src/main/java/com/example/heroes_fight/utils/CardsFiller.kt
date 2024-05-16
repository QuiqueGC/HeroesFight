package com.example.heroes_fight.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.ItemCardHeroBadBinding
import com.example.heroes_fight.databinding.ItemCardHeroBinding

object CardsFiller {

    fun fillDataIntoGoodCard(card: ItemCardHeroBinding, heroModel: HeroModel, context: Context) {
        with(card) {
            tvId.text = heroModel.serialNum
            tvName.text = heroModel.name
            tvStrengthContent.text = heroModel.strength
            tvCombatContent.text = heroModel.combat
            tvIntelligenceContent.text = heroModel.intelligence
            tvSpeedContent.text = heroModel.speed
            tvDurabilityContent.text = heroModel.durability
            tvPowerContent.text = heroModel.power
            Glide.with(context)
                .load(heroModel.image)
                .error(R.drawable.fight)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }

    fun fillDataIntoBadCard(card: ItemCardHeroBadBinding, heroModel: HeroModel, context: Context) {
        with(card) {
            tvId.text = heroModel.serialNum
            tvName.text = heroModel.name
            tvStrengthContent.text = heroModel.strength
            tvCombatContent.text = heroModel.combat
            tvIntelligenceContent.text = heroModel.intelligence
            tvSpeedContent.text = heroModel.speed
            tvDurabilityContent.text = heroModel.durability
            tvPowerContent.text = heroModel.power
            Glide.with(context)
                .load(heroModel.image)
                .error(R.drawable.fight)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }
}