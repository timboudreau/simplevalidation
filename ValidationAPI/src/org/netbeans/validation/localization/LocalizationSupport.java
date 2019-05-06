/*
 * Copyright 2010-2019 Tim Boudreau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.netbeans.validation.localization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper to allow reflective access to NetBeans' NbBundle API.
 *
 * @author Tim Boudreau
 */
public class LocalizationSupport {

    public static String getMessage(Class<?> type, String key, Object... args) {
        Iterator<LocalizationSupport> it = ServiceLoader.load(LocalizationSupport.class).iterator();
        LocalizationSupport supp = it.hasNext() ? it.next() : getDefault();
        return supp.getMessageSPI(type, key, args);
    }

    protected String getMessageSPI(Class<?> type, String key, Object... args) {
        String result = getMessageViaNbBundle(type, key, args);
        return result == null ? key : result;
    }

    private static LocalizationSupport defaultInstance;

    static LocalizationSupport getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new LocalizationSupport();
        }
        return defaultInstance;
    }

    private static String getMessageViaNbBundle(Class<?> type, String key, Object... args) {
        if (args.length == 0) {
            Method m = getMessageNoArgs();
            if (m == null) {
                return null;
            }
            try {
                return (String) m.invoke(null, type, key);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(LocalizationSupport.class.getName()).log(Level.INFO, key, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(LocalizationSupport.class.getName()).log(Level.INFO, key, ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
        } else {
            Method m = getMessageWithArgs();
            if (m == null) {
                return null;
            }
            try {
                return (String) m.invoke(null, type, key, args);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(LocalizationSupport.class.getName()).log(Level.INFO, key, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(LocalizationSupport.class.getName()).log(Level.INFO, key, ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause());
            }
        }
        return null;
    }

    private static Method getMessageNoArgs() {
        if (noArgsMethod != null) {
            return noArgsMethod;
        }
        Class<?> nbb = nbBundleType();
        if (nbb == null) {
            return null;
        }
        try {
            noArgsMethod = nbb.getMethod("getMessage", Class.class, String.class);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (SecurityException ex) {
            return null;
        }
        return noArgsMethod;
    }

    private static Method getMessageWithArgs() {
        if (withArgsMethod != null) {
            return withArgsMethod;
        }
        Class<?> nbb = nbBundleType();
        if (nbb == null) {
            return null;
        }
        try {
            withArgsMethod = nbb.getMethod("getMessage", Class.class, String.class, Object[].class);
        } catch (NoSuchMethodException ex) {
            // do nothing
        } catch (SecurityException ex) {
            // do nothing
        }
        return withArgsMethod;
    }

    private static Method withArgsMethod;
    private static Method noArgsMethod;
    private static boolean checked = false;
    private static Class<?> nbBundle;

    static Class<?> nbBundleType() {
        if (nbBundle == null && !checked) {
            checked = true;
            try {
                nbBundle = Class.forName("org.openide.util.NbBundle");
            } catch (ClassNotFoundException ex) {
                // do nothing
            }
        }
        return nbBundle;
    }
}
