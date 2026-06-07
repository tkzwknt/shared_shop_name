package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Favorite;
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	//ログインユーザーのお気に入り商品を検索
	List<Favorite>findByUserIdAndDeleteFlag(Integer userId,Integer deleteFlag);
	
	//お気に入りの最初のIDを検索
	Favorite findFirstByOrderByIdDesc();
	
	//ログインユーザーが商品をお気に入りしているかを検索
	Favorite findByUserIdAndItemId(Integer userId,Integer itemId);
}
