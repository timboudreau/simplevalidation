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
package org.netbeans.validation.spi.accessibility;

import java.util.Collection;
import org.netbeans.validation.api.Problem;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Tim Boudreau
 */
public abstract class GlobalValidationCallback {
    private static final Object LOCK = new Object();
    private static GlobalValidationCallback INSTANCE;
    static GlobalValidationCallback getDefault() {
        synchronized (LOCK) {
            if (INSTANCE == null) {
                INSTANCE = new ProxyGlobalCallback();
            }
        }
        return INSTANCE;
    }

    public void onValidationTrigger (Object source, Object triggeringEvent) {

    }

    public void onValidationFinished (Object source, Object triggeringEvent) {

    }

    public abstract void onProblem (Object component, Problem problem);
    public abstract void onProblemCleared (Object component, Problem problem);

    private static final class ProxyGlobalCallback extends GlobalValidationCallback {
        private ProxyGlobalCallback() {

        }

        Collection<? extends GlobalValidationCallback> registered() {
            return Lookup.getDefault().lookupAll(GlobalValidationCallback.class);
        }

        @Override
        public void onProblem(Object component, Problem problem) {
            for (GlobalValidationCallback cb : registered()) {
                try {
                    cb.onProblem(component, problem);
                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                }
            }
        }

        @Override
        public void onProblemCleared(Object component, Problem problem) {
            for (GlobalValidationCallback cb : registered()) {
                try {
                    cb.onProblemCleared(component, problem);
                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                }
            }
        }
    }
}
