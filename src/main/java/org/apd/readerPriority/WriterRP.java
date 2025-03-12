package org.apd.readerPriority;

import org.apd.storage.EntryResult;
import org.apd.storage.SharedDatabase;

public class WriterRP {
    private final int id;
    private String element;
    private final ReaderWriterSharedVars shared_vars;
    private final ReaderWriterSharedVars global_shared_vars;

    private SharedDatabase shared_database;


    public WriterRP(int id, String element, ReaderWriterSharedVars shared_vars, ReaderWriterSharedVars global_shared_vars, SharedDatabase share_database) {
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

        // TODO: Add synchronization
        shared_vars.readWrite.acquire();
        write();
        shared_vars.readWrite.release();

    }


    public void write() {
        EntryResult r1 =  shared_database.addData(id, element);
        global_shared_vars.t1.add(r1);
    }
}
