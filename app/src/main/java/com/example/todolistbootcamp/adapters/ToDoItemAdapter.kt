package com.example.todolistbootcamp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistbootcamp.R
import com.example.todolistbootcamp.models.TodoItem

class ToDoItemAdapter(var listener: ClickItemTodoListener) : RecyclerView.Adapter<ToDoItemAdapter.TodoAdapterViewHolder>(){

    private val list:MutableList<TodoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoAdapterViewHolder(view,list,listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodoAdapterViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(list: List<TodoItem>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class TodoAdapterViewHolder(itemView: View, var list: List<TodoItem>, var listener: ClickItemTodoListener) : RecyclerView.ViewHolder(itemView){
        private val tvName: TextView = itemView.findViewById(R.id.todo_name)
        private val tvPhone: TextView = itemView.findViewById(R.id.todo_phone)




        init {
            itemView.setOnClickListener{
                listener.clickItemTodo(list[adapterPosition])
            }

            val notebtndone = itemView.findViewById<Button>(R.id.notebtndone)

            notebtndone.setOnClickListener{
                listener.clickDoneItem(list[adapterPosition])
            }

        }


        fun bind(contact: TodoItem){
            tvName.text = contact.nome
            tvPhone.text = contact.descricao
            var itemData: TextView = itemView.findViewById(R.id.itemData)

            if(contact.isDated) {
                itemData.text = "${contact.valorData} - ${contact.valorHora}"
                itemData.setVisibility(View.VISIBLE)
            }else{
                itemData.setVisibility(View.GONE)
            }

        }
    }
}


