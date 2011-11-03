/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package bunchdemo;

import javax.swing.JPanel;
import javax.swing.UIManager;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ValidatorUtils;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.GroupValidator;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;
import org.netbeans.validation.api.ui.swing.ValidationPanel;

/**
 *
 * @author Tim Boudreau
 */
public class DemoPanel extends JPanel {

    private final SwingValidationGroup panelValidationGroup = SwingValidationGroup.create();

    public DemoPanel() {

        initComponents();

        // This demo shows a panel with four text fields, in three of which the
        // user shall type an integer, and in one of which the user shall type
        // a valid URL.

        // The point of the demo is to show how to handle the following use-case:
        // The three integer fields are logically coupled together - validation-wise -
        // because the sum of those integers must be 60. Hence, the validity
        // of each integer field depend also on the state of the two others.

        // The URL field on the other hand does not have such a validation
        // interdependency with the other fields.
        
        // What we will do is this: The three integer fields will be bunched together
        // into their own little sub-validation-group, that will handle the
        // "validation interdependency" (i.e check that the sum of the three
        // integers is 60).

        // Then this little sub-validation-group in turn will be added to the
        // "main" validation group (the one corresponding to the whole panel).
        // The URL-field will also be added to the main validation group.


        // The following is a useful validator for checking that the user
        // have typed an integer. (This also demonstrates how atomic validators
        // can be chained together).
        Validator<String> posIntValidator =
                ValidatorUtils.merge(StringValidators.REQUIRE_NON_EMPTY_STRING,
                ValidatorUtils.merge(StringValidators.NO_WHITESPACE,
                ValidatorUtils.merge(StringValidators.REQUIRE_VALID_NUMBER,
                ValidatorUtils.merge(StringValidators.REQUIRE_VALID_INTEGER,
                StringValidators.REQUIRE_NON_NEGATIVE_NUMBER))));

        // Sub-validation-group for the three integer fields:
        final SwingValidationGroup integerFieldSubGroup = SwingValidationGroup.create(new IntegerGroupSum60Validator());
        // Note how we pass an instance of a GroupValidator to the create-method
        // (and also that the panelValidationGroup above was passed no such object
        // because it has no special validity-interdependency checks it needs to do)
        // This GroupValidator instance will take care of "the extra sub-group"
        // validaton needed for the three integer fields.

        // The three integer fields are added to the sub-group, together with a
        // Validator<String> checking that the user has typed a valid integer.
        integerFieldSubGroup.add (aField, posIntValidator);
        integerFieldSubGroup.add (bField, posIntValidator);
        integerFieldSubGroup.add (cField, posIntValidator);

        panelValidationGroup.addItem(integerFieldSubGroup, false);

        // And finally add the URL-field to the main group.
        panelValidationGroup.add(urlField, ValidatorUtils.merge(StringValidators.REQUIRE_NON_EMPTY_STRING, StringValidators.URL_MUST_BE_VALID));

    }

    private class IntegerGroupSum60Validator extends GroupValidator {
        @Override
        protected void performGroupValidation(Problems problems) {
            Problem result = null;
            try {
                int val1 = Integer.parseInt(aField.getText());
                int val2 = Integer.parseInt(bField.getText());
                int val3 = Integer.parseInt(cField.getText());
                int sum = val1 + val2 + val3;
                if (sum != 60) {
                    result = new Problem (val1 + "+" + val2 + "+" + val3 +
                            " equals " + sum + ", not 60", Severity.FATAL);
                } else if (val1 == 60 || val2 == 60 || val3 == 60) {
                    result = new Problem ("Hey...that's cheating!",
                            Severity.WARNING);
                }
            } catch (NumberFormatException e) {
                // We should never end up here, the other validators should have
                // taken care of any non-integer
            }
            if (result != null) {
                problems.add(result);
            }
        }
    }


    public static void main(String[] ignored) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        DemoPanel demo = new DemoPanel();
        ValidationPanel pnl = new ValidationPanel(demo.panelValidationGroup);
        pnl.setInnerComponent(demo);
        pnl.showOkCancelDialog("Enter some stuff");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        aField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        bField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel1.setText("Sum of integers must be equal to 60:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 135;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 20);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Value A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 20, 0, 0);
        add(jLabel2, gridBagConstraints);

        aField.setText("0");
        aField.setName("A"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 20);
        add(aField, gridBagConstraints);

        jLabel3.setText("Value B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 20, 0, 0);
        add(jLabel3, gridBagConstraints);

        bField.setText("0");
        bField.setName("B"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 20);
        add(bField, gridBagConstraints);

        jLabel4.setText("Value C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 20, 0, 0);
        add(jLabel4, gridBagConstraints);

        cField.setText("0");
        cField.setName("C"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 20);
        add(cField, gridBagConstraints);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel5.setText("And a URL to have something else to validate:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 20, 0, 0);
        add(jLabel5, gridBagConstraints);

        jLabel6.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 20, 0, 0);
        add(jLabel6, gridBagConstraints);

        urlField.setName("URL"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 19, 20, 20);
        add(urlField, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aField;
    private javax.swing.JTextField bField;
    private javax.swing.JTextField cField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField urlField;
    // End of variables declaration//GEN-END:variables

}
