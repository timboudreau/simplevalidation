package org.netbeans.validation.api;

import java.util.Iterator;

/**
 * A separate exception class for invalid user (or stored values).  Specifically
 * used with the interface <a href="../validators/Validating.html">Validating</a>.
 * <p/>
 * Any InvalidInputException has an actual localized message which is safe
 * to show to a user - that is the distinguishing feature.
 *
 * @author Tim Boudreau
 */
@SuppressWarnings("serial")
public class InvalidInputException extends IllegalArgumentException implements Iterable<Problem> {

    private final Problems problems;

    public InvalidInputException(Problems problems) {
        this.problems = problems;
    }

    public InvalidInputException(String msg, Problems problems) {
        super(msg);
        this.problems = problems;
    }

    public InvalidInputException(String message, Validating v) {
        this(message, v.getProblems());
    }

    public InvalidInputException(Validating v) {
        this("Invalid " + v.getClass().getSimpleName(), v.getProblems());
    }

    public static void throwIfNecessary(Validating v) throws InvalidInputException {
        throwIfNecessary(null, v);
    }

    public static void throwIfNecessary(String msg, Validating v) throws InvalidInputException {
        if (v != null) {
            if (!v.isValid()) {
                Problems p = v.getProblems();
                if (p == null) {
                    throw new IllegalStateException(v + " (" + v.getClass().getSimpleName() + ") reports itself invalid but returns null from getProblems(): " + v);
                }
                if (p.hasFatal()) {
                    if (msg == null) {
                        throw new InvalidInputException(v);
                    } else {
                        throw new InvalidInputException(msg, v);
                    }
                }
            }
        }
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg != null) {
            msg += ": " + problems.toString();
        } else {
            msg = problems.toString();
        }
        return msg;
    }

    /**
     * Get an object which lists all of the problems encountered with the
     * input, with severity and localized message.
     * @return
     */
    public Problems getProblems() {
        return problems;
    }

    @Override
    public Iterator<Problem> iterator() {
        return problems.iterator();
    }
}
