package ac.kaist.ccs.domain;

import java.util.Comparator;

public class CCSUtils {
	public static double dist(CCSSourceData src1, CCSSourceData src2) {
		double dist = 0;

		dist = Math.sqrt((src1.getX() - src2.getX())
				* (src1.getX() - src2.getX()) + (src1.getY() - src2.getY())
				* (src1.getY() - src2.getY()));

		return dist;
	}
	
	public static class NodePair {
		public CCSSourceData first;
		public CCSSourceData second;

		public NodePair(CCSSourceData first, CCSSourceData second) {
			this.first = first;
			this.second = second;
		}
	}

	public static class SortTuple {
		public double dist;
		public Object firstId;
		public Object secondId;

		public SortTuple(double dist, Object firstId, Object secondId) {
			this.dist = dist;
			this.firstId = firstId;
			this.secondId = secondId;
		}

	}

	public static class NoAscCompare implements Comparator<SortTuple> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(SortTuple arg0, SortTuple arg1) {
			// TODO Auto-generated method stub
			return arg0.dist < arg1.dist ? -1 : arg0.dist > arg1.dist ? 1 : 0;
		}
	}

	public static class NoDecCompare implements Comparator<SortTuple> {

		/**
		 * 오름차순(ASC)
		 */
		@Override
		public int compare(SortTuple arg0, SortTuple arg1) {
			// TODO Auto-generated method stub
			return arg0.dist > arg1.dist ? -1 : arg0.dist < arg1.dist ? 1 : 0;
		}
	}
}
