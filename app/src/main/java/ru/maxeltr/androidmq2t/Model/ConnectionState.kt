package ru.maxeltr.androidmq2t.Model

data class ConnectionState(
    val host: String = "host",
    val port: String = "1883",
    val username: String = "",
    val password: String = "",
)