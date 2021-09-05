package com.example.todolistbootcamp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistbootcamp.R
import com.example.todolistbootcamp.models.MyList

class ListAdapter(var listener: ClickItemListListener) : RecyclerView.Adapter<ListAdapter.ContactAdapterViewHolder>(){

    private val myList:MutableList<MyList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ContactAdapterViewHolder(view,myList,listener)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: ContactAdapterViewHolder, position: Int) {
        holder.bind(myList[position])
    }

    fun updateList(myLists: kotlin.collections.List<MyList>){
        this.myList.clear()
        this.myList.addAll(myLists)
        notifyDataSetChanged()
    }

    class ContactAdapterViewHolder(itemView: View, var myLists: kotlin.collections.List<MyList>, var listener: ClickItemListListener) : RecyclerView.ViewHolder(itemView){
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)

        init {
            itemView.setOnClickListener{
                listener.clickItemList(myLists[adapterPosition])
            }
        }

        fun bind(myList: MyList){
            tvName.text = myList.nome
            tvPhone.text = myList.descricao
        }
    }
}
