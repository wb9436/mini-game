package com.zhiyou.wxgame.test;

import java.util.Arrays;
import java.util.Random;

import com.zhiyou.wxgame.server.card.CardUtils;

public class GameTest {

	public static void main(String[] args) {
//		initCardsTest();
		sendCardTest();
//		testShuffleCard();
	}
	
	public static void initCardsTest(){
		int[] initCards = CardUtils.initCards();
		Arrays.sort(initCards);
		System.out.println("正常顺序:" + Arrays.toString(initCards));
		
		System.out.println("随机顺序:" + Arrays.toString(CardUtils.shuffleCard()));
	}
	
	public static void sendCardTest(){
		int[] cards = CardUtils.shuffleCard();
		for (int i = 0; i < 3; i++) {
			int[] userCards = new int[0];
			for (int j = 0; j < 17; j++) {
				int card = cards[0];
				cards = CardUtils.remove(cards, card);
				userCards = CardUtils.add(userCards, card);
			}
			System.out.println("玩家" + i + "手牌:" + Arrays.toString(CardUtils.countCardsNum(userCards)));
		}
		System.out.println("地主0底牌:" + Arrays.toString(CardUtils.countCardsNum(cards)));
	}
	
	public static void testShuffleCard(){
		int cards[] = CardUtils.initCards();
		Random rand = new Random();
		for (int i = cards.length - 1; i >= 1; i--) {
			int j = rand.nextInt(i);
			int cardTmp = cards[i];
			cards[i] = cards[j];
			cards[j] = cardTmp;
		}
		for (int i = 0; i < 3; i++) {
			int[] userCards = new int[0];
			for (int j = 0; j < 17; j++) {
				int card = cards[0];
				cards = CardUtils.remove(cards, card);
				userCards = CardUtils.add(userCards, card);
			}
			System.out.println("玩家" + i + "手牌:" + Arrays.toString(CardUtils.countCardsNum(userCards)));
		}
		System.out.println("地主0底牌:" + Arrays.toString(CardUtils.countCardsNum(cards)));
	}
	
}
