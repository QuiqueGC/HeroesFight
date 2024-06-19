package com.example.heroes_fight.data.utils

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.fighter.FighterModel
import com.example.heroes_fight.data.domain.model.hero.HeroModel
import com.example.heroes_fight.databinding.ItemCardHeroBadBinding
import com.example.heroes_fight.databinding.ItemCardHeroBinding

object CardsFiller {

    fun fillDataIntoGoodCard(card: ItemCardHeroBinding, heroModel: HeroModel, context: Context) {
        with(card) {
            tvId.text = heroModel.serialNum
            tvName.text = heroModel.name
            tvStrengthContent.text = heroModel.strength.toString()
            tvCombatContent.text = heroModel.combat.toString()
            tvIntelligenceContent.text = heroModel.intelligence.toString()
            tvSpeedContent.text = heroModel.speed.toString()
            tvDurabilityContent.text = heroModel.durability.toString()
            tvPowerContent.text = heroModel.power.toString()
            Glide.with(context)
                .load(heroModel.image)
                .error(R.drawable.question_mark)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }

    fun fillDataIntoBadCard(card: ItemCardHeroBadBinding, heroModel: HeroModel, context: Context) {
        with(card) {
            tvId.text = heroModel.serialNum
            tvName.text = heroModel.name
            tvStrengthContent.text = heroModel.strength.toString()
            tvCombatContent.text = heroModel.combat.toString()
            tvIntelligenceContent.text = heroModel.intelligence.toString()
            tvSpeedContent.text = heroModel.speed.toString()
            tvDurabilityContent.text = heroModel.durability.toString()
            tvPowerContent.text = heroModel.power.toString()
            Glide.with(context)
                .load(heroModel.image)
                .error(R.drawable.question_mark)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }


    fun fillDataIntoHeroFighterCard(
        card: ItemCardHeroBinding,
        fighterModel: FighterModel,
        context: Context
    ) {

        with(card) {
            tvBonusCombat.visibility = View.VISIBLE
            tvBonusDefense.visibility = View.VISIBLE
            tvBonusValue.visibility = View.VISIBLE
            tvDefenseValue.visibility = View.VISIBLE
            btnBiography.visibility = View.GONE
            btnAppearance.visibility = View.GONE

            tvId.text = fighterModel.serialNum
            tvName.text = fighterModel.name
            tvStrengthContent.text = fighterModel.strength.toString()
            tvCombatContent.text = fighterModel.combat.toString()
            tvIntelligenceContent.text = fighterModel.intelligence.toString()
            tvSpeedContent.text = fighterModel.speed.toString()
            tvDurabilityContent.text = fighterModel.durability.toString()
            tvPowerContent.text = fighterModel.power.toString()
            tvBonusValue.text = "+${fighterModel.combatBonus}"
            tvDefenseValue.text = "+${fighterModel.defenseBonus}"
            if (fighterModel.isSabotaged) {
                tvSabotaged.visibility = View.VISIBLE
            } else {
                tvSabotaged.visibility = View.GONE
            }
            Glide.with(context)
                .load(fighterModel.image)
                .error(R.drawable.fight)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }

    fun fillDataIntoVillainFighterCard(
        card: ItemCardHeroBadBinding,
        fighterModel: FighterModel,
        context: Context
    ) {

        with(card) {
            tvBonusCombat.visibility = View.VISIBLE
            tvBonusDefense.visibility = View.VISIBLE
            tvBonusValue.visibility = View.VISIBLE
            tvDefenseValue.visibility = View.VISIBLE
            btnBiography.visibility = View.GONE
            btnAppearance.visibility = View.GONE

            tvId.text = fighterModel.serialNum
            tvName.text = fighterModel.name
            tvStrengthContent.text = fighterModel.strength.toString()
            tvCombatContent.text = fighterModel.combat.toString()
            tvIntelligenceContent.text = fighterModel.intelligence.toString()
            tvSpeedContent.text = fighterModel.speed.toString()
            tvDurabilityContent.text = fighterModel.durability.toString()
            tvPowerContent.text = fighterModel.power.toString()
            tvBonusValue.text = "+${fighterModel.combatBonus}"
            tvDefenseValue.text = "+${fighterModel.defenseBonus}"
            if (fighterModel.isSabotaged) {
                tvSabotaged.visibility = View.VISIBLE
            } else {
                tvSabotaged.visibility = View.GONE
            }
            Glide.with(context)
                .load(fighterModel.image)
                .error(R.drawable.question_mark)
                .apply(RequestOptions().centerCrop())
                .into(imgHero)
        }
    }
}