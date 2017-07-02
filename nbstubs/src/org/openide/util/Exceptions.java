package org.openide.util;

/**
 *
 * @author Tim Boudreau
 */
public class Exceptions {

    public static <E extends Throwable> E attachMessage(E e, String msg) {
        return e;
    }

    public static void printStackTrace(Throwable t) {
        t.printStackTrace();
    }
}
