package love.polardivision.engine.userinterface.events;

import love.polardivision.engine.userinterface.elements.Element;

public class Event {

  private Element caller;

  public Element caller() {
    return caller;
  }

  public Event caller(Element caller) {
    this.caller = caller;
    return this;
  }
}
