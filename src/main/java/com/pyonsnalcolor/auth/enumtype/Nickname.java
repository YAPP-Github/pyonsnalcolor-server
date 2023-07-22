package com.pyonsnalcolor.auth.enumtype;

import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
public enum Nickname {
    DREAM_ELEPHANT("꿈꾸는 코끼리"),
    FLYING_HUMMINGBIRD("날아가는 벌새"),
    SMILING_CAT("웃는 고양이"),
    PUPPY_IN_THE_MOONLIGHT("달빛을 보는 강아지"),
    WHISPERING_SNAKE("소곤소곤 말하는 뱀"),
    STEALTHY_RABBIT("살며시 걷는 토끼"),
    JUMPING_GIRAFFE("뛰어넘는 기린"),
    PANDA_MEDITATING("조용히 명상하는 판다"),
    RACOON_ON_A_WALK("산책하는 라쿤"),
    DANCING_BUTTERFLY("팔랑팔랑 춤추는 나비"),
    PENGUIN_SMILING_SECRETLY("몰래 웃는 펭귄"),
    SLEEPING_MONKEY("잠자는 원숭이"),
    JOLLY_LIZARD("즐겁게 노는 도마뱀"),
    SQUIRREL_WITH_BOTH_CHEEKS("양볼 빵빵 다람쥐"),
    EAGLE_PLAYING_IN_THE_WIND("바람 타고 노는 독수리"),
    SLEEPING_CRANE("잠자는 두루미"),
    HIPPOPOTAMUS_IN_THE_SUN("햇살 받는 하마"),
    LONESOME_LION("나른하게 누워있는 사자"),
    LOUD_LAUGHING_BADGER("소리내 웃는 오소리"),
    WAILING_WOLF("울음소리를 내는 늑대"),
    GALLANT_FROG("담력있게 달리는 개구리"),
    WAITING_TURTLE("참을성있게 기다리는 거북이"),
    PELICANS_FLYING_SOFTLY("부드럽게 날아가는 펠리컨"),
    DANCING_LYNX("춤추는 스라소니"),
    MOUSE_LOOKING_FOR_WATER("물을 찾아가는 쥐"),
    COOKING_PIGEON("요리하는 비둘기"),
    WAITING_SPARROW("기다리는 참새"),
    HIDDEN_FLYING_SQUIRREL("숨어 있는 날다람쥐"),
    MEDITATING_SEAL("명상하는 물개"),
    SMILING_PUFFER_FISH("빵긋 웃는 복어");

    private static final List<Nickname> NICKNAMES = List.of(values());
    private static final int SIZE = NICKNAMES.size();
    private static final Random RANDOM = new Random();

    private final String name;

    Nickname(String name) {
        this.name = name;
    }

    public static String getRandomNickname()  {
        int randomNumber = RANDOM.nextInt(SIZE);
        return NICKNAMES.get(randomNumber).name;
    }
}
