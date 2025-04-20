package ru.maxeltr.androidmq2t.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.R
import java.time.format.DateTimeFormatter
import java.util.UUID


class Mq2tViewModel(private val application: Application) : ViewModel() {
    private val TAG = "Mq2tViewModel"
    private val mutex = Mutex()
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("CardPreferences", Context.MODE_PRIVATE)
    private val mqttClient: Mqtt3Client = initClient()

    private val _cards = mutableStateListOf<CardState> ()
    val cards: List<CardState> get() = _cards
    var isConnected = mutableStateOf(false)
        private set

    init {
        initCards()
        connect()
        startMonitoringConnection()
    }

    private fun initCards() {

        if (_cards.isNotEmpty()) {
            _cards.clear()
        }

        val allEntries = sharedPreferences.all
        val allCards = allEntries.mapNotNull {entry ->
            gson.fromJson(entry.value as String, CardState::class.java)
        }
        _cards.addAll(allCards)

        if (_cards.isEmpty()) {
            val initialCards = List(1) {
                index -> CardState()
            }
            _cards.addAll(initialCards)


            Log.i(TAG, "No saved cards in SharedPreferences.")
        }
    }

    fun saveCard(cardState: CardState) {
        val json = gson.toJson(cardState)
        sharedPreferences.edit() {
            putString("card_${cardState.id}", json)
            apply()
        }
        initCards()
        Log.d(TAG, "Saved card in SharedPreferences $json.")
    }

    fun loadCard(id: Int): CardState {
        val json = sharedPreferences.getString("card_$id", null)
        Log.d(TAG, "Load card (provided id=$id) from SharedPreferences $json.")
        return if (json != null) {
            gson.fromJson(json, CardState::class.java)
        } else {
            CardState()
        }
    }

    fun updateCardState(message: Mqtt3Publish) {
        viewModelScope.launch {
            Log.i(TAG, "Received message $message")
            mutex.withLock {
                val currentStates = _cards.toList()
                val updatedStates = currentStates.map { cardState ->
                    if (cardState.subTopic == message.topic.toString()) {
                        cardState.copy(
                            subData = String(message.payloadAsBytes, Charsets.UTF_8)
                            //TODO time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd.mm.yy"))
                        )
                    } else {
                        cardState
                    }
                }
                _cards.clear()
                _cards.addAll(updatedStates)
            }
        }
    }

    fun addCard() {

    }

    fun removeCard() {

    }

    fun onPublishClick(id: Int) {
        viewModelScope.launch {
            Log.v(TAG, "viewModel button clicked id = $id!.")
        }
    }

    override fun onCleared() {      //TODO onDestroy()?
        super.onCleared()
        Log.i(TAG, "Disconnect.")
        viewModelScope.launch {
            //TODO Add disconnect logic
        }
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

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i(TAG, "Connecting...")
            mqttClient.toAsync()
                .connect()
                .whenComplete { mqtt3ConnAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        Log.v(TAG, "Connection failed.")
                    } else {
                        Log.v(TAG, "Connected.")
                        // setup subscribes or start publishing
                        subscribe()

                    }
                }
        }
    }

    private fun subscribe() {
        Log.d(TAG, "Amount of cards = ${_cards.size}.")



        _cards.forEach {
            card ->
                Log.d(TAG, "Subscribe for card ${card}.")
                if (card.subTopic.isNotBlank()) {
                    Log.i(TAG, "Subscribe to topic ${card.subTopic}.")
                    subscribe(
                        card.subTopic,
                        MqttQos.fromCode(card.subQos) ?: MqttQos.AT_MOST_ONCE
                    ) { mqtt3Publish -> updateCardState(mqtt3Publish) }
                } else {
                    Log.w(TAG, "Topic is blank for card $card")
                }

        }
    }

    private fun subscribe(topic: String, qos: MqttQos, onMessageReceived: (Mqtt3Publish) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            mqttClient.toAsync()
                .subscribeWith()
                .topicFilter(topic)
                .qos(qos)
                .callback { mqtt3Publish ->
                    // Process the received message
                    val message = String(mqtt3Publish.payloadAsBytes, Charsets.UTF_8)
                    Log.i(TAG, " Message received $message from topic ${mqtt3Publish.topic}")
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
    }

    fun refreshConnectivity() {
        if (!mqttClient.state.isConnected) {
            Log.i(TAG, "Try to reconnect.")
            connect()
        }
        isConnected.value = mqttClient.state.isConnected == true
    }

    private fun startMonitoringConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                refreshConnectivity()
                delay(10000)
            }
        }
    }

}