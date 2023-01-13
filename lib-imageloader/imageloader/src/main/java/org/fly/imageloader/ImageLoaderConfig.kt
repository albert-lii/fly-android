package org.fly.imageloader

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 图片加载器配置
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/1 3:42 下午
 * @since: 1.0.0
 */
class ImageLoaderConfig(
    val imageLoaderStrategy: ImageLoaderStrategy,
    val defaultOptions: ImageLoaderOptions = ImageLoaderOptions(),
    val executor: Executor = Executors.newFixedThreadPool(1)
)