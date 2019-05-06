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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 * enum indicating {@code Severity}, used for classifying {@link Problem} instances.
 *
 * @author Tim Boudreau
 */
public enum Severity {

    /**
     * An information message for the user, which should not block
     * them from proceeding but may provide advice
     */
    INFO,
    /**
     * A warning to the user that they should change a value, but 
     * which does not block them from proceeding
     */
    WARNING,
    /**
     * A fatal problem with user input which must be corrected
     */
    FATAL;
    private BufferedImage image;

    /**
     * Get a warning icon as an image
     * @return An image
     */
    public synchronized BufferedImage image() {
        if (image == null) {
            String name;
            switch (this) {
                case INFO:
                    name = "info.png"; //NOI18N
                    break;
                case WARNING:
                    name = "warning.png"; //NOI18N
                    break;
                case FATAL:
                    name = "error.png"; //NOI18N
                    break;
                default:
                    throw new AssertionError();
            }
            try {
                image = ImageIO.read(Severity.class.getResourceAsStream(name));
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return image;
    }

    /**
     * Get an icon version of the warning image
     * @return An icon
     */
    public Icon icon() {
        return new ImageIcon(image());
    }

    /**
     * Get a suitable color for displaying problem text
     * @return A color
     */
    public Color color() {
        switch (this) {
            case FATAL: {
                Color c = UIManager.getColor("nb.errorForeground"); //NOI18N
                if (c == null) {
                    c = Color.RED.darker();
                }
                return c;
            }
            case WARNING:
                return Color.BLUE.darker();
            case INFO:
                return UIManager.getColor("textText");
            default:
                throw new AssertionError();
        }
    }

    BufferedImage badge;
    public BufferedImage badge() {
        if (badge == null) {
            String name;
            switch (this) {
                case INFO:
                    name = "info-badge.png"; //NOI18N
                    break;
                case WARNING:
                    name = "warning-badge.png"; //NOI18N
                    break;
                case FATAL:
                    name = "error-badge.png"; //NOI18N
                    break;
                default:
                    throw new AssertionError();
            }
            try {
                badge = ImageIO.read(Severity.class.getResourceAsStream(name));
            } catch (IOException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        return badge;
    }

    /**
     * Annotate an error description with the severity.  Used for providing
     * accessible descriptions for error components.
     *
     * @param toDescribe
     * @return
     */
    public String describeError (String toDescribe) {
        return LocalizationSupport.getMessage(Severity.class, name() + ".annotation",
                toDescribe);
    }

    /**
     * Returns a localized name for this enum constant
     * @return A localized name
     */
    @Override
    public String toString() {
        return LocalizationSupport.getMessage(Severity.class, name());
    }
}
