package net.frooastside.engine.resource;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResourceManager extends Application {

  private static final FileChooser RESOURCE_CONTAINER_FILE_CHOOSER = createResourceContainerFileChooser();
  private static final FileChooser TEXTURE_FILE_CHOOSER = createTextureFileChooser();
  private static final FileChooser FONT_FILE_CHOOSER = createFontFileChooser();

  private Stage primaryStage;

  private final ResourceContainer currentResourceContainer = new ResourceContainer();
  private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private File currentFile;
  private boolean unsavedChanges;

  private ListView<String> resourceContainerItems;
  private BorderPane mainBorderPane;

  public static void main(String[] args) {
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
    if(resourceContainerItems.getSelectionModel().getSelectedItem() != null) {
      mainBorderPane.setCenter(currentResourceContainer.get(resourceContainerItems.getSelectionModel().getSelectedItem()).informationBox());
    }
  }

  private VBox createRightVBox() {
    VBox vBox = new VBox();
    vBox.setAlignment(Pos.CENTER);
    vBox.setPadding(new Insets(20));
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
      if(!nameFieldText.isEmpty()) {
        item.recalculate();
        executorService.execute(item.unspecificLoader());
        currentResourceContainer.put(nameFieldText, item);
        reload();
        unsavedChanges = true;
        configurationStage.close();
      }
    });
    BorderPane borderPane = new BorderPane();
    borderPane.setPadding(new Insets(10));
    borderPane.setTop(nameField);
    borderPane.setCenter(item.settingsBox());
    borderPane.setBottom(acceptButton);
    Scene scene = new Scene(borderPane, 320, 480);
    configurationStage.setScene(scene);
    return configurationStage;
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
    for (Map.Entry<String, ResourceItem> entry : currentResourceContainer.content().entrySet()) {
      resourceContainerItems.getItems().add(entry.getKey());
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
        executorService.execute(entry.getValue().unspecificLoader());
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

  private static FileChooser createResourceContainerFileChooser() {
    FileChooser fileChooser = new FileChooser();
    File engineDirectory = new File(System.getProperty("user.home") + "/Documents/Engine");
    if(engineDirectory.isDirectory()) {
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
    if(engineDirectory.isDirectory()) {
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
    if(engineDirectory.isDirectory()) {
      fileChooser.setInitialDirectory(engineDirectory);
    }
    fileChooser.getExtensionFilters().addAll(
      new FileChooser.ExtensionFilter("True Type Font",
        "*.ttf"));
    return fileChooser;
  }

}
