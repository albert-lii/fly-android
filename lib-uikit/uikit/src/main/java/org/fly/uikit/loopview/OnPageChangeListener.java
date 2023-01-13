package org.fly.uikit.loopview;

/**
 * 页面切换监听器
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/13 2:35 下午
 * @since: 1.0.0
 */
public interface OnPageChangeListener {

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);
}
