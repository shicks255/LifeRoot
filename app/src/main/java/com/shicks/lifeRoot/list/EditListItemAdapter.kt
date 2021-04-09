package com.shicks.lifeRoot.list

import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StrikethroughSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shicks.lifeRoot.R
import com.shicks.lifeRoot.database.entities.ListItem
import com.shicks.lifeRoot.databinding.EditListListItemBinding
import kotlinx.android.synthetic.main.edit_list_list_item.view.*

data class ListItemWithChange(
    val item: ListItem,
    var value: String
)

class EditListItemAdapter(
    val deleteFunction: (id: Long) -> Unit,
    val onCheckFunction: (item: ListItem) -> Unit,
    val offCheckFunction: (item: ListItem) -> Unit,
    val onTextChangeFunction: (item: ListItemWithChange) -> Unit
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
        println("Item " + item.item.itemDetails + " is at position " + position)
        holder.bind(item, deleteFunction, onCheckFunction, offCheckFunction, onTextChangeFunction)
    }

    class ViewHolder private constructor(val binding: EditListListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val deleteButton: ImageView = binding.imageView2
        val checkBox: CheckBox = binding.checkBox
        val addBox: ImageButton = binding.imageButton2
        val name: EditText = binding.listItemDetail
        fun bind(
            item: ListItemWithChange,
            deleteFunction: (id: Long) -> Unit,
            onCheckFunction: (item: ListItem) -> Unit,
            offCheckFunction: (item: ListItem) -> Unit,
            onTextChangeFunction: (item: ListItemWithChange) -> Unit
        ) {
            if (item.item.itemDetails.isEmpty()) {
//                checkBox.visibility = View.GONE
                addBox.visibility = View.VISIBLE
            } else {
                checkBox.visibility = View.VISIBLE
                addBox.visibility = View.GONE
            }
            if (item.item.isComplete()) {
                checkBox.isChecked = true
                val spannableString = SpannableString(item.item.itemDetails)
                spannableString.setSpan(StrikethroughSpan(), 0, item.item.itemDetails.length, 0)
                name.setText(spannableString)
            } else {
                checkBox.isChecked = false
                name.setText(item.item.itemDetails)
                if (item.item.listItemId == 0L) {
                    name.requestFocus()
                }
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
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            name.setOnKeyListener { v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (item.item.itemDetails != item.value && item.value.isNotEmpty()) {
                        name.clearFocus()
                    }
                }
                false;
            }
            name.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if (hasFocus) {
                        deleteButton.visibility = View.VISIBLE
                    }
                    if (!hasFocus) {
                        if (item.item.itemDetails != item.value && item.value.isNotEmpty()) {
                            onTextChangeFunction(item)
                        }

                        deleteButton.visibility = View.GONE
                    }
                }
            })
            deleteButton.setOnClickListener { view ->
                deleteFunction(item.item.listItemId)
            }
            checkBox.setOnClickListener { view ->
                val isChecked = view.checkBox.isChecked
                if (isChecked) {
                    if (!item.item.isComplete()) {
                        println("setting checkbox ON for " + item.item.itemDetails)
                        onCheckFunction(item.item)
                    }
                }
                if (!isChecked) {
                    if (item.item.isComplete()) {
                        println("setting checkbox OFF for " + item.item.itemDetails)
                        offCheckFunction(item.item)
                    }
                }
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.edit_list_list_item, parent, false)

                val binding = EditListListItemBinding.inflate(LayoutInflater.from(parent.context))
                return ViewHolder(binding)
            }
        }
    }
}
