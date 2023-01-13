package org.fly.utils.exts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 在UI线程中启动协程
 */
fun ViewModel.launchOnUI(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.Main) {
        block()
    }
}

/**
 * 在IO线程中创建协程
 */
fun ViewModel.launchOnIO(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(Dispatchers.IO) {
        block()
    }
}