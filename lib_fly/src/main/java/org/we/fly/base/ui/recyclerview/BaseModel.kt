package org.we.fly.base.ui.recyclerview

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 9:51 PM
 * @description: 网络请求model
 * @since: 1.0.0
 */
class BaseModel {

    fun <T> Observable<T>.transform() {
        this.compose(applySchedulers())
    }

    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}