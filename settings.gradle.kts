includeBuild("./buildPlugin")

include(":lib-dimen")

includeContainerModules(
    ":lib-eventbus",
    ":lib-http"
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
fun includeContainerModules(vararg paths: String, with: (ParentModuleSpec.() -> Unit)? = null) {
    paths.forEach {
        includeModule(it, with)
    }
}

fun includeModule(path: String, with: (ParentModuleSpec.() -> Unit)? = null) {
    val parentModuleSpec = ParentModuleSpec()
    with?.invoke(parentModuleSpec)
    // 容器module的目录路径
    val parentModuleDir = File(rootDir, path.replace(":", "/"))
    if (parentModuleDir.exists()) {
        include(path)
        val parentModule = project(path)
        // 设置容器module的目录路径，这一步一定要有
        parentModule.projectDir = parentModuleDir
        // 获取settings.gradle文件，此文件中配置了哪些module需要被include
        // 注意：此处设置文件名为settings.gradle，必须module中的文件名必须与其一致，不能使用其Kotlin格式的settings.gradle.kts
        val settingsFile = File(parentModuleDir, "settings.gradle")
        if (settingsFile.exists()) {
            // 从settingsFile中提取include()方法信息，转到SettingsProxy中处理
            // parentModule只是一个容器，parentModule内部的子module才是真正有用的module
            apply {
                from(settingsFile)
                to(SettingsProxy(settings, parentModule, parentModuleSpec))
            }
        }
        parentModuleDir.walk().maxDepth(1).filter { it.isDirectory && it != parentModuleDir }.forEach {
            val settingsFileInternal = File(it, "settings.gradle")
            if (settingsFileInternal.exists()) {
                val moduleInternal = settings.findProject("$path:${it.name}")
                if (moduleInternal != null) {
                    moduleInternal.projectDir = it
                    apply {
                        from(settingsFileInternal)
                        to(SettingsProxy(settings, moduleInternal, parentModuleSpec))
                    }
                }
            }
        }

    }
}

/**
 * 容器module的配置类，例如哪些子module需要被过滤或排除
 */
class ParentModuleSpec {
    private val excludes = mutableSetOf<String>()

    private var filter: ((String) -> Boolean)? = null

    fun exclude(vararg paths: String) {
        excludes.addAll(paths)
    }

    fun filter(predicate: (String) -> Boolean) {
        filter = predicate
    }

    fun accept(path: String): Boolean {
        if (excludes.contains(path)) {
            return false
        }
        if (filter?.invoke(path) == true) {
            return false
        }
        return true
    }
}

/**
 * 子module导入代理
 *
 * @property settingsObj settings对象
 * @property parentModule 容器module
 * @property parentModuleSpec 容器module的配置
 */
class SettingsProxy(
    private var settingsObj: Settings,
    private var parentModule: ProjectDescriptor,
    private var parentModuleSpec: ParentModuleSpec
) {

    fun getRootProject(): ProjectDescriptor {
        return parentModule
    }

    fun project(path: String): ProjectDescriptor {
        return settingsObj.project("${parentModule.path}$path")
    }

    /**
     * 此方法名必须与settings.gradle中定义的方法名一致，即include
     */
    fun include(vararg paths: String) {
        paths.forEach {
            if (parentModuleSpec.accept(it.replace(":", ""))) {
                val descendantPath = generateDescendantPath(it)
                settingsObj.include(descendantPath)
                val descendantProjectDir = File(parentModule.projectDir, it.replace(":", "/"))
                settingsObj.project(descendantPath).projectDir = descendantProjectDir
            }
        }
    }

    /**
     * 生成子module的路径
     */
    private fun generateDescendantPath(path: String): String {
        // 此处使用"${parentModule.path}${path}"而不是"${path}"是因为如果有很多子module时，直接导入短时间内会分不清它来自哪个容器module，
        // 在前面加上容器module的path，则可以让我们清晰知道当前子module从属于哪个容器module，如[:libs:lib-data]
        return "${parentModule.path}${path}"
    }
}
include(":lib-http:sample")
