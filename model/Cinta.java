package model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Cinta {
    private final Queue<String> bateriaQueue = new LinkedList<>();
    private final int capaitatMaxima;
    
    //Bloqueo explíxito y variables de condición
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cintaNoPlena =  lock.newCondition();
    private final Condition cintaNoBuida = lock.newCondition();
}
