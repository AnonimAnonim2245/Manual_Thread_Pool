package org.apd.writerPriority;


import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;

public class ReaderWP {

    private final int id;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private double start_time;
    private double completion_time;
    private SharedDatabase shared_database;

    public ReaderWP(int id, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
        this.id = id;
        this.shared_database = share_database;
        this.shared_vars = shared_vars;
        this.global_shared_vars = global_shared_vars;

    }

    public void run() throws InterruptedException {
        try {
            shared_vars.enter.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (shared_vars.nw + shared_vars.waiting_writers > 0) {
            shared_vars.waiting_readers++;
        } else {
            shared_vars.sem_reader.release();
            shared_vars.nr++;
        }

        shared_vars.enter.release();
        shared_vars.sem_reader.acquire();
        read();

        shared_vars.enter.acquire();
        shared_vars.nr--;

        if (shared_vars.waiting_writers > 0 && shared_vars.nr == 0) {
            shared_vars.sem_writer.release();
            shared_vars.nw++;
            shared_vars.waiting_writers--;

        }

        shared_vars.enter.release();
    }

    public double getCompletion_time() {
        return completion_time;
    }

    public void read() {
        EntryResult r1 = shared_database.getData(id);
        global_shared_vars.t1.add(r1);
    }
}

