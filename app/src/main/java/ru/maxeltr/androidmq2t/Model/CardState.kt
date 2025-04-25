package ru.maxeltr.androidmq2t.Model

data class CardState(
    val id: Int = -1,
    val name: String = "",
    val subTopic: String = "",
    val subData: String = "",
    val subQos: Int = 0,
    val pubTopic: String = "",
    val pubData: String = "",
    val pubQos: Int = 0,
    val pubRetain: Boolean = false,
    val time: String = "",
)