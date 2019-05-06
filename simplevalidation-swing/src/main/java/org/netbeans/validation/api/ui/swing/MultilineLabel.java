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

import java.awt.Dimension;
import java.lang.ref.WeakReference;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.ui.ValidationUI;
import org.openide.util.NbBundle;

/**
 * Label which uses a fixed height based on its icon and text.  Uses
 * multiline label UI to render multi-line text
 *
 * @author Tim Boudreau
 */
final class MultilineLabel extends JLabel {
    private final boolean isPopup;
    MultilineLabel() {
        this(false);
    }

    MultilineLabel(boolean isPopup) {
        this.isPopup = isPopup;
    }

    boolean isPopup() {
        return isPopup;
    }

    @Override
    public void updateUI() {
        setUI (new MultilineLabelUI());
    }

    private int tallest = Integer.MIN_VALUE;
    @Override
    public Dimension getPreferredSize() {
        Dimension result = super.getPreferredSize();
        if (!isPopup && !isPreferredSizeSet()) {
            if (tallest < result.height) {
                tallest = result.height;
            }
            result.height = tallest;
        }
        return result;
    }

    private int knownWidth = -1;
    @Override
    @SuppressWarnings("deprecation")
    public void reshape(int x, int y, int w, int h) {
        //May be deprecated, but AWT will call it internally - this is the
        //place to intercept size changes other than a listener
        if (!isPopup) {
            if (w != knownWidth) {
                knownWidth = w;
                tallest = Integer.MIN_VALUE;
            }
        }
        super.reshape(x, y, w, h);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!isPopup) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (!isPopup) {
            ToolTipManager.sharedInstance().unregisterComponent(this);
        }
    }

    @Override
    public String getToolTipText() {
        String s = getText();
        if (s != null && !"".equals(s.trim())) {
            return htmlize(s);
        }
        return super.getToolTipText();
    }

    private static String htmlize(String s) {
        StringBuilder res = new StringBuilder("<html>");
        String[] words = s.split(" ");
        boolean newline = false;
        int ct = 0;
        for (String word : words) {
            ct += word.length();
            newline = ct > 80;
            if(newline) {
                res.append ("<br>");
                ct = 0;
            }
            res.append(word);
            res.append(' ');
        }
        return res.toString();
    }

    ValidationUI createUI() {
        return new LblUI(this);
    }

    private static final class LblUI implements ValidationUI {
        //Don't allow the group to hold a reference to the label permanetly
        private final WeakReference<JLabel> label;
        LblUI(JLabel lbl) {
            assert lbl != null;
            this.label = new WeakReference<JLabel>(lbl);
        }

        @Override
        public void showProblem(Problem problem) {
            JLabel lbl = label.get();
            if (lbl != null) {
                if (problem == null) {
                    lbl.setText("   ");
                    lbl.setIcon(null);
                    lbl.getAccessibleContext().setAccessibleName(
                            NbBundle.getMessage(MultilineLabel.class,
                            "WARNING_LABEL"));
                    lbl.getAccessibleContext().setAccessibleDescription("");
                } else {
                    lbl.setText(problem.getMessage());
                    lbl.setIcon(problem.severity().icon());
                    lbl.setForeground(problem.severity().color());
                    lbl.getAccessibleContext().setAccessibleName(
                            problem.severity().toString());
                    lbl.getAccessibleContext().setAccessibleDescription(
                            problem.severity().describeError(problem.getMessage()));
                }
            }
        }

        public void clearProblem() {
            showProblem(null);
        }
    }
}
