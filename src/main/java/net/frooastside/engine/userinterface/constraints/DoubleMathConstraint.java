package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.elements.Element;

public class DoubleMathConstraint extends Constraint {

  private final Operator firstMathematicalOperator;
  private final Operator secondMathematicalOperator;
  private final Constraint firstConstraint;
  private final Constraint secondConstraint;
  private final Constraint thirdConstraint;
  private final boolean initializeConstraints;

  public DoubleMathConstraint(Operator firstMathematicalOperator, Operator secondMathematicalOperator, Constraint firstConstraint, Constraint secondConstraint, Constraint thirdConstraint, boolean initializeConstraints) {
    this.firstMathematicalOperator = firstMathematicalOperator;
    this.secondMathematicalOperator = secondMathematicalOperator;
    this.firstConstraint = firstConstraint;
    this.secondConstraint = secondConstraint;
    this.thirdConstraint = thirdConstraint;
    this.initializeConstraints = initializeConstraints;
  }

  @Override
  public void initialize(ElementConstraints constraints, Element current, Element parent) {
    super.initialize(constraints, current, parent);
    if(initializeConstraints) {
      firstConstraint.initialize(constraints, current, parent);
      secondConstraint.initialize(constraints, current, parent);
      thirdConstraint.initialize(constraints, current, parent);
    }
  }

  public void initializeChild(Constraint constraint) {
    constraint.initialize(constraints(), current(), parent());
  }

  @Override
  public float rawValue() {
    float firstNonRelativeValue = current().rawValueOf(firstConstraint);
    float secondNonRelativeValue = current().rawValueOf(secondConstraint);
    float thirdNonRelativeValue = current().rawValueOf(thirdConstraint);
    return secondMathematicalOperator
      .calculateRawValue(firstMathematicalOperator.calculateRawValue(firstNonRelativeValue, secondNonRelativeValue), thirdNonRelativeValue);
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
    if(initializeConstraints) {
      firstConstraint.setConstraintType(type);
      secondConstraint.setConstraintType(type);
      thirdConstraint.setConstraintType(type);
    }
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
    },
    DIVIDE {
      @Override
      public float calculateRawValue(float firstValue, float secondValue) {
        return firstValue / secondValue;
      }
    };

    public abstract float calculateRawValue(float firstValue, float secondValue);

  }

}
