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
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientConfig
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.maxeltr.androidmq2t.Model.CardState
import ru.maxeltr.androidmq2t.Model.ConnectionState
import ru.maxeltr.androidmq2t.R
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.Calendar
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale


class Mq2tViewModel(private val application: Application) : ViewModel() {
    private val TAG = "Mq2tViewModel"
    private val mutex = Mutex()
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("Mq2tPreferences", Context.MODE_PRIVATE)
    private var mqttClient: Mqtt3Client = initClient()
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("HH:mm:ss dd.MM.yy", Locale.getDefault())

    private val _cards = mutableStateListOf<CardState>()
    val cards: List<CardState> get() = _cards
    var isConnected = mutableStateOf(false)
        private set

    init {
        initCards()
        connectAndMonitor()
    }

    private fun initCards() {
        _cards.addAll(loadAllCardsFromPreferences())
        Log.d(TAG, "Initialized cards= ${_cards.size}.")

    }

    private fun reloadCards() {
        viewModelScope.launch {
            mutex.withLock {
                _cards.clear()
                _cards.addAll(loadAllCardsFromPreferences())
            }
        }
    }

    fun loadAllCardsFromPreferences(): List<CardState> {
        val cards = sharedPreferences
            .all
            .filterKeys { it.startsWith("card_") }
            .mapNotNull { entry ->
                gson.fromJson(entry.value as String, CardState::class.java)
            }
        Log.d(TAG, "Load cards from SharedPreferences. Amount=${cards.size}.")

        return cards
    }

    fun saveCardInPreferences(cardState: CardState) {
        viewModelScope.launch {
            if (cardState.id == -1) {
                Log.w(TAG, "Could not save invalid (empty) )ard with id=${cardState.id}.")
                return@launch
            }

            val jsonObject = gson.toJsonTree(cardState).asJsonObject
            jsonObject.remove("subData")
            jsonObject.remove("time")
            val json = gson.toJson(jsonObject)

            sharedPreferences.edit() {
                putString("card_${cardState.id}", json)
                apply()
            }

            reloadCards()
            Log.d(TAG, "Saved card in SharedPreferences $json.")
        }
    }

    fun loadCardFromPreferences(id: Int): CardState {
        val json = sharedPreferences.getString("card_$id", null)
        Log.d(TAG, "Load card (provided id=$id) from SharedPreferences $json.")
        return if (json != null) {
            gson.fromJson(json, CardState::class.java)
        } else {
            Log.w(
                TAG,
                "Could not load card (provided id=$id) from SharedPreferences. Return empty card."
            )
            CardState()
        }
    }

    fun deleteCardFromPreferences(id: Int) {
        sharedPreferences.edit() {
            remove("card_$id")
        }
        Log.i(TAG, "Try to delete card (provided id=$id) from SharedPreferences.")
        reloadCards()
    }

    fun getCardById(id: Int): CardState {
        var card = _cards.find { it.id == id }

        if (card == null) {
            Log.w(TAG, "Could not get card with id=$id. Return empty card.")
            card = CardState()
        }
        return card
    }

    fun loadConnectionFromPreferences(): ConnectionState {
        val json = sharedPreferences.getString("connectionState", null)
        Log.d(TAG, "Load connectionState from SharedPreferences $json.")

        return if (json != null) {
            gson.fromJson(json, ConnectionState::class.java)
        } else {
            Log.w(
                TAG,
                "Could not load connection state from SharedPreferences. Return empty connection state."
            )
            ConnectionState()
        }
    }

    fun saveConnectionInPreferences(connectionState: ConnectionState) {
        val json = gson.toJson(connectionState)
        sharedPreferences.edit() {
            putString("connectionState", json)
            apply()
        }

        Log.d(TAG, "Saved connectionState in SharedPreferences $connectionState.")
    }

    fun updateCardState(message: Mqtt3Publish) {
        viewModelScope.launch {
            mutex.withLock {
                val currentStates = _cards.toList()
                val updatedStates = currentStates.map { cardState ->
                    if (cardState.subTopic == message.topic.toString()) {
                        Log.i(TAG, "Update card ${cardState.name} (id=${cardState.id}).")
                        cardState.copy(
                            subData = String(message.payloadAsBytes, Charsets.UTF_8),
                            time = dateFormat.format(System.currentTimeMillis())
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

    fun onPublishClick(id: Int) {
        viewModelScope.launch {
            val card = getCardById(id)
            publish(card.pubTopic, card.pubData, card.pubQos)
        }
    }

    fun onDeleteClick(id: Int) {
        viewModelScope.launch {
            deleteCardFromPreferences(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            disconnect()
        }
    }

    private fun initClient(): Mqtt3AsyncClient {
        Log.d(TAG, "Initialising mqtt client.")
        val connection = loadConnectionFromPreferences()

        return Mqtt3Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(connection.host)
            .serverPort(connection.port.toInt())
            .simpleAuth()
            .username(connection.username)
            .password(connection.password.toByteArray())
            .applySimpleAuth()
            .buildAsync()
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mqttClient.toAsync().disconnect().whenComplete { _, throwable ->
                    if (throwable != null) {
                        Log.e(TAG, "Failed to disconnect. ${throwable.message}")
                    } else {
                        Log.i(TAG, "Successfully disconnected.")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error while disconnecting: ${e.message}")
            }
        }
        refreshConnectivityState()
    }

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            if (mqttClient.state.isConnected) {
                Log.i(TAG, "Already connected")
                return@launch
            }

            val config: Mqtt3ClientConfig = mqttClient.config
            val host = config.serverHost
            val port = config.serverPort


            Log.i(TAG, "Connecting... to host=$host via port=$port with username=")
            mqttClient.state.name

            mqttClient.toAsync()
                .connectWith()
                .cleanSession(true)
                .keepAlive(60)
                .send()
                .whenComplete { mqtt3ConnAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        Log.v(TAG, "Connection failed: ${throwable.message}")
                    } else {
                        Log.v(TAG, "Connected.")
                        // setup subscribes or start publishing
                        subscribe()

                    }
                }
            refreshConnectivityState()
        }
    }

    fun subscribe(topic: String, qos: Int) {
        if (topic.isBlank()) {
            Log.i(TAG, "Could not subscribe. Topic is empty.")
            return
        }


        val mqttQos = MqttQos.fromCode(qos) ?: MqttQos.AT_MOST_ONCE
        Log.d(TAG, "Subscribe topic=$topic qos=$mqttQos.")
        subscribe(
            topic,
            mqttQos,
        ) { mqtt3Publish -> updateCardState(mqtt3Publish) }
    }

    private fun subscribe() {
        Log.d(TAG, "Amount of cards = ${_cards.size} to subscribe.")
        _cards.forEach { card ->
            Log.d(TAG, "Subscribe for card ${card}.")
            if (card.subTopic.isNotBlank()) {
                Log.i(TAG, "Subscribe to topic ${card.subTopic}.")
                subscribe(
                    card.subTopic,
                    MqttQos.fromCode(card.subQos) ?: MqttQos.AT_MOST_ONCE
                ) { mqtt3Publish -> updateCardState(mqtt3Publish) }
            } else {
                Log.w(TAG, "Topic is blank for card $card. Skipped.")
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
                        Log.v(TAG, "Failure to subscribe: ${throwable.message}")
                        // Handle failure to subscribe

                    } else {
                        Log.v(TAG, "Successful to subscribe to topic=$topic with qos=$qos.")
                        // Handle successful subscription, e.g. logging or incrementing a metric

                    }
                }
        }
    }

    fun publish(topic: String, payload: String, qos: Int) {
        if (topic.isBlank()) {
            Log.i(TAG, "Could not publish message. Topic is empty.")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val message: Mqtt3Publish = Mqtt3Publish.builder()
                .topic(topic)
                .payload(payload.toByteArray())
                .qos(MqttQos.fromCode(qos) ?: MqttQos.AT_MOST_ONCE)
                .build()

            mqttClient.toAsync()
                .publish(message)
                .whenComplete { _, publishThrowable ->
                    if (publishThrowable != null) {
                        println("Failed to publish message: ${publishThrowable.message}")
                    } else {
                        println("Message published successfully.")
                    }
                }
        }
    }

    fun refreshConnectivityState() {
        isConnected.value = mqttClient.state.isConnected == true
    }

    private fun connectAndMonitor() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                refreshConnectivityState()
                if (!isConnected.value) {
                    Log.i(TAG, "Check connection. Client is disconnected. Try to reconnect.")
                    connect()
                }
                delay(10000)
            }
        }
    }

    fun reconnect() {
        disconnect()
        mqttClient = initClient()

    }

}