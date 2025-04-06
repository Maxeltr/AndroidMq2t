package ru.maxeltr.androidmq2t.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import kotlinx.coroutines.launch
import ru.maxeltr.androidmq2t.R
import java.util.UUID


class MqttViewModel(private val application: Application) : ViewModel() {
    private val client: Mqtt3Client = initClient()

    init {
        connect()
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

            client.toAsync()
                .connect()
                .whenComplete { mqtt3ConnAck, throwable ->
                    if (throwable != null) {
                        // handle failure
                        Log.v("HIVE-MQTT-LCDP", " connection failed")
                    } else {
                        Log.v("HIVE-MQTT-LCDP", " connected")
                        // setup subscribes or start publishing
                        subscribe()

                    }
                }
        }
    }

    fun subscribe() {
        client.toAsync().subscribeWith()
            .topicFilter("dch/wheather")
            .qos(MqttQos.AT_MOST_ONCE)
            .callback { mqtt3Publish ->
                // Process the received message
                val message = String(mqtt3Publish.payloadAsBytes, Charsets.UTF_8)
                Log.v("HIVE-MQTT-LCDP", " Message received $message")

            }
            .send()
            .whenComplete { mqtt3SubAck, throwable ->
                if (throwable != null) {
                    Log.v("HIVE-MQTT-LCDP", " failure to subscribe")
                    // Handle failure to subscribe

                } else {
                    Log.v("HIVE-MQTT-LCDP", " successful to subscribe")
                    // Handle successful subscription, e.g. logging or incrementing a metric

                }
            }
    }

}