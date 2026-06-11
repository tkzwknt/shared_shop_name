package jp.co.sss.shop.form;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewForm {
	
	/**
	 * 会員ID
	 */
	@NotNull
	private Integer userId;
	/**
	 * 商品ID
	 */
	@NotNull
	private Integer itemId;
	
	/**
	 * タイトル
	 */
	@NotBlank
	@Size(min = 1, max = 100, message = "{text.maxsize.message}")
	private String title;
	
	/**
	 * 本文
	 */
	@NotBlank
	@Size(min = 1, max = 1000, message = "{text.maxsize.message}")
	private String comment;
	
	/**
	 * 評価
	 */
	private Integer rating;
	
	/**
	 * 会員IDの取得
	 * @return 会員ID
	 */
	public Integer getUserId() {
		return userId;
	}
	
	/**
	 * 会員IDのセット
	 * @param userId 会員ID
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * 商品IDの取得
	 * @return 商品ID
	 */
	public Integer getItemId() {
		return itemId;
	}
	
	/**
	 * 商品IDのセット
	 * @param itemId 商品ID
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * タイトルの取得
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * タイトルのセット
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 本文の取得
	 * @return 本文
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * 本文のセット
	 * @param comment 本文
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 評価の取得
	 * @return 評価
	 */
	public Integer getRating() {
		return rating;
	}
	
	/**
	 * 評価のセット
	 * @param rating   評価
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}	

}
