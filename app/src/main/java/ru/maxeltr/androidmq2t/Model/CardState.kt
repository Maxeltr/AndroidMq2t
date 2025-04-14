package ru.maxeltr.androidmq2t.Model

data class CardState(
    val id: Int,
    var name: String,
    var subTopic: String,
    var pubTopic: String,
    val subData: String,
    val pubData: String,
)