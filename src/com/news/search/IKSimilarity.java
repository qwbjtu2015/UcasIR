package com.news.search;

import org.apache.lucene.search.similarities.DefaultSimilarity;


public class IKSimilarity extends DefaultSimilarity {
	
	/* (non-Javadoc)
	 * @see org.apache.lucene.search.Similarity#coord(int, int)
	 */
	public float coord(int overlap, int maxOverlap) {
		float overlap2 = (float)Math.pow(2, overlap);
		float maxOverlap2 = (float)Math.pow(2, maxOverlap);
		return (overlap2 / maxOverlap2);
	}
}

