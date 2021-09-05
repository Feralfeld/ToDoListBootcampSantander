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
import com.example.todolistbootcamp.adapters.ClickItemTodoListener
import com.example.todolistbootcamp.adapters.ToDoItemAdapter
import com.example.todolistbootcamp.application.ListApplication
import com.example.todolistbootcamp.models.ItemVO
import com.example.todolistbootcamp.models.MyList
import com.example.todolistbootcamp.models.TodoItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.toolbar.*

class ListActivity : BaseActivity(), ClickItemTodoListener {

    private var actualList: MyList? = null

    private val todoList: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.todo_list)
    }

    private val adapter = ToDoItemAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getExtras()
        initToolbar()
        bindViews()
        setupOnClicks()
        dataChanged()
    }

    private fun initToolbar(){

        val toolbar = findViewById<Toolbar>(R.id.toolber)

        actualList?.nome?.let { setupToolBar( toolbar, it, true, true) }
        // setSupportActionBar(toolbar)

        //supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)

        //supportActionBar?.setCustomView(R.layout.toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        dataChanged()
    }

    private fun setupOnClicks(){
        buttonadditem.setOnClickListener { onClickAdd() }
        editListToolbar.setOnClickListener { funcaoeditList() }
        donedsItens.setOnClickListener { onClickDoneds() }
    }

    private fun bindViews(){
        findViewById<TextView>(R.id.tv_descricao).text = actualList?.descricao

        todoList.adapter = adapter
        todoList.layoutManager = LinearLayoutManager(this)
        updateList()
    }

    private fun funcaoeditList() {
        val intent = Intent(this, EditListActivity::class.java)
        intent.putExtra("index",actualList?.id)
        startActivity(intent)
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

    companion object {
        const val EXTRA_CONTACT: String = "EXTRA CONTACT"
        const val EXTRA_TODOITEM: String = "EXTRA TODO"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    fun onClickDoneds(){
        val intent = Intent(this, DonedsActivity::class.java)
        intent.putExtra(EXTRA_CONTACT,actualList)
        startActivity(intent)
    }

    override fun clickItemTodo(todoItem: TodoItem){
        val intent = Intent(this, ItemActivity::class.java)
        intent.putExtra(EXTRA_CONTACT,actualList)
        intent.putExtra("index", todoItem.id)
        startActivity(intent)
    }

    override fun clickDoneItem(todoItem: TodoItem) {
        var idItem = todoItem.id
        var idLista = actualList?.id

        if (idLista == null ) {
            idLista = 0
        }

        ListApplication.instance.helperItemDB?.doneItem(todoItem.id, idLista)

        dataChanged()
    }

    private fun updateList(){
        adapter.updateList(getListTodoItens())
    }

    private fun getListTodoItens(): List<TodoItem> {

        var idLista = actualList?.id

        if (idLista == null ) {
            idLista = 0
        }

        return   ListApplication.instance.helperItemDB?.buscarItens("", idLista) ?: mutableListOf()
    }

    private fun dataChanged(){
        updateList()
        adapter.notifyDataSetChanged()
    }

}