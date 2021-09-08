package org.fly.imageloader


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 8:51 下午
 * @description: 图片加载器可选操作
 * @since: 1.0.0
 */
class ImageLoaderOptions(
    var placeHolder: HolderInfo? = null, // 加载占位图
    var errorHolder: HolderInfo? = null, // 加载失败显示图
    var resize: ImageSize? = null, // 设置图片尺寸
    var corners: Corners? = null, // 设置图片圆角
    var scaleType: ScaleType? = null, // 图片显示形式
    var resourceType: ResourceType = ResourceType.DRAWABLE, // 加载的图片的格式
    var gifExtOption: GifExtOption? = null,
    var isSkipMemoryCache: Boolean = false, // 是否不使用图片在内存中的缓存
    var diskCacheStrategy: DiskCacheStrategy? = null, // 本地磁盘缓存策略
    var listener: LoaderListener? = null // 图片加载监听
) {

    class HolderInfo(
        var resId: Int = -1,
        var corners: Corners? = null,
        var scaleType: ScaleType? = null
    )

    /**
     * 圆角模型
     */
    class Corners(
        var topLeftRadius: Float,
        var topRightRadius: Float = topLeftRadius,
        var bottomRightRadius: Float = topLeftRadius,
        var bottomLeftRadius: Float = topLeftRadius
    )

    /**
     * 图片尺寸
     */
    class ImageSize(val width: Int = ORIGIN_SIZE, val height: Int = ORIGIN_SIZE) {
        companion object {
            const val ORIGIN_SIZE = -2
        }
    }

    /**
     * 图片显示形式
     */
    enum class ScaleType {
        CENTER_CROP,
        CENTER_INSIDE,
        FIT_CENTER,
        CIRCLE_CROP
    }

    /**
     * 资源类型
     */
    enum class ResourceType {
        DRAWABLE,
        BITMAP,
        GIF,
        FILE
    }

    /**
     * 磁盘缓存策略
     */
    enum class DiskCacheStrategy {
        ALL,
        NONE,
        RESOURCE,
        DATA,
        AUTOMATIC
    }

    /**
     * 图片加载监听器
     */
    interface LoaderListener {

        fun onSuccess()

        fun onError(msg: String)

        fun onUpdateProgress(bytesRead: Long, bytesTotal: Long)
    }

    open class LoaderListenerAdapter : LoaderListener {

        override fun onSuccess() {
        }

        override fun onError(msg: String) {
        }

        override fun onUpdateProgress(bytesRead: Long, bytesTotal: Long) {
        }
    }

    /**
     * Gif的额外操作
     */
    class GifExtOption(val gifLoopCount: Int)
}