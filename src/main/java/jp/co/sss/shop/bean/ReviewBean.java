package jp.co.sss.shop.bean;

import java.util.Date;

public class ReviewBean {
	
	/**
	 * レビューID
	 */
	private Integer id;
	/**
	 * 会員ID
	 */
	private Integer userId;
	
	/**
	 * 会員名
	 */
	private String userName;
	
	/**
	 * 投稿日
	 */
	private Date createdAtText;
	
	/**
	 * 評価
	 */
	private Integer rating;
	
	/**
	 * タイトル
	 */
	private String title;
	
	/**
	 * 本文
	 */
	private String comment;
	/**
	 * 商品ID
	 */
	private Integer itemId;

	/**
	 * レビューIDの取得
	 * @return レビューID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * レビューIDのセット
	 * @param id レビューID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

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
	 * 会員名の取得
	 * @return 会員名
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 会員名のセット
	 * @param userName 会員名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 投稿日の取得
	 * @return 投稿日
	 */
	public Date getCreatedAtText() {
		return createdAtText;
	}

	public void setCreatedAtText(Date createdAtText) {
		this.createdAtText = createdAtText;
	}

	/**
	 * 評価の取得
	 * @return 評価
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * 投稿日のセット
	 * @param createdAtText 投稿日
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
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
	 * 商品IDの取得
	 * @return 商品ID
	 */
	public Integer getItemId() {
		return itemId;
	}

	/**
	 * 商品IDの取得
	 * @return 商品ID
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	
}
