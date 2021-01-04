package me.lilmayu.mayuCrypto.main.objects;

import lombok.Getter;
import me.lilmayu.mayuCrypto.main.Main;
import me.lilmayu.mayuCrypto.main.utils.Chart;
import me.lilmayu.mayuCrypto.main.utils.Image;
import me.lilmayu.mayuCrypto.main.utils.logger.Logger;

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
        Logger.debug("Name: '" + getName() + "'");
        return Main.getChartManager().getHostingURL() + getName();
    }

    public String getName() {
        return image.getFile().getName();
    }
}
