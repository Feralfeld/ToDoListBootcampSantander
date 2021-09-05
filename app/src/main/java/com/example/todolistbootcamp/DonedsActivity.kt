package com.example.todolistbootcamp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.example.todolistbootcamp.adapters.DoneAdapter
import ccom.example.todolistbootcamp.adapters.DoneListener
import com.example.todolistbootcamp.application.ListApplication
import com.example.todolistbootcamp.models.ItemVO
import com.example.todolistbootcamp.models.MyList
import com.example.todolistbootcamp.models.TodoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DonedsActivity : BaseActivity(), DoneListener {

    private var actualList: MyList? = null

    private val todoList: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.todo_list_doned)
    }

    private val adapter = DoneAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doneds)


        getExtras()
        dataChanged()
        bindViews()
        initToolbar()
        setupOnClicks()
    }

    override fun onResume() {
        super.onResume()
        dataChanged()
    }

    private fun dataChanged(){
        updateList()
        adapter.notifyDataSetChanged()
    }

    private fun setupOnClicks(){

    }

    private fun onClickAdd(){
        val intent = Intent(this,ItemActivity::class.java)
        intent.putExtra(EXTRA_CONTACT,actualList)
        startActivity(intent)
    }

    private fun getInstanceSharedPreferences(): SharedPreferences {
        return getSharedPreferences("br.com.bootcampkotlin.PREFERENCES", Context.MODE_PRIVATE)
    }

    private fun getExtras(){
        actualList = intent.getParcelableExtra(EXTRA_CONTACT)
    }


    private fun bindViews(){
        findViewById<TextView>(R.id.tv_phone_doned).text = actualList?.descricao

        todoList.adapter = adapter
        todoList.layoutManager = LinearLayoutManager(this)
        updateList()
    }

    private fun updateList(){
        adapter.updateList(getListTodoItens())
    }

    private fun getListTodoItens(): List<TodoItem> {

        var idLista = actualList?.id

        if (idLista == null ) {
            idLista = 0
        }

        return ListApplication.instance.helperItemDB?.buscarDoneds("", idLista) ?: mutableListOf()
    }


    companion object {
        const val EXTRA_CONTACT: String = "EXTRA CONTACT"
        const val EXTRA_TODOITEM: String = "EXTRA TODO"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun initToolbar(){
        val toolbar = findViewById<Toolbar>(R.id.toolber)
        actualList?.nome?.let { setupToolBar( toolbar, it, true, false) }
    }

    override fun clickItemTodoDoned(item: TodoItem){
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(ListActivity.EXTRA_CONTACT,actualList)
        intent.putExtra("index", item.id)
        startActivity(intent)
    }

    override fun clickItemUndone(item: TodoItem) {
        var idItem = item.id
        var idLista = actualList?.id

        if (idLista == null ) {
            idLista = 0
        }

        ListApplication.instance.helperItemDB?.unDoneItem(item.id, idLista)

        dataChanged()
    }
}