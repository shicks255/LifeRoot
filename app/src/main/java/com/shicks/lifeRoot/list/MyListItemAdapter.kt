package com.shicks.lifeRoot.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.entities.MyList

class MyListItemAdapter(val clickListener: (myList: MyList) -> Unit )
    : RecyclerView.Adapter<MyListItemAdapter.ViewHolder>() {

    var data: List<MyList> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, clickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data.get(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.myListItemDetail)

        fun bind(item: MyList, clickListener: (myList: MyList) -> Unit) {
            name.text = item.listName
            itemView.setOnClickListener { clickListener(item) }
        }

        companion object {
            fun from(parent: ViewGroup, clickListener: (myList: MyList) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.my_list_list_items, parent, false)

                return ViewHolder(view)
            }
        }
    }
}