package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.elements.Element;

public class MathConstraint extends Constraint {

  private final Operator mathematicalOperator;
  private final Constraint firstConstraint;
  private final Constraint secondConstraint;

  public MathConstraint(Operator mathematicalOperator, Constraint firstConstraint, Constraint secondConstraint) {
    this.mathematicalOperator = mathematicalOperator;
    this.firstConstraint = firstConstraint;
    this.secondConstraint = secondConstraint;
  }

  @Override
  public void initialize(ElementConstraints constraints, Element current, Element parent) {
    super.initialize(constraints, current, parent);
    firstConstraint.initialize(constraints, current, parent);
    secondConstraint.initialize(constraints, current, parent);
  }

  @Override
  public float rawValue() {
    float firstNonRelativeValue = current().rawValueOf(firstConstraint);
    float secondNonRelativeValue = current().rawValueOf(secondConstraint);
    return mathematicalOperator.calculateRawValue(firstNonRelativeValue, secondNonRelativeValue);
  }

  @Override
  public void setValue(float value) {
    throw new IllegalStateException(I18n.get("error.userinterface.mathValue"));
  }

  @Override
  public boolean relative() {
    return false;
  }

  @Override
  public void setConstraintType(ConstraintType type) {
    super.setConstraintType(type);
    firstConstraint.setConstraintType(type);
    secondConstraint.setConstraintType(type);
  }

  public enum Operator {

    ADD {
      @Override
      public float calculateRawValue(float firstValue, float secondValue) {
        return firstValue + secondValue;
      }
    },
    SUBTRACT {
      @Override
      public float calculateRawValue(float firstValue, float secondValue) {
        return firstValue - secondValue;
      }
    },
    MULTIPLY {
      @Override
      public float calculateRawValue(float firstValue, float secondValue) {
        return firstValue * secondValue;
      }
    };

    public abstract float calculateRawValue(float firstValue, float secondValue);

  }

}
