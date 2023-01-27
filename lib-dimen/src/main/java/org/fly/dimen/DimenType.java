package org.fly.dimen;

/**
 * 屏幕最小宽度的类型，适配Android 3.2以上
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 11:51 AM
 * @since: 1.0.0
 */
public enum DimenType {
    SW_240dp(240),
    SW_300dp(300),
    SW_310dp(310),
    SW_320dp(320),
    SW_330dp(330),
    SW_340dp(340),
    SW_350dp(350),
    SW_360dp(360),
    SW_370dp(370),
    SW_380dp(380),
    SW_390dp(390),
    SW_400dp(400),
    SW_410dp(410),
    SW_411dp(411),
    SW_420dp(420),
    SW_430dp(430),
    SW_432dp(432),
    SW_440dp(440),
    SW_450dp(450),
    SW_460dp(460),
    SW_470dp(470),
    SW_480dp(480),
    SW_490dp(490),
    SW_533dp(533),
    SW_540dp(540),
    SW_592dp(592),
    SW_600dp(600),
    SW_640dp(640),
    SW_662dp(662),
    SW_720dp(720),
    SW_768dp(768),
    SW_800dp(800);

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
