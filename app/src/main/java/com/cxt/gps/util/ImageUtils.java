package com.cxt.gps.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 图片简单处理工具类
 */
public class ImageUtils {

	/**
	 * 屏幕宽
	 *
	 * @param context
	 * @return
	 */
	public static int getWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 屏幕高
	 *
	 * @param context
	 * @return
	 */
	public static int getHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 解决小米、魅族等定制ROM
	 * @param context
	 * @param intent
	 * @return
	 */
	public static Uri getUri(Context context , Intent intent) {
		Uri uri = intent.getData();
		String type = intent.getType();
		if (uri.getScheme().equals("file") && (type.contains("image/"))) {
			String path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = context.getContentResolver();
				StringBuffer buff = new StringBuffer();
				buff.append("(").append(Images.ImageColumns.DATA).append("=")
						.append("'" + path + "'").append(")");
				Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { Images.ImageColumns._ID },
						buff.toString(), null, null);
				int index = 0;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(Images.ImageColumns._ID);
					// set _id value
					index = cur.getInt(index);
				}
				if (index == 0) {
					// do nothing
				} else {
					Uri uri_temp = Uri
							.parse("content://media/external/images/media/"
									+ index);
					if (uri_temp != null) {
						uri = uri_temp;
						Log.i("urishi", uri.toString());
					}
				}
			}
		}
		return uri;
	}

	/**
	 * 根据文件Uri获取路径
	 *
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getFilePathByFileUri(Context context, Uri uri) {
		String filePath = null;
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		cursor.close();
		return filePath;
	}

	/**
	 * 根据图片原始路径获取图片缩略图
	 *
	 * @param imagePath 图片原始路径
	 * @param width		缩略图宽度
	 * @param height	缩略图高度
	 * @return
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; //关于inJustDecodeBounds的作用将在下文叙述
		int h = options.outHeight;//获取图片高度
		int w = options.outWidth;//获取图片宽度
		int scaleWidth = w / width; //计算宽度缩放比
		int scaleHeight = h / height; //计算高度缩放比
		int scale = 1;//初始缩放比
		if (scaleWidth < scaleHeight) {//选择合适的缩放比
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		if (scale <= 0) {//判断缩放比是否符合条件
			scale = 1;
		}
		options.inSampleSize = scale;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把inJustDecodeBounds 设为 false
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 通过内容提供器来获取图片缩略图
	 缺点:必须更新媒体库才能看到最新的缩略图
	 * @param context
	 * @param cr
	 * @param Imagepath
	 * @return
	 */
	public static Bitmap getImageThumbnail(Context context, ContentResolver cr, String Imagepath) {
		ContentResolver testcr = context.getContentResolver();
		String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, };
		String whereClause = MediaStore.Images.Media.DATA + " = '" + Imagepath + "'";
		Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,null, null);
		int _id = 0;
		String imagePath = "";
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}else if (cursor.moveToFirst()) {
			int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			do {
				_id = cursor.getInt(_idColumn);
				imagePath = cursor.getString(_dataColumn);
			} while (cursor.moveToNext());
		}
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, _id, Images.Thumbnails.MICRO_KIND,options);
		return bitmap;
	}

}
