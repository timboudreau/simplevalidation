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
package org.netbeans.validation.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 * A collection of problems, to which a Validator can add additional problems.
 *
 * @author Tim Boudreau
 */
public final class Problems implements Iterable<Problem> {
    private final List<Problem> problems = new ArrayList<Problem>();
    private boolean hasFatal;
    /**
     * Convenience method to add a problem with the specified message and
     * Severity.FATAL
     * @param problem
     * @deprecated use append() instead
     */
    @Deprecated
    public final void add (String problem) {
        add (problem, Severity.FATAL);
    }

    /**
     * Add a problem with the specified severity
     * @param problem the message
     * @param severity the severity
     * @deprecated use append() instead
     */
    @Deprecated
    public final void add (String problem, Severity severity) {
        problems.add (new Problem (problem, severity));
        hasFatal |= severity == Severity.FATAL;
    }

    /**
     * Add a problem
     * @param problem The problem (may be null)
     * @deprecated use append() instead
     */
    @Deprecated
    public final void add (Problem problem) {
        if( problem == null ) { return; }
        problems.add (problem);
        hasFatal |= (problem.severity() == Severity.FATAL);
    }
    /**
     * Convenience method to add a problem with the specified message and
     * Severity.FATAL
     * @param problem
     * @return this
     * @since 1.6
     */
    @SuppressWarnings( "deprecation" )
    public final Problems append(String problem) {
        add(problem);
        return this;
    }
    /**
     * Add a problem with the specified severity
     * @param problem the message
     * @param severity the severity
     * @return this
     * @since 1.6
     */
    @SuppressWarnings( "deprecation" )
    public final Problems append(String problem, Severity severity) {
        add(problem, severity);
        return this;
    }
    /**
     * Add a problem
     * @param problem The problem (may be null)
     * @return this
     * @since 1.6
     */
    @SuppressWarnings( "deprecation" )
    public final Problems append(Problem problem) {
        add(problem);
        return this;
    }

    /**
     * Merge a set of problems into this one
     * @param problems Problems with validation
     * @return this
     * @since 1.6
     */
    @SuppressWarnings( "deprecation" )
    public final Problems addAll(Problems problems) {
        putAll(problems);
        return this;
    }

    /**
     * Dump all problems in another instance of Problems into this one.
     * @param problems The other problems.
     * @deprecated Use addAll instead
     */
    @Deprecated
    public final void putAll (Problems problems) {
        if (problems == this) throw new IllegalArgumentException (
                "putAll to self"); //NOI18N
        this.problems.addAll (problems.problems);
        hasFatal |= (problems.hasFatal());
    }


    /**
     * Determine if this set of problems includes any that are fatal.
     * @return true if a fatal problem has been encountered
     */
    public final boolean hasFatal() {
        return hasFatal;
    }

    /**
     * Create a new Problems with the initial (fatal) problem.
     * @param message A localized message
     * @return A Problems
     */
    public static Problems create (String message) {
        assert message != null;
        Problems result = new Problems();
        result.add(message);
        return result;
    }

    /**
     * Create a Problems which contains the localized message belonging to
     * the passed class and key, according to the lookup semantics of
     * Localizer.
     * @param localizerClass A class which (or whose package) has an associated
     * resource bundle
     * @param bundleKey The key in that bundle
     * @return a Problems with a single fatal problem in it
     */
    public static Problems create(Class<?> localizerClass, String bundleKey) {
        assert bundleKey != null;
        assert localizerClass != null;
        return create(LocalizationSupport.getMessage(localizerClass, bundleKey));
    }

    /**
     * Get the {@code Problem} with the highest severity.
     *
     * If there is more than one problem with equal severity, <i>the
     * one first added</i> will be considered more severe.
     *
     * @return The most severe {@code Problem} in this set
     * @return null if there was no {@code Problem}
     */
    public final Problem getLeadProblem() {
        // Note that Collections.sort() is *stable*, a fact we use to guarantee
        // this: of problems with equal severity, the problems added first will
        // remain before the later ones, and will thusly be considered "more leading".
        // (This may be helpful if the problems added first
        // have *occured* more *recently* and thusly can be regarded as leading
        //  -- more natural to indicate to a user).
        Collections.sort (problems);
        return problems.isEmpty() ? null : problems.get(0);
    }

    /**
     * Get the entire set of problems, sorted by severity first, order of
     * addition second.
     * @return A list of Problems
     */
    public final List<? extends Problem> allProblems() {
        List<Problem> result = new ArrayList<Problem>(problems);
        Collections.sort(result);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Problem p : problems) {
            if (sb.length() == 0) {
                sb.append (p);
            } else {
                sb = new StringBuilder (
                    LocalizationSupport.getMessage(Problems.class, "CONCAT_PROBLEMS", sb, p));
            }
        }
        return sb.toString();
    }

    public void throwIfFatalPresent(String msg) {
        if (hasFatal()) {
            throw new InvalidInputException(msg, this);
        }
    }


    public void throwIfFatalPresent() {
        if (hasFatal()) {
            throw new InvalidInputException(this);
        }
    }

    @Override
    public Iterator<Problem> iterator() {
        return Collections.unmodifiableCollection(problems).iterator();
    }
}
