package jp.co.sss.shop.bean;

/**
 * お気に入りクラス
 */
public class FavoriteBean {
	
	
	
	/**
	 * 会員id
	 */
	private Integer userId;
	/**
	 * 商品id
	 */
	private Integer itemId;
	
	/**
	 * 商品名
	 */
	private String itemName;
	
	/**
	 * 価格
	 */
	private Integer price;
	
	/**
	 * 商品説明
	 */
	private String description;
	
	/**
	 * 商品画像ファイル名
	 */
	private String image;
	
	/**
	 * カテゴリ名
	 */
	private String categoryName;
	
	
	/**
	 * 会員IDの取得
	 * @return 会員ID
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 会員IDのセット
	 * @param id 会員ID
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
	 * @param id 商品ID
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * 商品名の取得
	 * @return 商品名
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * 商品名のセット
	 * @param itemName 商品名
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	/**
	 * 価格の取得
	 * @return 価格
	 */
	public Integer getPrice() {
		return price;
	}
	
	/**
	 * 価格のセット
	 * @param price 価格
	 */
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	/**
	 * 商品説明の取得
	 * @return 商品説明
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 商品説明のセット
	 * @param description 商品説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 商品画像ファイル名の取得
	 * @return 商品画像ファイル名
	 */
	public String getImage() {
		return image;
	}
	
	/**
	 * 画像ファイル名のセット
	 * @param image 画像ファイル名
	 */
	public void setImage(String image) {
		this.image = image;
	}
	
	
	/**
	 * カテゴリ名の取得
	 * @return カテゴリ名
	 */
	public String getCategoryName() {
		return categoryName;
	}
	
	/**
	 * カテゴリ名のセット
	 * @param categoryId カテゴリ名
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	

	
}
