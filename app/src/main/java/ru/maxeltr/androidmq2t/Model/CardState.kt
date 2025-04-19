package ru.maxeltr.androidmq2t.Model

data class CardState(
    val id: Int = 0,
    val name: String = "",
    val subTopic: String = "",
    val subData: String = "",
    val subQos: Int = 0,
    val pubTopic: String = "",
    val pubData: String = "",
    val pubQos: Int = 0,
    val pubRetain: Boolean = false,
    val time: Long = 0L,
)