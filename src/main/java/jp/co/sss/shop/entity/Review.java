package jp.co.sss.shop.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "review")
public class Review {
	/**
	 * レビューID
	 */
	@Id		
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_review_gen")
	@SequenceGenerator(name = "seq_review_gen", sequenceName = "seq_review",
	allocationSize = 1)
	@Column(name = "ID")
	private Integer id;
	
	/**
	 * レビュー情報
	 */
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "id")
	private User user;
	
	/**
	 * 商品情報
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", referencedColumnName = "id")
	private Item item;
	
	/**
	 * タイトル
	 */
	@Column(name="title")
	private String title;
	
	/**
	 * 本文
	 */
	@Column(name ="comments")
	private String comment;
	
	/**
	 * 評価
	 */
	@Column(name ="rating")
	private Integer rating;
	
	/**
	 * 投稿日
	 */
	@CreationTimestamp
	@Column(name="created_at_text")
	private Date createdAtText;
	
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
	 * 会員情報の取得
	 * @return 会員情報
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * 会員情報のセット
	 * @param user 会員情報
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * 商品情報の取得
	 * @return 商品情報
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * 商品情報のセット
	 * @param item 商品情報
	 */
	public void setItem(Item item) {
		this.item = item;
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
	 * @param rating 評価
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
	/**
	 * 投稿日の取得
	 * @return 投稿日
	 */
	public Date getCreatedAtText() {
		return createdAtText;
	}
	
	/**
	 * 投稿日のセット
	 * @param createdAtText 投稿日
	 */
	public void setCreatedAtText(Date createdAtText) {
		this.createdAtText = createdAtText;
	}
	
}
