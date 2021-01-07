package net.frooastside.engine.world;

import java.util.List;
import java.util.Map;

public class Level {

  private Map<RenderType, List<Entity>> renderEntities;

  public void tick() {

  }

  public enum RenderType {

    TERRAIN, OPAQUE_STATIC, OPAQUE_MOVABLE, TRANSLUCENT_STATIC, TRANSLUCENT_MOVABLE;

  }

  public enum ReactionType {

    PERMEABLE, IMPERMEABLE;

  }

}