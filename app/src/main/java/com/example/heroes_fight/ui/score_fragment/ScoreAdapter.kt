package com.example.heroes_fight.ui.score_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heroes_fight.R
import com.example.heroes_fight.data.domain.model.fighter.ScoreModel
import com.example.heroes_fight.databinding.ItemScoreBinding

class ScoreAdapter(
    private val scores: MutableList<ScoreModel>,
    private val context: Context,
    private val areVillains: Boolean
) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemScoreBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            tvSerialNum.text = scores[position].serialNum
            tvFighterName.text = scores[position].name
            tvKillsContent.text = scores[position].kills.toString()
            tvMeleeAttacksContent.text = scores[position].meleeAtks.toString()
            tvMeleeDamageContent.text = scores[position].meleeDmg.toString()
            tvRangeAttacksContent.text = scores[position].rangeAtks.toString()
            tvRangeDamageContent.text = scores[position].rangeDmg.toString()
            tvDefendedAttacksContent.text = scores[position].defAtks.toString()
            tvMeleeDamageReceivedContent.text = scores[position].meleeDmgRec.toString()
            tvDodgedAttacksContent.text = scores[position].dodgedAtks.toString()
            tvRangeDamageReceivedContent.text = scores[position].rangeDmgRec.toString()
            tvSurvivedContent.text = if (scores[position].survived) {
                "Yes"
            } else {
                "No"
            }

            Glide.with(context)
                .load(scores[position].img)
                .error(R.drawable.question_mark)
                .apply(RequestOptions().centerCrop())
                .into(imgFighter)
        }

    }

    override fun getItemCount(): Int = scores.size
}