package ru.maxeltr.androidmq2t.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

class IdGenerator(private val context: Context) {	//add
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Mq2tPreferences", Context.MODE_PRIVATE)

    fun generateId(): Int {
        var newId: Int
        do {
            newId = Random.nextInt(1, Int.MAX_VALUE)
        } while (isIdExists(newId))

        return newId
    }

    private fun isIdExists(id: Int): Boolean {
        return sharedPreferences.contains(id.toString())
    }
}