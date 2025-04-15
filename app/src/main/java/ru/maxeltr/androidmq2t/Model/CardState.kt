package ru.maxeltr.androidmq2t.Model

data class CardState(
    val id: Int = 0,
    val name: String = "n/a",
    val subTopic: String = "",
    val subData: String = "n/a",
    val subQos: String = "AT_MOST_ONCE",
    val pubTopic: String = "",
    val pubData: String = "",
    val pubQos: String = "AT_MOST_ONCE",
    val pubRetain: Boolean = false,
    val time: Long = 0L,
)