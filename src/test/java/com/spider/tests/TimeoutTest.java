package com.spider.tests;

import java.lang.reflect.Method;
import java.util.concurrent.*;

public class TimeoutTest {
	private static ExecutorService executorService = Executors.newSingleThreadExecutor();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			long start = System.currentTimeMillis();
			Class[] clzs = {int.class};
			Object[] objs = {1};
			TimeoutTest tt = new TimeoutTest();
			String result1 = timeoutMethod(tt, "xxx", clzs, objs, 3000);
			System.out.println("方法实际耗时：" + (System.currentTimeMillis() - start) + "毫秒");
			System.out.println("结果：" + result1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 超时处理
	 *
	 * @param timeout
	 * @return
	 */
	private static String timeoutMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] params, int timeout) {
		String result = null;
		//		ExecutorService executorService = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask<>(() -> {
			String value = null;
			try {
				Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
				Object returnValue = method.invoke(obj, params);
				value = returnValue != null ? returnValue.toString() : null;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			return value;
		});

		executorService.execute(futureTask);
		try {
			result = futureTask.get(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			futureTask.cancel(true);
			System.out.println("方法执行中断！");
		} catch (ExecutionException e) {
			futureTask.cancel(true);
			System.out.println("Excution异常！");
		} catch (TimeoutException e) {
			futureTask.cancel(true);
			throw new RuntimeException("执行超时！", e);
		}
		executorService.shutdownNow();

		return result;
	}

	public void xxx(int i) {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
