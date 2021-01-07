package test;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VulkanTest {

  private static ByteBuffer[] layers = {
    MemoryUtil.memUTF8("VK_LAYER_LUNARG_standard_validation")
  };

  public static void main(String[] args) {
  }

  private static void checkStatusCode(int statusCode) {
    if (statusCode != VK10.VK_SUCCESS)
      throw new AssertionError("An error occurred with error code " + statusCode);
  }

  private static VkInstance createVkInstance() {
    VkApplicationInfo applicationInfo = VkApplicationInfo.calloc()
      .sType(VK10.VK_STRUCTURE_TYPE_APPLICATION_INFO)
      .apiVersion(VK10.VK_API_VERSION_1_0);

    VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo.calloc()
      .sType(VK10.VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
      .pApplicationInfo(applicationInfo)
      .ppEnabledExtensionNames(null)
      .ppEnabledLayerNames(null);

    PointerBuffer instancePointerBuffer = MemoryUtil.memAllocPointer(1);
    int statusCode = VK10.vkCreateInstance(instanceCreateInfo, null, instancePointerBuffer);
    long instanceAddress = instancePointerBuffer.get(0);
    MemoryUtil.memFree(instancePointerBuffer);
    checkStatusCode(statusCode);

    VkInstance vkInstance = new VkInstance(instanceAddress, instanceCreateInfo);

    instanceCreateInfo.free();
    applicationInfo.free();

    return vkInstance;
  }

  private static VkPhysicalDevice getFirstPhysicalDevice(VkInstance vkInstance) {
    IntBuffer physicalDeviceCountPointer = MemoryUtil.memAllocInt(1);
    int statusCode = VK10.vkEnumeratePhysicalDevices(vkInstance, physicalDeviceCountPointer, null);
    checkStatusCode(statusCode);

    PointerBuffer physicalDevicesPointer = MemoryUtil.memAllocPointer(physicalDeviceCountPointer.get(0));
    statusCode = VK10.vkEnumeratePhysicalDevices(vkInstance, physicalDeviceCountPointer, physicalDevicesPointer);
    checkStatusCode(statusCode);

    long physicalDeviceAddress = physicalDevicesPointer.get(0);

    MemoryUtil.memFree(physicalDeviceCountPointer);
    MemoryUtil.memFree(physicalDevicesPointer);

    return new VkPhysicalDevice(physicalDeviceAddress, vkInstance);
  }

  private static class DeviceAndGraphicsQueueFamily {

    VkDevice device;
    int queueFamilyIndex;
    VkPhysicalDeviceMemoryProperties memoryProperties;

  }

  private static DeviceAndGraphicsQueueFamily createDeviceAndGetGraphicsQueueFamily(VkPhysicalDevice vkPhysicalDevice) {
    IntBuffer queueFamilyPropertyCountPointer = MemoryUtil.memAllocInt(1);

    VK10.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, queueFamilyPropertyCountPointer, null);
    int queueCount = queueFamilyPropertyCountPointer.get(0);

    VkQueueFamilyProperties.Buffer queueFamilyProperties = VkQueueFamilyProperties.calloc(queueCount);
    VK10.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, queueFamilyPropertyCountPointer, queueFamilyProperties);

    MemoryUtil.memFree(queueFamilyPropertyCountPointer);

    int graphicsQueueFamilyIndex;
    for (graphicsQueueFamilyIndex = 0; graphicsQueueFamilyIndex < queueCount; graphicsQueueFamilyIndex++) {
      if ((queueFamilyProperties.get(graphicsQueueFamilyIndex).queueFlags() & VK10.VK_QUEUE_GRAPHICS_BIT) != 0)
        break;
    }

    queueFamilyProperties.free();
    FloatBuffer queuePriorities = MemoryUtil.memAllocFloat(1).put(0.0f);
    queuePriorities.flip();

    VkDeviceQueueCreateInfo.Buffer queueCreateInfo = VkDeviceQueueCreateInfo.calloc(1)
      .sType(VK10.VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO)
      .queueFamilyIndex(graphicsQueueFamilyIndex)
      .pQueuePriorities(queuePriorities);

    PointerBuffer extensions = MemoryUtil.memAllocPointer(1);
    ByteBuffer VK_KHR_SWAPCHAIN_EXTENSION = MemoryUtil.memUTF8(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME);
    extensions.put(VK_KHR_SWAPCHAIN_EXTENSION);
    extensions.flip();

    PointerBuffer enabledLayerNamesPointer = MemoryUtil.memAllocPointer(layers.length);
    for (ByteBuffer layer : layers) {
      enabledLayerNamesPointer.put(layer);
    }
    enabledLayerNamesPointer.flip();

    VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.calloc()
      .sType(VK10.VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO)
      .pQueueCreateInfos(queueCreateInfo)
      .ppEnabledExtensionNames(extensions)
      .ppEnabledLayerNames(enabledLayerNamesPointer);

    PointerBuffer devicePointer = MemoryUtil.memAllocPointer(1);
    int statusCode = VK10.vkCreateDevice(vkPhysicalDevice, deviceCreateInfo, null, devicePointer);
    checkStatusCode(statusCode);
    long deviceAddress = devicePointer.get(0);
    MemoryUtil.memFree(devicePointer);
    VkPhysicalDeviceMemoryProperties memoryProperties = VkPhysicalDeviceMemoryProperties.calloc();
    VK10.vkGetPhysicalDeviceMemoryProperties(vkPhysicalDevice, memoryProperties);

    DeviceAndGraphicsQueueFamily deviceAndGraphicsQueueFamily = new DeviceAndGraphicsQueueFamily();
    deviceAndGraphicsQueueFamily.device = new VkDevice(deviceAddress, vkPhysicalDevice, deviceCreateInfo);
    deviceAndGraphicsQueueFamily.queueFamilyIndex = graphicsQueueFamilyIndex;
    deviceAndGraphicsQueueFamily.memoryProperties = memoryProperties;

    deviceCreateInfo.free();
    MemoryUtil.memFree(enabledLayerNamesPointer);
    MemoryUtil.memFree(VK_KHR_SWAPCHAIN_EXTENSION);
    MemoryUtil.memFree(extensions);
    MemoryUtil.memFree(queuePriorities);
    return deviceAndGraphicsQueueFamily;
  }

}
