package love.polardivision.engine.shader;

public record ShaderConstant(String name, String value) {

  private static final String PLACEHOLDER = "/*--%s--*/";

  @Override
  public String name() {
    return String.format(PLACEHOLDER, name);
  }
}
