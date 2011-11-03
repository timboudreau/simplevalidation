/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.openide.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Tim Boudreau
 */
public class PropsWrapperMap implements Map<String,String> {
    private Properties props;
    PropsWrapperMap(Properties props) {
        this.props = props;
    }

    public Collection<String> values() {
        Collection<?> c = props.values();
        Set<String> result = new HashSet<String>();
        for (Object o : c) {
            result.add (String.class.cast(o));
        }
        return result;
    }

    @Override
    public synchronized String toString() {
        return props.toString();
    }

    public synchronized int size() {
        return props.size();
    }

    public synchronized String remove(Object key) {
        return (String) props.remove(key);
    }

    public synchronized void putAll(Map<? extends String, ? extends String> t) {
        props.putAll(t);
    }

    public synchronized String put(String key, String value) {
        return (String) props.setProperty(key, value);
    }

    public synchronized Enumeration<Object> keys() {
        return props.keys();
    }

    public Set<String> keySet() {
        return props.stringPropertyNames();
    }

    public synchronized boolean isEmpty() {
        return props.isEmpty();
    }

    @Override
    public synchronized int hashCode() {
        return props.hashCode();
    }

    public synchronized String get(Object key) {
        return props.getProperty((String) key);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public synchronized boolean equals(Object o) {
        return props.equals(o);
    }

    @SuppressWarnings("unchecked")
    public Set<Entry<String, String>> entrySet() {
        Set s = props.entrySet();
        return (Set<Entry<String,String>>) s;
    }

    public synchronized Enumeration<Object> elements() {
        return props.elements();
    }

    public boolean containsValue(Object value) {
        return props.containsValue(value);
    }

    public synchronized boolean containsKey(Object key) {
        return props.containsKey(key);
    }

    public synchronized boolean contains(Object value) {
        return props.contains(value);
    }

    @Override
    public synchronized Object clone() {
        return new PropsWrapperMap(props);
    }

    public synchronized void clear() {
        props.clear();
    }
}
