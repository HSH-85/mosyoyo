package com.project.msy.payment.kakao;

import com.project.msy.config.KakaoPayProperties;
import com.project.msy.payment.kakao.dto.KakaoPayApproveResponse;
import com.project.msy.payment.kakao.dto.KakaoPayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService {
    private final RestTemplate restTemplate;
    private final KakaoPayProperties props;

    public KakaoPayReadyResponse ready(Long orderId, String userId, String itemName, int totalAmount) {
        String url = "https://kapi.kakao.com/v1/payment/ready";

        System.out.println("🔑 [DEBUG] Kakao REST Key → " + props.getRestKey());
        System.out.println("🆔 [DEBUG] Kakao CID      → " + props.getCid());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + props.getRestKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", props.getCid());
        params.add("partner_order_id", orderId.toString());
        params.add("partner_user_id", userId);
        params.add("item_name", itemName);
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", "0");
        params.add("approval_url",
                props.getFrontHost() + "/payment/result?orderId=" + orderId);
        params.add("cancel_url", props.getHost() + "/api/payment/kakao/cancel");
        params.add("fail_url",   props.getHost() + "/api/payment/kakao/fail");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoPayReadyResponse> resp = restTemplate.postForEntity(url, request, KakaoPayReadyResponse.class);
        return resp.getBody();
    }

    public KakaoPayApproveResponse approve(String tid, Long orderId, String userId, String pgToken) {
        String url = "https://kapi.kakao.com/v1/payment/approve";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + props.getRestKey());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", props.getCid());
        params.add("tid", tid);
        params.add("partner_order_id", orderId.toString());
        params.add("partner_user_id", userId);
        params.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoPayApproveResponse> resp = restTemplate.postForEntity(url, request, KakaoPayApproveResponse.class);
        return resp.getBody();
    }
}