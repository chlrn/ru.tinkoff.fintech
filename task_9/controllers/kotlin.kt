package com.example.rutinkofffintech.TASK_5.controllers

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.io.File
import kotlinx.coroutines.io.writeText
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import net.bytebuddy.agent.ByteBuddyAgent.install
import java.net.http.HttpClient
import java.util.concurrent.Executors
class kotlin {
    // Создаем пул потоков
    val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()

    // Функция для получения новостей
    suspend fun getNews(): List<News> {
        // Используем Ktor для получения данных
        val client = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        return client.get("https://kudago.com/public-api/v1.4/events/?lang=&fields=&expand=&order_by=&text_format=&ids=&location=&actual_since=1444385206&actual_until=1444385405&is_free=&categories=kvn,-magic&lon=&lat=&radius=")
    }

    // Worker для получения данных
    suspend fun worker(channel: SendChannel<News>) {
        val news = getNews()
        for (item in news) {
            channel.send(item)
        }
    }

    // Processor для записи данных в файл
    suspend fun processor(channel: ReceiveChannel<News>) {
        val file = File("output.txt")
        for (newsItem in channel) {
            file.writeText(newsItem.toString()) // Запись в файл
        }
    }

    // Главная функция
    fun main() = runBlocking {
        val channel = Channel<News>(Channel.UNLIMITED)

        // Запускаем несколько worker'ов
        repeat(10) {
            launch(dispatcher) {
                worker(channel)
            }
        }

        // Запускаем процессор
        launch {
            processor(channel)
            channel.close() // Закрываем канал после завершения
        }

        // Ожидаем завершения всех задач
        delay(10000) // Измените время ожидания по необходимости
        dispatcher.close() // Закрываем пул потоков
    }
}