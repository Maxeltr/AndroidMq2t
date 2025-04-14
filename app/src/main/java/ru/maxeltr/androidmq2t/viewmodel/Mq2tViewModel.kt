package ru.maxeltr.androidmq2t.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import kotlinx.coroutines.launch
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.R
import java.util.UUID


class Mq2tViewModel(private val application: Application) : ViewModel() {
    private val TAG = "Mq2tViewModel"
    private val mqttClient: Mqtt3Client = initClient()

    private val _cards = mutableStateListOf<CardState> ()
    val cards: List<CardState> get() = _cards


    init {

        val initialCards = List(5) {
            index ->
            CardState(id = index, name = "defaultValue", subTopic = "dch/opi1/uptime", pubTopic = "defaultValue", subData = "defaultValue", pubData = "defaultValue")
        }
        _cards.addAll(initialCards)
        connect()
    }

    fun initCards() {

    }

    fun updateCardState(message: Mqtt3Publish) {
        val currentStates = _cards.toList()

        val updatedStates = currentStates.map { cardState ->
            if (cardState.subTopic == message.topic.toString()) {
                cardState.copy(subData = String(message.payloadAsBytes, Charsets.UTF_8))
            } else {
                cardState
            }
        }
        _cards.clear()
        _cards.addAll(updatedStates)
    }

    fun addCard() {

    }

    fun removeCard() {

    }

    fun onButtonClicked(id: Int) {
        Log.v(TAG, "viewModel button clicked id = $id!.")
    }

    override fun onCleared() {      //TODO onDestroy()?
        super.onCleared()
        Log.i(TAG, "Disconnect.")
        //TODO Add disconnect logic
    }

    private fun initClient(): Mqtt3AsyncClient {
        return Mqtt3Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(application.getString(R.string.host))
            .serverPort(1883)
            .simpleAuth()
            .username(application.getString(R.string.mq2t_username))
            .password(application.getString(R.string.mq2t_password).toByteArray())
            .applySimpleAuth()
            .buildAsync()
    }

    private fun connect() {
        viewModelScope.launch {

            mqttClient.toAsync()
                .connect()
                .whenComplete { mqtt3ConnAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        Log.v(TAG, "Connection failed.")
                    } else {
                        Log.v(TAG, "Connected.")
                        // setup subscribes or start publishing
                        subscribe("dch/opi1/uptime", MqttQos.AT_MOST_ONCE) {mqtt3Publish -> updateCardState(mqtt3Publish)}

                    }
                }
        }
    }

    fun subscribe(topic: String, qos: MqttQos, onMessageReceived: (Mqtt3Publish) -> Unit) {
        mqttClient.toAsync().subscribeWith()
            .topicFilter(topic)
            .qos(qos)
            .callback { mqtt3Publish ->
                // Process the received message
                val message = String(mqtt3Publish.payloadAsBytes, Charsets.UTF_8)
                Log.v(TAG, " Message received $message from topic ${mqtt3Publish.topic}")
                onMessageReceived(mqtt3Publish)

            }
            .send()
            .whenComplete { mqtt3SubAck, throwable ->
                if (throwable != null) {
                    Log.v(TAG, "Failure to subscribe.")
                    // Handle failure to subscribe

                } else {
                    Log.v(TAG, "Successful to subscribe.")
                    // Handle successful subscription, e.g. logging or incrementing a metric

                }
            }
    }

    fun isConnected() = mqttClient.state.isConnected


}