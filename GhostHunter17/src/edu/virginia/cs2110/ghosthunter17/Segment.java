package edu.virginia.cs2110.ghosthunter17;

import java.util.List;

import android.graphics.PointF;

/*
 * T103-17
 * Henry Conklin, hwc5gj
 * Wylie Wang, ww5ts	
 * Cornelius Nelson, cn3dh	
 * Ryan Montgomery, rmm4wf
 */

public class Segment {

	public PointF p1, p2;

	public Segment(PointF p1, PointF p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * Finds the intersection of this line segment and the argument
	 * 
	 * @param s
	 * @return Point of intersection if the two segments intersect, null if the
	 *         lines do not intersect, null if the two segments are colinear and
	 *         overlap
	 */
	public PointF intersect(Segment other) {
		PointF r = new PointF(this.p2.x - this.p1.x, this.p2.y - this.p1.y);
		PointF s = new PointF(other.p2.x - other.p1.x, other.p2.y - other.p1.y);

		float rxs = cross(r, s);
		PointF pq = new PointF(other.p1.x - this.p1.x, other.p1.y - this.p1.y);

		if (rxs != 0) {
			float t = cross(pq, s) / rxs;
			float u = cross(pq, r) / rxs;

			if (t > 0 && t < 1 && u > 0 && u < 1) {
				return new PointF(this.p1.x + r.x * t, this.p1.y + r.y * t);
			} else
				return null;
		} else
			return null;
	}

	public static float cross(PointF v1, PointF v2) {
		return v1.x * v2.y - v2.x * v1.y;
	}
	
	public static PointF raycast(Segment s, List<Segment> segs) {
		float dx = s.p2.x - s.p1.x;
		float dy = s.p2.y - s.p1.y;
		PointF farPoint = new PointF(s.p1.x + dx * 10000, s.p1.y + dy * 10000);
		Segment ray = new Segment(s.p1, farPoint);
		for (Segment o : segs) {
			PointF inter = ray.intersect(o);
			if (inter != null) {
				ray.p2 = inter;
			}
		}
		
		return ray.p2;
	}

}
