package com.pyonsnalcolor.member.enumtype;

import lombok.Getter;

import java.util.Random;

@Getter
public enum Nickname {

    DESCRIPTORS("달빛을 보는 ", "양볼 빵빵 ", "즐겁게 노는 ", "잠자는 ", "햇살 받는 ", "귀엽게 웃는 ", "울음소리를 내는 ",
            "열심히 달리는 ", "차분히 기다리는 ", "부드럽게 날아가는 ", "신나게 춤추는 ", "요리하는 ", "기다리는 ", "숨어 있는 ",
            "명상하는 ", "빵긋 웃는 ", "노래하는 ", "공부하는 ", "산책하는 ", "거칠게 우는 ", "뛰어넘는 ", "잠깐 쉬는 ", "농구하는 ",
            "축구하는 ", "야구하는 ", "높이 뛰는 ", "탐험하는 ", "꿈꾸는 ", "세상을 보는 ", "속삭이는 ", "살며시 걷는 ", "헤엄치는 ",
            "점프하는 ", "감동하는 ", "뛰어다니는 ", "청소하는 ", "한껏 꾸민 ", "울부짖는 ", "윙크하는 ", "달려가는 ", "배가 고픈 ",
            "콧노래 부르는 ", "높이 뛰는 ", "콧노래 부르는 "),
    ANIMALS("코끼리", "벌새", "고양이", "강아지", "뱀", "토끼", "기린", "판다", "라쿤", "나비", "펭귄", "원숭이",
            "도마뱀", "다람쥐", "독수리", "두루미", "하마", "사자", "오소리", "늑대", "개구리", "거북이", "펠리컨", "스라소니",
            "쥐", "비둘기", "참새", "날다람쥐", "물개", "너구리", "원숭이", "스콜피온");

    private final String[] words;
    private static final Random random = new Random();

    Nickname(String... words) {
        this.words = words;
    }

    public static String getRandomNickname() {
        StringBuilder randomNickname = new StringBuilder();
        return randomNickname.append(getRandomWord(DESCRIPTORS.words))
                .append(getRandomWord(ANIMALS.words))
                .toString();
    }

    private static String getRandomWord(String[] words)  {
        int index = random.nextInt(words.length);
        return words[index];
    }
}