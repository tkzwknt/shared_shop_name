package jp.co.sss.shop.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import jp.co.sss.shop.bean.ReviewBean;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.repository.UserRepository;
@Service
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	/**
	 * レビューを保存
	 *
	 * @param userId ログインユーザーID itemId 商品ID　reviewForm 入力値
	 */
	public void saveReview(Integer userId, Integer itemId,ReviewForm reviewForm) {
		//レビューの新規登録
		Review review = new Review();
		BeanUtils.copyProperties(reviewForm, review);
		review.setUser(userRepository.getReferenceById(userId));
		review.setItem(itemRepository.getReferenceById(itemId));
		reviewRepository.save(review);
	}
	
	/**
	 * レビューを削除
	 *
	 * @param レビューID
	 */
	public void deleteReview(Integer reviewId) {
		//主キーを取り出してレビューを削除
		reviewRepository.deleteById(reviewId);
	}
	
	/**
	 * レビューを編集
	 *
	 * @param reviewId レビューID   model  Viewとの値受渡し
	 */
	public void editReview(Integer reviewId,Model model) {

		//選択されたレビューを呼び出し、Beanにコピー
		Review review = reviewRepository.findById(reviewId).orElse(null);
		ReviewBean reviewBean= new ReviewBean();
		BeanUtils.copyProperties(review, reviewBean);
		reviewBean.setItemId(review.getItem().getId());
		reviewBean.setUserId(review.getUser().getId());
		reviewBean.setUserName(review.getUser().getName());
		//リクエストスコープに保存
		model.addAttribute("review",reviewBean);
	}
	
	/**
	 * レビューを更新
	 *
	 * @param reviewId レビューID reviewForm 入力値  userId ログインユーザーID itemId 商品ID　
	 */
	public void updateReview(Integer reviewId,ReviewForm reviewForm,Integer userId, Integer itemId) {
		//入力された値を入れて保存
		Review review = reviewRepository.findById(reviewId).orElse(null);
		BeanUtils.copyProperties(reviewForm, review);
		review.setUser(userRepository.getReferenceById(userId));
		review.setItem(itemRepository.getReferenceById(itemId));
		reviewRepository.save(review);
	}

}
