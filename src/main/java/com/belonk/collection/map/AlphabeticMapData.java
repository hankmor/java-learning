package com.belonk.collection.map;

import java.util.HashMap;

/**
 * Created by sun on 2021/12/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class AlphabeticMapData extends HashMap<Integer, String> {
	//~ Static fields/constants/initializer

	private static final char[] CAPITAL_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	//~ Instance fields

	private int size;

	//~ Constructors

	public AlphabeticMapData(int size) {
		this.size = size;
		this.fill();
	}

	//~ Methods

	public void fill() {
		if (this.size <= 0) return;
		for (int i = 0; i < this.size; i++) {
			put(i, String.valueOf(CAPITAL_CHARS[i % CAPITAL_CHARS.length]));
		}
	}

	public static void main(String[] args) {
		AlphabeticMapData alphabeticMapData = new AlphabeticMapData(27);
		System.out.println(alphabeticMapData);
	}
}