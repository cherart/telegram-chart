package com.cherkashyn.telegramchart.chart;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cherkashyn.telegramchart.model.Followers;
import com.cherkashyn.telegramchart.model.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

import static com.cherkashyn.telegramchart.utils.Utils.dpToPx;

public class LineChart extends View {

    private Followers followers;
    private List<Line> lines; //TODO
    private List<Integer> maxValues = new ArrayList<>();
    private List<Path> linePathsFull = new ArrayList<>();
    private List<Path> linePathsDetailed = new ArrayList<>();
    private List<Integer> removedLines = new ArrayList<>();

    private float heightDetailedChartPx;
    private float heightFullChartPx;
    private float marginSixteenDp;

    private float windowLeftBorder;
    private float windowRightBorder;
    private float windowTopBorder;
    private float windowBottomBorder;

    private boolean isMaxValueUpdated = false;
    private boolean isWindowTouched = false;
    private boolean isLeftBorderTouched = false;
    private boolean isRightBorderTouched = false;
    private boolean cachedGrid = false;

    private int maxValue;
    private int countX = 24;
    private int defaultCountX = 24;
    private int textSize = 12;
    private float strokeWidthVertical = dpToPx(6);
    private float strokeWidthHorizontal = dpToPx(2);
    private float stepXFullChart;
    private float stepXPosDetailed;
    private float eventX;

    private TextPaint paintText;
    private Paint paintWindowSelector;
    private Paint paintLine;
    private Paint paintGridLine;
    private Path pathGridLine;
    private Path pathWindowHorizontal;
    private Path pathWindowVertical;
    private Paint paintRectangle;

    private ValueAnimator animator;
    private float stepYDetailed;
    private float stepYFull;
    private int alpha;

    float dx;
    int startIndex;
    int endIndex;

    public LineChart(Context context) {
        super(context);
        init();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        heightDetailedChartPx = dpToPx(256);
        heightFullChartPx = dpToPx(38);
        marginSixteenDp = dpToPx(16);

        animator = new ValueAnimator();
        animator.setDuration(750);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setAntiAlias(true);
        paintLine.setStrokeJoin(Paint.Join.ROUND);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
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

        paintWindowSelector = new Paint();
        paintWindowSelector.setColor(Color.parseColor("#DBE7F0"));
        paintWindowSelector.setStrokeWidth(dpToPx(2));
        paintWindowSelector.setAlpha(210);
        paintWindowSelector.setStyle(Paint.Style.STROKE);

        paintRectangle = new Paint();
        paintRectangle.setColor(Color.parseColor("#E4EEF5"));
        paintRectangle.setAlpha(125);

        pathGridLine = new Path();
        pathWindowHorizontal = new Path();
        pathWindowVertical = new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        stepXFullChart = w / (float) (followers.getListOfX().size() - 1);
        windowRightBorder = w;
        windowLeftBorder = w - countX * stepXFullChart;
        windowTopBorder = h - heightFullChartPx;
        windowBottomBorder = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!lines.isEmpty()) {
            drawGridWithYValues(canvas);
            drawXValues(canvas);
            drawDetailedLineChart(canvas);
            drawFullChart(canvas);
            drawRectangle(canvas);
            drawWindowSelector(canvas);
        }
    }

    private void drawGridWithYValues(Canvas canvas) {
        float marginSixDp = dpToPx(6);
        float startYPosGridLine = marginSixteenDp;
        float startYPosValueText = marginSixteenDp - marginSixDp;
        float stepYPos = heightDetailedChartPx / 5f;
        int stepValue = maxValue / 5;

        for (int i = 0; i < 6; i++) {
            float yPosGridLine = startYPosGridLine + (stepYPos * i);
            pathGridLine.moveTo(0, yPosGridLine);
            pathGridLine.lineTo(getWidth(), yPosGridLine);
            float yPosValue = startYPosValueText + (stepYPos * i);
            canvas.drawText(String.valueOf(stepValue * (5 - i)), 0, yPosValue, paintText);
        }
        canvas.drawPath(pathGridLine, paintGridLine);
    }

    private void drawXValues(Canvas canvas) {
        float startYPos = heightDetailedChartPx + marginSixteenDp * 2;
        float startXPos = 0;
        float stepXPos = getWidth() / 5f - dpToPx(7);
        for (int i = 0; i < 6; i++) {
            canvas.drawText("Jan 22", startXPos + stepXPos * i, startYPos, paintText);
        }
    }

    private void drawDetailedLineChart(Canvas canvas) {
        float stepYPos = heightDetailedChartPx / maxValue;
        if (isMaxValueUpdated) {
            stepYPos = (float) animator.getAnimatedValue("detailed");
        }
        stepXPosDetailed = getWidth() / (float) (countX - 1);
        paintLine.setStrokeWidth(dpToPx(2));

        for (int i = 0; i < countX; i++) {
            for (int j = 0; j < lines.size(); j++) {
                float y = lines.get(j).getListOfY().get(startIndex + i) * stepYPos;
                Path path = linePathsDetailed.get(j);
                if (i == 0)
                    path.moveTo(0, heightDetailedChartPx + marginSixteenDp - y);
                else {
                    path.lineTo(i * stepXPosDetailed, heightDetailedChartPx + marginSixteenDp - y);
                    if (i == countX - 1) {
                        paintLine.setColor(Color.parseColor(lines.get(j).getColor()));
                        if (removedLines.contains(j)) {
                            paintLine.setAlpha(alpha);
                        } else {
                            paintLine.setAlpha(255);
                        }
                        canvas.drawPath(path, paintLine);
                        path.reset();
                    }
                }
            }
        }
    }

    private void drawRectangle(Canvas canvas) {
        canvas.drawRect(0, heightDetailedChartPx + marginSixteenDp * 3, windowLeftBorder, heightDetailedChartPx + marginSixteenDp * 3 + heightFullChartPx, paintRectangle);
        canvas.drawRect(windowRightBorder, heightDetailedChartPx + marginSixteenDp * 3, getWidth(), heightDetailedChartPx + marginSixteenDp * 3 + heightFullChartPx, paintRectangle);
    }

    private void drawFullChart(Canvas canvas) {
        paintLine.setStrokeWidth(2);

        float stepY = (heightFullChartPx - dpToPx(4)) / maxValue;
        if (isMaxValueUpdated) {
            stepY = (float) animator.getAnimatedValue("full");
        }

        for (int i = 0; i < followers.getListOfX().size(); i++) {
            for (int j = 0; j < lines.size(); j++) {
                float y = (lines.get(j).getListOfY().get(i) * stepY);

                Path path = linePathsFull.get(j);
                if (i == 0)
                    path.moveTo(0, heightDetailedChartPx + marginSixteenDp * 3 + heightFullChartPx - y - dpToPx(2));
                else {
                    path.lineTo(i * stepXFullChart, heightDetailedChartPx + marginSixteenDp * 3 + heightFullChartPx - y - dpToPx(2));
                    if (i == followers.getListOfX().size() - 1) {
                        paintLine.setColor(Color.parseColor(lines.get(j).getColor()));
                        if (removedLines.contains(j)) {
                            paintLine.setAlpha(alpha);
                        } else {
                            paintLine.setAlpha(255);
                        }
                        canvas.drawPath(path, paintLine);
                        path.reset();
                    }
                }
            }
        }
    }

    private void drawWindowSelector(Canvas canvas) {
        pathWindowHorizontal.moveTo(windowLeftBorder, windowTopBorder + strokeWidthHorizontal / 2);
        pathWindowHorizontal.lineTo(windowRightBorder, windowTopBorder + strokeWidthHorizontal / 2);
        pathWindowHorizontal.moveTo(windowLeftBorder, windowBottomBorder - strokeWidthHorizontal / 2);
        pathWindowHorizontal.lineTo(windowRightBorder, windowBottomBorder - strokeWidthHorizontal / 2);

        pathWindowVertical.moveTo(windowLeftBorder + strokeWidthVertical / 2, windowTopBorder);
        pathWindowVertical.lineTo(windowLeftBorder + strokeWidthVertical / 2, windowBottomBorder);
        pathWindowVertical.moveTo(windowRightBorder - strokeWidthVertical / 2, windowTopBorder);
        pathWindowVertical.lineTo(windowRightBorder - strokeWidthVertical / 2, windowBottomBorder);

        paintWindowSelector.setStrokeWidth(strokeWidthHorizontal);
        canvas.drawPath(pathWindowHorizontal, paintWindowSelector);
        pathWindowHorizontal.reset();

        paintWindowSelector.setStrokeWidth(strokeWidthVertical);
        canvas.drawPath(pathWindowVertical, paintWindowSelector);
        pathWindowVertical.reset();
    }

    public void setData(Followers followers) {
        this.followers = followers;
        lines = followers.getLines();
        startIndex = followers.getListOfX().size() - defaultCountX;
        initMaxValues();
        initChartPaths();
        initStepY();
    }

    private void initMaxValues() {
        for (Line line : lines) {
            maxValues.add(Collections.max(line.getListOfY()));
        }
        maxValue = Collections.max(maxValues) / 10 * 10 + 10;
    }

    private void initChartPaths() {
        for (int i = 0; i < lines.size(); i++) {
            linePathsFull.add(new Path());
            linePathsDetailed.add(new Path());
        }
    }

    private void initStepY() {
        stepYDetailed = heightDetailedChartPx / maxValue;
        stepYFull = heightFullChartPx / maxValue;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (y < getHeight() && y > getHeight() - heightFullChartPx) {
                    isLeftBorderTouched = x >= windowLeftBorder && x <= windowLeftBorder + strokeWidthVertical;
                    isRightBorderTouched = x <= windowRightBorder && x >= windowRightBorder - strokeWidthVertical;
                    isWindowTouched = x >= windowLeftBorder + strokeWidthVertical && x <= windowRightBorder - strokeWidthVertical;
                    Log.i("LineChart", "LeftBorder: " + isLeftBorderTouched);
                    Log.i("LineChart", "RightBorder: " + isRightBorderTouched);
                    Log.i("LineChart", "Window: " + isWindowTouched);
                }
                eventX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                dx = event.getX() - eventX;
                if (isWindowTouched) {
                    if (windowLeftBorder + dx > 0 && windowRightBorder + dx < getWidth()) {
                        windowLeftBorder += dx;
                        windowRightBorder += dx;
//                        Log.i("LineChart", "Left Border: " + windowLeftBorder);
//                        Log.i("LineChart", "Right Border: " + windowRightBorder);
//                        Log.i("LineChart", "Start Index: " + windowLeftBorder / stepXFullChart);
//                        Log.i("LineChart", "End Index: " + windowRightBorder / stepXFullChart);
//                        Log.i("LineChart", "Count: " + (endIndex - startIndex));
                    }
                } else if (isLeftBorderTouched) {

                    if (windowRightBorder - (windowLeftBorder + dx) < defaultCountX * stepXFullChart) {
                        windowLeftBorder = windowRightBorder - defaultCountX * stepXFullChart;
                        Log.i("LineChart", "InLeft");
                    } else if (windowLeftBorder + dx > 0) {
                        windowLeftBorder += dx;
                    }
                } else if (isRightBorderTouched) {
                    if ((windowRightBorder + dx) - windowLeftBorder < defaultCountX * stepXFullChart) {
                        windowRightBorder = windowLeftBorder + defaultCountX * stepXFullChart;
                    } else if (windowRightBorder + dx < getWidth()) {
                        windowRightBorder += dx;
                    }
                }
                startIndex = (int) (windowLeftBorder / stepXFullChart);
                endIndex = (int) (windowRightBorder / stepXFullChart);
                countX = endIndex - startIndex;
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

    public boolean isWindowTouched() {
        return isWindowTouched || isLeftBorderTouched || isRightBorderTouched; //TODO
    }

    public void removeLine(int index) {
        removedLines.add(index - 1);
        createValueAnimator(false);
    }

    public void showLine(int index) {
        removedLines.remove(new Integer(index - 1)); //TODO remove object
        createValueAnimator(true);
    }


    private void updateMaxValue() {
        int max = 0;
        for (int i = 0; i < maxValues.size(); i++) {
            if (!removedLines.contains(i) && maxValues.get(i) > max) {
                max = maxValues.get(i);
            }
        }
        if (max / 10 * 10 + 10 != maxValue) {
            isMaxValueUpdated = true;
            maxValue = max / 10 * 10 + 10;
        }
    }

    private void createValueAnimator(boolean isShow) {
        updateMaxValue();

        float newStepYDetailed = heightDetailedChartPx / maxValue;
        float newStepYFull = heightFullChartPx / maxValue;

        List<PropertyValuesHolder> holder = new ArrayList<>();
        holder.add(PropertyValuesHolder.ofFloat("detailed", stepYDetailed, newStepYDetailed));
        holder.add(PropertyValuesHolder.ofFloat("full", stepYFull, newStepYFull));
        if (isShow)
            holder.add(PropertyValuesHolder.ofInt("alpha", 0, 255));
        else
            holder.add(PropertyValuesHolder.ofInt("alpha", 255, 0));

        animator.setValues(holder.toArray(new PropertyValuesHolder[0]));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                stepYDetailed = (float) valueAnimator.getAnimatedValue("detailed");
                stepYFull = (float) valueAnimator.getAnimatedValue("full");
                alpha = (int) valueAnimator.getAnimatedValue("alpha");
                invalidate();
            }
        });
        animator.start();
    }
}
