package jp.co.sss.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.repository.FavoriteRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;
@Service
@Transactional
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

	public FavoriteService(FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}

	public List<Favorite> findAll(Integer userId) {
		return favoriteRepository.findByUserIdAndDeleteFlag(userId, Constant.NOT_DELETED);
	}
	/**
	 * お気に入りの登録、削除
	 *
	 * @param userId ログインユーザーID itemId 商品ID
	 */
	public void insertFavorite(Integer userId, Integer itemId) {
		Favorite favoriteItem = favoriteRepository.findByUserIdAndItemId(userId, itemId);
		//お気に入りがあるか検索
		if (favoriteItem == null) {
			Favorite favorite = new Favorite();
			Favorite maxId = favoriteRepository.findFirstByOrderByIdDesc();
			//なければお気に入りIDに1をセットする。
			if (maxId == null) {
				favorite.setId(1);
				//あればお気に入りID+1する。
			} else {
				favorite.setId(maxId.getId() + 1);
			}
			//favoriteに追加する。
			favorite.setDeleteFlag(0);
			favorite.setUser(userRepository.getReferenceById(userId));
			favorite.setItem(itemRepository.getReferenceById(itemId));
			favorite = favoriteRepository.save(favorite);
			//もし、削除フラグが1なら0にする
		} else if (favoriteItem.getDeleteFlag() == 1) {
			favoriteItem.setDeleteFlag(0);
			favoriteItem = favoriteRepository.save(favoriteItem);
			//もし、削除フラグが0なら1にする
		} else if (favoriteItem.getDeleteFlag() == 0) {
			favoriteItem.setDeleteFlag(1);
			favoriteItem = favoriteRepository.save(favoriteItem);
		}
	}

}
