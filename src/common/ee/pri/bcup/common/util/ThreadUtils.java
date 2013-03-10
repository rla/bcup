package ee.pri.bcup.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Helper methods for working with threads.
 * 
 * Thread listing code is from: http://nadeausoftware.com/articles/2008/04/
 * java_tip_how_list_and_find_threads_and_thread_groups
 * #Gettingtherootthreadgroup
 * 
 * @author Raivo Laanemets
 */
public class ThreadUtils {

	/**
	 * Joins thread. See {@link Thread#join()}. Throws
	 * {@link InterruptedException} wrapped inside {@link RuntimeException}.
	 */
	public static void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Cannot join on thread " + thread, e);
		}
	}

	/**
	 * Returns the root thread group.
	 */
	public static ThreadGroup getRootThreadGroup() {
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		ThreadGroup ptg;

		while ((ptg = tg.getParent()) != null) {
			tg = ptg;
		}

		return tg;
	}

	/**
	 * Returns all thread groups.
	 */
	public static ThreadGroup[] getAllThreadGroups() {
		final ThreadGroup root = getRootThreadGroup();

		int nAlloc = root.activeGroupCount();
		int n = 0;
		ThreadGroup[] groups;
		do {
			nAlloc *= 2;
			groups = new ThreadGroup[nAlloc];
			n = root.enumerate(groups, true);
		} while (n == nAlloc);

		ThreadGroup[] allGroups = new ThreadGroup[n + 1];
		allGroups[0] = root;
		System.arraycopy(groups, 0, allGroups, 1, n);

		return allGroups;
	}

	/**
	 * Returns all threads.
	 */
	public static Thread[] getAllThreads() {
		ThreadGroup root = getRootThreadGroup();
		ThreadMXBean thbean = ManagementFactory.getThreadMXBean();
		
		int nAlloc = thbean.getThreadCount();
		int n = 0;
		Thread[] threads;
		do {
			nAlloc *= 2;
			threads = new Thread[nAlloc];
			n = root.enumerate(threads, true);
		} while (n == nAlloc);
		
		return java.util.Arrays.copyOf(threads, n);
	}

}
