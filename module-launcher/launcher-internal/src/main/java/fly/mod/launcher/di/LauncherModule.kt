package fly.mod.launcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/15 8:36 下午
 * @description: -
 * @since: 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
object LauncherModule {

    @Provides
    @Singleton
    fun provideLauncerService() = LauncherApiServiceImpl()
}