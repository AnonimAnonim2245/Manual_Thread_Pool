package org.apd.executor;
import org.apd.readerPriority.ReaderWriterSharedVars;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadPool {
    private int nr_tasks = 0, nr_threads = 0;
    public int number = 0;

    private List<String> tasks = new ArrayList<>();
    public ConcurrentLinkedQueue<Runnable> QueueTask;
    private List<MyWorker> listRun = new ArrayList<>();


    public ThreadPool(int nr_thread, int nr_tasks, ReaderWriterSharedVars shared_vars) {
        this.nr_tasks = nr_tasks;
        this.nr_threads = nr_thread;
        QueueTask = new ConcurrentLinkedQueue<>();
        for(int i=0; i<nr_thread; i++){
            MyWorker myR = new MyWorker(QueueTask, shared_vars);
            listRun.add(myR);
            myR.start();
        }
    }


    public void doTask(Runnable c) throws InterruptedException {

        QueueTask.add(c);
    }

    public void stopAll() throws InterruptedException {
        for(MyWorker work : listRun){
            work.join();
        }
    }

}

