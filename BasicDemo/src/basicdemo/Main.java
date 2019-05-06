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
package basicdemo;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.netbeans.validation.api.ValidatorUtils;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.swing.ValidationPanel;
public class Main {
  public static void main(String[] args) {
    //This is our actual UI
    JPanel inner = new JPanel();
    JLabel lbl = new JLabel("Enter a URL");
    JTextField f = new JTextField();
    f.setColumns(40);

    //Setting the component name is important - it is used in
    //error messages
    f.setName("URL");

    inner.add(lbl);
    inner.add(f);

    //Create a ValidationPanel - this is a panel that will show
    //any problem with the input at the bottom with an icon
    ValidationPanel panel = new ValidationPanel();
    panel.setInnerComponent(inner);
    ValidationGroup group = panel.getValidationGroup();

    //This is all we do to validate the URL:
    group.add(f, ValidatorUtils.merge(StringValidators.REQUIRE_NON_EMPTY_STRING,
            ValidatorUtils.merge(StringValidators.NO_WHITESPACE,
            StringValidators.URL_MUST_BE_VALID)));

    //Convenience method to show a simple dialog
    if (panel.showOkCancelDialog("URL")) {
      System.out.println("User clicked OK.  URL is " + f.getText());
      System.exit(0);
    } else {
      System.err.println("User clicked cancel.");
      System.exit(1);
    }
  }

  private Main() {}
}
