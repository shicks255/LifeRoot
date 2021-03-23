package com.shicks.lifeRoot.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.entities.ListItem

class EditListItemAdapter
    : RecyclerView.Adapter<EditListItemAdapter.ViewHolder>() {

    var data = listOf<ListItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.listItemDetail)

        fun bind(item: ListItem) {
            val res = itemView.context.resources
            name.text = item.itemDetails
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.edit_list_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}