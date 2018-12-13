package ba.unsa.etf.rpr.tutorijal08;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;


public class Controller {
    public ListView listView;
    public Button traziButton;
    public Button prekiniButton;
    public TextField pretraga;

    public void traziBtn(ActionEvent actionEvent) {
        traziButton.setDisable(true);
        prekiniButton.setDisable(false);
        listView.getSelectionModel().clearSelection();
        listView.getItems().clear();
        String home = System.getProperty("user.home");
        Pretraga p = new Pretraga(home);
        Thread thread = new Thread(p);
        thread.start();
    }

    public void prekiniBtn(ActionEvent actionEvent) {
        prekiniButton.setDisable(true);
        traziButton.setDisable(false);
    }

    @FXML
    public void initialize() {
        prekiniButton.setDisable(true);
        traziButton.disabledProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                prekiniButton.setDisable(true);
            else
                prekiniButton.setDisable(false);
        });
    }

    private class Pretraga implements Runnable {
        private File root;

        public Pretraga(String home) {
            root = new File(home);
        }

        @Override
        public void run() {
            trazi(root, root);
        }

        public void trazi(File root, File current) {
            if (!traziButton.isDisabled())
                return;
            if (current.isDirectory()) {
                File[] files = current.listFiles();
                if (files == null)
                    return;
                for (File file : files) {
                    if (file.isDirectory())
                        trazi(root, file);
                    if (file.isFile() && file.getName().contains(pretraga.getText()))
                        Platform.runLater(() -> listView.getItems().add(file.getAbsolutePath()));
                }
            }
            if (root.getAbsolutePath().equals(current.getAbsolutePath()))
                traziButton.setDisable(false);
        }

    }
}
