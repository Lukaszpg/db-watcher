package sample;

import java.io.IOException;
import java.nio.file.*;

import javafx.scene.control.Alert;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class Controller {

    private boolean dialogShown = false;

    public void launchWatchers() {
        initializeWatcherForTrunk();
    }

    private void initializeWatcherForTrunk() {
        try {
            FileSystem fs = FileSystems.getDefault();
            WatchService ws;

            ws = fs.newWatchService();

            Path pTemp = Paths.get("C:\\Play\\Trunk\\sql\\db_versions");
            pTemp.register(ws, new WatchEvent.Kind[] {ENTRY_MODIFY, ENTRY_CREATE}, FILE_TREE);
            while (true) {
                WatchKey k = ws.take();
                for (WatchEvent<?> e : k.pollEvents()) {
                    showDialog("Utworzono nowe pliki db versions w trunk");
                    launchTimer();
                }
                k.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String message) {
        if(!dialogShown) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nowe pliki");
            alert.setHeaderText(message);
            alert.setContentText("Update time!");

            dialogShown = true;

            alert.showAndWait();
        }
    }

    private void launchTimer() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        dialogShown = false;
                    }
                },
                60000
        );
    }
}
