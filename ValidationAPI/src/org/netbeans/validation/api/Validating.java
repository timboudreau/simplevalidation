package org.netbeans.validation.api;

import org.netbeans.validation.api.Problems;

/**
 * Interface for things that are able to validate themselves, such as
 * email addresses.
 * <p/>
 * Something which is Validating is a wrapper for some data which
 * can determine, to some degree of accuracy, whether that data is probably
 * usable or definitely not usable.
 * <p/>
 * Basically, this allows us to do what <code>java.net.URL</code> cannot:
 * Construct an object which may or may not be valid, without necessarily
 * imposing immediate data validation on it - validation is possible, but
 * it is done by the thing which actually needs the data to be valid (and
 * it can also chose to silently ignore or fail on invalid elements depending
 * on the use-case).
 *
 * @author Tim Boudreau
 */
public interface Validating {
    public Problems getProblems();
    public boolean isValid();
}
