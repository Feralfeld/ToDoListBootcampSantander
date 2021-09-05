package ccom.example.todolistbootcamp.adapters

import com.example.todolistbootcamp.models.TodoItem


interface DoneListener {
    fun clickItemTodoDoned(item: TodoItem)
    fun clickItemUndone(item: TodoItem)
}