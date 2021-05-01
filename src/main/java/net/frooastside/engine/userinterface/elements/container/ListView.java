package net.frooastside.engine.userinterface.elements.container;

import net.frooastside.engine.userinterface.Color;
import net.frooastside.engine.userinterface.constraints.*;
import net.frooastside.engine.userinterface.elements.Element;
import net.frooastside.engine.userinterface.elements.FunctionalElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListView<T> extends FunctionalElement {

  private final List<T> items = new ArrayList<>();
  private final Map<T, Item<T>> elements = new HashMap<>();

  private final RelativeConstraint scrollConstraint = new RelativeConstraint(0);

  private final TypeAdapter<T> typeAdapter;

  private final Color color;
  private final int offset;

  public ListView(TypeAdapter<T> typeAdapter, Color color, int offset) {
    this.typeAdapter = typeAdapter;
    this.color = color;
    this.offset = offset;
  }

  public void update(T t) {
    if(elements.containsKey(t)) {
      typeAdapter.update(t, elements.get(t));
    }
  }

  public void add(T t) {
    Panel panel = new Panel(color);

    ElementConstraints elementConstraints;
    if (items.isEmpty()) {
      Constraint verticalOffset = new PixelConstraint(this.offset);
      verticalOffset.setConstraintType(Constraint.ConstraintType.Y);
      elementConstraints = new ElementConstraints(
        new PixelConstraint(this.offset),
        new MathConstraint(MathConstraint.Operator.ADD, scrollConstraint, verticalOffset, false, false),
        new CenterConstraint(),
        typeAdapter.height(t));
      verticalOffset.initialize(elementConstraints, panel, this);
    }else {
      elementConstraints = new ElementConstraints(
        new PixelConstraint(this.offset),
        elements.get(items.get(items.size() - 1)).nextPosition(),
        new CenterConstraint(),
        typeAdapter.height(t));
    }

    addElement(panel, elementConstraints);
    Item<T> item = typeAdapter.create(t, panel);

    Constraint offset = new PixelConstraint(this.offset);
    offset.setConstraintType(Constraint.ConstraintType.X);
    offset.initialize(elementConstraints, item.element(), this);
    item.createNextConstraints(elementConstraints, offset);

    items.add(t);
    elements.put(t, item);
  }

  public void remove(T t) {
    int index = items.indexOf(t);
    items.remove(t);
    children().remove(elements.get(t).element());
    Constraint nextPosition = elements.remove(t).nextPosition();
    if(index > 0 && items.size() > index) {
      T next = items.get(index);
      Element nextElement = elements.get(next).element();
      nextElement.constraints().setY(nextPosition);
      nextElement.constraints().initialize(nextElement, this);
    }
  }

  public void scroll(float distance) {
    scrollConstraint.setValue(scrollConstraint.rawValue() - distance);
    recalculateBounds();
  }

  public List<T> items() {
    return items;
  }

  public static class Item<T> {

    private final Element element;

    private Constraint nextPosition;

    public Item(Element element) {
      this.element = element;
    }

    public void createNextConstraints(ElementConstraints constraints, Constraint offset) {
      nextPosition = new DoubleMathConstraint(DoubleMathConstraint.Operator.ADD, DoubleMathConstraint.Operator.ADD, constraints.y(), constraints.height(), offset, false, false, false);
    }

    public Constraint nextPosition() {
      return nextPosition;
    }

    public Element element() {
      return element;
    }
  }

  public interface TypeAdapter<T> {

    Item<T> create(T t, FunctionalElement rootElement);

    Constraint height(T t);

    void update(T t, Item<T> item);

  }

}
