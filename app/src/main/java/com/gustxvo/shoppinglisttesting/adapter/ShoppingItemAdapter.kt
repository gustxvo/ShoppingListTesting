package com.gustxvo.shoppinglisttesting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gustxvo.shoppinglisttesting.R
import com.gustxvo.shoppinglisttesting.data.local.ShoppingItem
import com.gustxvo.shoppinglisttesting.databinding.ItemShoppingBinding
import java.text.NumberFormat

class ShoppingItemAdapter(
    private val context: Context,
    private val onClickListener: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingItemAdapter.ShoppingViewHolder>(DiffCallback) {

    class ShoppingViewHolder(val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindImage(imageUrl: String) {
            Glide.with(itemView).load(imageUrl).into(binding.ivShoppingImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingViewHolder {
        return ShoppingViewHolder(
            ItemShoppingBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val shoppingItem = getItem(position)

        holder.apply {
            binding.tvName.text = shoppingItem.name
            binding.tvShoppingItemAmount.text =
                context.getString(R.string.amount, shoppingItem.amount)
            binding.tvShoppingItemPrice.text =
                NumberFormat.getCurrencyInstance().format(shoppingItem.price)
            bindImage(shoppingItem.imageUrl)

            itemView.setOnClickListener { onClickListener(shoppingItem) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {

            override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}