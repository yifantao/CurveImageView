package com.example.taoyifan.curves;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Description:</br>
 *
 * @author:muye@in66.com </br>
 * Date:2017/12/29 上午10:31
 */

public class CurveImageView extends ImageView {
    public static final int NEED_PADDING = 200;
    private Bitmap mBitmap;
    private Camera camera;

    private static final int MESH_WIDTH = 200;
    private static final int MESH_HEIGHT = 200;
    private int MESH_COUNT = (MESH_WIDTH + 1) * (MESH_HEIGHT + 1);
    private float[] dst = new float[MESH_COUNT * 2];
    private float[] origin = new float[MESH_COUNT * 2];
    private Matrix m3DMatrix;
    private float mAngle = 120;//整张图片卷起来占的角度大小
    private float mRotateY;
    private double mRadius;
    private float offsetY;

    public CurveImageView(Context context) {
        super(context);
    }

    public CurveImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        camera = new Camera();
        m3DMatrix = new Matrix();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, w - NEED_PADDING, h - NEED_PADDING, true);
    }

    public float getAngle() {
        return mAngle;
    }

    /***
     *
     * @param angle Bitmap宽度所跨过的角度大小
     */
    public void setAngle(float angle) {
        this.mAngle = angle;
        invalidate();
    }

    public float getRotateY() {
        return mRotateY;
    }

    /***
     *
     * @param rotate 绕柱中心的旋转角度
     */
    public void setRotateY(float rotate) {
        this.mRotateY = rotate;
        invalidate();
    }

    public void init() {
        mRadius = mBitmap.getWidth() / (2 * Math.sin(mAngle / 2 * Math.PI / 180));

        int index = 0;
        float bw = mBitmap.getWidth();
        float bh = mBitmap.getHeight();
        for (int i = 0; i < MESH_HEIGHT + 1; i++) {
            float fy = bh / MESH_HEIGHT * i;
            for (int j = 0; j < MESH_WIDTH + 1; j++) {
                float fx = bw / MESH_WIDTH * j;

                origin[index * 2 + 0] = fx;
                origin[index * 2 + 1] = fy;
                float d = Math.abs(fx - bw / 2);
                double offsetZ = mRadius * (1 - Math.cos(Math.asin(d / mRadius)));
                camera.save();
                camera.translate(0, 0, (float) offsetZ);
                camera.getMatrix(m3DMatrix);
                camera.restore();
                float[] point = null;
                float px = fx;
                float py = fy;

                point = new float[]{px, py};
                m3DMatrix.preTranslate(-bw / 2, bh / 2);
                m3DMatrix.postTranslate(bw / 2, -bh / 2);
                m3DMatrix.mapPoints(point);

                dst[index * 2 + 0] = point[0];
                dst[index * 2 + 1] = point[1];

                index++;
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        init();
        canvas.save();
        canvas.translate(NEED_PADDING / 2, NEED_PADDING / 2);
        canvas.translate(getWidth() / 2, getHeight() / 2);
        camera.save();
        camera.translate(0, 0, (float) mRadius);
        camera.rotateY(mRotateY);
        camera.translate(0, 0, (float) -mRadius);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.translate(-getWidth() / 2, -getHeight() / 2);
        canvas.drawBitmapMesh(mBitmap, MESH_WIDTH, MESH_HEIGHT, dst, 0, null, 0, null);
        canvas.restore();
    }
}
