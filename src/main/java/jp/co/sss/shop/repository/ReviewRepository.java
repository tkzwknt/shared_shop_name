package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Review;

public interface ReviewRepository  extends JpaRepository<Review, Integer>{
	/**
	 * 会員IDと商品IDを条件にしてレビューを検索
	 * @param userId 会員ID
	 * @param itemId 商品ID
	 * @return レビューエンティティ
	 */
	List<Review> findByUserIdAndItemId(Integer userId,Integer itemId);
	
	/**
	 * 商品IDを条件にして検索
	 * @param itemId 商品ID
	 * @return レビューエンティティ
	 */
	List<Review> findByItemId(Integer itemId);
}
