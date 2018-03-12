package com.mogu.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.mogu.app.Url;

public class ImageUtils {

	public static String getImageUrl(String imageUrl) {

		if (imageUrl == null) {
			return "";
		}
		
		if (imageUrl.startsWith("http")) {
			return imageUrl;
		}
		
		if (imageUrl.startsWith("/")) {
			return Url.BASE_IMAGE_URL + imageUrl;
		}

		return Url.BASE_IMAGE_URL + Url.URL_SPLITTER + imageUrl;
	}

	public static List<String> getImageUrlList(String imageUrl) {

		List<String> imageUrlList = new ArrayList<String>();

		if (imageUrl == null) {
			return imageUrlList;
		}
		String[] imageUrls = imageUrl.split(";");
		for (String string : imageUrls) {
			imageUrlList.add(getImageUrl(string));
		}

		return imageUrlList;
	}

	public static List<String> getImageUrlList(String imageUrl, String suffix) {

		List<String> imageUrlList = new ArrayList<String>();

		if (imageUrl == null) {
			return imageUrlList;
		}
		String[] imageUrls = imageUrl.split(suffix);
		for (String string : imageUrls) {
			imageUrlList.add(getImageUrl(string));
		}

		return imageUrlList;
	}

	public static String getFirstImageUrl(String imageUrl) {

		if (imageUrl == null) {
			return "";
		}
		String[] imageUrls = imageUrl.split(";");
		if (imageUrls.length == 0) {
			return "";
		}

		return getImageUrl(imageUrls[0]);
	}

	public static Bitmap decodeScaleImage(String imageUrl, int width, int height) {
		Bitmap source = BitmapFactory.decodeFile(imageUrl);
		if (source==null) {
			return null;
		}
		height = width / source.getWidth() * source.getHeight();
		Bitmap bitmap = ThumbnailUtils.extractThumbnail(source, width, height);
		return bitmap;

	}
}
