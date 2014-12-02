package edu.virginia.cs2110.ghosthunter17;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

public class ResourceManager {
	private static SparseArray<Bitmap> images;
	public static Resources res;
	
	static {
		images = new SparseArray<Bitmap>();
	}
	
	static Bitmap getImage(int id) {
		if (images.indexOfKey(id) >= 0) {
			return images.get(id);
		}
		else {
			Bitmap b = BitmapFactory.decodeResource(res, id);
			images.put(id, b);
			return b;
		}
	}

}
