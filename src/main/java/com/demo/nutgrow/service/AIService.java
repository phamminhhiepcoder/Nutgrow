package com.demo.nutgrow.service;

import com.demo.nutgrow.dto.AnalysisResult;
import com.demo.nutgrow.dto.QuizResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public AnalysisResult analyze(String trainText) {
        try {
            String prompt = createPrompt(trainText);

            Map<String, Object> request = Map.of(
                    "model", model,
                    "input", prompt);

            String responseBody = webClient.post()
                    .uri("https://api.openai.com/v1/responses")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("OpenAI RAW response: {}", responseBody);

            Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);

            List<Map<String, Object>> output = (List<Map<String, Object>>) map.get("output");

            Map<String, Object> message = output.stream()
                    .filter(o -> "message".equals(o.get("type")))
                    .findFirst()
                    .orElseThrow();

            List<Map<String, Object>> content = (List<Map<String, Object>>) message.get("content");

            String text = (String) content.get(0).get("text");

            return objectMapper.readValue(text, AnalysisResult.class);

        } catch (Exception e) {
            log.error("Analyze error", e);
            throw new RuntimeException(e);
        }
    }

    private String createPrompt(String documentText) {
        String truncated = documentText.length() > 12000
                ? documentText.substring(0, 12000)
                : documentText;

        return String.format("""
                Bạn là AI hỗ trợ học tập.
                Nhiệm vụ của bạn là PHÂN TÍCH CHI TIẾT tài liệu học tập sau cho HỌC SINH.

                YÊU CẦU BẮT BUỘC:
                1. Viết RẤT CHI TIẾT, GIẢI THÍCH KỸ
                2. Diễn giải lại bằng lời văn dễ hiểu
                3. Có ví dụ minh họa nếu tài liệu có kiến thức toán
                4. Không được viết quá ngắn

                =========================
                NỘI DUNG TÀI LIỆU:
                %s
                =========================

                HÃY THỰC HIỆN:

                (A) TÓM TẮT CHI TIẾT:
                - Viết 6–10 câu
                - Giải thích tài liệu nói về chủ đề gì
                - Vai trò của kiến thức này trong học tập
                - Ứng dụng thực tế hoặc trong giải toán

                (B) PHÂN TÍCH THEO PHẦN:
                - Chia thành các PHẦN / CHƯƠNG logic
                - Mỗi phần cần:
                  + Giải thích khái niệm
                  + Trình bày quy tắc / dấu hiệu
                  + Giải thích vì sao quy tắc đó đúng
                  + Nêu ví dụ minh họa (nếu có thể)

                =========================
                BẮT BUỘC TRẢ VỀ JSON ĐÚNG FORMAT SAU:
                {
                  "summary": "Tóm tắt rất chi tiết (6–10 câu)...",
                  "parts": [
                    {
                      "title": "Phần 1: ...",
                      "content": "Giải thích chi tiết, có ví dụ, diễn giải dễ hiểu..."
                    },
                    {
                      "title": "Phần 2: ...",
                      "content": "Phân tích kỹ kiến thức, ứng dụng, ví dụ..."
                    }
                  ]
                }

                LƯU Ý CỰC KỲ QUAN TRỌNG:
                - KHÔNG viết ngắn
                - KHÔNG liệt kê sơ sài
                - KHÔNG thêm chữ ngoài JSON
                - Viết như sách giáo khoa cho học sinh
                """, truncated);
    }

    public QuizResult generateQuiz(String documentText) {
        try {
            String truncated = documentText.length() > 12000
                    ? documentText.substring(0, 12000)
                    : documentText;

            String prompt = """
                    Bạn là AI hỗ trợ học tập.
                    Hãy tạo 10 câu hỏi trắc nghiệm từ tài liệu sau.

                    NỘI DUNG:
                    %s

                    Trả về JSON đúng format:
                    {
                      "questions": [
                        {
                          "question": "...",
                          "options": ["A","B","C","D"],
                          "answer": 0
                        }
                      ]
                    }
                    """.formatted(truncated);

            Map<String, Object> request = Map.of(
                    "model", model,
                    "input", prompt);

            String responseBody = webClient.post()
                    .uri("https://api.openai.com/v1/responses")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("OpenAI RAW quiz response: {}", responseBody);

            Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);

            List<Map<String, Object>> output = (List<Map<String, Object>>) map.get("output");

            Map<String, Object> message = output.stream()
                    .filter(o -> "message".equals(o.get("type")))
                    .findFirst()
                    .orElseThrow();

            List<Map<String, Object>> content = (List<Map<String, Object>>) message.get("content");

            String text = (String) content.get(0).get("text");

            return objectMapper.readValue(text, QuizResult.class);

        } catch (Exception e) {
            log.error("Quiz error", e);
            throw new RuntimeException(e);
        }
    }

}