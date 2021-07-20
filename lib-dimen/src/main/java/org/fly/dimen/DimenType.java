package org.fly.dimen;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 11:51 AM
 * @description: 屏幕最小宽度的类型，适配Android 3.2以上
 * @since: 1.0.0
 */
public enum DimenType {
    DP_sw240(240),
    DP_sw300(300),
    DP_sw310(310),
    DP_sw320(320),
    DP_sw330(330),
    DP_sw340(340),
    DP_sw350(350),
    DP_sw360(360),
    DP_sw370(370),
    DP_sw380(380),
    DP_sw390(390),
    DP_sw400(400),
    DP_sw410(410),
    DP_sw411(411),
    DP_sw420(420),
    DP_sw430(430),
    DP_sw432(432),
    DP_sw440(440),
    DP_sw450(450),
    DP_sw460(460),
    DP_sw470(470),
    DP_sw480(480),
    DP_sw490(490),
    DP_sw533(533),
    DP_sw540(540),
    DP_sw592(592);

    /**
     * 屏幕最小宽度
     */
    private int swWidthDp;


    DimenType(int swWidthDp) {
        this.swWidthDp = swWidthDp;
    }

    public int getSwWidthDp() {
        return swWidthDp;
    }

    public void setSwWidthDp(int swWidthDp) {
        this.swWidthDp = swWidthDp;
    }
}
