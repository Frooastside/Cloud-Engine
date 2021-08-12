package net.frooastside.engine.userinterface.events;

import net.frooastside.engine.userinterface.elements.Element;

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
