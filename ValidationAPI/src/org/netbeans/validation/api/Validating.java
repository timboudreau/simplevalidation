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
