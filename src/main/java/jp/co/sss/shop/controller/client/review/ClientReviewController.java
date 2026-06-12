package jp.co.sss.shop.controller.client.review;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.form.ReviewForm;
import jp.co.sss.shop.service.ReviewService;
@Controller
public class ClientReviewController {
//	@Autowired
//	ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	public ClientReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	/**
	 * レビュー追加
	 * 
	 * @param  id 商品ID  reviewForm  レビューフォーム session ログインユーザー
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/client/review/add", method = RequestMethod.POST )
    public String reviewadd(@RequestParam("id") Integer id,ReviewForm reviewForm,HttpSession session) {
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		Integer userId=loggedInUser.getId();
		reviewService.saveReview(userId,id,reviewForm);  
		return "redirect:/client/item/detail/" +id;
    }
	
	/**
	 * レビュー削除
	 * 
	 * @param  id 商品ID  reviewId  レビューID
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/client/review/delete", method = RequestMethod.POST )
    public String reviewdelete(@RequestParam("id") Integer id,@RequestParam("reviewId") Integer reviewId) {
    	reviewService.deleteReview(reviewId);
    	return "redirect:/client/item/detail/" +id;
    }
	
	/**
	 * レビュー編集
	 * 
	 * @param   id 商品ID  reviewId  レビューID　　model    Viewとの値受渡し
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/client/review/edit", method = RequestMethod.POST )
    public String reviewedit(@RequestParam("id") Integer id,@RequestParam("reviewId") Integer reviewId,Model model) {
    	reviewService.editReview(reviewId,id,model);
    	return "client/review/edit";
    }
	
	/**
	 * レビュー更新
	 * 
	 * @param   id 商品ID  reviewId  reviewForm  レビューフォーム session ログインユーザー
	 * @return "redirect:/client/item/detail/" +id 商品詳細画面 
	 */
	@RequestMapping(path = "/client/review/update", method = RequestMethod.POST )
    public String reviewupdate(@RequestParam("id") Integer id,@RequestParam("reviewId") Integer reviewId,ReviewForm reviewForm,HttpSession session) {
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		Integer userId=loggedInUser.getId();
    	reviewService.updateReview(reviewId,reviewForm,userId,id);
    	return "redirect:/client/item/detail/" +id;
    }
    
}
