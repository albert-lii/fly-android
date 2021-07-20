package org.fly.base.arch.mvvm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import org.fly.base.utils.PermissionUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/20 12:08 PM
 * @description: Activity的封装
 * @since: 1.0.0
 */
abstract class BaseActivity : AppCompatActivity(), PermissionUtils.PermissionCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initialize(savedInstanceState)
    }

    protected open fun initContentView() {
        setContentView(getLayoutId())
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    /**
     *  初始化操作
     */
    protected abstract fun initialize(savedInstanceState: Bundle?)


    /**============================================================
     *  权限相关
     **===========================================================*/

    /**
     * 检查是否有权限
     */
    fun hasPermissions(vararg perms: String): Boolean {
        return PermissionUtils.hasPermissions(this, *perms)
    }

    /**
     * 申请权限
     */
    fun applyPermissions(
        tip: String? = null, // 弹框提示
        positiveButtonText: String? = null, // 弹框确定按钮文字
        negativeButtonText: String? = null, // 弹框取消按钮文字
        theme: Int? = null,
        requestCode: Int,
        vararg perms: String
    ) {
        PermissionUtils.applyPermissions(
            this,
            tip,
            positiveButtonText,
            negativeButtonText,
            theme,
            requestCode,
            *perms
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 申请权限失败
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (!perms.isNullOrEmpty()) {
            val refusedPerms = ArrayList<String>()
            val neverAskPerms = ArrayList<String>()
            for (item in perms) {
                if (PermissionUtils.permissionNeverAsk(this, item)) {
                    neverAskPerms.add(item)
                } else {
                    refusedPerms.add(item)
                }
            }
            onPermissonRefused(requestCode, refusedPerms)
            onPermissonNeverAsk(requestCode, neverAskPerms)
        }
    }

    /**
     * 申请权限成功
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    open fun onPermissonRefused(requestCode: Int, perms: MutableList<String>) {

    }

    open fun onPermissonNeverAsk(requestCode: Int, perms: MutableList<String>) {

    }
}