/* ������վ��http://www.androidbks.com
* �ǽ�iOS���ã�http://www.51work6.com
* �ǽ�iOS�������߿��ã�http://v.51work6.com
* �ǽ�iOS��������΢����http://weibo.com/u/3215753973
* ����΢����http://weibo.com/516inc
* �ٷ�csdn���ͣ�http://blog.csdn.net/tonny_guan
* QQ��1575716557 ���䣺jylong06@163.com
*/ 

package com.work6.designpattern;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * ��Ϸ����ĳ����࣬������һ��View��
 * @author �ض���
 *
 */
public class Scene extends SurfaceView implements SurfaceHolder.Callback {

	private final static String TAG = "Scene";

	private SurfaceHolder holder;
	private Director mDirector;

	private ArrayList<Spirit> spirits = new ArrayList<Spirit>();

	public Scene(Context context) {
		super(context);
		// ����һ���µ�SurfaceHolder
		holder = getHolder();
		holder.addCallback(this);
	}

	public void addSpirit(Spirit sp) {
		spirits.add(sp);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return false;
		}

		float X = event.getX();
		float Y = event.getY();
		// TODO ����x��y�������жϴ������Ǹ�����
		// Log.v(TAG, "X=" + X + " Y=" + Y);

		synchronized (holder) {
			for (Spirit sp : spirits) {

				Spirit.Coordinates coord;
				coord = sp.getCoordinates();

				// �ж��Ƿ�����ĳ������
				if ((X >= coord.getX() && X <= (coord.getX() + sp.getImage()
						.getWidth()))
						&& (Y >= coord.getY() && Y <= (coord.getY() + sp
								.getImage().getHeight()))) {
					sp.onTouchEvent(this, event);
				}

			}
			return true;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		for (Spirit graphic : spirits) {

			Spirit.Coordinates coord;
			Spirit.Speed speed;

			coord = graphic.getCoordinates();
			speed = graphic.getSpeed();

			// Direction
			if (speed.getXDirection() == Spirit.Speed.X_DIRECTION_RIGHT) {
				coord.setX(coord.getX() + speed.getX());
			} else {
				coord.setX(coord.getX() - speed.getX());
			}
			if (speed.getYDirection() == Spirit.Speed.Y_DIRECTION_DOWN) {
				coord.setY(coord.getY() + speed.getY());
			} else {
				coord.setY(coord.getY() - speed.getY());
			}

			// borders for x...
			if (coord.getX() < 0) {
				speed.toggleXDirection();
				coord.setX(-coord.getX());
			} else if (coord.getX() + graphic.getImage().getWidth() > getWidth()) {
				speed.toggleXDirection();
				coord.setX(coord.getX() + getWidth()
						- (coord.getX() + graphic.getImage().getWidth()));
			}

			// borders for y...
			if (coord.getY() < 0) {
				speed.toggleYDirection();
				coord.setY(-coord.getY());
			} else if (coord.getY() + graphic.getImage().getHeight() > getHeight()) {
				speed.toggleYDirection();
				coord.setY(coord.getY() + getHeight()
						- (coord.getY() + graphic.getImage().getHeight()));
			}

			Bitmap bitmap = graphic.getImage();
			canvas.drawBitmap(bitmap, coord.getX(), coord.getY(), null);
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// �����߳�
		mDirector = new Director();
		mDirector.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// ֹͣ�߳�
		if (mDirector != null) {
			mDirector.requestExitAndWait();
			mDirector = null;
		}
	}

	class Director extends Thread {

		private boolean done;

		Director() {
			super();
			done = false;
		}

		@Override
		public void run() {
			SurfaceHolder surfaceHolder = holder;
			// ����ѭ��ִ���߳�
			while (!done) {
				// ����surface������canvas���ڻ���2Dͼ��
				Canvas canvas = surfaceHolder.lockCanvas();
				//Android 4.03֮����뱳��״̬canvasΪnull
				if (canvas == null) {
					requestExitAndWait();
				}
				synchronized (surfaceHolder) {
					// �ص�MySurfaceView��onDraw����
					onDraw(canvas);
				}
				// ������������Ⱦ��ǰͼ��
				surfaceHolder.unlockCanvasAndPost(canvas);
				try {
					Thread.sleep(1000 / 60);// 60֡��
				} catch (InterruptedException e) {
				}
			}

		}

		public void requestExitAndWait() {
			// ����̲߳��ϲ����̵߳����߳�
			done = true;
			try {
				join();
			} catch (InterruptedException ex) {
			}
		}

	}

}