package com.shicks.lifeRoot.list

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.entities.ListItem


data class ListItemWithChange(
    val item: ListItem,
    var value: String
)

class EditListItemAdapter(
    val deleteFunction: (id: Long) -> Unit,
    val onCheckFunction: (item: ListItem) -> Unit,
    val offCheckFunction: (item: ListItem) -> Unit
) : RecyclerView.Adapter<EditListItemAdapter.ViewHolder>() {

    var data: MutableList<ListItemWithChange> = mutableListOf()

    fun setupData(listItems: List<ListItem>) {
        val listId = listItems.firstOrNull()?.listId ?: 0
        data = listItems
            .map { ListItemWithChange(it, "") }
            .toMutableList()
        data.add(
            ListItemWithChange(
                ListItem(listId = listId, itemDetails = "", itemNumber = 0),
                ""
            )
        )

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
        holder.bind(item, deleteFunction, onCheckFunction, offCheckFunction)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val deleteButton: ImageView = itemView.findViewById(R.id.imageView2)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val name: EditText = itemView.findViewById(R.id.listItemDetail)
        fun bind(
            item: ListItemWithChange,
            deleteFunction: (id: Long) -> Unit,
            onCheckFunction: (item: ListItem) -> Unit,
            offCheckFunction: (item: ListItem) -> Unit
        ) {
            if (item.item.isComplete()) {
                val spannableString = SpannableString(item.item.itemDetails)
                spannableString.setSpan(StrikethroughSpan(), 0, item.item.itemDetails.length, 0)
                name.setText(spannableString)
            } else {
                name.setText(item.item.itemDetails)
            }
            name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    item.value = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            name.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (hasFocus) {
                        deleteButton.visibility = View.VISIBLE
                    }
                    if (!hasFocus) {
                        deleteButton.visibility = View.GONE
                    }
                }
            })
            deleteButton.setOnClickListener { view ->
                deleteFunction(item.item.listItemId)
            }
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    onCheckFunction(item.item)
                }
                if (!isChecked) {
                    offCheckFunction(item.item)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.edit_list_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}
