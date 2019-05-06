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
package org.netbeans.validation.api.ui;

import javax.swing.event.ListSelectionEvent;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;

/**
 * 
 * @author Hugo Heden
 */
class JListValidationListenerImpl extends ValidationListener<JList>
        implements EventListener, ListSelectionListener, FocusListener {
    private Validator<ListSelectionModel> validator;
    private boolean hasFatalProblem = false;

    public JListValidationListenerImpl(JList component,
            ValidationStrategy strategy,
            ValidationUI validationUI,
            Validator<ListSelectionModel> validator
            ) {
        super(JList.class, validationUI, component);
        this.validator = validator;
        if (strategy == null) {
            throw new NullPointerException("strategy null");
        }
        getTarget().addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                performValidation();
            }
        });

        switch (strategy) {
            case DEFAULT:
            case ON_CHANGE_OR_ACTION:
                component.addListSelectionListener(this);
                break;
            case ON_FOCUS_LOSS:
                component.addFocusListener(this);
                break;
            case INPUT_VERIFIER:
                component.setInputVerifier( new InputVerifier() {
                    @Override
                    public boolean verify(JComponent input) {
                        performValidation();
                        return !hasFatalProblem;
                    }
                });
                break;
            default:
                throw new AssertionError();
        }
        performValidation(); // Make sure any initial errors are discovered immediately.
    }

    @Override
    protected void performValidation(Problems ps){
        JList component = getTarget();
        if (!getTarget().isEnabled()) {
            return;
        }
        validator.validate(ps, SwingValidationGroup.nameForComponent(component), component.getSelectionModel());
        hasFatalProblem = ps.hasFatal();
    }

    @Override
    public void focusLost(FocusEvent e) {
        performValidation();
    }

    public void valueChanged(ListSelectionEvent lse) {
        performValidation();
    }

    @Override
    public void focusGained(FocusEvent e) {
    }


}
