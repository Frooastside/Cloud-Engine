module net.frooastside.engine {

  exports net.frooastside.engine.animation;
  exports net.frooastside.engine.camera;
  exports net.frooastside.engine.window;
  exports net.frooastside.engine.window.callbacks;
  exports net.frooastside.engine.graphicobjects;
  exports net.frooastside.engine.graphicobjects.framebuffer;
  exports net.frooastside.engine.graphicobjects.framebuffer.attachments;
  exports net.frooastside.engine.graphicobjects.texture;
  exports net.frooastside.engine.graphicobjects.vertexarray;
  exports net.frooastside.engine.graphicobjects.vertexarray.vertexbuffer;
  exports net.frooastside.engine.language;
  exports net.frooastside.engine.postprocessing;
  exports net.frooastside.engine.resource;
  exports net.frooastside.engine.resource.settings;
  exports net.frooastside.engine.shader;
  exports net.frooastside.engine.shader.uniforms;
  exports net.frooastside.engine.userinterface;
  exports net.frooastside.engine.userinterface.animation;
  exports net.frooastside.engine.userinterface.animation.transitions;
  exports net.frooastside.engine.userinterface.constraints;
  exports net.frooastside.engine.userinterface.elements;
  exports net.frooastside.engine.userinterface.elements.basic;
  exports net.frooastside.engine.userinterface.elements.container;
  exports net.frooastside.engine.userinterface.elements.render;
  exports net.frooastside.engine.userinterface.events;
  exports net.frooastside.engine.userinterface.renderer;

  requires java.desktop;

  requires transitive org.lwjgl;

  requires transitive org.lwjgl.assimp;
  requires transitive org.lwjgl.glfw;
  requires transitive org.lwjgl.nfd;
  requires transitive org.lwjgl.nuklear;
  requires transitive org.lwjgl.openal;
  requires transitive org.lwjgl.opengl;
  requires transitive org.lwjgl.opus;
  requires transitive org.lwjgl.stb;
  requires transitive org.lwjgl.vulkan;
  requires transitive org.lwjgl.yoga;

  requires transitive org.joml;

}