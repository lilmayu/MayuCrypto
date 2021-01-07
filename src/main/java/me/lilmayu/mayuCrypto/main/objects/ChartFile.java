package me.lilmayu.mayuCrypto.main.objects;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.utils.Chart;
import me.lilmayu.mayuCrypto.main.utils.Image;

public class ChartFile {

    private @Getter Image image;
    private @Getter Chart chart;
    private @Getter long ID;

    public ChartFile(Image image, Chart chart, long ID) {
        this.image = image;
        this.chart = chart;
        this.ID = ID;
    }

    public String getURLToChart() {
        return Main.getChartManager().getHostingURL() + getName();
    }

    public String getName() {
        return image.getFile().getName();
    }
}
