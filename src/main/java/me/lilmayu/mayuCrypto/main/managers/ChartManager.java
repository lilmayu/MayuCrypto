package me.lilmayu.mayuCrypto.main.managers;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.objects.ChartFile;
import me.lilmayu.mayuCrypto.main.utils.Chart;
import me.lilmayu.mayuCrypto.main.utils.ExceptionInformer;
import me.lilmayu.mayuCrypto.main.utils.Image;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;
import org.json.JSONArray;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChartManager {

    private @Getter String pathToCharts = "/var/www/html/";
    private @Getter String hostingURL = "http://88.99.15.52/";

    private @Getter List<ChartFile> chartFiles = new ArrayList<>();

    private @Getter Timer timer;

    public ChartManager() {
        runTimerDeleter();
    }

    public ChartFile createNewChart(JSONArray arr, int width, int height) {
        Chart chart = new Chart(arr, width, height);
        String name = createNewName();
        try {
            File f = new File("temp.svg");
            FileWriter fw = new FileWriter(f);
            fw.write(chart.generate());
            fw.close();
            Image finalImage = new Image(f).convertToPng(name);
            ChartFile chartFile = new ChartFile(finalImage, chart, getID());
            chartFiles.add(chartFile);
            return chartFile;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionInformer.handle(e);
            Logger.error("Error occurred while creating a chart!");
        }
        return null;
    }

    private String createNewName() {
        return "graph_" + System.currentTimeMillis() + ".png";
    }

    private long getID() {
        return System.currentTimeMillis();
    }

    public boolean removeChartFile(ChartFile chartFile) {
        return this.chartFiles.remove(chartFile);
    }

    public void runTimerDeleter() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean log = Main.getBotConfig().isLogChartClearing();
                List<ChartFile> chartFiles = new ArrayList<>(Main.getChartManager().getChartFiles());
                if (log) Logger.debug("Clearing up " + chartFiles.size() + " charts...");
                for (ChartFile chartFile : chartFiles) {
                    if (chartFile.getID() + 5000 <= System.currentTimeMillis()) {
                        if (log)
                            Logger.debug("Deleting chart '" + chartFile.getImage().getFile().getAbsolutePath() + "'.");
                        boolean deleted = false;
                        try {
                            deleted = chartFile.getImage().getFile().delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ExceptionInformer.handle(e);
                        }
                        if (!deleted) {
                            Logger.error("There was problem deleting chart named '" + chartFile.getName() + "'!");
                        }
                        if (log) Main.getChartManager().removeChartFile(chartFile);
                    }
                }
                if (log)
                    Logger.debug("Done with clearing up charts. Size reduced from " + chartFiles.size() + " to " + Main.getChartManager().getChartFiles().size());
            }
        }, 60000, 60000);
    }
}
