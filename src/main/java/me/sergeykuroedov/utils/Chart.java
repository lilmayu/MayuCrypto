package me.sergeykuroedov.utils;

import org.json.JSONArray;

import static java.lang.Math.abs;

public class Chart {
    JSONArray arr;
    int width;
    int height;

    public Chart(JSONArray arr, int width, int height) {
        this.arr = arr;
        this.width = width;
        this.height = height;
    }

    double findMaximum() {
        double max = 0;
        for (int i = 0; i < arr.length(); i++) {
            JSONArray item = arr.getJSONArray(i);
            if (item.getDouble(3) > max) {
                max = item.getDouble(3);
            }
        }
        return max;
    }

    double findMinimum() {
        double min = 999999999;
        for (int i = 0; i < arr.length(); i++) {
            JSONArray item = arr.getJSONArray(i);
            if (item.getDouble(4) < min) {
                min = item.getDouble(4);
            }
        }
        return min;
    }

    public String generate() {
        StringBuilder res = new StringBuilder("<?xml version=\"1.0\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + width + "\" height=\"" + height + "\">");
        int currentX = 0;
        int candleWidth = (8 * width - 42) / (9 * arr.length());
        int space = candleWidth/8;
        double max = findMaximum();
        double min = findMinimum();
        double minCeil = Math.ceil(min);
        double maxCeil = Math.ceil(max);
        double k = height / (max - min);
        double step = (maxCeil - minCeil) / 5;

        for (double i = minCeil; i < maxCeil; i += step) {
            res.append(String.format("<line x1=\"0\" x2=\"%d\" y1=\"%f\" y2=\"%<f\" stroke-width=\"1\" stroke=\"#ccc\" />", width, k * (max - i)));
            res.append(String.format("<text x=\"%d\" y=\"%f\" fill=\"#ccc\">%f</text>", width - 42, k * (max - i) - 2, i));
        }

        double open, close, low, high, height, x, y;
        String color;
        for (int i = arr.length()-1; i >= 0; i--) {
            JSONArray item = arr.getJSONArray(i);

            high = (max - item.getDouble(3)) * k;
            low = (max - item.getDouble(4)) * k;

            open = (max - item.getDouble(1)) * k;
            close = (max - item.getDouble(2)) * k;

            height = abs(open - close);
            x = currentX - ((float) candleWidth / 2);
            if (open > close) {
                y = open - height / 2;
                color = "#01aa78";
            } else {
                y = close - height / 2;
                color = "#c63c4d";
            }


            res.append(String.format("<line x1=\"%d\" x2=\"%<d\" y1=\"%f\" y2=\"%f\" stroke-width=\"%d\" stroke=\"%s\" />", currentX, high, low, 2, color));
            res.append(String.format("<rect width=\"%d\" height=\"%f\" x=\"%f\" y=\"%f\" shape-rendering=\"crispEdges\" fill=\"%s\" />", candleWidth, height, x, y, color));
            currentX += candleWidth + space;
        }
        res.append("</svg>");
        return res.toString();
    }
}
