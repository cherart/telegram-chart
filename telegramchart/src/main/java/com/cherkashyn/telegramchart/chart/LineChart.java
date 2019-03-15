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
import com.cherkashyn.telegramchart.model.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

import static com.cherkashyn.telegramchart.utils.DensityConverter.dpToPx;

public class LineChart extends View {

    private Followers followers;
    private List<Line> lines; //TODO
    private List<Integer> maxValues = new ArrayList<>();
    private List<Path> linePathsFull = new ArrayList<>();
    private List<Path> linePathsDetailed = new ArrayList<>();

    private float heightDetailedChartPx;
    private float heightFullChartPx;
    private float margin;
    private float windowLeftBorder;
    private float windowRightBorder;

    private boolean isWindowTouched = false;
    private boolean isLeftBorderTouched = false;
    private boolean isRightBorderTouched = false;
    private boolean cachedFullChart = false;
    private boolean cachedGrid = false;

    private int maxValue;
    private int countX = 24;
    private int textSize = 12;
    private float stepXFullChart;
    private float eventX;

    private TextPaint paintText;
    private Paint paintWindowVerticalBorder;
    private Paint paintWindowHorizontalBorder;
    private Paint paintLine;
    private Paint paintYOne;
    private Paint paintGridLine;
    private Path pathYZeroDetailed;
    private Path pathYOneDetailed;
    private Path pathYZeroFull;
    private Path pathYOneFull;
    private Path pathGridLine;
    private Path pathWindowHorizontal;
    private Path pathWindowVertical;

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        heightDetailedChartPx = dpToPx(256);
        heightFullChartPx = dpToPx(38);
        margin = dpToPx(16);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeJoin(Paint.Join.ROUND);
        paintLine.setStrokeWidth(dpToPx(2));
        paintLine.setStyle(Paint.Style.STROKE);

        paintGridLine = new Paint();
        paintGridLine.setColor(Color.parseColor("#E0E0E0"));
        paintGridLine.setStrokeWidth(2);
        paintGridLine.setStyle(Paint.Style.STROKE);

        paintText = new TextPaint();
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

        if (lines != null) {
            drawGridWithYValues(canvas);
            drawXValues(canvas);
            drawDetailedLineChart(canvas);
            drawFullChart(canvas);
            drawRectangle(canvas);
            drawRectangleWindow(canvas);
        }
    }

    private void drawGridWithYValues(Canvas canvas) {
        float startYGridLine = margin;
        float startYValue = margin - dpToPx(6);
        float stepY = heightDetailedChartPx / 5f;
        int stepValue = maxValue / 5;

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
        float startY = heightDetailedChartPx + margin * 2;
        float stepX = getWidth() / 5f - dpToPx(7);
        for (int i = 0; i < 6; i++) {
            canvas.drawText("Jan 22", stepX * i, startY, paintText);
        }
    }

    private void drawDetailedLineChart(Canvas canvas) {
        float stepY = heightDetailedChartPx / maxValue;
        float stepX = getWidth() / (float) (countX - 1);
        int startIndex = followers.getX().size() - countX;

        for (int i = 0; i < countX; i++) {
            for (int j = 0; j < lines.size(); j++) {
                float y = lines.get(j).getY().get(startIndex + i) * stepY;
                Path path = linePathsDetailed.get(j);
                if (i == 0)
                    path.moveTo(0, heightDetailedChartPx + margin - y);
                else
                    path.lineTo(i * stepX, heightDetailedChartPx + margin - y);
                if (i == countX - 1) {
                    paintLine.setColor(Color.parseColor(lines.get(j).getColor()));
                    canvas.drawPath(path, paintLine);
                    path.reset();
                }
            }
        }
    }

    private void drawRectangle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#E4EEF5"));
        paint.setAlpha(125);
        canvas.drawRect(0, heightDetailedChartPx + margin * 3, windowLeftBorder, heightDetailedChartPx + margin * 3 + heightFullChartPx, paint);
        canvas.drawRect(windowRightBorder, heightDetailedChartPx + margin * 3, getWidth(), heightDetailedChartPx + margin * 3 + heightFullChartPx, paint);
    }

    private void drawFullChart(Canvas canvas) {
        if (!cachedFullChart) {
            float stepY = (heightFullChartPx - dpToPx(4)) / maxValue;

            for (int i = 0; i < followers.getX().size(); i++) {
                for (int j = 0; j < lines.size(); j++) {
                    float y = lines.get(j).getY().get(i) * stepY;
                    Path path = linePathsFull.get(j);
                    if (i == 0)
                        path.moveTo(0, heightDetailedChartPx + margin * 3 + heightFullChartPx - y - dpToPx(2));
                    else
                        path.lineTo(i * stepXFullChart, heightDetailedChartPx + margin * 3 + heightFullChartPx - y - dpToPx(2));
                    if (i == followers.getX().size() - 1) {
                        paintLine.setColor(Color.parseColor(lines.get(j).getColor()));
                        canvas.drawPath(path, paintLine);
                    }
                }
            }
            cachedFullChart = true;
        } else {
            for (int i = 0; i < lines.size(); i++) {
                Path path = linePathsFull.get(i);
                paintLine.setColor(Color.parseColor(lines.get(i).getColor()));
                canvas.drawPath(path, paintLine);
            }
        }
    }

    private void drawRectangleWindow(Canvas canvas) {
        pathWindowHorizontal.moveTo(windowLeftBorder, heightDetailedChartPx + margin * 3);
        pathWindowHorizontal.lineTo(windowRightBorder, heightDetailedChartPx + margin * 3);
        pathWindowHorizontal.moveTo(windowLeftBorder, heightDetailedChartPx + margin * 3 + heightFullChartPx);
        pathWindowHorizontal.lineTo(windowRightBorder, heightDetailedChartPx + margin * 3 + heightFullChartPx);

        pathWindowVertical.moveTo(windowLeftBorder, heightDetailedChartPx + margin * 3);
        pathWindowVertical.lineTo(windowLeftBorder, heightDetailedChartPx + margin * 3 + heightFullChartPx);
        pathWindowVertical.moveTo(windowRightBorder, heightDetailedChartPx + margin * 3);
        pathWindowVertical.lineTo(windowRightBorder, heightDetailedChartPx + margin * 3 + heightFullChartPx);

        canvas.drawPath(pathWindowHorizontal, paintWindowHorizontalBorder);
        canvas.drawPath(pathWindowVertical, paintWindowVerticalBorder);
        pathWindowHorizontal.reset();
        pathWindowVertical.reset();
    }

    public void setData(Followers followers) {
        this.followers = followers;
        lines = followers.getLines();
        for (Line line : lines) {
            maxValues.add(Collections.max(line.getY()));
        }
        maxValue = Collections.max(maxValues) / 10 * 10;
        for (int i = 0; i < lines.size(); i++) {
            linePathsFull.add(new Path());
            linePathsDetailed.add(new Path());
        }
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
                } else if (x > windowLeftBorder && x < windowRightBorder && y < getHeight() && y > getHeight() - heightFullChartPx) { //Add y
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

    public boolean isWindowTouched(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x > windowLeftBorder && x < windowRightBorder && y < getHeight() && y > getHeight() - heightFullChartPx) {
            isWindowTouched = true;
        }
        return isWindowTouched;
    }

}
