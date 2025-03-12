package org.apd.writerPriority2;

import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.storage.SharedDatabase;
import org.apd.storage.EntryResult;
public class ReaderWP2 {

    private final int id;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private SharedDatabase shared_database;

    public ReaderWP2(int id, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
        this.id = id;
        this.shared_database = share_database;
        this.shared_vars = shared_vars;
        this.global_shared_vars = global_shared_vars;

    }

    public void run() throws InterruptedException {
        shared_vars.mutex.lock();

        try {
            while (shared_vars.i < 0) {
                shared_vars.access.await();

            }
            shared_vars.i++;


        } finally {
            shared_vars.mutex.unlock();

        }

        read();

        shared_vars.mutex.lock();
        try {
            shared_vars.i--;

            if (shared_vars.i == 0) {
                shared_vars.access.signal();
            }
        } finally {
            shared_vars.mutex.unlock();
        }

    }


    public void read() {

        EntryResult r1 = shared_database.getData(id);
        global_shared_vars.t1.add(r1);
    }
}
