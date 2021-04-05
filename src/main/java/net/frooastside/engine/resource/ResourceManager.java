package net.frooastside.engine.resource;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.frooastside.engine.resource.settings.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResourceManager extends Application {

  private static final FileChooser RESOURCE_CONTAINER_FILE_CHOOSER = createResourceContainerFileChooser();
  private static final FileChooser TEXTURE_FILE_CHOOSER = createTextureFileChooser();
  private static final FileChooser FONT_FILE_CHOOSER = createFontFileChooser();

  public static final SettingsCreator FONT_SETTINGS_LAYOUT = SettingsCreator.createLayout(
    new ComboBoxSetting<>("imageSize", Arrays.asList(256, 512, 1024, 2048, 4096, 8192, 16384, 32768), 16384),
    new IntegerSpinnerSetting("downscale", 1, 32, 4),
    new IntegerSpinnerSetting("spread", 1, 128, 32),
    new ComboBoxSetting<>("padding", Arrays.asList(0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512), 64),
    new ComboBoxSetting<>("firstCharacter", Arrays.asList(0, 32), 32),
    new ComboBoxSetting<>("characterCount", Arrays.asList(224, 352, 560, 255, 383, 591), 352),
    new IntegerTextField("characterHeight", 1536));

  public static final SettingsCreator DEFAULT_SETTINGS_LAYOUT = SettingsCreator.createLayout();

  private static final Map<ResourceItem, Node> settingBoxes = new HashMap<>();
  private static final Map<ResourceItem, Map<String, Node>> settingsMap = new HashMap<>();

  private Stage primaryStage;

  private final ResourceContainer currentResourceContainer = new ResourceContainer();
  private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private File currentFile;
  private boolean unsavedChanges;

  private ListView<String> resourceContainerItems;
  private BorderPane mainBorderPane;

  public static void launchResourceManager(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.initStyle(StageStyle.DECORATED);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Resource Manager");
    primaryStage.setScene(createMainScene());
    primaryStage.setOnCloseRequest(windowEvent -> {
      if (!close()) {
        windowEvent.consume();
      }
    });
    primaryStage.show();
  }

  public Scene createMainScene() {
    mainBorderPane = new BorderPane();
    mainBorderPane.setTop(createMainMenuBar());
    mainBorderPane.setLeft(createLeftVBox());
    mainBorderPane.setRight(createRightVBox());
    mainBorderPane.setBottom(createBottomMenuBar());
    return new Scene(mainBorderPane, 940, 540);
  }

  private MenuBar createMainMenuBar() {
    MenuBar menuBar = new MenuBar();

    Menu file = new Menu("File");

    MenuItem open = new MenuItem("Open Resource Container");
    open.setOnAction(this::onMenuOpen);
    Menu recentFilesMenu = new Menu("Recent Files");
    SeparatorMenuItem recentFilesMenuSeparator = new SeparatorMenuItem();

    MenuItem save = new MenuItem("Save");
    save.setOnAction(this::onMenuSave);
    MenuItem saveAs = new MenuItem("Save As...");
    saveAs.setOnAction(this::onMenuSaveAs);
    SeparatorMenuItem saveAsSeparator = new SeparatorMenuItem();

    MenuItem exit = new MenuItem("Exit");
    exit.setOnAction(actionEvent -> close());

    file.getItems().addAll(open, recentFilesMenu, recentFilesMenuSeparator,
      save, saveAs, saveAsSeparator,
      exit);

    menuBar.getMenus().addAll(file);
    return menuBar;
  }

  private void onMenuOpen(ActionEvent actionEvent) {
    if (askToSaveCurrentFileIfNeeded(true)) {
      File selectedFile = RESOURCE_CONTAINER_FILE_CHOOSER.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        openFile(selectedFile);
      }
    }
  }

  private void onMenuSave(ActionEvent actionEvent) {
    askToSaveCurrentFileIfNeeded(true);
  }

  private void onMenuSaveAs(ActionEvent actionEvent) {
    if (askToSaveCurrentFileIfNeeded(false)) {
      File selectedFile = RESOURCE_CONTAINER_FILE_CHOOSER.showSaveDialog(primaryStage);
      if (selectedFile != null) {
        saveFile(selectedFile);
      }
    }
  }

  private MenuBar createBottomMenuBar() {
    MenuBar menuBar = new MenuBar();

    Menu add = new Menu("Add");

    MenuItem texture = new MenuItem("Texture");
    texture.setOnAction(this::onBottomMenuAddTexture);
    MenuItem font = new MenuItem("Font");
    font.setOnAction(this::onBottomMenuAddFont);

    add.getItems().addAll(texture, font);

    menuBar.getMenus().addAll(add);

    return menuBar;
  }

  private void onBottomMenuAddTexture(ActionEvent actionEvent) {
    List<File> selectedFiles = TEXTURE_FILE_CHOOSER.showOpenMultipleDialog(primaryStage);
    if (selectedFiles != null && !selectedFiles.isEmpty()) {
      selectedFiles.forEach(this::addTexture);
    }
  }

  private void onBottomMenuAddFont(ActionEvent actionEvent) {
    List<File> selectedFiles = FONT_FILE_CHOOSER.showOpenMultipleDialog(primaryStage);
    if (selectedFiles != null && !selectedFiles.isEmpty()) {
      selectedFiles.forEach(this::addFont);
    }
  }

  private VBox createLeftVBox() {
    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.setPadding(new Insets(20));

    resourceContainerItems = new ListView<>();
    resourceContainerItems.setOnMouseClicked(this::resourceContainerItemsClicked);

    vBox.getChildren().addAll(resourceContainerItems);
    return vBox;
  }

  private void resourceContainerItemsClicked(MouseEvent mouseEvent) {
    if (resourceContainerItems.getSelectionModel().getSelectedItem() != null) {
      ResourceItem resourceItem = currentResourceContainer.get(resourceContainerItems.getSelectionModel().getSelectedItem());
      mainBorderPane.setCenter(createInformationBox(resourceItem));
    }
  }

  private VBox createRightVBox() {
    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.setPadding(new Insets(20));
    vBox.setSpacing(20);
    Button editButton = new Button("Edit");
    editButton.setOnMouseClicked(event -> {
      String selectedItem = resourceContainerItems.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        createConfigurationStage(selectedItem, currentResourceContainer.get(selectedItem)).show();
      }
    });
    Button removeButton = new Button("Remove");
    removeButton.setOnMouseClicked(event -> {
      String selectedItem = resourceContainerItems.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        currentResourceContainer.remove(selectedItem);
        reload();
      }
    });
    vBox.getChildren().addAll(editButton, removeButton);
    return vBox;
  }

  private void addTexture(File file) {
    try {
      addItem(file.getName(), new ResourceTexture(BufferUtils.readFile(file)));
    } catch (IOException exception) {
      showErrorAlert("An error occurred while reading the texture file: " + exception.getLocalizedMessage());
    }
  }

  private void addFont(File file) {
    try {
      addItem(file.getName(), new ResourceFont(BufferUtils.readFile(file)));
    } catch (IOException exception) {
      showErrorAlert("An error occurred while reading the font file: " + exception.getLocalizedMessage());
    }
  }

  private void addItem(String key, ResourceItem item) {
    createConfigurationStage(key, item).show();
  }

  private Stage createConfigurationStage(String key, ResourceItem item) {
    Stage configurationStage = new Stage();
    configurationStage.initStyle(StageStyle.DECORATED);
    configurationStage.setResizable(false);
    configurationStage.setTitle("Settings");
    TextField nameField = new TextField();
    nameField.setText(key);
    Button acceptButton = new Button("Hinzufügen");
    acceptButton.setOnMouseClicked(event -> {
      String nameFieldText = nameField.getText();
      if (!nameFieldText.isEmpty()) {
        setValues(item);
        executorService.execute(() -> item.loadUnspecific(executorService));
        currentResourceContainer.remove(key);
        currentResourceContainer.put(nameFieldText, item);
        reload();
        unsavedChanges = true;
        configurationStage.close();
        if(item.progress() != -1) {
          createProgressBarStage(item);
        }
      }
    });
    BorderPane borderPane = new BorderPane();
    borderPane.setPadding(new Insets(10));
    borderPane.setTop(nameField);
    borderPane.setCenter(settingsBoxFor(item));
    borderPane.setBottom(acceptButton);
    Scene scene = new Scene(borderPane, 320, 480);
    configurationStage.setScene(scene);
    return configurationStage;
  }

  private void createProgressBarStage(ResourceItem item) {
    Stage progressStage = new Stage();
    progressStage.initStyle(StageStyle.DECORATED);
    progressStage.setResizable(false);
    progressStage.setTitle("Creating...");
    BorderPane borderPane = new BorderPane();
    borderPane.setPadding(new Insets(10));
    ProgressBar progressBar = new ProgressBar();
    borderPane.setCenter(progressBar);
    Scene scene = new Scene(borderPane, 320, 120);
    progressStage.setScene(scene);
    progressStage.show();
    Thread updateThread = new Thread(() -> {
      while(item.progress() < 1) {
        if(item.progress() != 0) {
          Platform.runLater(() -> progressBar.setProgress(item.progress()));
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException exception) {
          exception.printStackTrace();
        }
      }
      Platform.runLater(progressStage::close);
    });
    updateThread.start();
  }

  private void showErrorAlert(String message) {
    Alert alert = new Alert(
      Alert.AlertType.ERROR,
      message,
      ButtonType.OK);
    alert.setTitle("Error!");
    System.err.println(message);
    alert.showAndWait();
  }

  private void reload() {
    resourceContainerItems.getItems().clear();
    for (String key : currentResourceContainer.content().keySet()) {
      resourceContainerItems.getItems().add(key);
    }
  }

  private boolean close() {
    if (askToSaveCurrentFileIfNeeded(true)) {
      primaryStage.close();
      executorService.shutdown();
      return true;
    } else {
      return false;
    }
  }

  private void openFile(File file) {
    try {
      currentResourceContainer.clear();
      currentResourceContainer.load(file);
      for (Map.Entry<String, ResourceItem> entry : currentResourceContainer.content().entrySet()) {
        executorService.execute(() -> entry.getValue().loadUnspecific(executorService));
      }
      reload();
      currentFile = file;
      unsavedChanges = false;
    } catch (IOException | ClassNotFoundException exception) {
      showErrorAlert("An error occurred while opening the file: " + exception.getLocalizedMessage());
    }
  }

  private void saveFile(File file) {
    try {
      currentResourceContainer.save(file);
      currentFile = file;
      unsavedChanges = false;
    } catch (IOException exception) {
      showErrorAlert("An error occurred while saving the file: " + exception.getLocalizedMessage());
      exception.printStackTrace();
    }
  }

  private boolean askToSaveCurrentFile() {
    Alert alert = new Alert(
      Alert.AlertType.NONE,
      "You have modified the File but didn't saved the changes. Do you want to save?",
      ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    alert.setTitle("Unsaved Changes!");
    AtomicBoolean returnValue = new AtomicBoolean();
    alert.showAndWait().ifPresent(buttonType -> {
      if (buttonType == ButtonType.YES) {
        if (currentFile != null) {
          saveFile(currentFile);
          returnValue.set(true);
        } else {
          File selectedFile = RESOURCE_CONTAINER_FILE_CHOOSER.showSaveDialog(primaryStage);
          if (selectedFile != null) {
            saveFile(selectedFile);
            returnValue.set(true);
          } else {
            returnValue.set(false);
          }
        }
      } else if (buttonType == ButtonType.NO) {
        returnValue.set(true);
      }
    });
    return returnValue.get();
  }

  private boolean askToSaveCurrentFileIfNeeded(boolean askForFileIfNull) {
    if (unsavedChanges) {
      if (currentFile != null) {
        return askToSaveCurrentFile();
      } else {
        if (askForFileIfNull) {
          return askToSaveCurrentFile();
        }
      }
    }
    return true;
  }

  private boolean checkValues(ResourceItem resourceItem) {
    if(resourceItem instanceof ResourceFont) {
      ResourceFont font = (ResourceFont) resourceItem;
      return font.imageSize() == 0
        && font.downscale() == 0
        && font.spread() == 0
        && font.padding() == 0
        && font.firstCharacter() == 0
        && font.characterCount() == 0;
    }else if(resourceItem instanceof ResourceTexture) {
      return false;
    }else {
      return false;
    }
  }

  private void setValues(ResourceItem resourceItem) {
    Map<String, Node> settings = settingsFor(resourceItem);
    if(resourceItem instanceof ResourceFont) {
      ResourceFont font = (ResourceFont) resourceItem;
      Object imageSize = Setting.getComboBoxItem(settings, "imageSize");
      if (imageSize != null) {
        font.setImageSize((int) imageSize);
      }
      font.setDownscale(Setting.getSpinnerInteger(settings, "downscale"));
      font.setSpread(Setting.getSpinnerInteger(settings, "spread"));
      Object padding = Setting.getComboBoxItem(settings, "padding");
      if (padding != null) {
        font.setPadding((int) padding);
      }
      Object firstCharacter = Setting.getComboBoxItem(settings, "firstCharacter");
      if (firstCharacter != null) {
        font.setFirstCharacter((int) firstCharacter);
      }
      Object characterCount = Setting.getComboBoxItem(settings, "characterCount");
      if (characterCount != null) {
        font.setCharacterCount((int) characterCount);
      }
      font.setCharacterHeight(Setting.getTextFieldInteger(settings, "characterHeight"));
    }
  }

  private SettingsCreator layoutFor(ResourceItem resourceItem) {
    if(resourceItem instanceof ResourceFont) {
      return FONT_SETTINGS_LAYOUT;
    }else {
      return DEFAULT_SETTINGS_LAYOUT;
    }
  }

  private Map<String, Node> settingsFor(ResourceItem resourceItem) {
    if(!settingsMap.containsKey(resourceItem)) {
      Map<String, Node> settings = layoutFor(resourceItem).createSettings();
      settingsMap.put(resourceItem, settings);
      if (checkValues(resourceItem)) {
        setValues(resourceItem);
      } else {
        reloadSettingsBox(resourceItem);
      }
    }
    return settingsMap.get(resourceItem);
  }

  private Node settingsBoxFor(ResourceItem resourceItem) {
    if(!settingBoxes.containsKey(resourceItem)) {
      Node box = SettingsCreator.getBox(settingsFor(resourceItem));
      settingBoxes.put(resourceItem, box);
      return box;
    }
    return settingBoxes.get(resourceItem);
  }

  public Node createInformationBox(ResourceItem resourceItem) {
    VBox informationBox = new VBox();
    informationBox.setAlignment(Pos.CENTER);
    if(resourceItem instanceof ResourceFont) {
      ResourceFont font = (ResourceFont) resourceItem;
      informationBox.setAlignment(Pos.CENTER);
      informationBox.getChildren().addAll(
        new Label("ImageSize: " + font.imageSize()),
        new Separator(),
        new Label("Downscale: " + font.downscale()),
        new Separator(),
        new Label("Spread: " + font.spread()),
        new Separator(),
        new Label("Padding: " + font.padding()),
        new Separator(),
        new Label("FirstCharacter: " + font.firstCharacter()),
        new Separator(),
        new Label("CharacterCount: " + font.characterCount()),
        new Separator(),
        new Label("CharacterHeight: " + font.characterHeight()),
        fontPreview(font));
    }else if(resourceItem instanceof ResourceTexture) {
      ResourceTexture texture = (ResourceTexture) resourceItem;
      informationBox.getChildren().addAll(
        new Label("Width: " + texture.width()),
        new Separator(),
        new Label("Height: " + texture.height()),
        new Separator(),
        new Label("Channel: " + texture.channel()),
        new Separator(),
        new Label("Filter: " + texture.filter()),
        new Separator(),
        new Label("DataType: " + texture.dataType()),
        new Separator(),
        new Label("InternalFormat: " + texture.internalFormat()),
        new Separator(),
        new Label("InputFormat: " + texture.inputFormat())
      );
    }
    return informationBox;
  }

  private ImageView fontPreview(ResourceFont resourceFont) {
    ResourceTexture texture = resourceFont.texture();
    if(texture != null) {
      int imageSize = texture.width();
      byte[] pixelArray = BufferUtils.copyToArray(texture.pixelBuffer());
      BufferedImage bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_GRAY);
      for (int y = 0; y < imageSize; y++) {
        for (int x = 0; x < imageSize; x++) {
          byte rgb = pixelArray[y * imageSize + x];
          bufferedImage.setRGB(x, y, rgb);
        }
      }
      WritableImage writableImage = SwingFXUtils.toFXImage(bufferedImage, null);
      ImageView imageView = new ImageView(writableImage);
      imageView.setFitWidth(200);
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageView.setCache(true);
      return imageView;
    }
    return null;
  }

  private void reloadSettingsBox(ResourceItem resourceItem) {
    Map<String, Node> settings = settingsFor(resourceItem);
    if(resourceItem instanceof ResourceFont) {
      ResourceFont font = (ResourceFont) resourceItem;
      Setting.setComboBoxItem(settings, "imageSize", font.imageSize());
      Setting.setSpinnerValue(settings, "downscale", font.downscale());
      Setting.setSpinnerValue(settings, "spread", (int) font.spread());
      Setting.setComboBoxItem(settings, "padding", font.padding());
      Setting.setComboBoxItem(settings, "firstCharacter", font.firstCharacter());
      Setting.setComboBoxItem(settings, "characterCount", font.characterCount());
      Setting.setTextFieldInteger(settings, "characterHeight", font.characterHeight());
    }
  }

  private static FileChooser createResourceContainerFileChooser() {
    FileChooser fileChooser = new FileChooser();
    File engineDirectory = new File(System.getProperty("user.home") + "/Documents/Engine");
    if (engineDirectory.isDirectory()) {
      fileChooser.setInitialDirectory(engineDirectory);
    }
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("Resource Container",
        "*.pak",
        "*.werc",
        "*.icrc",
        "*.icc",
        "*.wec",
        "*.rc"));
    return fileChooser;
  }

  private static FileChooser createTextureFileChooser() {
    FileChooser fileChooser = new FileChooser();
    File engineDirectory = new File(System.getProperty("user.home") + "/Documents/Engine");
    if (engineDirectory.isDirectory()) {
      fileChooser.setInitialDirectory(engineDirectory);
    }
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("PNG",
        "*.png"));
    return fileChooser;
  }

  private static FileChooser createFontFileChooser() {
    FileChooser fileChooser = new FileChooser();
    File engineDirectory = new File(System.getProperty("user.home") + "/Documents/Engine");
    if (engineDirectory.isDirectory()) {
      fileChooser.setInitialDirectory(engineDirectory);
    }
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("True Type Font",
        "*.ttf"));
    return fileChooser;
  }

}
