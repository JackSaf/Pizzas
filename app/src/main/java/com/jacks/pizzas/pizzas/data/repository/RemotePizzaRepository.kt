package com.jacks.pizzas.pizzas.data.repository

import android.content.Context
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.jacks.pizzas.core.domain.util.DataError
import com.jacks.pizzas.core.domain.util.Result
import com.jacks.pizzas.pizzas.data.mapper.toDomain
import com.jacks.pizzas.pizzas.data.remote.PizzaApi
import com.jacks.pizzas.pizzas.domain.PizzaRepository
import com.jacks.pizzas.pizzas.domain.model.Pizza
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import java.io.IOException

class RemotePizzaRepository(
    private val pizzaApi: PizzaApi,
    private val imageLoader: ImageLoader,
    private val appContext: Context
) : PizzaRepository {
    override suspend fun getPizzas(): Result<List<Pizza>, DataError.Network> {
        var currentDelayMs = INITIAL_DELAY_MS
        repeat(MAX_RETRIES) {
            try {
                val response = pizzaApi.getPizzas()
                if (response.isSuccessful) {
                    val pizzas = response.body()?.pizzas?.map { it.toDomain() }
                        ?: return Result.Error(DataError.Network.SERIALIZATION)
                    prefetchImages(pizzas)
                    return Result.Success(pizzas)
                }

            } catch (e: IOException) {
                e.printStackTrace()
                return Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: SerializationException) {
                e.printStackTrace()
                return Result.Error(DataError.Network.SERIALIZATION)
            }
            val delayMs = (currentDelayMs * BACKOFF_MULTIPLIER).toLong()
            delay(delayMs)
            currentDelayMs = delayMs
        }
        return Result.Error(DataError.Network.SERVER_SIDE)
    }

    private suspend fun prefetchImages(pizzas: List<Pizza>) {
        withContext(Dispatchers.IO) {
            pizzas
                .asSequence()
                .map { it.imageUrl }
                .filter { it.isNotBlank() }
                .distinct()
                .filterNot { imageUrl -> isImageCached(imageUrl) }
                .forEach { imageUrl ->
                    imageLoader.execute(
                        ImageRequest.Builder(appContext)
                            .data(imageUrl)
                            .memoryCacheKey(imageUrl)
                            .diskCacheKey(imageUrl)
                            .build()
                    )
                }
        }


    }

    @OptIn(ExperimentalCoilApi::class)
    private fun isImageCached(imageUrl: String): Boolean {
        val memoryCached = imageLoader.memoryCache?.get(MemoryCache.Key(imageUrl)) != null
        val diskCached = imageLoader.diskCache?.openSnapshot(imageUrl) != null
        return memoryCached || diskCached
    }

    companion object {
        private const val MAX_RETRIES = 5
        private const val INITIAL_DELAY_MS = 100L
        private const val BACKOFF_MULTIPLIER = 2.0
    }
}
