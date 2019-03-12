package com.cherkashyn.telegramchart.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.cherkashyn.telegramchart.model.Followers;

import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

public class FollowersLineChart extends View {

    private Followers followers;
    private int maxValue;
    private List<Integer> listYZero;
    private List<Integer> listYOne;
    private int axisColor = Color.parseColor("#E0E0E0");

    public FollowersLineChart(Context context) {
        super(context);
    }

    public FollowersLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowersLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FollowersLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawChartPreview(canvas);
        drawChartPart(canvas);
        drawGrid(canvas);
//        axisPath.moveTo(0f, 900f);
//        axisPath.lineTo(996f, 900f);
//        axisPath.moveTo(canvas.getWidth() / 2, 0f);
//        axisPath.lineTo(canvas.getWidth() / 2, 1000f);
//
//
//
//        Paint fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        fontPaint.setTextSize(50f);
//        fontPaint.setStyle(Paint.Style.STROKE);
//
//        canvas.translate(0, 1000);
//        canvas.drawText("Test", 0, 0, fontPaint);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#DBE7F0"));
        paint.setAlpha(200);
        canvas.drawRect(0, 775, 750, 1050, paint);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLUE);
        paint1.setStrokeWidth(3);
        paint1.setStyle(Paint.Style.STROKE);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(6);
        paint2.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        path.moveTo(750, 775);
        path.lineTo(996, 775);
        path.moveTo(750, 1050);
        path.lineTo(996, 1050);
        Path path2 = new Path();
        path2.moveTo(750, 775);
        path2.lineTo(750, 1050);
        path2.moveTo(996, 775);
        path2.lineTo(996, 1050);

        canvas.drawPath(path, paint1);
        canvas.drawPath(path2, paint2);
    }

    private void drawChartPreview(Canvas canvas) {
        Paint paintYZero = new Paint();
        paintYZero.setColor(Color.RED);
        paintYZero.setStrokeWidth(3);
        paintYZero.setStyle(Paint.Style.STROKE);

        Paint paintYOne = new Paint();
        paintYOne.setColor(Color.GREEN);
        paintYOne.setStrokeWidth(3);
        paintYOne.setStyle(Paint.Style.STROKE);

        Path pathYOne = new Path();
        Path pathYZero = new Path();

        float stepY = maxValue > 250 ? maxValue / 250f : 250f / maxValue;
        float stepX = canvas.getWidth() / (float) followers.getX().size();

        if (maxValue > 250) {
            pathYZero.moveTo(0, canvas.getHeight() - listYZero.get(0) / stepY);
            pathYOne.moveTo(0, canvas.getHeight() - listYOne.get(0) / stepY);
        } else {
            pathYZero.moveTo(0, canvas.getHeight() - listYZero.get(0) * stepY);
            pathYOne.moveTo(0, canvas.getHeight() - listYOne.get(0) * stepY);
        }
        for (int i = 1; i < followers.getX().size(); i++) {
            if (maxValue > 250) {
                pathYZero.lineTo(i * stepX, canvas.getHeight() - followers.getLineYZero().getY().get(i) / stepY);
                pathYOne.lineTo(i * stepX, canvas.getHeight() - followers.getLineYOne().getY().get(i) / stepY);
            } else {
                pathYZero.lineTo(i * stepX, canvas.getHeight() - followers.getLineYZero().getY().get(i) * stepY);
                pathYOne.lineTo(i * stepX, canvas.getHeight() - followers.getLineYOne().getY().get(i) * stepY);
            }
        }

        canvas.drawPath(pathYZero, paintYZero);
        canvas.drawPath(pathYOne, paintYOne);
    }

    private void drawChartPart(Canvas canvas) {
        Paint paintYZero = new Paint();
        paintYZero.setColor(Color.RED);
        paintYZero.setStrokeWidth(3);
        paintYZero.setStyle(Paint.Style.STROKE);

        Paint paintYOne = new Paint();
        paintYOne.setColor(Color.GREEN);
        paintYOne.setStrokeWidth(3);
        paintYOne.setStyle(Paint.Style.STROKE);

        Path pathYOne = new Path();
        Path pathYZero = new Path();

        float stepY = maxValue > 750 ? maxValue / 750f : 750f / maxValue;
        float stepX = canvas.getWidth() / 14f;

        if (maxValue > 750) {
            pathYZero.moveTo(0, 750 - listYZero.get(98) / stepY);
            pathYOne.moveTo(0, 750 - listYOne.get(98) / stepY);
        } else {
            pathYZero.moveTo(0, 750 - listYZero.get(98) * stepY);
            pathYOne.moveTo(0, 750 - listYOne.get(98) * stepY);
        }
        for (int i = 1; i < 14; i++) {
            if (maxValue > 750) {
                pathYZero.lineTo(i * stepX, 750 - followers.getLineYZero().getY().get(98 + i) / stepY);
                pathYOne.lineTo(i * stepX, 750 - followers.getLineYOne().getY().get(98 + i) / stepY);
            } else {
                pathYZero.lineTo(i * stepX, 750 - followers.getLineYZero().getY().get(98 + i) * stepY);
                pathYOne.lineTo(i * stepX, 750 - followers.getLineYOne().getY().get(98 + i) * stepY);
            }
        }

        canvas.drawPath(pathYZero, paintYZero);
        canvas.drawPath(pathYOne, paintYOne);
    }

    private void drawGrid(Canvas canvas) {
        Paint axisPaint = new Paint();
        axisPaint.setColor(axisColor);
        axisPaint.setStrokeWidth(3.0f);
        axisPaint.setStyle(Paint.Style.STROKE);

        Path axisPath = new Path();
        axisPath.moveTo(0f, 150f);
        axisPath.lineTo(996f, 150f);
        axisPath.moveTo(0f, 300f);
        axisPath.lineTo(996f, 300f);
        axisPath.moveTo(0f, 450f);
        axisPath.lineTo(996f, 450f);
        axisPath.moveTo(0f, 600f);
        axisPath.lineTo(996f, 600f);
        axisPath.moveTo(0f, 750f);
        axisPath.lineTo(996f, 750f);
        canvas.drawPath(axisPath, axisPaint);
    }

    public void setData(Followers followers) {
        this.followers = followers;
        init(followers);
    }

    private void init(Followers followers) {
        listYZero = followers.getLineYZero().getY();
        listYOne = followers.getLineYOne().getY();
        int maxValueYZero = Collections.max(listYZero);
        int maxValueYOne = Collections.max(listYOne);
        maxValue = maxValueYZero > maxValueYOne ? maxValueYZero : maxValueYOne;
    }
}
