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

	public void insertFavorite(Integer userId, Integer itemId) {
		Favorite favoriteItem = favoriteRepository.findByUserIdAndItemId(userId, itemId);

		if (favoriteItem == null) {

			Favorite favorite = new Favorite();
			Favorite maxId = favoriteRepository.findFirstByOrderByIdDesc();

			if (maxId == null) {
				favorite.setId(1);
			} else {
				favorite.setId(maxId.getId() + 1);
			}

			favorite.setDeleteFlag(0);
			favorite.setUser(userRepository.getReferenceById(userId));
			favorite.setItem(itemRepository.getReferenceById(itemId));
			favorite = favoriteRepository.save(favorite);

		} else if (favoriteItem.getDeleteFlag() == 1) {

			favoriteItem.setDeleteFlag(0);
			favoriteItem = favoriteRepository.save(favoriteItem);

		} else if (favoriteItem.getDeleteFlag() == 0) {

			favoriteItem.setDeleteFlag(1);
			favoriteItem = favoriteRepository.save(favoriteItem);
		}
	}
	
}
