package base.utils

import java.util.concurrent.TimeUnit

/**
 * 计时工具类
 */
class TimerUtils {
	private long _start
	private long _end
	private int _cost

	/**
	 * 开始计时
	 */
	void start() {
		_start = System.currentTimeMillis()
	}

	/**
	 * 停止计时
	 */
	void end() {
		_end = System.currentTimeMillis()
		_cost = _end - _start
	}

	/**
	 * 计算开始到停止之间所花的时间，单位为m分n秒
	 * @return 所花费时间的字符串
	 */
	String cost() {
		end()
		return String.format("%d min %d sec",
				TimeUnit.MILLISECONDS.toMinutes(_cost),
				TimeUnit.MILLISECONDS.toSeconds(_cost) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(_cost)),
				TimeUnit.MILLISECONDS.toMillis(_cost) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(_cost))
		);
	}
}
