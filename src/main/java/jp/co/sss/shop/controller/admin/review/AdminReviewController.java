package jp.co.sss.shop.controller.admin.review;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.service.ReviewService;

@Controller
public class AdminReviewController {

	@Autowired
	UserRepository userRepository;
	
	private final ReviewService reviewService;
	public AdminReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	
	/**
	 * レビュー削除
	 * 
	 * @param  id 商品ID  reviewId  レビューID
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/admin/item/review/delete", method = RequestMethod.POST )
    public String adminReviewDelete(@RequestParam("id") Integer id,@RequestParam("reviewId") Integer reviewId) {
    	reviewService.deleteReview(reviewId);
    	return "redirect:/admin/item/detail/" +id;
    }
	
	/**
	 * レビュー編集
	 * 
	 * @param   id 商品ID  reviewId  レビューID　　model    Viewとの値受渡し
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/admin/item/review/edit", method = RequestMethod.POST )
    public String adminReviewEdit(@RequestParam("id") Integer id,@RequestParam("reviewId") Integer reviewId,Model model) {
    	reviewService.editReview(reviewId,id,model);
    	return "admin/review/edit";
    }
	
	/**
	 * レビュー更新
	 * 
	 * @param   id 商品ID  reviewId  reviewForm  レビューフォーム session ログインユーザー
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/admin/item/review/update", method = RequestMethod.POST )
    public String adminReviewUpdate(@RequestParam("id") Integer id,@RequestParam("reviewId")Integer reviewId,@RequestParam("userId")Integer userId,ReviewForm reviewForm,HttpSession session) {
		User user = userRepository.findById(userId).orElse(null);
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);
		Integer reviewUserId=userBean.getId();
    	reviewService.updateReview(reviewId,reviewForm,reviewUserId,id);
    	return "redirect:/admin/item/detail/" +id;
    }
    
}
