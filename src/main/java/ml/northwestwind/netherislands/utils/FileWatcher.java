package ml.northwestwind.netherislands.utils;

import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcher extends Thread {
    private final Path directory;
    private final String name;
    private final Runnable callback;

    public FileWatcher(Path directory, Runnable callback) {
        this.directory = directory.getParent();
        this.name = directory.getFileName().toString();
        this.callback = callback;
    }

    @Override
    public void run() {
        try (final WatchService watchService = directory.getFileSystem().newWatchService()) {
            directory.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            LogManager.getLogger().info("Watching file {} of directory {}", name, directory);
            while (!Thread.interrupted()) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                    if (changed.endsWith(name)) callback.run();
                }
                wk.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        super.run();
    }
}
