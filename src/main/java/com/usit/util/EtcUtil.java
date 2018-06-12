package com.usit.util;

import java.security.SecureRandom;

public class EtcUtil {

    /**
     * 특정숫자 집합에서 랜덤 숫자를 구하는 기능 시작숫자와 종료숫자 사이에서 구한 랜덤 숫자를 반환.
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getRandomNum(int startNum, int endNum) {
        int randomNum = 0;

        try {
            // 랜덤 객체 생성
            SecureRandom rnd = new SecureRandom();

            do {
                // 종료숫자내에서 랜덤 숫자를 발생시킨다.
                randomNum = rnd.nextInt(endNum + 1);
            } while (randomNum < startNum); // 랜덤 숫자가 시작숫자보다 작을경우 다시 랜덤숫자를
                                            // 발생시킨다.
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e); // 2011.10.10 보안점검 후속조치
        }

        return randomNum;
    }

}
