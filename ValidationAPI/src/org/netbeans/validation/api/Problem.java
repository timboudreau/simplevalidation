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

/**
 * Represents a problem produced by a validator.
 *
 * @author Tim Boudreau
 */
public final class Problem implements Comparable<Problem> {

    private final String message;
    private final Severity severity;
    /**
     * Create a new problem with the given message and severity
     * @param message A localized, human readable message
     * @param severity The severity
     */
    public Problem(String message, Severity severity) {
        if (message == null) {
            throw new NullPointerException ("Null message"); //NOI18N
        }
        if (severity == null) {
            throw new NullPointerException ("Null severity"); //NOI18N
        } 
        this.message = message;
        this.severity = severity;
    }

    /**
     * Get the {@code Severity} of this Problem.  The severity indicates whether
     * the user should be blocked from further action until the problem
     * is corrected, or if continuing with a warning is reasonable.
     * It also determines the warning icon which can be displayed to the
     * user.
     * @return The severity of the Problem
     */
    public Severity severity() {
        return severity;
    }

    /**
     * Determine which Problem is more severe. Uses compareTo(). 
     * @param p1 p2 the two Problems to compare. Any of them (or both) may be null.
     * @return p1 if p1 is worse
     * @return p2 if p2 is worse
     * @return p1 (the first argument) if p1 and p2 are equally severe.
     * @return null if both problems to compare are null
     */
    public static Problem worst(Problem p1, Problem p2){
        if( p1==null ) { return p2; }
        if( p2==null ) { return p1; }
        return p2.compareTo(p1)<0 ? p2 : p1;
    }

    /**
     * Convenience method to determine if this problem is of Severity.FATAL
     * severity
     * @return true if severity() == Severity.FATAL
     */
    public boolean isFatal() {
        return severity == Severity.FATAL;
    }

    /**
     * Get the localized, human-readable description of the problem
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Compare, such that most severe Problems will appear first, least last
     * @param o
     * @return the difference in severity as an integer
     */
    @Override
    public int compareTo(Problem o) {
        int ix = severity.ordinal();
        int oid = o == null ? -1 : o.severity.ordinal();
        // return ix - oid;
        return oid - ix;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || o.getClass() != Problem.class ) { return false; }
        Problem p = (Problem) o;
        return p.severity == severity && p.getMessage().equals(getMessage());
    }

    @Override
    public String toString() {
        return getMessage() + " (" + severity() + ")";
    }

    @Override
    public int hashCode() {
        return message.hashCode() * (severity.hashCode() + 1);
    }
}
