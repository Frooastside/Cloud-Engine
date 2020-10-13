package net.frooastside.engine.userinterface.constraints;

import net.frooastside.engine.language.I18n;
import net.frooastside.engine.userinterface.Constraint;

public class CenterConstraint extends Constraint {

  @Override
  public void recalculate() {
    Constraint oppositeConstraint = getOpposite();
    if(!(oppositeConstraint instanceof CenterConstraint)) {
      oppositeConstraint.recalculate();
      if(type() == ConstraintType.X || type() == ConstraintType.Y) {
        setRawValue((type() == ConstraintType.X ? constraints().parent().bounds().z - oppositeConstraint.rawValue() : constraints().parent().bounds().w - oppositeConstraint.rawValue()) / 2);
      }else if(type() == ConstraintType.WIDTH || type() == ConstraintType.HEIGHT) {
        float offset = oppositeConstraint.rawValue() - (type() == ConstraintType.WIDTH ? constraints().parent().bounds().x : constraints().parent().bounds().y);
        setRawValue(((type() == ConstraintType.WIDTH) ? constraints().parent().bounds().z : constraints().parent().bounds().w) - (offset * 2));
      }
    }else {
      throw new IllegalStateException(I18n.get("error.userinterface.center"));
    }
  }

  @Override
  public boolean relative() {
    return false;
  }
}
