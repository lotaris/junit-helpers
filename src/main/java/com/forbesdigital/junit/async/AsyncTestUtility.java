package com.forbesdigital.junit.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for asynchronous test.
 *
 * Provide some method to put the test in 'sleep' mode and wait for
 * a method to return a result.
 *
 * Typical usage would be :
 *
 * doAsyncOperation();
 * waitUntil(resultAvailable(), 1000, 3);
 * continueTheTest()
 *
 * @author Valentin Delaye <valentin.delaye@forbes-digital.com>
 */
public class AsyncTestUtility {

	//<editor-fold defaultstate="collapsed" desc="Inner Classes">
	/**
	 * Exception raised when an asynchronous test raise an exception
	 */
	public static class AsyncTestException extends RuntimeException {

		public AsyncTestException(String errorMesssage) {
			super(errorMesssage);
		}

		public AsyncTestException(String string, Throwable thrwbl) {
			super(string, thrwbl);
		}
	}

	/**
	 * POJO class to store result for an asynchronous operation.
	 *
	 * Have to be marked as 'completed' to indicate that the async result
	 * is now available for test
	 *
	 * @author Laurent Prevost <laurent.prevost@forbes-digital.com>
	 */
	public static class AsyncResult {

		/**
		 * The current error message. Can be null.
		 */
		private String errorMessage;

		/**
		 * To mark the result has success
		 */
		private boolean completed = false;

		/**
		 * To indicate an error
		 */
		private boolean error = false;

		//<editor-fold defaultstate="collapsed" desc="Constructors">
		public AsyncResult() {

		}

		public AsyncResult(String errorMessage) {
			this.errorMessage = errorMessage;
			this.error = true;
		}
		//</editor-fold>

		//<editor-fold defaultstate="collapsed" desc="Getters & Setters">
		public void setCompleted() {
			completed = true;
		}

		public boolean isCompleted() {
			return completed;
		}

		public boolean isError() {
			return error;
		}

		public void setErrorMessage(String errorMessage) {
			error = true;
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
		//</editor-fold>

	}

	/**
	 * Asynchronous call back to be executed periodically
	 */
	public abstract static class AsyncCallback implements Callable<AsyncResult> {

		@Override
		public final AsyncResult call() throws Exception {

			AsyncResult result = new AsyncResult();

			try {
				execute(result);
			}
			catch(Exception e) {
				result.setCompleted();
				result.setErrorMessage(e.getMessage());
			}

			return result;
		}

		/**
		 * Execute method to be implemented by the test to execute some code periodically to check
		 * if the asynchronous method to test has been completed.
		 *
		 * @param result The result. Have to be marked as 'completed' to indicate that the test can continue.
		 * @throws Exception
		 */
		public abstract void execute(AsyncResult result) throws Exception;

	}
	//</editor-fold>

	/**
	 * Wait until a specific method completes.
	 *
	 * @param callback The callback to execute periodically.
	 * @param timeToWait Time in milliseconds to wait between each call.
	 * @throws AsyncTestException in case of timeout or other error
	 */
	public static void waitUntil(AsyncCallback callback, long timeToWait) throws AsyncTestException {
		waitUntil(callback, timeToWait, 1);
	}

	/**
	 * Wait until a specific method completes
	 *
	 * @param callback The callback to execute periodically.
	 * @param timeToWait Time in milliseconds to wait between each call.
	 * @param retry The number of retry
	 * @throws AsyncTestException in case of timeout or other error
	 */
	public static void waitUntil(AsyncCallback callback, long timeToWait, int retry) throws AsyncTestException {

		for(int i = 0 ; i < retry ; i++) {

			// A single thread will be used to execute the callable
			ExecutorService service = Executors.newSingleThreadExecutor();

			try {

				// Submit the callable and wait for the result
				Future<AsyncResult> future = service.submit(callback);
				service.awaitTermination(timeToWait, TimeUnit.MILLISECONDS);
				AsyncResult result = future.get();

				// If the result has been marked has success no need to continue
				if(result.isCompleted()) {
					return;
				}
				else if(result.isError()) {
					throw new AsyncTestException(result.getErrorMessage());
				}

				// Wait a bit before next executor
				Thread.sleep(timeToWait);

			}
			catch(InterruptedException | ExecutionException e) {
				service.shutdown();
				throw new AsyncTestException(e.getMessage(), e.getCause());
			}
		}

		throw new AsyncTestException("Unable to retrieve asynchronous result after " + retry + " attemps of " + timeToWait + " miliseconds");
	}
}
