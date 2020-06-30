package org.we.fly.dimenmaker;

import java.io.File;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 12:05 PM
 * @description: 生成values-sw<N>dp文件夹的启动类
 * @since: 1.0.0
 */
public class DimenMaker {
    /**
     * 设计稿的最小宽度，最小宽度是不区分方向的，宽度和高度哪一边小，哪一边就是最小宽度
     * px，dp，dpi 之间的转换公式：px = dp * (dpi / 160)
     */
    public static final int SMALLEST_WIDTH = 375;

    public static void main(String[] args) {
        int smallest = SMALLEST_WIDTH;
        DimenType[] values = DimenType.values();
        for (DimenType value : values) {
            File file = new File("");
            MakeUtil.makeAll(smallest, value, file.getAbsolutePath());
        }
    }
}
