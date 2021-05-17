package net.frooastside.engine.userinterface;

import net.frooastside.engine.userinterface.elements.Element;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Deque;

public class CloudInterfaceReader {

  public static Screen read(File file) throws FileNotFoundException, XMLStreamException {
    XMLInputFactory inputFactory = XMLInputFactory.newFactory();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new FileInputStream(file));
    Deque<Element> queue = new ArrayDeque<>();
    eventReader.nextEvent();
    while (eventReader.hasNext()) {
      XMLEvent event = eventReader.nextEvent();
      if (event.isStartElement()) {
        processStartElement(event.asStartElement(), queue);
      } else if (event.isEndElement()) {
        EndElement endElement = event.asEndElement();
        queue.pollLast();
      }
    }
    return null;
  }

  private static void processStartElement(StartElement startElement, Deque<Element> queue) {
    String elementName = startElement.getName().getLocalPart();
    Element element;
    switch (elementName) {
      case "interface":
        element = processInterface(startElement);
        break;
      case "panel":
        element = processPanel(startElement);
        break;
      default:
        throw new IllegalArgumentException(elementName);
    }
    queue.offer(element);
  }

  private static Element processInterface(StartElement startElement) {
    //Screen screen = new Screen();
    //return screen;
    return null;
  }

  private static Element processPanel(StartElement startElement) {
    return null;
  }

  //private static

}
