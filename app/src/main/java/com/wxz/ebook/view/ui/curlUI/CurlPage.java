/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.wxz.ebook.view.ui.curlUI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

/**
 * Storage class for page textures, blend colors and possibly some other values
 * in the future.
 * 页面纹理、混合颜色以及将来可能的某些其他值的存储类
 * 
 * @author harism
 */
public class CurlPage {

	public static final int SIDE_BACK = 2;
	public static final int SIDE_BOTH = 3;
	public static final int SIDE_FRONT = 1;

	private int mColorBack;
	private int mColorFront;
	private Bitmap mTextureBack;
	private Bitmap mTextureFront;
	private boolean mTexturesChanged;

	/**
	 * Default constructor.
	 */
	public CurlPage() {
		reset();
	}

	/**
	 * Getter for color.
	 * 获取颜色
	 */
	public int getColor(int side) {
		switch (side) {
		case SIDE_FRONT:
			return mColorFront;
		default:
			return mColorBack;
		}
	}

	/**
	 * Calculates the next highest power of two for a given integer.
	 * 计算给定整数下一个最大 2 的幂值。
	 */
	private int getNextHighestPO2(int n) {
		n -= 1;
		n = n | (n >> 1);
		n = n | (n >> 2);
		n = n | (n >> 4);
		n = n | (n >> 8);
		n = n | (n >> 16);
		n = n | (n >> 32);
		return n + 1;
	}

	/**
	 * Generates nearest power of two sized Bitmap for give Bitmap. Returns this
	 * new Bitmap using default return statement + original texture coordinates
	 * are stored into RectF.
	 * 生成给定图像的位图大小最接近 2 的幂的值
	 * 返回一个新的位图，使用默认返回语句 +  存储在 RectF 中的原始纹理坐标
	 */
	private Bitmap getTexture(Bitmap bitmap, RectF textureRect) {
		// Bitmap original size. 位图原始大小
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Bitmap size expanded to next power of two. This is done due to
		// the requirement on many devices, texture width and height should
		// be power of two.
		// 位图大小扩展为 2 的下一个幂，这是由于许多设备的要求，纹理宽度和高度应该是2的幂数。
		int newW = getNextHighestPO2(w);
		int newH = getNextHighestPO2(h);

		// TODO: Is there another way to create a bigger Bitmap and copy
		// original Bitmap to it more efficiently? Immutable bitmap anyone?
		// TODO: 还有其他方法可以创建和复制更大的位图
		// 原来的位图更有效?不变的位图吗?
		Bitmap bitmapTex = Bitmap.createBitmap(newW, newH, bitmap.getConfig());
		Canvas c = new Canvas(bitmapTex);
		c.drawBitmap(bitmap, 0, 0, null);

		// Calculate final texture coordinates.
		// 计算最终的纹理坐标。
		float texX = (float) w / newW;
		float texY = (float) h / newH;
		textureRect.set(0f, 0f, texX, texY);

		return bitmapTex;
	}

	/**
	 * Getter for textures. Creates Bitmap sized to nearest power of two, copies
	 * original Bitmap into it and returns it. RectF given as parameter is
	 * filled with actual texture coordinates in this new upscaled texture
	 * Bitmap.
	 * 获取纹理，创建位图大小为最近的 2 的幂值。复制原始位图到其中并返回，
	 * RectF作为参数，以真实的纹理坐标填充在这个新的缩放纹理坐标中。
	 */
	public Bitmap getTexture(RectF textureRect, int side) {
		switch (side) {
		case SIDE_FRONT:
			return getTexture(mTextureFront, textureRect);
		default:
			return getTexture(mTextureBack, textureRect);
		}
	}

	/**
	 * Returns true if textures have changed. 如果纹理改变将返回 true
	 */
	public boolean getTexturesChanged() {
		return mTexturesChanged;
	}

	/**
	 * Returns true if back siding texture exists and it differs from front
	 * facing one.
	 * 如果背面纹理存在，并与正面不同，返回 true
	 */
	public boolean hasBackTexture() {
		return !mTextureFront.equals(mTextureBack);
	}

	/**
	 * Recycles and frees underlying Bitmaps. 回收和释放下面的位图
	 */
	public void recycle() {
		if (mTextureFront != null) {
			mTextureFront.recycle();
		}
		mTextureFront = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		mTextureFront.eraseColor(mColorFront);
		if (mTextureBack != null) {
			mTextureBack.recycle();
		}
		mTextureBack = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
		mTextureBack.eraseColor(mColorBack);
		mTexturesChanged = false;
	}

	/**
	 * Resets this CurlPage into its initial state. 将此曲面页重置为初始状态
	 */
	public void reset() {
		mColorBack = Color.WHITE;
		mColorFront = Color.WHITE;
		recycle();
	}

	/**
	 * Setter blend color. 设置混合颜色
	 */
	public void setColor(int color, int side) {
		switch (side) {
		case SIDE_FRONT:
			mColorFront = color;
			break;
		case SIDE_BACK:
			mColorBack = color;
			break;
		default:
			mColorFront = mColorBack = color;
			break;
		}
	}

	/**
	 * Setter for textures. 设置纹理
	 */
	public void setTexture(Bitmap texture, int side) {
		if (texture == null) {
			texture = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
			if (side == SIDE_BACK) {
				texture.eraseColor(mColorBack);
			} else {
				texture.eraseColor(mColorFront);
			}
		}
		switch (side) {
		case SIDE_FRONT:
			if (mTextureFront != null)
				mTextureFront.recycle();
			mTextureFront = texture;
			break;
		case SIDE_BACK:
			if (mTextureBack != null)
				mTextureBack.recycle();
			mTextureBack = texture;
			break;
		case SIDE_BOTH:
			if (mTextureFront != null)
				mTextureFront.recycle();
			if (mTextureBack != null)
				mTextureBack.recycle();
			mTextureFront = mTextureBack = texture;
			break;
		}
		mTexturesChanged = true;
	}

}
