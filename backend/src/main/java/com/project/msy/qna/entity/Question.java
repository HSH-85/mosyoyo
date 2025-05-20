package com.project.msy.qna.entity;

import com.project.msy.facility.entity.Facility;
import com.project.msy.product.entity.Product;
import com.project.msy.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 상품 문의일 경우 연결 (nullable 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    // ✅ 시설 문의일 경우 연결 (nullable 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = true)
    private Facility facility;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ✅ 전체 필드 생성자
    public Question(String title, String content, User user, Product product, Facility facility) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.product = product;
        this.facility = facility;
    }

    // ✅ 상품 문의용 생성자
    public Question(String title, String content, User user, Product product) {
        this(title, content, user, product, null);
    }

    // ✅ 시설 문의용 생성자
    public Question(String title, String content, User user, Facility facility) {
        this(title, content, user, null, facility);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
