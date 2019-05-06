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
 * Convenience base class for validators.
 *
 * @author Tim Boudreau
 */
public abstract class AbstractValidator<T> implements Validator<T> {
    private final Class<T> type;
    protected AbstractValidator(Class<T> type) {
        this.type = type;
    }

    /**
     * Model type for this validator - the type of argument it validates.
     * @return The model type
     */
    @Override
    public final Class<T> modelType() {
        return type;
    }
}
