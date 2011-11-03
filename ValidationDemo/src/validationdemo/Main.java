package validationdemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ValidatorUtils;
import org.netbeans.validation.api.ui.swing.ValidationPanel;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;
import org.netbeans.validation.api.ui.ValidationUI;
import org.netbeans.validation.api.ui.swing.AbstractValidationListener;
import org.netbeans.validation.api.ui.swing.SwingComponentDecorationFactory;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;

public class Main {

  public static void main(String[] args) throws Exception {
    //Set the system look and feel
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    final JFrame jf = new JFrame("Validators Demo");
    jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    //Here we create our Validation Panel.  It has a built-in
    //ValidationGroup we can use - we will just call
    //pnl.getValidationGroup() and add validators to it tied to
    //components
    final ValidationPanel pnl = new ValidationPanel();
    jf.setContentPane(pnl);

    //A panel to hold most of our components that we will be
    //validating
    JPanel inner = new JPanel();
    inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
    pnl.setInnerComponent(inner);
    JLabel lbl;
    JTextField field;

    //Okay, here's our first thing to validate
    lbl = new JLabel("Not a java keyword:");
    inner.add(lbl);
    field = new JTextField("listener");
    field.setName("Non Identifier");
    inner.add(field);

    //So, we'll get a validator, which does trim strings (that's the boolean
    // argument set to true), which will not accept empty strings or java keywords
    Validator<String> d = StringValidators.trimString(ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.REQUIRE_JAVA_IDENTIFIER));

    //Now we add it to the validation group
    pnl.getValidationGroup().add(field, d);

    //This one is similar to the example above, but it will split the string
    //into component parts divided by '.' characters first
    lbl = new JLabel("Legal java package name:");
    inner.add(lbl);
    field = new JTextField("com.foo.bar.baz");
    field.setName("package name");
    inner.add(field);
    d = StringValidators.trimString(ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            ValidatorUtils.merge(StringValidators.JAVA_PACKAGE_NAME,
            StringValidators.MAY_NOT_END_WITH_PERIOD)));
    pnl.getValidationGroup().add(field, d);

    lbl = new JLabel("IP Address or Host Name");
    inner.add(lbl);
    field = new JTextField("127.0.0.1");
    field.setName("Address");
    inner.add(field);
    d = ValidatorUtils.merge(StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.HOST_NAME_OR_IP_ADDRESS);
    pnl.getValidationGroup().add(field, d);

    lbl = new JLabel("Must be a non-negative integer");
    inner.add(lbl);
    field = new JTextField("42");
    field.setName("the number");
    inner.add(field);

    //Note that we're very picky here - require non-negative number and
    //require valid number don't care that we want an Integer - we also
    //need to use require valid integer
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            ValidatorUtils.merge(StringValidators.REQUIRE_VALID_NUMBER,
            ValidatorUtils.merge(StringValidators.REQUIRE_VALID_INTEGER,
            StringValidators.REQUIRE_NON_NEGATIVE_NUMBER))));

    lbl = new JLabel("Email address");
    inner.add(lbl);
    field = new JTextField("Foo Bar <foo@bar.com>");
    field.setName("Email address");
    inner.add(field);

    //Note that we're very picky here - require non-negative number and
    //require valid number don't care that we want an Integer - we also
    //need to use require valid integer
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.EMAIL_ADDRESS));

    lbl = new JLabel("Hexadecimal number ");
    inner.add(lbl);
    field = new JTextField("CAFEBABE");
    field.setName("hex number");
    inner.add(field);

    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.VALID_HEXADECIMAL_NUMBER));

    lbl = new JLabel("No spaces: ");
    field = new JTextField("ThisTextHasNoSpaces");
    field.setName("No spaces");
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.NO_WHITESPACE));
    inner.add(lbl);
    inner.add(field);

    lbl = new JLabel("Enter a URL");
    field = new JTextField("http://netbeans.org/");
    field.setName("url");
    pnl.getValidationGroup().add(field, StringValidators.URL_MUST_BE_VALID);
    inner.add(lbl);
    inner.add(field);

    lbl = new JLabel("file that exists");
    //Find a random file so we can populate the field with a valid initial
    //value, if possible
    File userdir = new File(System.getProperty("user.dir"));
    File aFile = null;
    for (File f : userdir.listFiles()) {
      if (f.isFile()) {
        aFile = f;
        break;
      }
    }
    field = new JTextField(aFile == null ? "" : aFile.getAbsolutePath());

    //We could call field.setName("File").
    //Note there is an alternative to field.setName() if we are using that
    //for some other purpose:
    SwingValidationGroup.setComponentName(field, "File");
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.FILE_MUST_BE_FILE));
    inner.add(lbl);
    inner.add(field);

    lbl = new JLabel("Folder that exists");
    field = new JTextField(System.getProperty("user.dir"));
    field.setName("folder");
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.FILE_MUST_BE_DIRECTORY));
    inner.add(lbl);
    inner.add(field);

    lbl = new JLabel("Valid file name");
    field = new JTextField("Validators.java");
    field.setName("File Name");

    //Here we're requiring a valid file name
    //(no file or path separator chars)
    pnl.getValidationGroup().add(field, ValidatorUtils.merge(
            StringValidators.REQUIRE_NON_EMPTY_STRING,
            StringValidators.REQUIRE_VALID_FILENAME));
    inner.add(lbl);
    inner.add(field);

    //Here we will do custom validation of a JColorChooser

    final JColorChooser chooser = new JColorChooser();
    //Use an intermediary panel to keep the layout from jumping when
    //the problem is shown/hidden
    final JPanel ccPanel = new JPanel();
    ccPanel.add(chooser);
    //Add it to the main panel because GridLayout will make it too small
    //ValidationPanel panel uses BorderLayout (and will throw an exception
    //if you try to change it)
    pnl.add(ccPanel, BorderLayout.EAST);

    //Set a default value that won't show an error
    chooser.setColor(new Color(191, 86, 86));

    //ColorValidator is defined below
    final ValidationUI ccDecorator =
            SwingComponentDecorationFactory.getDefault().decorationFor(chooser);
    final ColorValidator val = new ColorValidator();

    //Note if we could also implement Validator directly on this class;
    //however it's more reusable if we don't
    class ColorListener extends AbstractValidationListener<JColorChooser, Color> implements ChangeListener {

      ColorListener() {
        super(JColorChooser.class, chooser, ccDecorator, val);
      }

      public void stateChanged(ChangeEvent ce) {
        super.performValidation();
      }

      @Override
      protected Color getModelObject(JColorChooser comp) {
        return comp.getColor();
      }
    }
    ColorListener cl = new ColorListener();
    chooser.getSelectionModel().addChangeListener(cl);
    //Add our custom validation code to the validation group
    // XXX even when problems.add is called below, nothing appears - why?
    pnl.getValidationGroup().addItem(cl, false);
    boolean okClicked = pnl.showOkCancelDialog("Validation Demo");
    System.out.println(okClicked ? "User clicked OK" : "User did not click OK");
    System.exit(0);
  }

  private static final class ColorValidator extends AbstractValidator<Color> {

    ColorValidator() {
      super(Color.class);
    }

    public void validate(Problems problems, String compName, Color model) {
      //Convert the color to Hue/Saturation/Brightness
      //scaled from 0F to 1.0F
      float[] hsb = Color.RGBtoHSB(model.getRed(), model.getGreen(),
              model.getBlue(), null);
      if (hsb[2] < 0.25) {
        //Dark colors cause a fatal error
        problems.add("Color is too dark");
      } else if (hsb[2] > 0.9) {
        //Very bright colors get an information message
        problems.add("Color is very bright", Severity.INFO);
      }
      if (hsb[1] > 0.8) {
        //highly saturated colors get a warning
        problems.add("Color is very saturated", Severity.WARNING);
      }
    }
  }

  private Main() {}
}
