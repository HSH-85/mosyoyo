package com.project.msy.product.repository;

import com.project.msy.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    long countByStockQuantityGreaterThan(int quantity);

    /**
     * 주문 테이블(o.quantity) 기준 Top5 상품
     * 반환 Object[] 배열:
     * [0]=product_id(Long), [1]=name(String), [2]=salesCount(BigInteger),
     * [3]=revenue(BigDecimal), [4]=stockQuantity(Integer)
     */
    @Query(value = """
      SELECT 
        p.id,
        p.name,
        SUM(o.quantity)            AS salesCount,
        SUM(o.total_price)         AS revenue,
        p.stock_quantity           AS stockQuantity,
        p1.name                      AS categoryName
      FROM orders o
      JOIN products p ON o.product_id = p.id
      JOIN product_categories p1 ON p.category_id = p1.id
      WHERE o.status = '결제완료'
      GROUP BY p.id, p.name, p.stock_quantity
      ORDER BY salesCount DESC
      LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findTop5ByOrderVolume();
}