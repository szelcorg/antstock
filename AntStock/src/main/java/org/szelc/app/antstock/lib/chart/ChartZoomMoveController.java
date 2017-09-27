package org.szelc.app.antstock.lib.chart;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szelc.org
 */
public class ChartZoomMoveController {

    private static final float MOVE_SPEED = 0.3f;
    private static final float ZOOM_SPEED = 0.2f;
    private static final int FACTORIAL_MAX_ELEMENTS = 30;

    private final int totalData;
    private float countToDisplay;
    private float numberLastRecord;

    private static final List<Integer> factorials = new ArrayList();

    private int zoom;
    private Float lastZoomSign = null;

    public ChartZoomMoveController(int totalData, int countToDisplay, int numberLastRecord) {
        this.totalData = totalData;
        this.countToDisplay = countToDisplay;
        this.numberLastRecord = numberLastRecord;
        initCalculateFactorial();
        initCalculateZoom();
    }

    public void zoomMinusCenter() {
        zoomCenter(1f);
    }

    public void zoomPlusCenter() {
        zoomCenter(-1f);
    }

    public void zoomMinusLeftToRight() {
        zoomLeftToRight(1f);
    }

    public void zoomPlusLeftToRight() {
        zoomLeftToRight(-1f);
    }

    public void zommMinusRightToLeft() {
        zoomRightToLeft(1f);
    }

    public void zommPlusRightToLeft() {
        zoomRightToLeft(-1f);
    }

    public void moveLeft() {
        numberLastRecord -= MOVE_SPEED * countToDisplay;
        checkConsistenceAndRepair();
    }

    public void moveRight() {
        numberLastRecord += MOVE_SPEED * countToDisplay;
        checkConsistenceAndRepair();
    }

    private boolean checkConsistenceAndRepair() {
        boolean result = true;
        if (numberLastRecord > totalData) {
            numberLastRecord = totalData;
        }
        if (numberLastRecord < 1) {
            numberLastRecord = 1;
        }
        if (countToDisplay > totalData) {
            countToDisplay = totalData;
            result = false;
        }
        if (countToDisplay < 1) {
            countToDisplay = 1;
            result = false;
        }
        return result;
    }

    private void zoomCenter(float zoomSign) {
        int zoomBackup = zoom;
        float sub = zoom(zoomSign);
        countToDisplay += (zoomSign * sub);
        numberLastRecord += (zoomSign * (sub / 2));
        lastZoomSign = zoomSign;
        if (!checkConsistenceAndRepair()) {
            zoom = zoomBackup;
        }
    }

    private void zoomLeftToRight(float zoomSign) {
        float sub = zoom(zoomSign);
        countToDisplay += zoomSign * sub;
        lastZoomSign = zoomSign;
    }

    private void zoomRightToLeft(float zoomSign) {
        float sub = zoom(zoomSign);
        countToDisplay += zoomSign * sub;
        numberLastRecord += zoomSign * sub;
        lastZoomSign = zoomSign;
    }

    private float zoom(float zoomSign) {
        if (lastZoomSign == null || zoomSign == lastZoomSign) {
            zoom += zoomSign * 1;
            if (zoom < 0) {
                zoom = 0;
            }
        }
        return factorials.get(zoom);
    }

    private void initCalculateFactorial() {
        factorials.add(1);
        factorials.add(2);
        for (int i = 2; i < FACTORIAL_MAX_ELEMENTS; i++) {
            factorials.add(factorials.get(i - 1) + factorials.get(i - 2));
        }
    }

    private void initCalculateZoom() {
        int sumFactorial = 0;
        for (int i = 0; i < FACTORIAL_MAX_ELEMENTS; i++) {
            sumFactorial += factorials.get(i);
            if (sumFactorial > ZOOM_SPEED * countToDisplay) {
                return;
            }
            zoom++;
        }
    }

    @Override
    public String toString() {
        return "ChartZoomMoveController{" + "totalData=" + totalData + ", countToDisplay=" + countToDisplay + ", pointerRight=" + numberLastRecord + ", zoom=" + zoom + ", factorial=" + factorials + '}';
    }

}
