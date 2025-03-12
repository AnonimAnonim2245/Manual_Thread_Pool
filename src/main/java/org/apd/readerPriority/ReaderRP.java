package org.apd.readerPriority;

import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.storage.SharedDatabase;
import org.apd.storage.EntryResult;

public class ReaderRP {

    private final int id;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private SharedDatabase shared_database;

    public ReaderRP(int id, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
        this.id = id;
        this.shared_database = share_database;
        this.shared_vars = shared_vars;
        this.global_shared_vars = global_shared_vars;

    }

    public void run() throws InterruptedException {

        // TODO: add the synchronization
        shared_vars.mutexNumberOfReaders.acquire();
        shared_vars.readers++;

        if (shared_vars.readers == 1) {
            shared_vars.readWrite.acquire();
        };

        shared_vars.mutexNumberOfReaders.release();
        read();
        shared_vars.mutexNumberOfReaders.acquire();

        shared_vars.readers = shared_vars.readers - 1;

        if (shared_vars.readers == 0) {
            shared_vars.readWrite.release();
        }
        shared_vars.mutexNumberOfReaders.release();
    }


    public void read() {
        EntryResult r1 = shared_database.getData(id);
        global_shared_vars.t1.add(r1);
    }
}
