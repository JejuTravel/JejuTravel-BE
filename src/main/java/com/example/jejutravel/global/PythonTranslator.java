package com.example.jejutravel.global;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PythonTranslator {

    public static String translate(String text, String src, String dest) {
        String translatedText = "";

        try {
            // 텍스트를 UTF-8로 인코딩하여 파이썬에 전달
            String utf8Text = new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            ProcessBuilder pb = new ProcessBuilder("python", "src/main/resources/translator.py", utf8Text, src, dest);
            pb.environment().put("PYTHONIOENCODING", "UTF-8");

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 파이썬 스크립트의 출력을 읽어온다
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = in.readLine()) != null) {
                translatedText += line;
            }

            while ((line = error.readLine()) != null) {
                System.err.println("Python 오류 출력: " + line);
            }
            in.close();
            error.close();
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return translatedText;
    }

    public static void main(String[] args) {
        // 테스트를 위한 main 메서드
        // Debug 해볼 때, 파이썬 경로 맨 앞에 JejuTravel-BE/ 추가하기
        String result = translate("韩国医院", "zh-cn", "ko");  // 중국어 -> 한국어
        // String result = translate("한국병원", "ko", "zh-cn");  // 한국어 -> 중국어
        // String result = translate("서귀포", "ko", "zh-cn");  // 한국어 -> 중국어
        System.out.println("번역 결과: " + result);
    }
}
