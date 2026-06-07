package jp.co.sss.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FAVORITES")
public class Favorite {
	
	/**
	 * お気に入りID
	 */
	@Id
	@Column(name = "ID")
	private Integer id;
	
	/**
	 * 会員情報
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
	 * 削除フラグ
	 */
	@Column(name="DELETE_FLAG")
	private Integer deleteFlag;
	
	/**
	 * お気に入りIDの取得
	 * @return お気に入りID
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * お気に入りIDのセット
	 * @param id お気に入りID
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
	 * 削除フラグの取得
	 * @return 削除フラグ
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	
	/**
	 * 削除フラグのセット
	 * @param detetefrag 削除フラグ
	 */
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
