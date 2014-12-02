package edu.virginia.cs2110.ghosthunter17;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.SparseArray;

public class ResourceManager {
	private static SparseArray<Bitmap> images;
	private static SparseArray<MediaPlayer> sounds;
	public static Context c;
	
	static {
		images = new SparseArray<Bitmap>();
		sounds = new SparseArray<MediaPlayer>();
	}
	
	static Bitmap getImage(int id) {
		if (images.indexOfKey(id) >= 0) {
			return images.get(id);
		}
		else {
			Bitmap b = BitmapFactory.decodeResource(c.getResources(), id);
			images.put(id, b);
			return b;
		}
	}
	
	static MediaPlayer loadSong(int id) {
		if (sounds.indexOfKey(id) >= 0) {
			return sounds.get(id);
		}
		else {
			MediaPlayer p = MediaPlayer.create(c, id);
			sounds.put(id, p);
			return p;
		}
	}

}
