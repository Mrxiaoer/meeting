package com.spider.modules.spider.utils;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 执行超时处理的方法
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-14
 */
public class RunTimeout {

	/**
	 * 超时处理
	 *
	 * @param timeout
	 * @return
	 */
	public static String timeoutMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] params, int timeout)
			throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		String result = null;
		//		ExecutorService executorService = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask<>(() -> {
			String value = null;
			try {
				Method method = obj.getClass().getMethod(methodName, parameterTypes);
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
		} catch (InterruptedException | ExecutionException e) {
			futureTask.cancel(true);
			throw e;
		} catch (TimeoutException e) {
			futureTask.cancel(true);
			throw new RuntimeException("执行超时！", e);
		}
		executorService.shutdownNow();

		return result;
	}

}
