package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query("SELECT i FROM Item i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC,i.id DESC")
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
			@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	public Item findByNameAndDeleteFlag(String name, int notDeleted);

	/**
	 * 削除フラグを条件に注文数が多い順に検索
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	@Query("SELECT i FROM Item i LEFT JOIN OrderItem o ON o.item = i WHERE i.deleteFlag = :deleteFlag GROUP BY i.id, i.name, i.price, i.stock, i.description, i.image, i.category, i.mapX, i.mapY, i.insertDate, i.deleteFlag ORDER BY COUNT(o) DESC")
	List<Item> findByDeleteFlagOrderByOrderItemCountDesc(Integer deleteFlag);

	/**
	 * 削除フラグを条件にして商品登録順に検索
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	List<Item> findByDeleteFlagOrderByInsertDateDesc(Integer deleteFlag);

	List<Item> findByDeleteFlagAndCategoryIdOrderByInsertDateDesc(Integer deleteFlag, Integer categoryId);

	//売れ筋淳カテゴリ対応メソッド
	@Query("""
			SELECT i FROM Item i
			LEFT JOIN OrderItem o ON o.item = i
			WHERE i.deleteFlag = :deleteFlag
			AND i.category.id = :categoryId
			GROUP BY i.id, i.name, i.price, i.stock, i.description, i.image,
			         i.category, i.mapX, i.mapY, i.insertDate, i.deleteFlag
			ORDER BY COUNT(o) DESC
			""")
	List<Item> findByDeleteFlagAndCategoryIdOrderByOrderItemCountDesc(
			@Param("deleteFlag") Integer deleteFlag,
			@Param("categoryId") Integer categoryId);

}
