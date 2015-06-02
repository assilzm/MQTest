package base.utils

/**
 * 工程工具类
 */
class ProjectUtils {

    /**
     * 默认的源码包名
     */
    final static String SOURCE_PACKAGE_NAME = "src"

    /**
     * 取得工程所在的路径
     * @return 工程所在路径的字符串
     * */
    static String getProjectDir() {
        String projectPath = System.getProperty("user.dir").replaceAll(/(^.*?[\\\/][^\\\/]*[\\\/])$SOURCE_PACKAGE_NAME[\\\/]?.*/, "\$1")
        projectPath = projectPath.replaceAll(/\/?$/, "/")
        projectPath = projectPath.replaceAll("\\\\", "/")
        return projectPath
    }


    /**
     * 取得工程源码包所在的路径
     * @return 工程源码包所在路径的字符串
     * */
    static String getProjectSrcDir() {
        return getProjectDir() + "$SOURCE_PACKAGE_NAME/"
    }

    /**
     * 取得工程代码文件夹(main文件夹)所在的路径
     * @return 工程代码文件夹所在路径的字符串
     */
    static String getProjectMainDir() {
        return getProjectSrcDir() + "main/"
    }

    /**
     * 取得工程资源包所在的路径
     * @return 工程资源包所在的路径
     */
    static String getProjectResourcesDir() {
        return getProjectMainDir() + "resources/"
    }

    /**
     * 取得工程测试包所在的路径
     * @return 工程测试包所在的路径
     */
    static String getProjectTestDir() {
        return getProjectSrcDir() + "test/"
    }

    /**
     * 取得工程测试资源包所在的路径
     * @return 工程测试资源包所在的路径
     */
    static String getProjectTestResourcesDir() {
        return getProjectTestDir() + "resources/"
    }


}
