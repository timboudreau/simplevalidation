/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2018 Tim Boudreau. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
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
