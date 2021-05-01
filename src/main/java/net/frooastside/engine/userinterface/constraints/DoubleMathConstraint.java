package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.elements.Element;

public class DoubleMathConstraint extends Constraint {

  private final Operator firstMathematicalOperator;
  private final Operator secondMathematicalOperator;
  private final Constraint firstConstraint;
  private final Constraint secondConstraint;
  private final Constraint thirdConstraint;
  private final boolean initializeFirstConstraint;
  private final boolean initializeSecondConstraint;
  private final boolean initializeThirdConstraint;

  public DoubleMathConstraint(Operator firstMathematicalOperator, Operator secondMathematicalOperator, Constraint firstConstraint, Constraint secondConstraint, Constraint thirdConstraint, boolean initializeFirstConstraint, boolean initializeSecondConstraint, boolean initializeThirdConstraint) {
    this.firstMathematicalOperator = firstMathematicalOperator;
    this.secondMathematicalOperator = secondMathematicalOperator;
    this.firstConstraint = firstConstraint;
    this.secondConstraint = secondConstraint;
    this.thirdConstraint = thirdConstraint;
    this.initializeFirstConstraint = initializeFirstConstraint;
    this.initializeSecondConstraint = initializeSecondConstraint;
    this.initializeThirdConstraint = initializeThirdConstraint;
  }

  @Override
  public void initialize(ElementConstraints constraints, Element current, Element parent) {
    super.initialize(constraints, current, parent);
    if(initializeFirstConstraint) {
      firstConstraint.initialize(constraints, current, parent);
    }
    if(initializeSecondConstraint) {
      secondConstraint.initialize(constraints, current, parent);
    }
    if(initializeThirdConstraint) {
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
    if(initializeFirstConstraint) {
      firstConstraint.setConstraintType(type);
    }
    if(initializeSecondConstraint) {
      secondConstraint.setConstraintType(type);
    }
    if(initializeThirdConstraint) {
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
