package org.apd.writerPriority2;

import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.storage.SharedDatabase;
import org.apd.storage.EntryResult;

public class WriterWP2 {
    private final int id;
    private String element;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private SharedDatabase shared_database;


    public WriterWP2(int id, String element, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
        this.id = id;
        this.element = element;
        this.shared_vars = shared_vars;
        this.shared_database = share_database;
        this.global_shared_vars = global_shared_vars;
    }

    public void setElement(String element){
        this.element = element;
    }

    public void run() throws InterruptedException {
        shared_vars.mutex.lock();
        try {
            while (shared_vars.i != 0) {

                try {
                    shared_vars.access.await();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
            shared_vars.i--;
        } finally {
            shared_vars.mutex.unlock();
        }

        write();

        shared_vars.mutex.lock();
        try {
            shared_vars.i = 0;
            shared_vars.access.signalAll();
        } finally {
            shared_vars.mutex.unlock();
        }

    }


    public void write() {

        EntryResult r1 =  shared_database.addData(id, element);
        global_shared_vars.t1.add(r1);

    }
}
