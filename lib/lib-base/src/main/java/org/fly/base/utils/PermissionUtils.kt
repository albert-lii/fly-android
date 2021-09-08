package org.fly.base.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/10 6:31 PM
 * @description: 权限管理工具类封装
 * @since: 1.0.0
 */
class PermissionUtils {

    companion object {

        /**
         * 是否具有指定权限
         */
        @JvmStatic
        fun hasPermissions(context: Context, vararg perms: String): Boolean {
            return EasyPermissions.hasPermissions(context, *perms)
        }

        /**
         * 是否选择了Never Ask
         */
        @JvmStatic
        fun isPermissionNeverAsk(host: Activity, perm: String): Boolean {
            return EasyPermissions.permissionPermanentlyDenied(host, perm)
        }

        /**
         * 是否选择了Never Ask
         */
        @JvmStatic
        fun isPermissionNeverAsk(host: Fragment, perm: String): Boolean {
            return EasyPermissions.permissionPermanentlyDenied(host, perm)
        }

        /**
         * 是否选择了Never Ask
         */
        @JvmStatic
        fun isPermissionsNeverAsk(host: Activity, perms: List<String>): Boolean {
            return EasyPermissions.somePermissionPermanentlyDenied(host, perms)
        }

        /**
         * 是否选择了Never Ask
         */
        @JvmStatic
        fun isPermissionsNeverAsk(host: Fragment, perms: List<String>): Boolean {
            return EasyPermissions.somePermissionPermanentlyDenied(host, perms)
        }

        /**
         * 是否申请权限失败
         */
        @JvmStatic
        fun isPermissionsDenied(host: Activity, vararg perms: String): Boolean {
            return EasyPermissions.somePermissionDenied(host, *perms)
        }

        /**
         * 是否申请权限失败
         */
        @JvmStatic
        fun isPermissionsDenied(host: Fragment, vararg perms: String): Boolean {
            return EasyPermissions.somePermissionDenied(host, *perms)
        }

        /**
         * 申请权限
         */
        @JvmStatic
        fun applyPermissions(
            host: Activity,
            tip: String? = null, // 弹框提示
            positiveButtonText: String? = null, // 弹框确定按钮文字
            negativeButtonText: String? = null, // 弹框取消按钮文字
            theme: Int? = null,
            requestCode: Int,
            vararg perms: String
        ) {
            val builder = PermissionRequest.Builder(host, requestCode, *perms)
                .also {
                    tip?.run { it.setRationale(this) }
                    positiveButtonText?.run { it.setPositiveButtonText(this) }
                    negativeButtonText?.run { it.setNegativeButtonText(this) }
                    theme?.run { it.setTheme(this) }
                }
            EasyPermissions.requestPermissions(builder.build())
        }

        @JvmStatic
        fun applyPermissions(
            host: Fragment,
            tip: String? = null, // 弹框提示
            positiveButtonText: String? = null, // 弹框确定按钮文字
            negativeButtonText: String? = null, // 弹框取消按钮文字
            theme: Int? = null,
            requestCode: Int,
            vararg perms: String
        ) {
            val builder = PermissionRequest.Builder(host, requestCode, *perms)
                .also {
                    tip?.run { it.setRationale(this) }
                    positiveButtonText?.run { it.setPositiveButtonText(this) }
                    negativeButtonText?.run { it.setNegativeButtonText(this) }
                    theme?.run { it.setTheme(this) }
                }
            EasyPermissions.requestPermissions(builder.build())
        }

        /**
         * 申请权限的回调
         */
        fun onRequestPermissionsResult(
            requestCode: Int,
            perms: Array<out String>,
            grantResults: IntArray,
            host: Activity,
            callback: PermissionCallbacks? = null,
        ) {
            EasyPermissions.onRequestPermissionsResult(
                requestCode,
                perms,
                grantResults,
                object : EasyPermissions.PermissionCallbacks {
                    override fun onRequestPermissionsResult(
                        requestCode: Int,
                        permissions: Array<out String>,
                        grantResults: IntArray
                    ) {
                        // do nothing
                    }

                    override fun onPermissionsGranted(
                        requestCode: Int,
                        perms: MutableList<String>
                    ) {
                        callback?.onPermissionsGranted(requestCode, perms)
                    }

                    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
                        if (!perms.isNullOrEmpty()) {
                            val refusedPerms = ArrayList<String>()
                            val neverAskPerms = ArrayList<String>()
                            for (item in perms) {
                                if (isPermissionNeverAsk(host, item)) {
                                    neverAskPerms.add(item)
                                } else {
                                    refusedPerms.add(item)
                                }
                            }
                            callback?.onPermissonRefused(requestCode, refusedPerms)
                            callback?.onPermissonNeverAsk(requestCode, neverAskPerms)
                        }
                    }
                }
            )
        }
    }

    interface PermissionCallbacks {
        /**
         * 申请权限成功
         */
        fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>)

        /**
         * 申请权限失败
         */
        fun onPermissonRefused(requestCode: Int, perms: MutableList<String>)

        /**
         * 申请权限失败，并且用户选择了不再询问
         */
        fun onPermissonNeverAsk(requestCode: Int, perms: MutableList<String>)
    }
}