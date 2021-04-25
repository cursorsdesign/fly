package com.cursorsdesign.fly.os;

import org.apache.commons.lang3.SystemUtils;

public class Plateforme {

    public static boolean LINUX = SystemUtils.IS_OS_LINUX;
    public static boolean WINDOW = SystemUtils.IS_OS_WINDOWS;
    public static boolean MAC = SystemUtils.IS_OS_MAC;

}
