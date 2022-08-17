package love.polardivision.engine.glwrapper.query;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL43;

public enum QueryType {

  SAMPLES_PASSED(GL15.GL_SAMPLES_PASSED),
  ANY_SAMPLES_PASSED(GL33.GL_ANY_SAMPLES_PASSED),
  ANY_SAMPLES_PASSED_CONSERVATIVE(GL43.GL_ANY_SAMPLES_PASSED_CONSERVATIVE),
  PRIMITIVES_GENERATED(GL30.GL_PRIMITIVES_GENERATED),
  TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN(GL30.GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN),
  TIME_ELAPSED(GL33.GL_TIME_ELAPSED);

  private final int value;

  QueryType(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
