/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ubersimples;

import java.util.ArrayList;

/**
 *
 * @author Crisanto
 */
public class SynchronizedArrayList {

    private ArrayList list;

    public SynchronizedArrayList() {
        this.list = new ArrayList();
    }

    public synchronized void add(Object o) {
        list.add(o);
    }

    public synchronized ArrayList get() { // devia tirar este e criar um que só retornava uma posição em vez de retornar tudo ...
        return list;
    }

    public synchronized void clear() {
        list.clear();
    }

    public synchronized int getSize() {
        return list.size();
    }

    public synchronized void removeFromPosition(int position) {
        list.remove(position);
    }
}
