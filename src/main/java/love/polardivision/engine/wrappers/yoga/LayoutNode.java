package love.polardivision.engine.wrappers.yoga;

import love.polardivision.engine.wrappers.NativeObject;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.yoga.YGValue;
import org.lwjgl.util.yoga.Yoga;

public class LayoutNode implements NativeObject {

  private long identifier;

  private final Layout layout = new Layout();

  private final NodeType nodeType;
  private Alignment alignSelf = Alignment.AUTO;
  private Alignment alignItems = Alignment.AUTO;
  private Alignment alignContent = Alignment.AUTO;
  private Direction direction = Direction.COLUMN;
  private DisplayMode displayMode = DisplayMode.FLEX;
  private Justify justifyContent = Justify.START;
  private Overflow overflow = Overflow.VISIBLE;
  private PositionType positionType = PositionType.STATIC;
  private Wrap wrap = Wrap.NO_WRAP;

  public LayoutNode() {
    this(NodeType.DEFAULT);
  }

  public LayoutNode(NodeType nodeType) {
    this.nodeType = nodeType;
  }

  @Override
  public void initialize() {
    identifier = Yoga.YGNodeNew();
    if (nodeType != NodeType.DEFAULT) {
      Yoga.YGNodeSetNodeType(identifier, nodeType.value());
    }
  }

  @Override
  public void delete() {
    Yoga.YGNodeFree(identifier);
  }

  public long identifier() {
    return identifier;
  }

  public class Layout {

    private final Vector4f position = new Vector4f();
    private final Vector2f dimensions = new Vector2f();
    private final Vector4f margin = new Vector4f();
    private final Vector4f border = new Vector4f();
    private final Vector4f padding = new Vector4f();

    public void calculate(Vector2i resolution, TextDirection textDirection) {
      Yoga.YGNodeCalculateLayout(identifier, resolution.x, resolution.y, textDirection.value());
      update();
    }

    private void update() {
      if (!Yoga.YGNodeGetHasNewLayout(LayoutNode.this.identifier)) {
        return;
      }
      position.set(
          Yoga.YGNodeLayoutGetLeft(identifier),
          Yoga.YGNodeLayoutGetTop(identifier),
          Yoga.YGNodeLayoutGetRight(identifier),
          Yoga.YGNodeLayoutGetBottom(identifier));
      dimensions.set(Yoga.YGNodeLayoutGetWidth(identifier), Yoga.YGNodeLayoutGetHeight(identifier));
      margin.set(
          Yoga.YGNodeLayoutGetMargin(identifier, Edge.LEFT.value()),
          Yoga.YGNodeLayoutGetMargin(identifier, Edge.TOP.value()),
          Yoga.YGNodeLayoutGetMargin(identifier, Edge.RIGHT.value()),
          Yoga.YGNodeLayoutGetMargin(identifier, Edge.BOTTOM.value()));
      border.set(
          Yoga.YGNodeLayoutGetBorder(identifier, Edge.LEFT.value()),
          Yoga.YGNodeLayoutGetBorder(identifier, Edge.TOP.value()),
          Yoga.YGNodeLayoutGetBorder(identifier, Edge.RIGHT.value()),
          Yoga.YGNodeLayoutGetBorder(identifier, Edge.BOTTOM.value()));
      margin.set(
          Yoga.YGNodeLayoutGetPadding(identifier, Edge.LEFT.value()),
          Yoga.YGNodeLayoutGetPadding(identifier, Edge.TOP.value()),
          Yoga.YGNodeLayoutGetPadding(identifier, Edge.RIGHT.value()),
          Yoga.YGNodeLayoutGetPadding(identifier, Edge.BOTTOM.value()));
    }

    public Vector4f position() {
      update();
      return position;
    }

    public Vector2f dimensions() {
      update();
      return dimensions;
    }

    public Vector4f margin() {
      update();
      return margin;
    }

    public Vector4f border() {
      update();
      return border;
    }

    public Vector4f padding() {
      update();
      return padding;
    }
  }

  public int childCount() {
    return Yoga.YGNodeGetChildCount(identifier);
  }

  public void insertChild(LayoutNode child, int index) {
    Yoga.YGNodeInsertChild(identifier, child.identifier(), index);
  }

  public void removeChild(LayoutNode child) {
    Yoga.YGNodeRemoveChild(identifier, child.identifier());
  }

  public Layout layout() {
    return layout;
  }

  public Alignment alignSelf() {
    return alignSelf;
  }

  public void alignSelf(Alignment alignment) {
    if (this.alignSelf != alignment) {
      this.alignSelf = alignment;
      Yoga.YGNodeStyleSetAlignSelf(identifier, alignment.value());
    }
  }

  public Alignment alignItems() {
    return alignItems;
  }

  public void alignItems(Alignment alignment) {
    if (this.alignItems != alignment) {
      this.alignItems = alignment;
      Yoga.YGNodeStyleSetAlignItems(identifier, alignment.value());
    }
  }

  public Alignment alignContent() {
    return alignContent;
  }

  public void alignContent(Alignment alignment) {
    if (this.alignContent != alignment) {
      this.alignContent = alignment;
      Yoga.YGNodeStyleSetAlignContent(identifier, alignment.value());
    }
  }

  public Direction direction() {
    return direction;
  }

  public void setDirection(Direction direction) {
    if (this.direction != direction) {
      this.direction = direction;
      Yoga.YGNodeStyleSetFlexDirection(identifier, direction.value());
    }
  }

  public DisplayMode displayMode() {
    return displayMode;
  }

  public void setDisplayMode(DisplayMode displayMode) {
    if (this.displayMode != displayMode) {
      this.displayMode = displayMode;
      Yoga.YGNodeStyleSetDisplay(identifier, displayMode.value());
    }
  }

  public Justify justifyContent() {
    return justifyContent;
  }

  public void justifyContent(Justify justify) {
    if (this.justifyContent != justify) {
      this.justifyContent = justify;
      Yoga.YGNodeStyleSetJustifyContent(identifier, justify.value());
    }
  }

  public Overflow overflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    if (this.overflow != overflow) {
      this.overflow = overflow;
      Yoga.YGNodeStyleSetOverflow(identifier, overflow.value());
    }
  }

  public PositionType positionType() {
    return positionType;
  }

  public void setPositionType(PositionType positionType) {
    if (this.positionType != positionType) {
      this.positionType = positionType;
      Yoga.YGNodeStyleSetPositionType(identifier, positionType.value());
    }
  }

  public Wrap wrap() {
    return wrap;
  }

  public void setWrap(Wrap wrap) {
    if (this.wrap != wrap) {
      this.wrap = wrap;
      Yoga.YGNodeStyleSetFlexWrap(identifier, wrap.value());
    }
  }

  public Value position(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue =
          Yoga.YGNodeStyleGetPosition(identifier, edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setPosition(Edge edge, float position) {
    this.setPosition(edge, position, false);
  }

  public void setPosition(Edge edge, float position, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetPosition(identifier, edge.value(), position);
    } else {
      Yoga.YGNodeStyleSetPositionPercent(identifier, edge.value(), position);
    }
  }

  public float aspectRatio() {
    return Yoga.YGNodeStyleGetAspectRatio(identifier);
  }

  public void setAspectRatio(float aspectRatio) {
    Yoga.YGNodeStyleSetAspectRatio(identifier, aspectRatio);
  }

  public float border(Edge edge) {
    return Yoga.YGNodeStyleGetBorder(identifier, edge.value());
  }

  public void setBorder(Edge edge, float border) {
    Yoga.YGNodeStyleSetBorder(identifier, edge.value(), border);
  }

  public Value flexBasis() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetFlexBasis(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetFlexBasis() {
    Yoga.YGNodeStyleSetFlexBasisAuto(identifier);
  }

  public void setFlexBasis(float flexBasis) {
    this.setFlexBasis(flexBasis, false);
  }

  public void setFlexBasis(float flexBasis, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetFlexBasis(identifier, flexBasis);
    } else {
      Yoga.YGNodeStyleSetFlexBasisPercent(identifier, flexBasis);
    }
  }

  public float flexGrow() {
    return Yoga.YGNodeStyleGetFlexGrow(identifier);
  }

  public void setFlexGrow(float flexGrow) {
    Yoga.YGNodeStyleSetFlexGrow(identifier, flexGrow);
  }

  public float flexShrink() {
    return Yoga.YGNodeStyleGetFlexShrink(identifier);
  }

  public void setFlexShrink(float flexShrink) {
    Yoga.YGNodeStyleSetFlexShrink(identifier, flexShrink);
  }

  public Value padding(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue =
          Yoga.YGNodeStyleGetPadding(identifier, edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setPadding(Edge edge, float padding) {
    this.setPadding(edge, padding, false);
  }

  public void setPadding(Edge edge, float padding, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetPadding(identifier, edge.value(), padding);
    } else {
      Yoga.YGNodeStyleSetPaddingPercent(identifier, edge.value(), padding);
    }
  }

  public Value margin(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue =
          Yoga.YGNodeStyleGetMargin(identifier, edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetMargin(Edge edge) {
    Yoga.YGNodeStyleSetMarginAuto(identifier, edge.value());
  }

  public void setMargin(Edge edge, float margin) {
    this.setMargin(edge, margin, false);
  }

  public void setMargin(Edge edge, float margin, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMargin(identifier, edge.value(), margin);
    } else {
      Yoga.YGNodeStyleSetMarginPercent(identifier, edge.value(), margin);
    }
  }

  public Value width() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetWidth(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetWidth() {
    Yoga.YGNodeStyleSetWidthAuto(identifier);
  }

  public void setWidth(float width) {
    this.setWidth(width, false);
  }

  public void setWidth(float width, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetWidth(identifier, width);
    } else {
      Yoga.YGNodeStyleSetWidthPercent(identifier, width);
    }
  }

  public Value minWidth() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMinWidth(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMinWidth(float minWidth) {
    this.setMinWidth(minWidth, false);
  }

  public void setMinWidth(float minWidth, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMinWidth(identifier, minWidth);
    } else {
      Yoga.YGNodeStyleSetMinWidthPercent(identifier, minWidth);
    }
  }

  public Value maxWidth() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMaxWidth(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMaxWidth(float maxWidth) {
    this.setMaxWidth(maxWidth, false);
  }

  public void setMaxWidth(float maxWidth, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMaxWidth(identifier, maxWidth);
    } else {
      Yoga.YGNodeStyleSetMaxWidthPercent(identifier, maxWidth);
    }
  }

  public Value height() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetHeight(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetHeight() {
    Yoga.YGNodeStyleSetHeightAuto(identifier);
  }

  public void setHeight(float height) {
    this.setHeight(height, false);
  }

  public void setHeight(float height, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetHeight(identifier, height);
    } else {
      Yoga.YGNodeStyleSetHeightPercent(identifier, height);
    }
  }

  public Value minHeight() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMinHeight(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMinHeight(float minHeight) {
    this.setMinHeight(minHeight, false);
  }

  public void setMinHeight(float minHeight, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMinHeight(identifier, minHeight);
    } else {
      Yoga.YGNodeStyleSetMinHeightPercent(identifier, minHeight);
    }
  }

  public Value maxHeight() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMaxHeight(identifier, YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMaxHeight(float maxHeight) {
    this.setMaxHeight(maxHeight, false);
  }

  public void setMaxHeight(float maxHeight, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMaxHeight(identifier, maxHeight);
    } else {
      Yoga.YGNodeStyleSetMaxHeightPercent(identifier, maxHeight);
    }
  }
}
