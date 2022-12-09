package org.fly.dimen;

import java.io.File;

/**
 * 生成values-sw<N>dp文件夹的启动类
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 12:05 PM
 * @since: 1.0.0
 */
public class Executor {
    /**
     * 设计稿的最小宽度，最小宽度是不区分方向的，宽度和高度哪一边小，哪一边就是最小宽度
     * px，dp，dpi 之间的转换公式：px = dp * (dpi / 160)
     */
    public static final int SMALLEST_WIDTH = 375;

    public static void main(String[] args) {
        DimenType[] values = DimenType.values();
        for (DimenType value : values) {
            File file = new File("");
            XmlMaker.makeAll(SMALLEST_WIDTH, value, file.getAbsolutePath() + File.separator + "dimenOutput");
        }
    }
}
