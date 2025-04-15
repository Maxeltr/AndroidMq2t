package ru.maxeltr.androidmq2t.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import ru.maxeltr.androidmq2t.Model.CardState

class EditCardViewModel(private val application: Application) : ViewModel() {
    private val TAG = "EditCardViewModel"

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("CardPreferences", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveCard(cardState: CardState) {
        val json = gson.toJson(cardState)
        sharedPreferences.edit() { putString("card_${cardState.id}", json) }
    }

    fun loadCard(id: Int): CardState? {
        val json = sharedPreferences.getString("card_$id", null)
        return if (json != null) {
            gson.fromJson(json, CardState::class.java)
        } else {
            null
        }
    }


}