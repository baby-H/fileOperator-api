package cn.huyingjie.grabticket.util;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * �̳߳ع���(�߳�ͳһ���ȹ���)
 */
public final class ThreadPoolManager {

    private static ThreadPoolManager sThreadPoolManager = new ThreadPoolManager();

    // �̳߳�ά���̵߳���������
    private static final int SIZE_CORE_POOL = 3;

    // �̳߳�ά���̵߳��������
    private static final int SIZE_MAX_POOL = 4;

    // �̳߳�ά���߳�������Ŀ���ʱ��
    private static final int TIME_KEEP_ALIVE = 5000;

    // �̳߳���ʹ�õĻ�����д�С
    private static final int SIZE_WORK_QUEUE = 500;

    // �����������
    private static final int PERIOD_TASK_QOS = 1000;

    /*
     * �̳߳ص�����������
     */
    public static ThreadPoolManager newInstance() {
        return sThreadPoolManager;
    }

    // ���񻺳����
    private final Queue<Runnable> mTaskQueue = new LinkedList<Runnable>();

    /*
     * �̳߳س�������ʱ��������뻺�����
     */
    private final RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
            executor.getQueue().offer(task);
            mTaskQueue.offer(task);
        }
    };

    /*
     * ����������е��������¼��ص��̳߳�
     */
    private final Runnable mAccessBufferThread = new Runnable() {
        @Override
        public void run() {
            if (hasMoreAcquire()) {
                mThreadPool.execute(mTaskQueue.poll());
            }
        }
    };

    /*
     * ����һ�������̳߳�
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /*
     * ͨ�������߳������Ե�ִ�л������������
     */
    protected final ScheduledFuture<?> mTaskHandler = scheduler.scheduleAtFixedRate(mAccessBufferThread, 0,
            PERIOD_TASK_QOS, TimeUnit.MILLISECONDS);

    /*
     * �̳߳�
     */
    private final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL,
            TIME_KEEP_ALIVE, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(SIZE_WORK_QUEUE), mHandler);

    /*
     * �����췽���������η���Ϊ˽�У���ֹ����ʵ������
     */
    private ThreadPoolManager() {
    }

    public void perpare() {
        if (mThreadPool.isShutdown() && !mThreadPool.prestartCoreThread()) {
            @SuppressWarnings("unused")
            int startThread = mThreadPool.prestartAllCoreThreads();
        }
    }

    /*
     * ��Ϣ���м�鷽��
     */
    private boolean hasMoreAcquire() {
        return !mTaskQueue.isEmpty();
    }

    /*
     * ���̳߳���������񷽷�
     */
    public void addExecuteTask(Runnable task) throws ExecutionException, InterruptedException {
        if (task != null) {
//            Future<?> future = mThreadPool.submit(task);
//            future.get();
            mThreadPool.execute(task);
        }
    }

    protected boolean isTaskEnd() {
        if (mThreadPool.getActiveCount() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void shutdown() {
        mTaskQueue.clear();
        mThreadPool.shutdown();
    }
}

