package com.jacks.pizzas.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.jacks.pizzas.core.data.network.di.networkModule
import com.jacks.pizzas.pizzas.data.di.pizzasDataModule
import com.jacks.pizzas.pizzas.presentation.di.pizzaListViewModelModule
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PizzasApp : Application(), ImageLoaderFactory {

	val okHttpClient: OkHttpClient by inject()

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@PizzasApp)
			modules(networkModule, pizzasDataModule, pizzaListViewModelModule)
		}
	}

	override fun newImageLoader(): ImageLoader {
		return ImageLoader.Builder(this)
			.okHttpClient(okHttpClient)
			.memoryCache {
				MemoryCache.Builder(this)
					.maxSizePercent(0.25)
					.build()
			}
			.diskCache {
				DiskCache.Builder()
					.directory(cacheDir.resolve("image_cache"))
					.maxSizeBytes(128L * 1024L * 1024L)
					.build()
			}
			.crossfade(true)
			.build()
	}
}