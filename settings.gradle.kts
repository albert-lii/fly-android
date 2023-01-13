includeBuild("./buildPlugin")

include(
    ":lib-dimen",
    ":lib-utils",
    ":lib-screenadapter",
)

includeModules(
    ":lib-eventbus",
    ":lib-http",
    ":lib-imageloader",
    ":lib-scan",
    ":lib-uikit",
    ":lib-router",
)

/**=================================================================================================
 * 将容器module中的子module导入到项目中
 * 核心代码是：
 * <code>
 *     // 将module添加到项目构建中
 *     include([modulePath])
 *     // 设置module所在的文件目录
 *     project([modulePath]).projectDir = File([module的目录])
 * </code>
 * 使用上述代码，可将指定的module导入项目中，例如导入项目外部的module到本项目中
 *================================================================================================*/

fun includeModules(vararg paths: String, with: (ModuleSpec.() -> Unit)? = null) {
    if (the<ExtraPropertiesExtension>().properties["mavenDep"] == "true" && paths.contains(":module-dynamic")
            .not()
    ) return

    paths.forEach {
        includeModule(it, with)
    }
}

fun includeModule(path: String, with: (ModuleSpec.() -> Unit)? = null) {
    val moduleSpec = ModuleSpec()
    with?.invoke(moduleSpec)
    // 容器module的目录路径
    val moduleDir = File(rootDir, path.replace(':', '/'))
    if (moduleDir.exists()) {
        include(path)
        val containerModule = project(path)
        // 设置容器module的目录路径，这一步一定要有
        containerModule.projectDir = moduleDir
        // 获取settings.gradle文件，此文件中配置了哪些module需要被include
        // 注意：此处设置文件名为settings.gradle，必须module中的文件名必须与其一致，不能使用其Kotlin格式的settings.gradle.kts
        val settingsFile = File(moduleDir, "settings.gradle")
        if (settingsFile.exists()) {
            // 从settingsFile中提取include()方法信息，转到SettingsProxy中处理
            // containerModule只是一个容器，containerModule内部的子module才是真正有用的module
            apply {
                from(settingsFile)
                to(SettingsProxy(settings, containerModule, moduleSpec))
            }
        }
        moduleDir.walk().maxDepth(1).filter { it.isDirectory && it != moduleDir }.forEach {
            val settingsFileInternal = File(it, "settings.gradle")
            if (settingsFileInternal.exists()) {
                val moduleInternal = settings.findProject("$path:${it.name}")
                if (moduleInternal != null) {
                    moduleInternal.projectDir = it
                    apply {
                        from(settingsFileInternal)
                        to(SettingsProxy(settings, moduleInternal, moduleSpec))
                    }
                }
            }
        }
    }
}

/**
 * 容器module的配置类，例如哪些子module需要被过滤或排除
 */
class ModuleSpec {
    private val mExcludes = mutableSetOf<String>()

    private var mFilter: ((String) -> Boolean)? = null

    fun exclude(vararg paths: String) {
        mExcludes.addAll(paths)
    }

    fun filter(predicate: (String) -> Boolean) {
        mFilter = predicate
    }

    fun accept(path: String): Boolean {
        if (mExcludes.contains(path)) {
            return false
        }
        if (mFilter?.invoke(path) == true) {
            return false
        }

        return true
    }
}

class SettingsProxy(
    private var mSettings: Settings, // Settings对象
    private var mModule: ProjectDescriptor, // 容器module
    private var mModuleSpec: ModuleSpec // 容器module的配置
) {

    fun getRootProject(): ProjectDescriptor {
        return mModule
    }

    /**
     * 此方法名必须与settings.gradle中定义的方法名一致，即include
     */
    fun include(vararg paths: String) {
        paths.forEach {
            if (mModuleSpec.accept(it.replace(":", ""))) {
                val descendantPath = generateDescendantPath(it)
                mSettings.include(descendantPath)
                val descendantProjectDir = File(mModule.projectDir, it.replace(':', '/'))
                mSettings.project(descendantPath).projectDir = descendantProjectDir
            }

        }
    }

    fun project(path: String): ProjectDescriptor {
        return mSettings.project("${mModule.path}$path")
    }

    private fun generateDescendantPath(path: String): String {
        // 此处使用"${parentModule.path}${path}"而不是"${path}"是因为如果有很多子module时，直接导入短时间内会分不清它来自哪个容器module，
        // 在前面加上容器module的path，则可以让我们清晰知道当前子module从属于哪个容器module，如[:libs:lib-data]
        return "${mModule.path}$path"
    }
}
include(":lib-router:sample")
