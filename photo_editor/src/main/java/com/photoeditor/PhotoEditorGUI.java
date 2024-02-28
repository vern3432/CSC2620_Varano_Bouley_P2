package com.photoeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes.Name;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.STRING;
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSliderUI;
import java.util.regex.*;


public class PhotoEditorGUI extends JFrame {

  private static boolean trace = true; // Turn tracing on and off

  private JLabel toolStatusLabel; // JLabel to display currently selected tool
  private JLabel imageStatusLabel; // Status of the image
  private String sidebarStatus = "Paint"; // Initialized sidebarStatus
  private String saveDirectory = "";
  private String loadedImageDirectory = "";
  private boolean Undo = false;
  private Color selectedColor = Color.BLACK; // Start colot
  private JButton colorPickerButton;
  private String Filename = "";

  // primary image
  public BufferedImage image;
  public HashMap<String, CardObject> GeneratedImages = new HashMap<>();
  public String[] keysImage;
  public String SelectedImage = "";


  /**
   * Combine images
   */
  public void MergerImage() {
    BufferedImage combinedImage = new BufferedImage(
        image.getWidth(),
        image.getHeight(),
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = combinedImage.createGraphics();
    g2d.drawImage(image, 0, 0, null);

    // Draw the lines on the combined image
    g2d.setColor(selectedColor);
    for (Line line : lines) {
      g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
    }
    image = combinedImage;
    g2d.dispose();
    lines.clear();

  }

  /**
   * Set the current image
   * @param inputimage
   */
  public void setImage(BufferedImage inputimage) {
    this.image = inputimage;

  }

  /**
   * Get the current image
   * @return
   */
  public BufferedImage getImage() {
    return this.image;

  }

  /**
   * Get the combo box for the cards
   * @param name
   */
  public void selectCombobox(String name) {
    GeneratedImages.get(SelectedImage).setAssociatedImag(image);

    setImage(GeneratedImages.get(name).getAssociatedImag());
    drawingPanel.repaint();

  }

  JComboBox comboBox = new JComboBox(GeneratedImages.keySet().toArray());


  public static String incrementTrailingInteger(String input) {
    StringBuilder base = new StringBuilder();
    StringBuilder number = new StringBuilder();

    // Find the base string and the trailing integer
    boolean foundNumber = false;
    for (int i = input.length() - 1; i >= 0; i--) {
        char ch = input.charAt(i);
        if (Character.isDigit(ch)) {
            number.insert(0, ch); // prepend digit to number
            foundNumber = true;
        } else if (foundNumber) {
            base.insert(0, ch); // prepend non-digit character to base
        } else {
            base.insert(0, ch); // prepend non-digit character to base
        }
    }

    // If no integer found, add 2 to the end
    if (number.length() == 0) {
        return input + "2";
    }

    // Increment the integer value
    int intValue = Integer.parseInt(number.toString()) + 1;

    return base.toString() + intValue;
}

  /**
   * Add a new card with an associated image
   * @param Name
   * @param image│                           │   ├── FailableFunctionsTest$9.class
│                           │   ├── FailableFunctionsTest$CloseableObject.class
│                           │   ├── FailableFunctionsTest$FailureOnOddInvocations.class
│                           │   ├── FailableFunctionsTest$SomeException.class
│                           │   ├── FailableFunctionsTest$Testable.class
│                           │   ├── FailableFunctionsTest.class
│                           │   ├── FailableSupplierTest.class
│                           │   ├── FunctionsTest.class
│                           │   ├── IntToCharFunctionTest.class
│                           │   ├── MethodFixtures.class
│                           │   ├── MethodInvokersBiConsumerTest.class
│                           │   ├── MethodInvokersBiFunctionTest.class
│                           │   ├── MethodInvokersFailableBiConsumerTest.class
│                           │   ├── MethodInvokersFailableBiFunctionTest.class
│                           │   ├── MethodInvokersFailableFunctionTest.class
│                           │   ├── MethodInvokersFailableSupplierTest.class
│                           │   ├── MethodInvokersFunctionTest.class
│                           │   ├── MethodInvokersSupplierTest.class
│                           │   ├── Objects.class
│                           │   ├── ObjectsTest$TestableFailableSupplier.class
│                           │   ├── ObjectsTest$TestableSupplier.class
│                           │   ├── ObjectsTest.class
│                           │   ├── SuppliersTest.class
│                           │   ├── ToBooleanBiFunctionTest.class
│                           │   ├── TriConsumerTest.class
│                           │   └── TriFunctionTest.class
│                           ├── FunctionsTest$10.class
│                           ├── FunctionsTest$11.class
│                           ├── FunctionsTest$12.class
│                           ├── FunctionsTest$13.class
│                           ├── FunctionsTest$14.class
│                           ├── FunctionsTest$15.class
│                           ├── FunctionsTest$16.class
│                           ├── FunctionsTest$17.class
│                           ├── FunctionsTest$18.class
│                           ├── FunctionsTest$1.class
│                           ├── FunctionsTest$2.class
│                           ├── FunctionsTest$3.class
│                           ├── FunctionsTest$4.class
│                           ├── FunctionsTest$5.class
│                           ├── FunctionsTest$6.class
│                           ├── FunctionsTest$7.class
│                           ├── FunctionsTest$8.class
│                           ├── FunctionsTest$9.class
│                           ├── FunctionsTest$CloseableObject.class
│                           ├── FunctionsTest$FailureOnOddInvocations.class
│                           ├── FunctionsTest$SomeException.class
│                           ├── FunctionsTest$Testable.class
│                           ├── FunctionsTest.class
│                           ├── HashSetvBitSetTest.class
│                           ├── IntegerRangeTest.class
│                           ├── JavaVersionTest.class
│                           ├── LocaleUtilsTest.class
│                           ├── LongRangeTest.class
│                           ├── math
│                           │   ├── FractionTest.class
│                           │   ├── IEEE754rUtilsTest.class
│                           │   └── NumberUtilsTest.class
│                           ├── Month.class
│                           ├── mutable
│                           │   ├── MutableBooleanTest.class
│                           │   ├── MutableByteTest.class
│                           │   ├── MutableDoubleTest.class
│                           │   ├── MutableFloatTest.class
│                           │   ├── MutableIntTest.class
│                           │   ├── MutableLongTest.class
│                           │   ├── MutableObjectTest.class
│                           │   ├── MutableShortTest.class
│                           │   └── PrintAtomicVsMutable.class
│                           ├── NotImplementedExceptionTest.class
│                           ├── ObjectUtilsTest$CharSequenceComparator.class
│                           ├── ObjectUtilsTest$CloneableString.class
│                           ├── ObjectUtilsTest$NonComparableCharSequence.class
│                           ├── ObjectUtilsTest$UncloneableString.class
│                           ├── ObjectUtilsTest.class
│                           ├── RandomStringUtilsTest$1.class
│                           ├── RandomStringUtilsTest.class
│                           ├── RandomUtilsTest.class
│                           ├── RangeTest$AbstractComparable.class
│                           ├── RangeTest$DerivedComparableA.class
│                           ├── RangeTest$DerivedComparableB.class
│                           ├── RangeTest.class
│                           ├── reflect
│                           │   ├── AAAClass$BBBClass.class
│                           │   ├── AAAClass.class
│                           │   ├── AAClass$BBClass.class
│                           │   ├── AAClass.class
│                           │   ├── AClass$AInterface.class
│                           │   ├── AClass$BClass.class
│                           │   ├── AClass$CClass.class
│                           │   ├── AClass$DClass.class
│                           │   ├── AClass$EClass.class
│                           │   ├── AClass$FClass.class
│                           │   ├── AClass$GClass.class
│                           │   ├── AClass.class
│                           │   ├── ConstructorUtilsTest$BaseClass.class
│                           │   ├── ConstructorUtilsTest$PrivateClass$PublicInnerClass.class
│                           │   ├── ConstructorUtilsTest$PrivateClass.class
│                           │   ├── ConstructorUtilsTest$SubClass.class
│                           │   ├── ConstructorUtilsTest$TestBean.class
│                           │   ├── ConstructorUtilsTest.class
│                           │   ├── FieldUtilsTest.class
│                           │   ├── InheritanceUtilsTest.class
│                           │   ├── Lang1703Test.class
│                           │   ├── MethodUtilsTest$AbstractGetMatchingMethod.class
│                           │   ├── MethodUtilsTest$ChildInterface.class
│                           │   ├── MethodUtilsTest$ChildObject.class
│                           │   ├── MethodUtilsTest$GetMatchingMethodClass.class
│                           │   ├── MethodUtilsTest$GetMatchingMethodImpl.class
│                           │   ├── MethodUtilsTest$GrandParentObject.class
│                           │   ├── MethodUtilsTest$InheritanceBean.class
│                           │   ├── MethodUtilsTest$MethodDescriptor.class
│                           │   ├── MethodUtilsTest$ParentObject.class
│                           │   ├── MethodUtilsTest$PrivateInterface.class
│                           │   ├── MethodUtilsTest$TestBean.class
│                           │   ├── MethodUtilsTest$TestBeanWithInterfaces.class
│                           │   ├── MethodUtilsTest$TestMutable.class
│                           │   ├── MethodUtilsTest.class
│                           │   ├── Test1.class
│                           │   ├── testbed
│                           │   │   ├── Ambig.class
│                           │   │   ├── Annotated.class
│                           │   │   ├── AnotherChild.class
│                           │   │   ├── AnotherParent.class
│                           │   │   ├── Bar.class
│                           │   │   ├── Foo.class
│                           │   │   ├── GenericConsumer.class
│                           │   │   ├── GenericParent.class
│                           │   │   ├── GenericTypeHolder.class
│                           │   │   ├── Grandchild.class
│                           │   │   ├── Parent.class
│                           │   │   ├── PrivatelyShadowedChild.class
│                           │   │   ├── PublicChild.class
│                           │   │   ├── PubliclyShadowedChild.class
│                           │   │   ├── StaticContainerChild.class
│                           │   │   ├── StaticContainer.class
│                           │   │   └── StringParameterizedChild.class
│                           │   ├── TypeLiteralTest$10.class
│                           │   ├── TypeLiteralTest$11.class
│                           │   ├── TypeLiteralTest$12.class
│                           │   ├── TypeLiteralTest$2.class
│                           │   ├── TypeLiteralTest$3.class
│                           │   ├── TypeLiteralTest$4.class
│                           │   ├── TypeLiteralTest$5.class
│                           │   ├── TypeLiteralTest$6.class
│                           │   ├── TypeLiteralTest$7.class
│                           │   ├── TypeLiteralTest$8.class
│                           │   ├── TypeLiteralTest$9.class
│                           │   ├── TypeLiteralTest.class
│                           │   ├── TypeUtilsTest$And.class
│                           │   ├── TypeUtilsTest$ClassWithSuperClassWithGenericType.class
│                           │   ├── TypeUtilsTest$MyInnerClass.class
│                           │   ├── TypeUtilsTest$Other.class
│                           │   ├── TypeUtilsTest$Tester.class
│                           │   ├── TypeUtilsTest$That.class
│                           │   ├── TypeUtilsTest$The.class
│                           │   ├── TypeUtilsTest$Thing.class
│                           │   ├── TypeUtilsTest$This.class
│                           │   └── TypeUtilsTest.class
│                           ├── RegExUtilsTest.class
│                           ├── SerializationUtilsTest$1.class
│                           ├── SerializationUtilsTest.class
│                           ├── stream
│                           │   ├── FailableStreamTest.class
│                           │   ├── IntStreamsTest.class
│                           │   ├── LangCollectorsTest$Fixture.class
│                           │   ├── LangCollectorsTest.class
│                           │   └── StreamsTest.class
│                           ├── StreamsTest.class
│                           ├── StringEscapeUtilsTest.class
│                           ├── StringUtilsContainsTest.class
│                           ├── StringUtilsEmptyBlankTest.class
│                           ├── StringUtilsEqualsIndexOfTest$CustomCharSequence.class
│                           ├── StringUtilsEqualsIndexOfTest.class
│                           ├── StringUtilsIsMixedCaseTest.class
│                           ├── StringUtilsIsTest.class
│                           ├── StringUtilsStartsEndsWithTest.class
│                           ├── StringUtilsSubstringTest.class
│                           ├── StringUtilsTest$1.class
│                           ├── StringUtilsTest.class
│                           ├── StringUtilsTrimStripTest.class
│                           ├── StringUtilsValueOfTest.class
│                           ├── Supplementary.class
│                           ├── SystemPropertiesTest.class
│                           ├── SystemUtilsTest.class
│                           ├── test
│                           │   ├── NotVisibleExceptionFactory$NotVisibleException.class
│                           │   └── NotVisibleExceptionFactory.class
│                           ├── text
│                           │   ├── CompositeFormatTest$1.class
│                           │   ├── CompositeFormatTest$2.class
│                           │   ├── CompositeFormatTest.class
│                           │   ├── ExtendedMessageFormatTest$LowerCaseFormat.class
│                           │   ├── ExtendedMessageFormatTest$LowerCaseFormatFactory.class
│                           │   ├── ExtendedMessageFormatTest$OtherExtendedMessageFormat.class
│                           │   ├── ExtendedMessageFormatTest$OverrideShortDateFormatFactory.class
│                           │   ├── ExtendedMessageFormatTest$UpperCaseFormat.class
│                           │   ├── ExtendedMessageFormatTest$UpperCaseFormatFactory.class
│                           │   ├── ExtendedMessageFormatTest.class
│                           │   ├── FormattableUtilsTest.class
│                           │   ├── StrBuilderAppendInsertTest$10.class
│                           │   ├── StrBuilderAppendInsertTest$11.class
│                           │   ├── StrBuilderAppendInsertTest$12.class
│                           │   ├── StrBuilderAppendInsertTest$1.class
│                           │   ├── StrBuilderAppendInsertTest$2.class
│                           │   ├── StrBuilderAppendInsertTest$3.class
│                           │   ├── StrBuilderAppendInsertTest$4.class
│                           │   ├── StrBuilderAppendInsertTest$5.class
│                           │   ├── StrBuilderAppendInsertTest$6.class
│                           │   ├── StrBuilderAppendInsertTest$7.class
│                           │   ├── StrBuilderAppendInsertTest$8.class
│                           │   ├── StrBuilderAppendInsertTest$9.class
│                           │   ├── StrBuilderAppendInsertTest.class
│                           │   ├── StrBuilderTest$1.class
│                           │   ├── StrBuilderTest$MockReadable.class
│                           │   ├── StrBuilderTest.class
│                           │   ├── StrLookupTest.class
│                           │   ├── StrMatcherTest.class
│                           │   ├── StrSubstitutorTest$1.class
│                           │   ├── StrSubstitutorTest.class
│                           │   ├── StrTokenizerTest$1.class
│                           │   ├── StrTokenizerTest$2.class
│                           │   ├── StrTokenizerTest$3.class
│                           │   ├── StrTokenizerTest.class
│                           │   ├── translate
│                           │   │   ├── EntityArraysTest.class
│                           │   │   ├── LookupTranslatorTest.class
│                           │   │   ├── NumericEntityEscaperTest.class
│                           │   │   ├── NumericEntityUnescaperTest.class
│                           │   │   ├── OctalUnescaperTest.class
│                           │   │   ├── UnicodeEscaperTest.class
│                           │   │   ├── UnicodeUnescaperTest.class
│                           │   │   └── UnicodeUnpairedSurrogateRemoverTest.class
│                           │   └── WordUtilsTest.class
│                           ├── ThreadUtilsTest$TestThread.class
│                           ├── ThreadUtilsTest.class
│                           ├── time
│                           │   ├── CalendarUtilsTest.class
│                           │   ├── DateFormatUtilsTest.class
│                           │   ├── DateUtilsFragmentTest.class
│                           │   ├── DateUtilsRoundingTest.class
│                           │   ├── DateUtilsTest.class
│                           │   ├── DurationFormatUtilsTest.class
│                           │   ├── DurationUtilsTest.class
│                           │   ├── FastDateFormat_PrinterTest.class
│                           │   ├── FastDateFormatTest$1.class
│                           │   ├── FastDateFormatTest.class
│                           │   ├── FastDateParser_MoreOrLessTest.class
│                           │   ├── FastDateParserSDFTest.class
│                           │   ├── FastDateParserTest$Expected1806.class
│                           │   ├── FastDateParserTest.class
│                           │   ├── FastDateParser_TimeZoneStrategyTest.class
│                           │   ├── FastDatePrinterTest$Expected1806.class
│                           │   ├── FastDatePrinterTest.class
│                           │   ├── FastDatePrinterTimeZonesTest.class
│                           │   ├── FastTimeZoneTest.class
│                           │   ├── GmtTimeZoneTest.class
│                           │   ├── Java15BugFastDateParserTest.class
│                           │   ├── StopWatchTest.class
│                           │   └── WeekYearTest.class
│                           ├── TooMany.class
│                           ├── Traffic2.class
│                           ├── Traffic.class
│                           ├── tuple
│                           │   ├── ImmutablePairTest.class
│                           │   ├── ImmutableTripleTest.class
│                           │   ├── MutablePairTest.class
│                           │   ├── MutableTripleTest.class
│                           │   ├── PairTest.class
│                           │   └── TripleTest.class
│                           ├── util
│                           │   └── FluentBitSetTest.class
│                           ├── ValidateTest$ExclusiveBetween$WithComparable$WithMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithComparable$WithoutMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithComparable.class
│                           ├── ValidateTest$ExclusiveBetween$WithDouble$WithMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithDouble$WithoutMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithDouble.class
│                           ├── ValidateTest$ExclusiveBetween$WithLong$WithMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithLong$WithoutMessage.class
│                           ├── ValidateTest$ExclusiveBetween$WithLong.class
│                           ├── ValidateTest$ExclusiveBetween.class
│                           ├── ValidateTest$Finite$WithMessage.class
│                           ├── ValidateTest$Finite$WithoutMessage.class
│                           ├── ValidateTest$Finite.class
│                           ├── ValidateTest$InclusiveBetween$WithComparable$WithMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithComparable$WithoutMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithComparable.class
│                           ├── ValidateTest$InclusiveBetween$WithDouble$WithMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithDouble$WithoutMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithDouble.class
│                           ├── ValidateTest$InclusiveBetween$WithLong$WithMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithLong$WithoutMessage.class
│                           ├── ValidateTest$InclusiveBetween$WithLong.class
│                           ├── ValidateTest$InclusiveBetween.class
│                           ├── ValidateTest$IsAssignable$WithMessage.class
│                           ├── ValidateTest$IsAssignable$WithoutMessage.class
│                           ├── ValidateTest$IsAssignable.class
│                           ├── ValidateTest$IsInstanceOf$WithMessage.class
│                           ├── ValidateTest$IsInstanceOf$WithMessageTemplate.class
│                           ├── ValidateTest$IsInstanceOf$WithoutMessage.class
│                           ├── ValidateTest$IsInstanceOf.class
│                           ├── ValidateTest$IsTrue$WithDoubleTemplate.class
│                           ├── ValidateTest$IsTrue$WithLongTemplate.class
│                           ├── ValidateTest$IsTrue$WithMessage.class
│                           ├── ValidateTest$IsTrue$WithObjectTemplate.class
│                           ├── ValidateTest$IsTrue$WithoutMessage.class
│                           ├── ValidateTest$IsTrue.class
│                           ├── ValidateTest$MatchesPattern$WithMessage.class
│                           ├── ValidateTest$MatchesPattern$WithoutMessage.class
│                           ├── ValidateTest$MatchesPattern.class
│                           ├── ValidateTest$NoNullElements$WithArray$WithMessage.class
│                           ├── ValidateTest$NoNullElements$WithArray$WithoutMessage.class
│                           ├── ValidateTest$NoNullElements$WithArray.class
│                           ├── ValidateTest$NoNullElements$WithCollection$WithMessage.class
│                           ├── ValidateTest$NoNullElements$WithCollection$WithoutMessage.class
│                           ├── ValidateTest$NoNullElements$WithCollection.class
│                           ├── ValidateTest$NoNullElements.class
│                           ├── ValidateTest$NotBlank$WithMessage.class
│                           ├── ValidateTest$NotBlank$WithoutMessage.class
│                           ├── ValidateTest$NotBlank.class
│                           ├── ValidateTest$NotEmpty$WithArray$WithMessage.class
│                           ├── ValidateTest$NotEmpty$WithArray$WithoutMessage.class
│                           ├── ValidateTest$NotEmpty$WithArray.class
│                           ├── ValidateTest$NotEmpty$WithCharSequence$WithMessage.class
│                           ├── ValidateTest$NotEmpty$WithCharSequence$WithoutMessage.class
│                           ├── ValidateTest$NotEmpty$WithCharSequence.class
│                           ├── ValidateTest$NotEmpty$WithCollection$WithMessage.class
│                           ├── ValidateTest$NotEmpty$WithCollection$WithoutMessage.class
│                           ├── ValidateTest$NotEmpty$WithCollection.class
│                           ├── ValidateTest$NotEmpty$WithMap$WithMessage.class
│                           ├── ValidateTest$NotEmpty$WithMap$WithoutMessage.class
│                           ├── ValidateTest$NotEmpty$WithMap.class
│                           ├── ValidateTest$NotEmpty.class
│                           ├── ValidateTest$NotNaN$WithMessage.class
│                           ├── ValidateTest$NotNaN$WithoutMessage.class
│                           ├── ValidateTest$NotNaN.class
│                           ├── ValidateTest$NotNull$WithMessage.class
│                           ├── ValidateTest$NotNull$WithoutMessage.class
│                           ├── ValidateTest$NotNull.class
│                           ├── ValidateTest$UtilClassConventions.class
│                           ├── ValidateTest$ValidIndex$WithArray$WithMessage.class
│                           ├── ValidateTest$ValidIndex$WithArray$WithoutMessage.class
│                           ├── ValidateTest$ValidIndex$WithArray.class
│                           ├── ValidateTest$ValidIndex$WithCharSequence$WithMessage.class
│                           ├── ValidateTest$ValidIndex$WithCharSequence$WithoutMessage.class
│                           ├── ValidateTest$ValidIndex$WithCharSequence.class
│                           ├── ValidateTest$ValidIndex$WithCollection$WithMessage.class
│                           ├── ValidateTest$ValidIndex$WithCollection$WithoutMessage.class
│                           ├── ValidateTest$ValidIndex$WithCollection.class
│                           ├── ValidateTest$ValidIndex.class
│                           ├── ValidateTest$ValidState$WithoutMessage.class
│                           ├── ValidateTest$ValidState$WitMessage.class
│                           ├── ValidateTest$ValidState.class
│                           └── ValidateTest.class
├── junkfunctoins.txt
├── libs
│   ├── libopencv_java320.dylib
│   ├── libopencv_java320.so
│   └── opencv_java320.dll
├── photo_editor
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   ├── com
│   │       │   │   └── photoeditor
│   │       │   │       ├── BWCartoonEffect.java
│   │       │   │       ├── CardObject.java
│   │       │   │       ├── CartoonEffect.java
│   │       │   │       ├── ColorInverter.java
│   │       │   │       ├── currentConfig.json
│   │       │   │       ├── FilmBlurWithNoise.java
│   │       │   │       ├── FilterButton.java
│   │       │   │       ├── GaussianBlurGUI.java
│   │       │   │       ├── GaussianBlur.java
│   │       │   │       ├── GrayscaleConverter.java
│   │       │   │       ├── ImageFlipper.java
│   │       │   │       ├── ImageLoader.java
│   │       │   │       ├── Line.java
│   │       │   │       ├── MouseHandler.java
│   │       │   │       ├── PerlinNoiseFilter.java
│   │       │   │       ├── PerlinNoiseGUI.java
│   │       │   │       ├── PhotoEditorGUI.java
│   │       │   │       └── systemworking directory
│   │       │   │           └── junk.txt
│   │       │   └── org
│   │       │       └── openjfx
│   │       │           ├── App.java
│   │       │           └── SystemInfo.java
│   │       └── resources
│   │           ├── icon
│   │           │   ├── broom.png
│   │           │   ├── filter2.png
│   │           │   ├── filter.png
│   │           │   ├── flipvvertical.png
│   │           │   ├── folder.png
│   │           │   ├── mirrorsidebar2.png
│   │           │   ├── mirrorsidebar.png
│   │           │   ├── paintbrush.png
│   │           │   ├── paintbrushsidebar.png
│   │           │   ├── paintbucketsidebar.png
│   │           │   ├── reflect.png
│   │           │   ├── saveicon.png
│   │           │   ├── select_tool_box.png
│   │           │   ├── select_tool_lasso.png
│   │           │   ├── straightLine.png
│   │           │   ├── text_feild.png
│   │           │   └── undo_topbar.png
│   │           ├── opencv_java320
│   │           │   ├── libopencv_java320.dylib
│   │           │   ├── libopencv_java320.so
│   │           │   ├── opencv-320.jar
│   │           │   └── opencv_java320.dll
│   │           └── TestImages
│   │               ├── ff9.jpg
│   │               └── fullsized.jpg
│   └── target
│       ├── classes
│       │   ├── com
│       │   │   └── photoeditor
│       │   │       ├── BWCartoonEffect$1.class
│       │   │       ├── BWCartoonEffect$2.class
│       │   │       ├── BWCartoonEffect$3.class
│       │   │       ├── BWCartoonEffect.class
│       │   │       ├── CardObject.class
│       │   │       ├── CartoonEffect$1.class
│       │   │       ├── CartoonEffect$ApplyButtonListener.class
│       │   │       ├── CartoonEffect$OpenMenuItemListener.class
│       │   │       ├── CartoonEffect.class
│       │   │       ├── ColorInverter$1.class
│       │   │       ├── ColorInverter$ApplyButtonListener.class
│       │   │       ├── ColorInverter$OpenMenuItemListener.class
│       │   │       ├── ColorInverter.class
│       │   │       ├── currentConfig.json
│       │   │       ├── FilmBlurWithNoise$1.class
│       │   │       ├── FilmBlurWithNoise$ApplyButtonListener.class
│       │   │       ├── FilmBlurWithNoise$OpenMenuItemListener.class
│       │   │       ├── FilmBlurWithNoise.class
│       │   │       ├── FilterButton$1$1.class
│       │   │       ├── FilterButton$1$2.class
│       │   │       ├── FilterButton$1$3.class
│       │   │       ├── FilterButton$1$4.class
│       │   │       ├── FilterButton$1$5.class
│       │   │       ├── FilterButton$1$6.class
│       │   │       ├── FilterButton$1$7.class
│       │   │       ├── FilterButton$1.class
│       │   │       ├── FilterButton$2.class
│       │   │       ├── FilterButton.class
│       │   │       ├── GaussianBlur.class
│       │   │       ├── GaussianBlurGUI$OpenMenuItemListener.class
│       │   │       ├── GaussianBlurGUI.class
│       │   │       ├── GrayscaleConverter$1.class
│       │   │       ├── GrayscaleConverter$ApplyButtonListener.class
│       │   │       ├── GrayscaleConverter$OpenMenuItemListener.class
│       │   │       ├── GrayscaleConverter.class
│       │   │       ├── ImageFlipper$1.class
│       │   │       ├── ImageFlipper$2.class
│       │   │       ├── ImageFlipper$3.class
│       │   │       ├── ImageFlipper.class
│       │   │       ├── ImageLoader.class
│       │   │       ├── Line.class
│       │   │       ├── MouseHandler.class
│       │   │       ├── PerlinNoiseFilter.class
│       │   │       ├── PerlinNoiseGUI$1.class
│       │   │       ├── PerlinNoiseGUI$2.class
│       │   │       ├── PerlinNoiseGUI.class
│       │   │       ├── PhotoEditorGUI$10.class
│       │   │       ├── PhotoEditorGUI$11.class
│       │   │       ├── PhotoEditorGUI$12.class
│       │   │       ├── PhotoEditorGUI$13.class
│       │   │       ├── PhotoEditorGUI$14.class
│       │   │       ├── PhotoEditorGUI$15.class
│       │   │       ├── PhotoEditorGUI$16.class
│       │   │       ├── PhotoEditorGUI$17.class
│       │   │       ├── PhotoEditorGUI$18.class
│       │   │       ├── PhotoEditorGUI$19.class
│       │   │       ├── PhotoEditorGUI$1.class
│       │   │       ├── PhotoEditorGUI$20.class
│       │   │       ├── PhotoEditorGUI$21.class
│       │   │       ├── PhotoEditorGUI$22.class
│       │   │       ├── PhotoEditorGUI$23.class
│       │   │       ├── PhotoEditorGUI$2.class
│       │   │       ├── PhotoEditorGUI$3.class
│       │   │       ├── PhotoEditorGUI$4.class
│       │   │       ├── PhotoEditorGUI$5.class
│       │   │       ├── PhotoEditorGUI$6.class
│       │   │       ├── PhotoEditorGUI$7.class
│       │   │       ├── PhotoEditorGUI$8.class
│       │   │       ├── PhotoEditorGUI$9.class
│       │   │       ├── PhotoEditorGUI$ColorPickerListener.class
│       │   │       ├── PhotoEditorGUI$Line.class
│       │   │       ├── PhotoEditorGUI$ToleranceSliderListener.class
│       │   │       ├── PhotoEditorGUI.class
│       │   │       └── systemworking directory
│       │   │           └── junk.txt
│       │   ├── icon
│       │   │   ├── broom.png
│       │   │   ├── filter2.png
│       │   │   ├── filter.png
│       │   │   ├── flipvvertical.png
│       │   │   ├── folder.png
│       │   │   ├── mirrorsidebar2.png
│       │   │   ├── mirrorsidebar.png
│       │   │   ├── paintbrush.png
│       │   │   ├── paintbrushsidebar.png
│       │   │   ├── paintbucketsidebar.png
│       │   │   ├── reflect.png
│       │   │   ├── saveicon.png
│       │   │   ├── select_tool_box.png
│       │   │   ├── select_tool_lasso.png
│       │   │   ├── straightLine.png
│       │   │   ├── text_feild.png
│       │   │   └── undo_topbar.png
│       │   ├── opencv_java320
│       │   │   ├── libopencv_java320.dylib
│       │   │   ├── libopencv_java320.so
│       │   │   ├── opencv-320.jar
│       │   │   └── opencv_java320.dll
│       │   ├── org
│       │   │   └── openjfx
│       │   │       ├── App.class
│       │   │       └── SystemInfo.class
│       │   └── TestImages
│       │       ├── ff9.jpg
│       │       └── fullsized.jpg
│       └── test-classes
├── README.md
└── trash
    └── junk.txt
        ├── BWCartoonEffectApp.java
        ├── CartoonEffectAppv1.java
        ├── CartoonifyApp.java
        ├── CartoonizerApp.java
        ├── GaussianBlurWithNoise.java
        ├── ImageFilterGUI.java
        └── weirdbloodplatter.java
   * 
   * 
   * 
   */
  public void addCardImageToState(String Name, BufferedImage image) {

    while(GeneratedImages.containsKey(Name)) {
      Name=incrementTrailingInteger(Name);

    }

      GeneratedImages.put(Name, new CardObject(image, Name));
      UpdatedCombobox();
      selectCombobox(Name);
      SelectedImage = Name;
  }

  /**
   * Update the combo box with a new component
   */
  public void UpdatedCombobox() {

    JComboBox comboBoxtemp = null;
    Component[] components2 = topPanel.getComponents();

    // System.out.println("Current
    // Value"+GeneratedImages.keySet().toArray().toString());
    String[] strings = GeneratedImages.keySet().toArray(new String[GeneratedImages.size()]);
    comboBox = new JComboBox(strings);
    keysImage = strings;

    for (Component component : components2) {
      if (component instanceof JComboBox) {
        comboBoxtemp = (JComboBox) component;
        break;
      }
    }

    if (comboBoxtemp != null) {
      topPanel.remove(comboBoxtemp);
      topPanel.repaint();

    }

    // If combo box clicked
    comboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        MergerImage();

        String selectedValue = (String) comboBox.getSelectedItem();
        System.out.println("selecting:" + selectedValue);
        selectCombobox(selectedValue);
        System.out.println("previously selected: " + SelectedImage);
        SelectedImage = selectedValue;

      }

    });

    // If bombo box item selected
    comboBox.addKeyListener(new KeyListener() {
      int selectedIndex = 0;

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
          // Cycle to the next option
          selectedIndex = (selectedIndex + 1) % comboBox.getItemCount();
          comboBox.setSelectedIndex(selectedIndex);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }
    });

    topPanel.add(comboBox);
    topPanel.revalidate();
    topPanel.repaint();

    System.out.println("Combo box done");
    drawingPanel.repaint();
  }

  /**
   * Loads a new image from a file
   * @param filename
   * @return
   */
  public BufferedImage loadImage(String filename) {
    BufferedImage iconImage = null;
    try {
      // Load the icon image using ClassLoader
      InputStream inputStream = ImageLoader.class.getResourceAsStream("/icon/" + filename);
      if (inputStream != null) {
        iconImage = ImageIO.read(inputStream);
        inputStream.close();
      } else {
        System.err.println("Icon image not found: " + filename);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return iconImage;
  }

  /*
   * Round numbers
   */
  public static double round(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  private void updateImageinfo(BufferedImage updated) {
    int width = updated.getWidth();
    int height = updated.getHeight();
    int totalPixels = width * height;
    int sumRed = 0;
    int sumGreen = 0;
    int sumBlue = 0;

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        sumRed += red;
        sumGreen += green;
        sumBlue += blue;
      }
    }

    // calc average pixel values for each channel
    double avgRed = (double) sumRed / totalPixels;
    double avgGreen = (double) sumGreen / totalPixels;
    double avgBlue = (double) sumBlue / totalPixels;

    imageStatusLabel.setText(
        "File:" +
            Filename +
            " " +
            "avgRed:" +
            round(avgRed, 2) +
            " " +
            "avgGreen:" +
            round(avgGreen, 2) +
            " " +
            "avgBlue:" +
            round(avgBlue, 2) +
            " " +
            "Resolution=" +
            width +
            "*" +
            height +
            "=" +
            totalPixels +
            "px");
  }

  private JButton saveButton;
  private JButton loadButton;
  private JButton undoButton;
  private JButton paintButton;
  private JButton fillButton;
  private JButton textButton;
  private JButton clearButton;

  // private JButton filterButton;
  private JButton selectToolButton;
  private JButton RefelectButton;
  private JButton FlipButton;

  private JButton straightLineMenuItem;
  private JSlider toleranceSlider; // New tolerance slider
  private FilterButton Filter;

  // drawing components
  private JPanel drawingPanel;
  private Point startPoint;
  private Point endPoint;
  private ArrayList<Line> lines = new ArrayList<>();
  private boolean isDrawing = false;
  private boolean fillBucketMode = false;
  private boolean drawStraightLineMode = false;
  private int fillTolerance = 10; // changes this for sensitivity of bucket fill
  private JPanel sidebarPanel;
  private JPanel topPanel;

  // Begin the demo
  public PhotoEditorGUI() {
    // set up the JFrame
    setTitle("Photo Editor");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(600, 400));

    // create components
    undoButton = createButton2("undo_topbar.png", "Undo");
    clearButton = createButton2("broom.png", "Clear All");

    paintButton = createPaintButton("paintbrush.png", "Paint");

    paintButton
        .getInputMap()
        .put(
            KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK),
            "buttonAction");

    paintButton
        .getActionMap()
        .put(
            "buttonAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                // Perform the same action as clicking the button
                paintButton.doClick();
              }
            });
    fillButton = createBucketButton("paintbucketsidebar.png", "Fill");
    fillButton.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // Your mouseClicked action here
            BufferedImage combinedImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);

            // Draw the lines on the combined image
            g2d.setColor(selectedColor);
            for (Line line : lines) {
              g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
            image = combinedImage;
            g2d.dispose();

            // sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            // toolStatusLabel.setText("Selected Tool: " + toolTipText); // Update
            // toolStatusLabel
            fillBucketMode = true;
            drawStraightLineMode = false;
          }
        });

    fillButton
        .getInputMap()
        .put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
            "buttonAction");

    fillButton
        .getActionMap()
        .put(
            "buttonAction",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                // Perform the same action as clicking the button
                fillButton.doClick();
              }
            });

    textButton = createButton("text_feild.png", "Text"); // new text button
    RefelectButton = createButton("reflect.png", "Reflect Horizontially"); // new reflect button
    FlipButton = createButton("flipvvertical.png", "Reflect Horizontially"); // new flip button
    colorPickerButton = new JButton("Select Color"); // Initialize colorPickerButton

    // For the sliders
    toleranceSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, fillTolerance);
    toleranceSlider.setMajorTickSpacing(10);
    toleranceSlider.setMinorTickSpacing(5);
    toleranceSlider.setPaintTicks(true);
    toleranceSlider.setPaintLabels(true);
    toleranceSlider.addChangeListener(new ToleranceSliderListener());

    // Set up layout using GridBagLayout
    // Main Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());

    // The top panel
    topPanel = new JPanel(new CardLayout());
    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

    // If undo button is clicked
    undoButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            int size = lines.size(); // List size
            if (lines.size() != 0) {
              lines.remove(size - 1); // remove the last drawn line
              drawingPanel.repaint(); // repaint the drawing panel to reflect the change
              if (trace) {
                System.out.println("Line removed: size " + size);
              }
            } else {
              if (trace) {
                System.out.println("Nothing to undo");
              }
            }
          }
        });
    
    // If clear button is clicked
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        if (lines.size() > 0){
          lines.clear();
          drawingPanel.repaint();
        }
        else {
          System.out.println("The panel is cleared");
        }
      }
    });

    //If reflect button is clicked
    RefelectButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MergerImage();
            String selectedValue = (String) comboBox.getSelectedItem();
            System.out.println("selecting:" + selectedValue);
            selectCombobox(selectedValue);
            ImageFlipper flipper = new ImageFlipper();
            BufferedImage flipped = flipper.flipImage(image, "Horizontal");
            GeneratedImages.get(SelectedImage).setAssociatedImag(flipped);
            setImage(flipped);
          }
        });

    // If flip button is clicked
    FlipButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MergerImage();
            String selectedValue = (String) comboBox.getSelectedItem();
            System.out.println("selecting:" + selectedValue);
            selectCombobox(selectedValue);
            ImageFlipper flipper1 = new ImageFlipper();
            BufferedImage flipped = flipper1.flipImage(image, "Vertical");
            GeneratedImages.get(SelectedImage).setAssociatedImag(flipped);
            setImage(flipped);
          }
        });

    // Add the buttons to the top panel
    topPanel.add(undoButton);
    topPanel.add(clearButton);
    topPanel.add(comboBox);

    // Add the top panel to the main panel
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Initialize the status bar
    toolStatusLabel = new JLabel("Selected Tool: " + sidebarStatus);
    toolStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    toolStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
    imageStatusLabel = new JLabel("No selected Image");
    imageStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageStatusLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

    mainPanel.add(toolStatusLabel, BorderLayout.SOUTH); // Add label
    mainPanel.add(imageStatusLabel, BorderLayout.SOUTH); // Add label

    if (trace) {
      System.out.println("top bar picker added");
    }
    
    // add sidebar
    sidebarPanel = new JPanel();
    sidebarPanel.setLayout(new GridLayout(0, 1));
    sidebarPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    sidebarPanel.setPreferredSize(new Dimension(70, 70));

    // Add components to the side bar panel
    sidebarPanel.add(paintButton);
    sidebarPanel.add(fillButton);
    sidebarPanel.add(textButton);
    sidebarPanel.add(FlipButton);
    sidebarPanel.add(RefelectButton);
    colorPickerButton.addActionListener(new ColorPickerListener()); // add ActionListener to colorPickerButton
    System.out.println("color picker added");
    colorPickerButton.setBackground(selectedColor); // Set initial background color of colorPickerButton

    // Filter button 
    Filter = new FilterButton("filter2.png", "Apply Filter", this.image, this);

    sidebarPanel.add(Filter);
    sidebarPanel.add(colorPickerButton);
    mainPanel.add(sidebarPanel, BorderLayout.EAST);

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");

    // If menu item is clicked
    openMenuItem.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(PhotoEditorGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
              File selectedFile = fileChooser.getSelectedFile();
              try {
                image = ImageIO.read(selectedFile);
                drawingPanel.repaint();
                Filter.setImage(image);
                Filename = selectedFile.toString();

                updateImageinfo(image);
                FilterButton filterButton = null;
                Component[] components = sidebarPanel.getComponents();

                GeneratedImages.clear();

                String title = "primImage";
                SelectedImage = title;

                CardObject primImage = new CardObject(image, title);
                GeneratedImages.put(title, primImage);
                UpdatedCombobox(); // Update the combo box

                for (Component component : components) {
                  if (component instanceof FilterButton) {
                    filterButton = (FilterButton) component;
                    break;
                  }
                }
                if (filterButton != null) {
                  sidebarPanel.remove(filterButton);

                  filterButton.setImage(image);

                  sidebarPanel.add(filterButton);

                  sidebarPanel.revalidate();
                  sidebarPanel.repaint();
                }
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            }
          }
        });

    JMenuItem saveMenuItem = new JMenuItem("Save As");

    // If save menu item is clicked
    saveMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          // Your save logic here
          if (image != null) {
              JFileChooser fileChooser = new JFileChooser();
              int result = fileChooser.showSaveDialog(PhotoEditorGUI.this);
  
              if (result == JFileChooser.APPROVE_OPTION) {
                  File outputFile = fileChooser.getSelectedFile();
  
                  // Check if the file already exists
                  if (outputFile.exists()) {
                      int overwriteResult = JOptionPane.showConfirmDialog(PhotoEditorGUI.this,
                              "File already exists. Do you want to overwrite it?",
                              "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                      if (overwriteResult != JOptionPane.YES_OPTION) {
                          // User chose not to overwrite, return without saving
                          return;
                      }
                  }
  
                // Create a new BufferedImage to draw the lines on
                BufferedImage combinedImage = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = combinedImage.createGraphics();
                g2d.drawImage(image, 0, 0, null);

                // Draw the lines on the combined image
                g2d.setColor(selectedColor);
                for (Line line : lines) {
                  g2d.drawLine(
                      line.start.x,
                      line.start.y,
                      line.end.x,
                      line.end.y);
                }
                g2d.dispose(); // Dispose the Graphics2D object
                try {
                  ImageIO.write(combinedImage, "png", outputFile);
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            }
          }
        });

    JMenuItem newImage = new JMenuItem("New");

    // New image
    newImage.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            lines.clear();

            // Create a dialog to input height and width
            JTextField heightField = new JTextField(5);
            JTextField widthField = new JTextField(5);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Height:"));
            panel.add(heightField);
            panel.add(Box.createHorizontalStrut(15)); // Add some space between components
            panel.add(new JLabel("Width:"));
            panel.add(widthField);

            int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Enter Height and Width",
                JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
              int height = Integer.parseInt(heightField.getText());
              int width = Integer.parseInt(widthField.getText());
              System.out.println(
                  "Creating new image with height: " +
                      height +
                      " and width: " +
                      width);
              BufferedImage bufferedImage = new BufferedImage(
                  width,
                  height,
                  BufferedImage.TYPE_INT_RGB);
              Graphics2D g2d = bufferedImage.createGraphics();
              g2d.setColor(Color.WHITE);
              g2d.fillRect(0, 0, width, height);
              g2d.dispose();

              image = bufferedImage;
              drawingPanel.repaint();
              GeneratedImages.clear();

              String title = "primImage";
              SelectedImage = title;

              CardObject primImage = new CardObject(image, title);
              GeneratedImages.put(title, primImage);
              UpdatedCombobox();

              FilterButton filterButton = null;
              Component[] components = sidebarPanel.getComponents();

              for (Component component : components) {
                if (component instanceof FilterButton) {
                  filterButton = (FilterButton) component;
                  break;
                }
              }
              if (filterButton != null) {
                sidebarPanel.remove(filterButton);

                filterButton.setImage(image);

                sidebarPanel.add(filterButton);

                sidebarPanel.revalidate();
                sidebarPanel.repaint();
              }
            }
          }
        });

    newImage.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));

    openMenuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

    saveMenuItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));

    // saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control alt P"));
    fileMenu.add(newImage);
    fileMenu.add(openMenuItem);
    fileMenu.add(saveMenuItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);

    // Create the drawing panel
    drawingPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
          g.drawImage(image, 0, 0, this);
        }
        if (!fillBucketMode) {
          for (Line line : lines) {
            line.draw(g);
          }
        }
      }
    };
    drawingPanel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            if (fillBucketMode) {
              fillBucket(e.getPoint());

              drawingPanel.repaint();
              drawingPanel.repaint();
            } else {
              startPoint = e.getPoint();
              isDrawing = true;
            }
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            if (isDrawing && !fillBucketMode) {
              endPoint = e.getPoint();
              if (drawStraightLineMode) {
                lines.add(new Line(startPoint, endPoint, selectedColor));
              } else {
                lines.add(new Line(startPoint, startPoint, selectedColor)); // add single point for freehand drawing
              }
              isDrawing = false;
              drawingPanel.repaint();
            }
          }
        });
    drawingPanel.addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            if (isDrawing && !fillBucketMode) {
              endPoint = e.getPoint();
              if (!drawStraightLineMode) {
                lines.add(new Line(startPoint, endPoint, selectedColor));
              }
              startPoint = endPoint;
              drawingPanel.repaint();
            }
          }
        });
    mainPanel.add(drawingPanel);

    // add main panel to content pane
    getContentPane().add(mainPanel);

    // Pack and set visible
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private class Line {

    Point start;
    Point end;
    Color color;

    public Line(Point start, Point end, Color color) {
      this.start = start;
      this.end = end;
      this.color = color;
    }

    public void draw(Graphics g) {
      g.setColor(color);
      g.drawLine(start.x, start.y, end.x, end.y);
    }
  }

  private void fillBucket(Point point) {
    if (image != null) {
      int targetColor = image.getRGB(point.x, point.y);
      int replacementColor = selectedColor.getRGB();
      if (targetColor != replacementColor) {
        floodFill(point.x, point.y, targetColor, replacementColor);
      }
    }
  }

  private void floodFill(int x, int y, int targetColor, int replacementColor) {
    if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
      return;
    }
    if (!isWithinTolerance(image.getRGB(x, y), targetColor, fillTolerance)) {
      return;
    }
    image.setRGB(x, y, replacementColor);
    floodFill(x + 1, y, targetColor, replacementColor);
    floodFill(x - 1, y, targetColor, replacementColor);
    floodFill(x, y + 1, targetColor, replacementColor);
    floodFill(x, y - 1, targetColor, replacementColor);
  }

  private boolean isWithinTolerance(int color1, int color2, int tolerance) {
    int red1 = (color1 >> 16) & 0xFF;
    int green1 = (color1 >> 8) & 0xFF;
    int blue1 = color1 & 0xFF;
    int red2 = (color2 >> 16) & 0xFF;
    int green2 = (color2 >> 8) & 0xFF;
    int blue2 = color2 & 0xFF;

    int deltaRed = Math.abs(red1 - red2);
    int deltaGreen = Math.abs(green1 - green2);
    int deltaBlue = Math.abs(blue1 - blue2);

    return (deltaRed <= tolerance && deltaGreen <= tolerance && deltaBlue <= tolerance);
  }

  class ColorPickerListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      Color newColor = JColorChooser.showDialog(
          PhotoEditorGUI.this,
          "Select a Color",
          selectedColor);
      if (newColor != null) {
        selectedColor = newColor;
        colorPickerButton.setBackground(selectedColor); // change background color of colorPickerButton
      }
    }
  }

  private JButton createButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText;
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          }
        });

    return button;
  }

  private JButton createButton2(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText;
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
          }
        });

    return button;
  }

  private JButton createPaintButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
            fillBucketMode = false;
            drawStraightLineMode = false;
          }
        });

    return button;
  }

  private JButton createBucketButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.WHITE); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }
          // public void mouseClicked(MouseEvent e) {

          // sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
          // toolStatusLabel.setText("Selected Tool: " + toolTipText); // Update
          // toolStatusLabel
          // fillBucketMode = true;
          // drawStraightLineMode = false;
          // }
        });

    return button;
  }

  public void saveAsciiArt(BufferedImage image) {
    String resolutionInput = JOptionPane.showInputDialog(
        null,
        "Enter resolution percentage (1-100):");
    if (resolutionInput != null) {
      try {
        int resolutionPercentage = Integer.parseInt(resolutionInput);
        if (resolutionPercentage >= 1 && resolutionPercentage <= 100) {
          int scaledWidth = (int) (image.getWidth() * (resolutionPercentage / 100.0));
          int scaledHeight = (int) (image.getHeight() * (resolutionPercentage / 100.0));
          BufferedImage scaledImage = new BufferedImage(
              scaledWidth,
              scaledHeight,
              BufferedImage.TYPE_INT_RGB);
          Graphics2D g = scaledImage.createGraphics();
          g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
          g.dispose();

          StringBuilder asciiBuilder = new StringBuilder();
          for (int y = 0; y < scaledImage.getHeight(); y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
              int pixel = scaledImage.getRGB(x, y);
              int brightness = getBrightness(pixel);
              int index = (int) (brightness / 255.0 * (ASCIICHARS.length() - 1));
              asciiBuilder.append(ASCIICHARS.charAt(index));
            }
            asciiBuilder.append("\n");
          }

          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
          int result = fileChooser.showSaveDialog(null);
          if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            File outputFile = new File(selectedDirectory, "ascii_art.txt");
            try (
                BufferedWriter writer = new BufferedWriter(
                    new FileWriter(outputFile))) {
              writer.write(asciiBuilder.toString());
              JOptionPane.showMessageDialog(
                  null,
                  "ASCII art saved successfully to " +
                      outputFile.getAbsolutePath());
            } catch (IOException e) {
              e.printStackTrace();
              JOptionPane.showMessageDialog(
                  null,
                  "Error occurred while saving ASCII art");
            }
          }
        } else {
          JOptionPane.showMessageDialog(
              null,
              "Please enter a resolution percentage between 1 and 100");
        }
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Please enter a valid number");
      }
    }
  }

  private int getBrightness(int pixel) {
    int red = (pixel >> 16) & 0xff;
    int green = (pixel >> 8) & 0xff;
    int blue = pixel & 0xff;
    return (red + green + blue) / 3;
  }

  private static final String ASCIICHARS = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";

  private JButton createStraightButton(String iconPath, String toolTipText) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            sidebarStatus = toolTipText; // Set sidebarStatus when button is clicked
            toolStatusLabel.setText("Selected Tool: " + sidebarStatus); // Update toolStatusLabel
            fillBucketMode = false;
            drawStraightLineMode = true;
          }
        });

    return button;
  }

  // Number of images loaded
  int imageCount = 0;

  // Create the buttons
  private JButton createLoadButton(
      String iconPath,
      String toolTipText,
      String type) {
    // Total number of images selected so they can be stored in another card when
    // more are added

    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                "Image files",
                ImageIO.getReaderFileSuffixes());
            System.out.println(imageFilter);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(imageFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
              File selectedFile = fileChooser.getSelectedFile();
              System.out.println(
                  "Selected File: " + selectedFile.getAbsolutePath());
              imageCount = imageCount + 1;
              System.out.println("Number of images: " + imageCount);
            } else {
              System.out.println("No file selected");
            }
          }
        });

    return button;
  }

  private JButton createSaveButton(
      String iconPath,
      String toolTipText,
      String type) {
    BufferedImage image2 = loadImage(iconPath);
    System.out.println(iconPath);
    Image image = (Image) image2;
    // ImageIcon imageIcon = new ImageIcon("./" + iconPath);
    // System.out.println(imageIcon.toString());
    // Image image = imageIcon.getImage(); // transform it

    Image newimg = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    System.out.println(newimg.toString());

    ImageIcon imageIcon = new ImageIcon(newimg);

    JButton button = new JButton(imageIcon);
    button.setToolTipText(toolTipText);

    button.setToolTipText(toolTipText);

    button.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseEntered(MouseEvent e) {
            button.setBackground(Color.LIGHT_GRAY); // change background color on hover
          }

          @Override
          public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("Button.background")); // reset background color when mouse exits
          }

          public void mouseClicked(MouseEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
              File selectedDirectory = fileChooser.getSelectedFile();
              System.out.println(
                  "Selected Directory: " + selectedDirectory.getAbsolutePath());
            } else {
              System.out.println("No directory selected");
            }
          }
        });

    return button;
  }

  class ToleranceSliderListener implements ChangeListener {

    @Override
    public void stateChanged(ChangeEvent e) {
      JSlider source = (JSlider) e.getSource();
      if (!source.getValueIsAdjusting()) {
        fillTolerance = source.getValue();
        System.out.println("Tolerance changed to: " + fillTolerance);
      }
    }
  }

  private void updateToleranceSliderVisibility() {
    toleranceSlider.setVisible(fillBucketMode);
  }

  private void updateToleranceSliderValue() {
    toleranceSlider.setValue(fillTolerance);
  }

  private void configureToleranceSlider() {
    toleranceSlider.setMinimum(0);
    toleranceSlider.setMaximum(50);
    toleranceSlider.setMajorTickSpacing(50);
    toleranceSlider.setMinorTickSpacing(10);
    toleranceSlider.setPaintTicks(true);
    toleranceSlider.setPaintLabels(true);
    toleranceSlider.setSnapToTicks(true);
    toleranceSlider.setValue(fillTolerance);
  }

  private void addToleranceSliderChangeListener() {
    toleranceSlider.addChangeListener(new ToleranceSliderListener());
  }

  private void setupToleranceSlider() {
    configureToleranceSlider();
    addToleranceSliderChangeListener();
    updateToleranceSliderVisibility();
  }

  public static void main(String[] args) {
    System.out.println("Testing Space");

    // Create and show the GUI
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            new PhotoEditorGUI();
          }
        });
  }
}