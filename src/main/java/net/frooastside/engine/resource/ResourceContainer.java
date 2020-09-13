package net.frooastside.engine.resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceContainer {

  private final Map<String, ResourceItem> content = new HashMap<>();

  public void load(File containerFile) throws IOException, ClassNotFoundException {
    if (containerFile.exists() && containerFile.isFile()) {
      ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(containerFile));
      Map<?, ?> rawContent = (Map<?, ?>) objectInputStream.readObject();
      for (Map.Entry<?, ?> entry : rawContent.entrySet()) {
        Object key = entry.getKey();
        if (key instanceof String) {
          Object value = entry.getValue();
          if (value instanceof ResourceItem) {
            content.put((String) key, (ResourceItem) value);
          }
        }
      }
      objectInputStream.close();
    }
  }

  public void save(File containerFile) throws IOException {
    if (!containerFile.exists()) {
      if (!containerFile.createNewFile()) {
        throw new IOException();
      }
    } else {
      if (containerFile.delete()) {
        if (!containerFile.createNewFile()) {
          throw new IOException();
        }
      } else {
        throw new IOException();
      }
    }
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(containerFile));
    objectOutputStream.writeObject(content);
    objectOutputStream.close();
  }

  public ResourceItem get(String key) {
    return content.get(key);
  }

  public void put(String key, ResourceItem item) {
    content.put(key, item);
  }

  public void clear() {
    content.clear();
  }

  public Map<String, ResourceItem> content() {
    return content;
  }
}
