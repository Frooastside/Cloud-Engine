/*
 * Copyright © 2022-2023 @Frooastside
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package love.polardivision.engine.userinterface.elements;

import love.polardivision.engine.userinterface.Screen;
import love.polardivision.engine.userinterface.events.*;
import love.polardivision.engine.utils.NativeObject;
import love.polardivision.engine.window.Key;
import love.polardivision.engine.window.Window;
import love.polardivision.engine.window.callbacks.KeyCallback;
import love.polardivision.engine.ygwrapper.*;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.yoga.YGLayout;
import org.lwjgl.util.yoga.YGNode;
import org.lwjgl.util.yoga.YGValue;
import org.lwjgl.util.yoga.Yoga;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionalElement extends Element implements NativeObject {

  private final NodeType nodeType;
  private YGNode node;

  private Alignment alignSelf = Alignment.AUTO;
  private Alignment alignItems = Alignment.AUTO;
  private Alignment alignContent = Alignment.AUTO;
  private Direction direction = Direction.COLUMN;
  private Display display = Display.FLEX;
  private Justify justifyContent = Justify.START;
  private Overflow overflow = Overflow.VISIBLE;
  private PositionType positionType = PositionType.STATIC;
  private Wrap wrap = Wrap.NO_WRAP;

  private RenderElement background;

  private Screen root;
  private FunctionalElement parent;
  private final List<Element> children = new ArrayList<>();


  private String clickEventTarget;
  private String selectEventTarget;

  protected FunctionalElement(NodeType nodeType) {
    this.nodeType = nodeType;
  }

  @Override
  public void initialize() {
    node = YGNode.create(Yoga.YGNodeNew());
    node.nodeType(nodeType.value());
    if (background != null && background instanceof NativeObject nativeBackground) {
      nativeBackground.initialize();
    }
  }

  @Override
  public void delete() {
    Yoga.YGNodeFree(node.address());
    node.free();
    if (background != null && background instanceof NativeObject nativeBackground) {
      nativeBackground.delete();
    }
    for (Element element : children) {
      if (element instanceof FunctionalElement functionalElement) {
        functionalElement.delete();
      }
    }
  }

  @Override
  public void update(double delta) {
    if (background != null) {
      background.update(delta);
    }
    for (Element element : children) {
      if (element != null) {
        element.update(delta);
      }
    }
  }

  @Override
  public void updatePixelSize(Vector2f pixelSize) {
    super.updatePixelSize(pixelSize);
    if (background != null) {
      background.updatePixelSize(pixelSize);
    }
    for (Element element : children) {
      if (element != null) {
        element.updatePixelSize(pixelSize);
      }
    }
  }

  public RenderElement background() {
    return background;
  }

  public void setBackground(RenderElement background) {
    this.background = background;
  }

  public void calculateChildBounds() {
    if (background != null) {
      background.bounds().set(this.bounds());
    }
  }

  public void calculateBounds() {
    YGLayout layout = node.layout();
    if (node.hasNewLayout()) {
      float x = layout.positions(0);
      float y = layout.positions(1);
      float width = layout.dimensions(0);
      float height = layout.dimensions(1);
      bounds().set(x * pixelSize().x, y * pixelSize().y, width * pixelSize().x, height * pixelSize().y);
    }
  }

  public void handleKey(Window window, Key key, int scancode, int modifiers, KeyCallback.Action action) {
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        ((FunctionalElement) element).handleKey(window, key, scancode, modifiers, action);
      }
    }
  }

  public void handleChar(Window window, char codepoint) {
    for (Element element : children()) {
      if (element instanceof FunctionalElement) {
        ((FunctionalElement) element).handleChar(window, codepoint);
      }
    }
  }

  public Element scroll(ScrollEvent event) {
    if (event.inside()) {
      for (Element element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof FunctionalElement basicElement) {
            if (basicElement.scrollable()) {
              if (((ScrollEvent.Handler) basicElement).handleScroll(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              } else {
                return basicElement.scroll(event);
              }
            } else if (basicElement.selectable()) {
              return basicElement;
            } else {
              return basicElement.scroll(event);
            }
          }
        } else {
          if (element instanceof FunctionalElement basicElement) {
            ScrollEvent outsideScrollEvent = new ScrollEvent(false, event.x(), event.y(), event.scrollX(), event.scrollY());
            basicElement.scroll(outsideScrollEvent);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(outsideScrollEvent);
            }
          }
        }
      }
    } else {
      for (Element element : children) {
        if (element instanceof FunctionalElement basicElement) {
          if (!element.isPixelInside(event.x(), event.y())) {
            basicElement.scroll(event);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(event);
            }
          } else {
            ScrollEvent insideScrollEvent = new ScrollEvent(true, event.x(), event.y(), event.scrollX(), event.scrollY());
            basicElement.scroll(insideScrollEvent);
            if (basicElement.scrollable()) {
              ((ScrollEvent.Handler) basicElement).handleScroll(insideScrollEvent);
            }
          }
        }
      }
    }
    return null;
  }

  public Element click(ClickEvent event) {
    if (event.inside()) {
      for (Element element : children) {
        if (element.isPixelInside(event.x(), event.y())) {
          if (element instanceof FunctionalElement basicElement) {
            if (basicElement.clickable()) {
              if (((ClickEvent.Handler) basicElement).handleClick(event)) {
                if (basicElement.selectable()) {
                  return element;
                }
              } else {
                return basicElement.click(event);
              }
            } else if (basicElement.selectable()) {
              return basicElement;
            } else {
              return basicElement.click(event);
            }
          }
        } else {
          if (element instanceof FunctionalElement basicElement) {
            ClickEvent outsideClickEvent = new ClickEvent(event.key(), false, event.pressed(), event.x(), event.y());
            basicElement.click(outsideClickEvent);
            if (basicElement.clickable()) {
              ((ClickEvent.Handler) basicElement).handleClick(outsideClickEvent);
            }
          }
        }
      }
    } else {
      for (Element element : children) {
        if (element instanceof FunctionalElement basicElement) {
          if (!element.isPixelInside(event.x(), event.y())) {
            basicElement.click(event);
            if (basicElement.clickable()) {
              ((ClickEvent.Handler) basicElement).handleClick(event);
            }
          } else {
            ClickEvent insideClickEvent = new ClickEvent(event.key(), true, event.pressed(), event.x(), event.y());
            basicElement.click(insideClickEvent);
            if (basicElement.clickable()) {
              ((ClickEvent.Handler) basicElement).handleClick(insideClickEvent);
            }
          }
        }
      }
    }
    return null;
  }

  @Override
  public void display(boolean show, float parentDelay) {
    super.display(show, parentDelay);
    if (background != null) {
      background.display(show, parentDelay);
    }
    for (Element element : children) {
      if (element != null) {
        element.display(show, parentDelay);
      }
    }
  }

  public void emitEvent(Event event, Class<? extends EventHandler> type, String targets) {
    root().emitEvent(event, type, targets);
  }

  public void addElement(Element element) {
    addElement(element, Yoga.YGNodeGetChildCount(node.address()));
  }

  public void addElement(Element element, int index) {
    children.add(element);
    element.updatePixelSize(pixelSize());
    if (element instanceof FunctionalElement functionalElement) {
      Yoga.YGNodeInsertChild(node.address(), functionalElement.node().address(), index);
      functionalElement.setRoot(root());
      functionalElement.setParent(this);
      functionalElement.calculateBounds();
    }
  }

  public void removeElement(Element element) {
    if (children.contains(element)) {
      children.remove(element);
      if (element instanceof FunctionalElement functionalElement) {
        Yoga.YGNodeRemoveChild(node.address(), functionalElement.node().address());
      }
    }
  }

  public YGNode node() {
    return node;
  }

  public NodeType nodeType() {
    return nodeType;
  }

  public Alignment alignSelf() {
    return alignSelf;
  }

  public void alignSelf(Alignment alignment) {
    if (this.alignSelf != alignment) {
      this.alignSelf = alignment;
      Yoga.YGNodeStyleSetAlignSelf(node.address(), alignment.value());
    }
  }

  public Alignment alignItems() {
    return alignItems;
  }

  public void alignItems(Alignment alignment) {
    if (this.alignItems != alignment) {
      this.alignItems = alignment;
      Yoga.YGNodeStyleSetAlignItems(node.address(), alignment.value());
    }
  }

  public Alignment alignContent() {
    return alignContent;
  }

  public void alignContent(Alignment alignment) {
    if (this.alignContent != alignment) {
      this.alignContent = alignment;
      Yoga.YGNodeStyleSetAlignContent(node.address(), alignment.value());
    }
  }

  public Direction direction() {
    return direction;
  }

  public void setDirection(Direction direction) {
    if (this.direction != direction) {
      this.direction = direction;
      Yoga.YGNodeStyleSetFlexDirection(node.address(), direction.value());
    }
  }

  public Display display() {
    return display;
  }

  public void setDisplay(Display display) {
    if (this.display != display) {
      this.display = display;
      Yoga.YGNodeStyleSetDisplay(node.address(), display.value());
    }
  }

  public Justify justifyContent() {
    return justifyContent;
  }

  public void justifyContent(Justify justify) {
    if (this.justifyContent != justify) {
      this.justifyContent = justify;
      Yoga.YGNodeStyleSetJustifyContent(node.address(), justify.value());
    }
  }

  public Overflow overflow() {
    return overflow;
  }

  public void setOverflow(Overflow overflow) {
    if (this.overflow != overflow) {
      this.overflow = overflow;
      Yoga.YGNodeStyleSetOverflow(node.address(), overflow.value());
    }
  }

  public PositionType positionType() {
    return positionType;
  }

  public void setPositionType(PositionType positionType) {
    if (this.positionType != positionType) {
      this.positionType = positionType;
      Yoga.YGNodeStyleSetPositionType(node.address(), positionType.value());
    }
  }

  public Wrap wrap() {
    return wrap;
  }

  public void setWrap(Wrap wrap) {
    if (this.wrap != wrap) {
      this.wrap = wrap;
      Yoga.YGNodeStyleSetFlexWrap(node.address(), wrap.value());
    }
  }

  public Value position(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetPosition(node.address(), edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setPosition(Edge edge, float position) {
    this.setPosition(edge, position, false);
  }

  public void setPosition(Edge edge, float position, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetPosition(node.address(), edge.value(), position);
    } else {
      Yoga.YGNodeStyleSetPositionPercent(node.address(), edge.value(), position);
    }
  }

  public float aspectRatio() {
    return Yoga.YGNodeStyleGetAspectRatio(node.address());
  }

  public void setAspectRatio(float aspectRatio) {
    Yoga.YGNodeStyleSetAspectRatio(node.address(), aspectRatio);
  }

  public float border(Edge edge) {
    return Yoga.YGNodeStyleGetBorder(node.address(), edge.value());
  }

  public void setBorder(Edge edge, float border) {
    Yoga.YGNodeStyleSetBorder(node.address(), edge.value(), border);
  }

  public Value flexBasis() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetFlexBasis(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetFlexBasis() {
    Yoga.YGNodeStyleSetFlexBasisAuto(node.address());
  }

  public void setFlexBasis(float flexBasis) {
    this.setFlexBasis(flexBasis, false);
  }

  public void setFlexBasis(float flexBasis, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetFlexBasis(node.address(), flexBasis);
    } else {
      Yoga.YGNodeStyleSetFlexBasisPercent(node.address(), flexBasis);
    }
  }

  public float flexGrow() {
    return Yoga.YGNodeStyleGetFlexGrow(node.address());
  }

  public void setFlexGrow(float flexGrow) {
    Yoga.YGNodeStyleSetFlexGrow(node.address(), flexGrow);
  }

  public float flexShrink() {
    return Yoga.YGNodeStyleGetFlexShrink(node.address());
  }

  public void setFlexShrink(float flexShrink) {
    Yoga.YGNodeStyleSetFlexShrink(node.address(), flexShrink);
  }

  public Value padding(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetPadding(node.address(), edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setPadding(Edge edge, float padding) {
    this.setPadding(edge, padding, false);
  }

  public void setPadding(Edge edge, float padding, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetPadding(node.address(), edge.value(), padding);
    } else {
      Yoga.YGNodeStyleSetPaddingPercent(node.address(), edge.value(), padding);
    }
  }

  public Value margin(Edge edge) {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMargin(node.address(), edge.value(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetMargin(Edge edge) {
    Yoga.YGNodeStyleSetMarginAuto(node.address(), edge.value());
  }

  public void setMargin(Edge edge, float margin) {
    this.setMargin(edge, margin, false);
  }

  public void setMargin(Edge edge, float margin, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMargin(node.address(), edge.value(), margin);
    } else {
      Yoga.YGNodeStyleSetMarginPercent(node.address(), edge.value(), margin);
    }
  }

  public Value width() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetWidth(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetWidth() {
    Yoga.YGNodeStyleSetWidthAuto(node.address());
  }

  public void setWidth(float width) {
    this.setWidth(width, false);
  }

  public void setWidth(float width, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetWidth(node.address(), width);
    } else {
      Yoga.YGNodeStyleSetWidthPercent(node.address(), width);
    }
  }

  public Value minWidth() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMinWidth(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMinWidth(float minWidth) {
    this.setMinWidth(minWidth, false);
  }

  public void setMinWidth(float minWidth, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMinWidth(node.address(), minWidth);
    } else {
      Yoga.YGNodeStyleSetMinWidthPercent(node.address(), minWidth);
    }
  }

  public Value maxWidth() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMaxWidth(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMaxWidth(float maxWidth) {
    this.setMaxWidth(maxWidth, false);
  }

  public void setMaxWidth(float maxWidth, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMaxWidth(node.address(), maxWidth);
    } else {
      Yoga.YGNodeStyleSetMaxWidthPercent(node.address(), maxWidth);
    }
  }

  public Value height() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetHeight(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void resetHeight() {
    Yoga.YGNodeStyleSetHeightAuto(node.address());
  }

  public void setHeight(float height) {
    this.setHeight(height, false);
  }

  public void setHeight(float height, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetHeight(node.address(), height);
    } else {
      Yoga.YGNodeStyleSetHeightPercent(node.address(), height);
    }
  }

  public Value minHeight() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMinHeight(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMinHeight(float minHeight) {
    this.setMinHeight(minHeight, false);
  }

  public void setMinHeight(float minHeight, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMinHeight(node.address(), minHeight);
    } else {
      Yoga.YGNodeStyleSetMinHeightPercent(node.address(), minHeight);
    }
  }

  public Value maxHeight() {
    try (MemoryStack memoryStack = MemoryStack.stackPush()) {
      YGValue ygValue = Yoga.YGNodeStyleGetMaxHeight(node.address(), YGValue.malloc(memoryStack));
      return new Value(Unit.unitOf(ygValue.unit()), ygValue.value());
    }
  }

  public void setMaxHeight(float maxHeight) {
    this.setMaxHeight(maxHeight, false);
  }

  public void setMaxHeight(float maxHeight, boolean percent) {
    if (!percent) {
      Yoga.YGNodeStyleSetMaxHeight(node.address(), maxHeight);
    } else {
      Yoga.YGNodeStyleSetMaxHeightPercent(node.address(), maxHeight);
    }
  }

  public Screen root() {
    return root;
  }

  public void setRoot(Screen root) {
    this.root = root;
  }

  public Element parent() {
    return parent;
  }

  public void setParent(FunctionalElement parent) {
    this.parent = parent;
  }

  public List<Element> children() {
    return children;
  }

  public boolean selectable() {
    return this instanceof SelectionEvent.Handler;
  }

  public boolean clickable() {
    return this instanceof ClickEvent.Handler;
  }

  public boolean scrollable() {
    return this instanceof ScrollEvent.Handler;
  }

  public String clickEventTarget() {
    return clickEventTarget;
  }

  public void setClickEventTarget(String clickEventTarget) {
    this.clickEventTarget = clickEventTarget;
  }

  public String selectEventTarget() {
    return selectEventTarget;
  }

  public void setSelectEventTarget(String selectEventTarget) {
    this.selectEventTarget = selectEventTarget;
  }

}
