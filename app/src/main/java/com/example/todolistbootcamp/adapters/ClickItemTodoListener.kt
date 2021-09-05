package com.example.todolistbootcamp.adapters

import com.example.todolistbootcamp.models.TodoItem

interface ClickItemTodoListener {
    fun clickItemTodo(contact: TodoItem)
    fun clickDoneItem(todoItem: TodoItem)
}