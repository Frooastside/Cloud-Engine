package net.frooastside.engine.shader;

public class ShaderConstant {

  public static final String PLACEHOLDER = "/*--%s--*/";

  private final String name;
  private final String value;

  public ShaderConstant(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String name() {
    return String.format(PLACEHOLDER, name);
  }

  public String value() {
    return value;
  }
}
