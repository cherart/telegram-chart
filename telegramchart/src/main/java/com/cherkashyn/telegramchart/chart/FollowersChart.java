package com.cherkashyn.telegramchart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cherkashyn.telegramchart.model.Followers;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

import static com.cherkashyn.telegramchart.utils.DensityConverter.dpToPx;

public class FollowersChart extends View {

    private Followers followers;
    private List<Integer> listYZero;
    private List<Integer> listYOne;

    private float heightDetailedChartPx;
    private float heightFullChartPx;
    private float padding;
    private float windowLeftBorder;
    private float windowRightBorder;

    private boolean isWindowTouched = false;
    private boolean isLeftBorderTouched = false;
    private boolean isRightBorderTouched = false;
    private boolean cachedFullChart = false;
    private boolean cachedGrid = false;

    private int maxYValue;
    private int countX = 24;
    private int textSize = 12;
    private float stepXFullChart;
    private float eventX;

    private TextPaint paintText;
    private Paint paintWindowVerticalBorder;
    private Paint paintWindowHorizontalBorder;
    private Paint paintYZero;
    private Paint paintYOne;
    private Paint paintGridLine;
    private Path pathYZeroDetailed;
    private Path pathYOneDetailed;
    private Path pathYZeroFull;
    private Path pathYOneFull;
    private Path pathGridLine;
    private Path pathWindowHorizontal;
    private Path pathWindowVertical;

    public FollowersChart(Context context) {
        super(context);
        init();
    }

    public FollowersChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowersChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        heightDetailedChartPx = dpToPx(256);
        heightFullChartPx = dpToPx(38);
        padding = dpToPx(16);

        paintYZero = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintYZero.setAntiAlias(true);
        paintYZero.setStrokeJoin(Paint.Join.ROUND);
        paintYZero.setColor(Color.parseColor("#43C047"));
        paintYZero.setStrokeWidth(dpToPx(2));
        paintYZero.setStyle(Paint.Style.STROKE);

        paintYOne = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintYOne.setAntiAlias(true);
        paintYOne.setStrokeJoin(Paint.Join.ROUND);
        paintYOne.setColor(Color.parseColor("#EB6962"));
        paintYOne.setStrokeWidth(dpToPx(2));
        paintYOne.setStyle(Paint.Style.STROKE);

        paintGridLine = new Paint();
        paintGridLine.setColor(Color.parseColor("#E0E0E0"));
        paintGridLine.setStrokeWidth(2);
        paintGridLine.setStyle(Paint.Style.STROKE);

        paintText = new TextPaint();
        paintText.setColor(Color.BLACK);
        paintText.setColor(Color.parseColor("#96A2AA"));
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(dpToPx(textSize));

        paintWindowHorizontalBorder = new Paint();
        paintWindowHorizontalBorder.setColor(Color.parseColor("#DBE7F0"));
        paintWindowHorizontalBorder.setStrokeWidth(dpToPx(2));
        paintWindowHorizontalBorder.setAlpha(210);
        paintWindowHorizontalBorder.setStyle(Paint.Style.STROKE);

        paintWindowVerticalBorder = new Paint();
        paintWindowVerticalBorder.setColor(Color.parseColor("#DBE7F0"));
        paintWindowVerticalBorder.setStrokeWidth(dpToPx(4));
        paintWindowVerticalBorder.setAlpha(210);
        paintWindowVerticalBorder.setStyle(Paint.Style.STROKE);

        pathYZeroDetailed = new Path();
        pathYOneDetailed = new Path();
        pathYZeroFull = new Path();
        pathYOneFull = new Path();
        pathGridLine = new Path();
        pathWindowHorizontal = new Path();
        pathWindowVertical = new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        windowRightBorder = w;
        stepXFullChart = w / (float) followers.getX().size();
        windowLeftBorder = w - countX * stepXFullChart;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGridWithYValues(canvas);
        drawXValues(canvas);
        drawDetailedLineChart(canvas);
        drawFullChart(canvas);
        drawRectangle(canvas);
        drawRectangleWindow(canvas);
    }

    private void drawGridWithYValues(Canvas canvas) {
        float startYGridLine = padding;
        float startYValue = padding - dpToPx(6);
        float stepY = heightDetailedChartPx / 5f;
        int stepValue = maxYValue / 5;

        for (int i = 0; i < 6; i++) {
            if (!cachedGrid) {
                float yGridLine = startYGridLine + (stepY * i);
                pathGridLine.moveTo(0, yGridLine);
                pathGridLine.lineTo(getWidth(), yGridLine);
            }
            float yValue = startYValue + (stepY * i);
            canvas.drawText(String.valueOf(stepValue * (5 - i)), 0, yValue, paintText);
        }
        cachedGrid = true;
        canvas.drawPath(pathGridLine, paintGridLine);
    }

    private void drawXValues(Canvas canvas) {
        float startY = heightDetailedChartPx + padding * 2;
        float stepX = getWidth() / 5f - dpToPx(7);
        for (int i = 0; i < 6; i++) {
            canvas.drawText("Jan 22", stepX * i, startY, paintText);
        }
    }

    private void drawDetailedLineChart(Canvas canvas) {
        float stepY = heightDetailedChartPx / maxYValue;
        float stepX = getWidth() / (float) (countX - 1);
        int startIndex = listYOne.size() - countX;

        for (int i = 0; i < countX; i++) {
            float y0 = listYZero.get(startIndex + i) * stepY;
            float y1 = listYOne.get(startIndex + i) * stepY;
            if (i == 0) {
                pathYZeroDetailed.moveTo(0, heightDetailedChartPx + padding - y0);
                pathYOneDetailed.moveTo(0, heightDetailedChartPx + padding - y1);
            } else {
                pathYZeroDetailed.lineTo(i * stepX, heightDetailedChartPx + padding - y0);
                pathYOneDetailed.lineTo(i * stepX, heightDetailedChartPx + padding - y1);
            }
        }

        canvas.drawPath(pathYZeroDetailed, paintYZero);
        canvas.drawPath(pathYOneDetailed, paintYOne);
        pathYZeroDetailed.reset();
        pathYOneDetailed.reset();
    }

    private void drawRectangle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#E4EEF5"));
        paint.setAlpha(125);
        canvas.drawRect(0, heightDetailedChartPx + padding * 3, windowLeftBorder, heightDetailedChartPx + padding * 3 + heightFullChartPx, paint);
        canvas.drawRect(windowRightBorder, heightDetailedChartPx + padding * 3, getWidth(), heightDetailedChartPx + padding * 3 + heightFullChartPx, paint);
    }

    private void drawFullChart(Canvas canvas) {
        if (!cachedFullChart) {
            float stepY = (heightFullChartPx - dpToPx(4)) / maxYValue;

            for (int i = 0; i < followers.getX().size(); i++) {
                float y0 = listYZero.get(i) * stepY;
                float y1 = listYOne.get(i) * stepY;
                if (i == 0) {
                    pathYZeroFull.moveTo(0, heightDetailedChartPx + padding * 3 + heightFullChartPx - y0 - dpToPx(2));
                    pathYOneFull.moveTo(0, heightDetailedChartPx + padding * 3 + heightFullChartPx - y1 - dpToPx(2));
                } else {
                    pathYZeroFull.lineTo(i * stepXFullChart, heightDetailedChartPx + padding * 3 + heightFullChartPx - y0 - dpToPx(2));
                    pathYOneFull.lineTo(i * stepXFullChart, heightDetailedChartPx + padding * 3 + heightFullChartPx - y1 - dpToPx(2));
                }
            }
            cachedFullChart = true;
        }

        canvas.drawPath(pathYZeroFull, paintYZero);
        canvas.drawPath(pathYOneFull, paintYOne);
    }

    private void drawRectangleWindow(Canvas canvas) {
        pathWindowHorizontal.moveTo(windowLeftBorder, heightDetailedChartPx + padding * 3);
        pathWindowHorizontal.lineTo(windowRightBorder, heightDetailedChartPx + padding * 3);
        pathWindowHorizontal.moveTo(windowLeftBorder, heightDetailedChartPx + padding * 3 + heightFullChartPx);
        pathWindowHorizontal.lineTo(windowRightBorder, heightDetailedChartPx + padding * 3 + heightFullChartPx);

        pathWindowVertical.moveTo(windowLeftBorder, heightDetailedChartPx + padding * 3);
        pathWindowVertical.lineTo(windowLeftBorder, heightDetailedChartPx + padding * 3 + heightFullChartPx);
        pathWindowVertical.moveTo(windowRightBorder, heightDetailedChartPx + padding * 3);
        pathWindowVertical.lineTo(windowRightBorder, heightDetailedChartPx + padding * 3 + heightFullChartPx);

        canvas.drawPath(pathWindowHorizontal, paintWindowHorizontalBorder);
        canvas.drawPath(pathWindowVertical, paintWindowVerticalBorder);
        pathWindowHorizontal.reset();
        pathWindowVertical.reset();
    }

    public void setData(Followers followers) {
        this.followers = followers;
        listYZero = followers.getLineYZero().getY();
        listYOne = followers.getLineYOne().getY();
        int maxValueYZero = Collections.max(listYZero);
        int maxValueYOne = Collections.max(listYOne);
        maxYValue = (maxValueYZero > maxValueYOne ? maxValueYZero : maxValueYOne) / 10 * 10;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();

                if (x <= windowLeftBorder + dpToPx(2) && x >= windowLeftBorder - dpToPx(2)) {
                    isLeftBorderTouched = true;
                } else if (x <= windowRightBorder + dpToPx(2) && x >= windowRightBorder - dpToPx(2)) {
                    isRightBorderTouched = true;
                } else if (x > windowLeftBorder && x < windowRightBorder) { //Add y
                    isWindowTouched = true;
                }
                eventX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - eventX;
                if (isWindowTouched) {
                    if (windowLeftBorder + dx > 0 && windowRightBorder + dx < getWidth()) {
                        windowLeftBorder += dx;
                        windowRightBorder += dx;
                    }
                } else if (isLeftBorderTouched) {
                    if (windowRightBorder - windowLeftBorder < countX * stepXFullChart) {
                        windowLeftBorder = windowRightBorder - countX * stepXFullChart;
                    } else if (windowLeftBorder + dx > 0) {
                        windowLeftBorder += dx;
                    }
                } else if (isRightBorderTouched) {
                    if (windowRightBorder - windowLeftBorder + dx < countX * stepXFullChart) {
                        windowRightBorder = windowLeftBorder + countX * stepXFullChart;
                    } else if (windowRightBorder + dx < getWidth()) {
                        windowRightBorder += dx;
                    }
                }
                eventX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isWindowTouched = false;
                isLeftBorderTouched = false;
                isRightBorderTouched = false;
                break;
        }
        return true;
    }
}
