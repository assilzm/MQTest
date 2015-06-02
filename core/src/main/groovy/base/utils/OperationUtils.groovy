package base.utils

import base.emuns.OperationType

/**
 * 操作系统相关信息获取
 */
class OperationUtils {

    /**
     * 取得操作系统的名称
     * @return 操作系统名称的字符串
     */
    static String getOperationName(){
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        return os
    }



    /**
     * 取得操作系统的类型
     * @return 操作系统枚举
     */
    static OperationType getOperation(){
        String operationName = operationName.toLowerCase()
        if (operationName.contains("windows"))
            return OperationType.WINDOWS
        if (operationName.contains("mac"))
            return OperationType.MAC
        if (operationName.contains("linux"))
            return OperationType.LINUX
    }



}

