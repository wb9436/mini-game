package com.zhiyou.wxgame.server.card;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * 牌相关工具
 * 
 * @author WB
 *
 */
public class CardUtils {
	protected final static Logger logger = Logger.getLogger(CardUtils.class);

	/**
	 * 纸牌对应的数值 :黑桃 > 红桃 > 梅花 > 方块 
	 * 黑桃(3~K,A,2) 3~15
	 * 红桃(3~K,A,2) 18~30
	 * 黑桃(3~K,A,2) 33~45
	 * 黑桃(3~K,A,2) 48~60
	 * 
	 */
	public static final int[] values = { 
			3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 
			18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 
			33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 
			48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 
			61, 62
	};

	/**
	 * 初始化纸牌
	 * 
	 * @return
	 */
	public static int[] initCards() {
		int[] cards = new int[0];
		for (int index = 0; index <= 3; index++) {
			for (int cardNum = 3; cardNum <= 15; cardNum++) {
				cards = add(cards, cardNum + 15 * index);
			}
		}
		cards = add(cards, 61);
		cards = add(cards, 62);
		return cards;
	}

	/***
	 * 获取一副纸牌
	 * 
	 * @return
	 */
	public static int[] shuffleCard() {
		return shuffle(initCards());
	}

	/**
	 * 打乱顺序（洗牌）
	 * 
	 * @param tmp
	 * @return
	 */
	public static int[] shuffle(int[] tmp) {
		int length = tmp.length;
//		 Random rand = new Random();
//		 for (int index = length; index > 1; index--) {
//		 int rndIndex = rand.nextInt(index);
//		 int v = tmp[index - 1];
//		 tmp[index - 1] = tmp[rndIndex];
//		 tmp[rndIndex] = v;
//		 }

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int j = random.nextInt(length);
			int v = tmp[i];
			tmp[i] = tmp[j];
			tmp[j] = v;
		}
		return tmp;
	}

	/***
	 * 删除数组中的元素
	 * 
	 * @param a
	 * @param v
	 * @return
	 */
	public static int[] remove(int[] a, int v) {
		int len = a.length;
		int[] dest = new int[len - 1];
		for (int i = 0; i < len; i++) {
			if (a[i] == v) {
				if (i > 0) {
					System.arraycopy(a, 0, dest, 0, i);
				}
				if (i != len - 1) {
					System.arraycopy(a, i + 1, dest, i, len - i - 1);
				}
				break;
			}
		}
		return dest;
	}

	/**
	 * 向数组添加元素
	 * 
	 * @param int[] src
	 * @param v
	 * @return
	 */
	public static int[] add(int[] src, int v) {
		if (src == null) {
			src = new int[] {};
		}
		int srcLen = src.length;
		int[] dest = new int[srcLen + 1];
		System.arraycopy(src, 0, dest, 0, srcLen);
		dest[srcLen] = v;
		return dest;
	}

	/**
	 * 向数组添加数组
	 * 
	 * @param int[] src
	 * @param int[] a
	 * @return
	 */
	public static int[] add(int[] src, int[] a) {
		if (src == null) {
			src = new int[] {};
		}
		if (a == null) {
			a = new int[] {};
		}
		int[] dest = new int[src.length + a.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		System.arraycopy(a, 0, dest, src.length, a.length);
		return dest;
	}

	/**
	 * 获取牌点数
	 * 
	 * @param card
	 * @return
	 */
	public static int getCardNum(int card) {
		int cardNum = card;
		if (card == 15) {
			cardNum = 16;
		} else if (card > 15 && card <= 60) {
			cardNum = (card % 15 > 0) ? (card % 15) : 16;
		} else if (card >= 61) {
			if (card == 61) {
				cardNum = 17;
			} else if (card == 62) {
				cardNum = 18;
			}
		}
		return cardNum;
	}
	
	/**
	 * 统计牌数量
	 * 
	 * @param cards
	 * @return
	 */
	public static int[] countCardsNum(int[] cards) {
		int[] tmp = new int[19];
		for (int index = 0; index < cards.length; index++) {
			int card = cards[index];
			int cardNum = getCardNum(card);
			tmp[cardNum] += 1;
		}
		return tmp;
	}

}
