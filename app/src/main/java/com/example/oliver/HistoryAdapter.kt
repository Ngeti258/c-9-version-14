package com.example.oliver

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HistoryAdapter(private val context: Context, var productList: MutableList<HistoryFragment.OrderHistory>) :
    RecyclerView.Adapter<HistoryAdapter.ProductViewHolder>() {

    private var onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(product: HistoryFragment.OrderHistory) {

        }
    }

    interface OnItemClickListener {
        fun onItemClick(product: HistoryFragment.OrderHistory)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.fertilizerTypeTV.text = product.coffeeType
        holder.fertilizerQuantityTV.text = " ${product.quantity} kilograms"


    }

    override fun getItemCount() = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fertilizerTypeTV: TextView = itemView.findViewById(R.id.fertilizerTypeTV)
        val fertilizerQuantityTV: TextView = itemView.findViewById(R.id.fertilizerQuantityTV)

    }
}
