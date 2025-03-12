package org.apd.executor;

import org.apd.readerPriority.ReaderWriterSharedVars;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;


class MyWorker extends Thread{
    private final ConcurrentLinkedQueue<Runnable> QueueTask;
    private ReaderWriterSharedVars shared_vars;
    public MyWorker(ConcurrentLinkedQueue<Runnable> arraryQueue, ReaderWriterSharedVars shared_vars){
        QueueTask = arraryQueue;
        this.shared_vars = shared_vars;
    }


    public void run(){
        do{
            Runnable r1 = null;
            r1 = this.QueueTask.poll();
            if(r1 != null) {
                r1.run();
                if (QueueTask.isEmpty()) {
                    break;
                }
            }

        }while(shared_vars.t1.isEmpty() || !QueueTask.isEmpty());
        shared_vars.size2.addAndGet(1);
        this.interrupt();

    }




}

