package org.apd.readerPriority;

import org.apd.executor.StorageTask;
import org.apd.storage.EntryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderWriterSharedVars {
    int readers = 0;
    public int size=0;
    public AtomicInteger size2 =  new AtomicInteger();
    Semaphore readWrite = new Semaphore(1);

    public CopyOnWriteArrayList<EntryResult> t1 = new CopyOnWriteArrayList<>();
    Semaphore mutexNumberOfReaders = new Semaphore(1);


    public Semaphore sem_writer = new Semaphore(0);

    public Semaphore sem_reader = new Semaphore(0);
    public int waiting_readers = 0;
    public int waiting_writers = 0;

    public Semaphore enter = new Semaphore(1);
    public int nw = 0, nr = 0;

    public ReentrantLock mutex = new ReentrantLock();
    public final Condition access = mutex.newCondition();
    public int i = 0;



    // TODO: Add semaphores and anything else needed for synchronization


}
