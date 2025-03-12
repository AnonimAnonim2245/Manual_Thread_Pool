package org.apd.executor;

import org.apd.readerPriority.ReaderRP;
import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.readerPriority.WriterRP;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;
import org.apd.writerPriority.ReaderWP;
import org.apd.writerPriority.WriterWP;
import org.apd.writerPriority2.ReaderWP2;
import org.apd.writerPriority2.WriterWP2;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/* DO NOT MODIFY THE METHODS SIGNATURES */
public class TaskExecutor {
    private final SharedDatabase sharedDatabase;

    public TaskExecutor(int storageSize, int blockSize, long readDuration, long writeDuration) {
        sharedDatabase = new SharedDatabase(storageSize, blockSize, readDuration, writeDuration);
    }

    public List<EntryResult> ExecuteWork(int numberOfThreads, List<StorageTask> tasks, LockType lockType) throws InterruptedException {
        /* IMPLEMENT HERE THE THREAD POOL, ASSIGN THE TASKS AND RETURN THE RESULTS */
        ReaderWriterSharedVars shared_vars = new ReaderWriterSharedVars();




        ThreadPool threadPool = new ThreadPool(numberOfThreads, tasks.size(), shared_vars);

        ConcurrentHashMap<Integer, ReaderRP> findReaders = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, WriterRP> findWriters = new ConcurrentHashMap<>();


        ConcurrentHashMap<Integer, ReaderWP> findReadersWP = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, WriterWP> findWritersWP = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer, ReaderWP2> findReadersWP2 = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, WriterWP2> findWritersWP2 = new ConcurrentHashMap<>();

        ConcurrentHashMap<Integer, ReaderWriterSharedVars> findShared= new ConcurrentHashMap<>();


        if(lockType == LockType.ReaderPreferred) {
            for (int i = 0; i < tasks.size(); i++) {
                ReaderWriterSharedVars sharedVars = null;
                if(findShared.get(tasks.get(i).index()) == null){
                    sharedVars = new ReaderWriterSharedVars();
                    findShared.put(tasks.get(i).index(), sharedVars);
                } else{
                    sharedVars= findShared.get(tasks.get(i).index());
                }
                if (!tasks.get(i).isWrite()) {
                    int finalI = i;
                    ReaderWriterSharedVars finalSharedVars = sharedVars;
                    threadPool.doTask(() -> {

                        StorageTask t1 = tasks.get(finalI);
                        ReaderRP reader = null;
                        if(findReaders.get(t1.index()) == null){
                            reader = new ReaderRP(t1.index(), finalSharedVars, shared_vars, sharedDatabase);
                            findReaders.put(t1.index(), reader);
                        } else {
                            reader = findReaders.get(t1.index());
                        }
                        try {
                            reader.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                    });
                } else {
                    int finalI1 = i;

                    ReaderWriterSharedVars finalSharedVars1 = sharedVars;
                    threadPool.doTask(() -> {
                        StorageTask t1 = tasks.get(finalI1);
                        WriterRP writer = null;
                        if(findWriters.get(t1.index()) == null){
                            writer = new WriterRP(t1.index(), t1.data(), finalSharedVars1, shared_vars, sharedDatabase);
                            findWriters.put(t1.index(), writer);
                        } else {
                            writer = findWriters.get(t1.index());
                        }
                        writer.setElement(t1.data());
                        try {
                            writer.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }

        } else if(lockType == LockType.WriterPreferred1){
            for (int i = 0; i < tasks.size(); i++) {
                ReaderWriterSharedVars sharedVars = null;
                if(findShared.get(tasks.get(i).index()) == null){
                    sharedVars = new ReaderWriterSharedVars();
                    findShared.put(tasks.get(i).index(), sharedVars);
                } else{
                    sharedVars= findShared.get(tasks.get(i).index());
                }
                if (!tasks.get(i).isWrite()) {
                    int finalI = i;
                    ReaderWriterSharedVars finalSharedVars = sharedVars;
                    threadPool.doTask(() -> {

                        StorageTask t1 = tasks.get(finalI);
                        ReaderWP reader = null;
                        if(findReadersWP.get(t1.index()) == null){
                            reader = new ReaderWP(t1.index(), finalSharedVars, shared_vars, sharedDatabase);
                            findReadersWP.put(t1.index(), reader);
                        } else {
                            reader = findReadersWP.get(t1.index());
                        }

                        try {
                            reader.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                } else {
                    int finalI1 = i;

                    ReaderWriterSharedVars finalSharedVars1 = sharedVars;
                    threadPool.doTask(() -> {
                        StorageTask t1 = tasks.get(finalI1);
                        long c = System.currentTimeMillis();
                        WriterWP writer = null;
                        if(findWritersWP.get(t1.index()) == null){
                            writer = new WriterWP(t1.index(), t1.data(), finalSharedVars1, shared_vars, sharedDatabase);
                            findWritersWP.put(t1.index(), writer);
                        } else {
                            writer = findWritersWP.get(t1.index());
                        }
                        writer.setElement(t1.data());

                        try {
                            writer.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }
        } else{
            for (int i = 0; i < tasks.size(); i++) {
                ReaderWriterSharedVars sharedVars = null;
                if(findShared.get(tasks.get(i).index()) == null){
                    sharedVars = new ReaderWriterSharedVars();
                    findShared.put(tasks.get(i).index(), sharedVars);
                } else{
                    sharedVars= findShared.get(tasks.get(i).index());
                }
                if (!tasks.get(i).isWrite()) {
                    int finalI = i;
                    ReaderWriterSharedVars finalSharedVars = sharedVars;
                    threadPool.doTask(() -> {

                        StorageTask t1 = tasks.get(finalI);
                        ReaderWP2 reader = null;
                        if(findReadersWP2.get(t1.index()) == null){
                            reader = new ReaderWP2(t1.index(), finalSharedVars, shared_vars, sharedDatabase);
                            findReadersWP2.put(t1.index(), reader);
                        } else {
                            reader = findReadersWP2.get(t1.index());
                        }
                        try {
                            reader.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                } else {
                    int finalI1 = i;

                    ReaderWriterSharedVars finalSharedVars1 = sharedVars;
                    threadPool.doTask(() -> {
                        StorageTask t1 = tasks.get(finalI1);
                        WriterWP2 writer = null;
                        if(findWritersWP2.get(t1.index()) == null){
                            writer = new WriterWP2(t1.index(), t1.data(), finalSharedVars1, shared_vars, sharedDatabase);
                            findWritersWP2.put(t1.index(), writer);
                        } else {
                            writer = findWritersWP2.get(t1.index());
                        }
                        writer.setElement(t1.data());

                        try {
                            writer.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    });
                }
            }
        }



        while(shared_vars.size2.get() == 0 || shared_vars.t1.size() != tasks.size()){
            System.out.print("");
        }

        threadPool.stopAll();
        System.out.println(shared_vars.t1);

        return shared_vars.t1;
    }

    public List<EntryResult> ExecuteWorkSerial(List<StorageTask> tasks) {
        var results = tasks.stream().map(task -> {
            try {
                if (task.isWrite()) {
                    return sharedDatabase.addData(task.index(), task.data());
                } else {
                    return sharedDatabase.getData(task.index());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();

        return results.stream().toList();
    }
}
