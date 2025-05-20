package com.project.msy.payment.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoPayApproveResponse {
    private String aid;
    private String tid;
    private String cid;

    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("payment_method_type")
    private String paymentMethodType;

    private Amount amount;

    @Data
    public static class Amount {
        private Integer total;
        @JsonProperty("tax_free")
        private Integer taxFree;
        private Integer vat;
    }
}