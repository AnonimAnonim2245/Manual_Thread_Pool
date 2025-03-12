package org.apd.writerPriority;

import org.apd.readerPriority.ReaderWriterSharedVars;
import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;

public class WriterWP {

    private final int id;
    private String element;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private double start_time;
    private double completion_time;
    private SharedDatabase shared_database;


    public WriterWP(int id, String element, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
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
        try {
            shared_vars.enter.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (shared_vars.nr + shared_vars.nw > 0) {
            shared_vars.waiting_writers++;
        } else {
            shared_vars.sem_writer.release();
            shared_vars.nw++;
        }

        shared_vars.enter.release();
        shared_vars.sem_writer.acquire();
        write();

        shared_vars.enter.acquire();
        shared_vars.nw--;

        if (shared_vars.waiting_writers > 0) {
            shared_vars.sem_writer.release();
            shared_vars.nw++;
            shared_vars.waiting_writers--;
        } else if (shared_vars.waiting_readers > 0) {
            while(shared_vars.waiting_readers > 0) {
                shared_vars.sem_reader.release();
                shared_vars.nr++;
                shared_vars.waiting_readers--;
            }

        }

        shared_vars.enter.release();
    }

    public void write() {
        EntryResult r1 =  shared_database.addData(id, element);
        global_shared_vars.t1.add(r1);
    }
}