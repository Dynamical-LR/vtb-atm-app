package ru.dynamical_lr.vtb_atm_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.dynamical_lr.vtb_atm_app.R
import ru.dynamical_lr.vtb_atm_app.models.CardInfo

class CardsAdapter(private val cards: List<CardInfo>):
    RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {
    class CardsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleView = itemView.findViewById<TextView>(R.id.title_atm_card)
        val addressView = itemView.findViewById<TextView>(R.id.address_atm_card)
        val typeView = itemView.findViewById<TextView>(R.id.type_atm_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.atm_card, parent, false)
        return CardsViewHolder(itemView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        holder.titleView.text = cards[position].title
        holder.addressView.text = cards[position].address
        holder.typeView.text = cards[position].type
    }


}