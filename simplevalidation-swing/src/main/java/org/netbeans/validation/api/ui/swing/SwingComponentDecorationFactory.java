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
package org.netbeans.validation.api.ui.swing;

import org.netbeans.validation.api.ui.*;
import org.openide.util.Lookup;
import javax.swing.JComponent;

/**
 * Factory class for creating {@link ValidationUI} instances that can decorate
 * a Swing GUI-component when it has a Problem.
 *
 * <p> By default, one instance of a class implementing this interface
 * is used to create a
 * ValidationUI for all components handled by the simplevalidation
 * framework. This instance can be replaced with a custom one, as
 * described below.  
 * <p>
 * For custom decoration, simply pass a different
 * decorator factory in ValidationGroup.add() (and if necessary, proxy the
 * default decorator factory for all but some specific kind of component).
 * 
 * A rudimentary example of writing a component decorator:  The code and
 * description below show how to replace the default
 * SwingComponentDecorationFactory with one that will create {@code ValidationUI}
 * instances that draws a thick colored border around the component
 * when there is an error.
 * <p>
 *<blockquote><pre>{@code
 * package com.foo.myapp;
 * public class MySwingComponentDecorationFactory extends SwingComponentDecorationFactory {
 *      public ValidationUI decorationFor(final JComponent c) {
 *         return new ValidationUI() {
 *             private javax.swing.border.Border origBorder = c.getBorder();
 *             public void showProblem(Problem problem) {
 *                if( problem == null ) {
 *                    c.setBorder(origBorder);
 *                } else {
 *                    c.setBorder(javax.swing.BorderFactory.createLineBorder(problem.severity().color(), 3));
 *                }
 *             }
 *        };
 *     }
 *  };
 * }</pre></blockquote>
 * 
 * Our <code>MySwingComponentDecorationFactory</code> is then registered so that
 * it can be found using JDK 6's <code>ServiceLoader</code> (or NetBeans'
 * <code>Lookup</code>):  Create a file named <code>org.netbeans.validation.api.ui.swing.SwingComponentDecorationFactory</code>
 * in the folder <code>META-INF/services</code> in your source root (so that
 * it will be included in the JAR file).  Add one line of text to this file -
 * the fully qualified name of your class, e.g.
 * <pre>
 * com.foo.myapp.MySwingComponentDecorationFactory
 * </pre>
 *
 * @author Tim Boudreau
 * @author Hugo Heden
 */
public abstract class SwingComponentDecorationFactory {
    private static SwingComponentDecorationFactory componentDecorator =
            new SimpleDefaultDecorator();

     private static final SwingComponentDecorationFactory noOpDecorationFactory = new SwingComponentDecorationFactory() {
        @Override
        public ValidationUI decorationFor(JComponent c) {
            return ValidationUI.NO_OP;
        }
    };

    /**
     *
     * Special decorator that does not decorate at all -- even if
     * there is a problem -- a "null" decorator. This is useful if no
     * component decorations are desired. For example application
     * wide:
     *
     * <blockquote><pre>{@code
     * SwingComponentDecorationFactory.set(SwingComponentDecorationFactory.getNoOpDecorationFactory());
     * }</pre></blockquote>
     * Or just for one specific group of components, here a SwingValidationGroup:
     * <blockquote><pre>{@code
     * SwingValidationGroup group = SwingValidationGroup.create(SwingComponentDecorationFactory.getNoOpDecorationFactory());
     * }</pre></blockquote>
     *
     *
     */

    public static final SwingComponentDecorationFactory getNoOpDecorationFactory() {
        return noOpDecorationFactory;
    }


    /**
     * Factory method that creates a {@code ValidationUI} visually attached to
     * the Swing GUI-component when there is a {@code Problem}.  When a
     * {@code Problem} occurs in a component, this {@code ValidationUI} needs to be
     * updated using {@link ValidationUI#showProblem} (this is
     * typically done from within a {@code ValidationListener}) and
     * will then apply some visual mark to the component. When the
     * problem disappears, {@code ValidationListener} will pass {@code
     * null} to {@code ValidationUI#showProblem},
     * which makes sure that the visual cue is removed, so that the
     * components is restored to its original visual state.
     * 
     * @param c The component
     * @return A ValidationUI, visually attached to the component. 
     */
    public abstract ValidationUI decorationFor(JComponent c);
    
    /**
     * 
     * Get the current application wide component decorator
     */
    public static final SwingComponentDecorationFactory getDefault() {
        SwingComponentDecorationFactory result = Lookup.getDefault().lookup(SwingComponentDecorationFactory.class);
        if (result == null) {
            result = componentDecorator;
        }
        return result;
    }

}

