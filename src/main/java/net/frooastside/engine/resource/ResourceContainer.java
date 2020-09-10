package net.frooastside.engine.resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceContainer {

  private final Map<String, ResourceItem> contents = new HashMap<>();

  public void load(String containerPath) throws IOException, ClassNotFoundException {
    File containerFile = new File(containerPath);
    if(containerFile.exists() && containerFile.isFile()) {
      ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(containerFile));
      Map<?, ?> rawContent = (Map<?, ?>) objectInputStream.readObject();
      for(Map.Entry<?, ?> entry : rawContent.entrySet()) {
        Object key = entry.getKey();
        if(key instanceof String) {
          Object value = entry.getValue();
          if(value instanceof ResourceItem) {
            contents.put((String) key, (ResourceItem) value);
          }
        }
      }
      objectInputStream.close();
    }
  }

  public void save(String containerPath) throws IOException {
    File containerFile = new File(containerPath);
    if(!containerFile.exists()) {
      if(!containerFile.createNewFile()) {
        throw new IOException();
      }
    }else {
      if(containerFile.delete()) {
        if(!containerFile.createNewFile()) {
          throw new IOException();
        }
      }else {
        throw new IOException();
      }
    }
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(containerFile));
    objectOutputStream.writeObject(contents);
    objectOutputStream.close();
  }

  public ResourceItem get(String key) {
    return contents.get(key);
  }

  public void put(String key, ResourceItem item) {
    contents.put(key, item);
  }

  public void clear() {
    contents.clear();
  }

}
