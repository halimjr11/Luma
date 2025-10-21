package com.halimjr11.luma.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.halimjr11.luma.BuildConfig
import com.halimjr11.luma.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.luma.core.coroutines.impl.DefaultDispatcherProvider
import com.halimjr11.luma.data.remote.interceptor.AuthInterceptor
import com.halimjr11.luma.data.remote.interceptor.SessionInterceptor
import com.halimjr11.luma.data.local.preferences.SharedPreferenceHelper
import com.halimjr11.luma.data.local.preferences.impl.SharedPreferenceHelperImpl
import com.halimjr11.luma.data.remote.mapper.RemoteDataMapper
import com.halimjr11.luma.data.remote.mapper.impl.RemoteDataMapperImpl
import com.halimjr11.luma.domain.repository.LocationRepository
import com.halimjr11.luma.domain.repository.LumaLocalRepository
import com.halimjr11.luma.domain.repository.LumaRemoteRepository
import com.halimjr11.luma.data.remote.repository.LocationRepositoryImpl
import com.halimjr11.luma.data.remote.repository.LumaLocalRepositoryImpl
import com.halimjr11.luma.data.remote.repository.LumaRemoteRepositoryImpl
import com.halimjr11.luma.data.remote.service.LumaService
import com.halimjr11.luma.domain.usecase.GetHomeStoryUseCase
import com.halimjr11.luma.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object AppModules {
    private val repositoryModule = module {
        single<LumaRemoteRepository> { LumaRemoteRepositoryImpl(get(), get(), get(), get(), get()) }
        single<LumaLocalRepository> { LumaLocalRepositoryImpl(get(), get()) }
        single<LocationRepository> { LocationRepositoryImpl(get()) }
    }
    private val coroutineModule = module {
        single<CoroutineDispatcherProvider> { DefaultDispatcherProvider() }
    }
    private val useCaseModule = module {
        single { GetHomeStoryUseCase(get(), get()) }
    }
    private val mapperModule = module {
        single<RemoteDataMapper> { RemoteDataMapperImpl(get()) }
    }
    private val sharedPrefHelperModule = module {
        single<SharedPreferenceHelper> { SharedPreferenceHelperImpl(get()) }
    }
    private val sharedPrefModule = module {
        single { androidContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE) }
    }
    private val interceptorModule = module {
        single {
            HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
        single {
            ChuckerInterceptor.Builder(androidContext())
                .collector(
                    ChuckerCollector(
                        context = androidContext(),
                        showNotification = true,
                        retentionPeriod = RetentionManager.Period.ONE_HOUR
                    )
                )
                .maxContentLength(250_000L)
                .alwaysReadResponseBody(true)
                .build()
        }
        single { SessionInterceptor(androidContext(), get()) }
        single {
            AuthInterceptor(get())
        }
    }

    private val serviceModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(get<HttpLoggingInterceptor>())
                .addInterceptor(get<ChuckerInterceptor>())
                .addInterceptor(get<SessionInterceptor>())
                .addInterceptor(get<AuthInterceptor>())
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get<OkHttpClient>())
                .build()
        }

        single<LumaService> { get<Retrofit>().create(LumaService::class.java) }
    }

    fun getAppModules() = listOf(
        coroutineModule,
        interceptorModule,
        sharedPrefModule,
        mapperModule,
        serviceModule,
        repositoryModule,
        useCaseModule,
        sharedPrefHelperModule
    )
}