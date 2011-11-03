/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openide.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Mock of the real org.openide.util.Lookup which delegates to JDK 6
 * ServiceLoader.
 * 
 * @author Tim Boudreau
 */
public abstract class Lookup {
    public abstract <T> T lookup (Class<T> type);
    public abstract <T> Collection<? extends T> lookupAll (Class<T> type);
    public static Lookup getDefault() {
        return new FakeDefaultLookup();
    }

    private static final class FakeDefaultLookup extends Lookup {

        @Override
        public <T> T lookup(Class<T> type) {
            Iterator<T> ldr = ServiceLoader.load(type).iterator();
            return ldr.hasNext() ? ldr.next() : null;
        }

        @Override
        public <T> Collection<? extends T> lookupAll(Class<T> type) {
            Set<T> result = new HashSet<T>();
            for (T t : ServiceLoader.load(type)) {
                result.add(t);
            }
            return result;
        }

    }
}
