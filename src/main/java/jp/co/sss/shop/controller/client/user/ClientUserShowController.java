package jp.co.sss.shop.controller.client.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.repository.UserRepository;

@Controller
public class ClientUserShowController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;
	
	/**
	 * 会員詳細画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "redirect:/login" ログインしていないならログイン画面に遷移
	 * @return "client/user/detail" 会員詳細画面に遷移
	 */
	@RequestMapping(path = "/client/user/detail", method = {RequestMethod.GET, RequestMethod.POST})
	public String showDetail(Model model) {
		//loggedInUser変数にuserの値を代入
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		//もしloggedInUserが空ならログイン画面に遷移
		if (loggedInUser == null) {
			return "redirect:/login";
		}
		//User型のuser変数にログイン中のユーザー情報を取得、もし見つからなければnullを代入
		User user = userRepository.findById(loggedInUser.getId()).orElse(null);
		UserBean userBean = new UserBean();
		//userがnullでなければuserBeanにuserの内容をコピー
		if (user != null) {
			BeanUtils.copyProperties(user, userBean);
		}
		//userBeanをリクエストスコープに保存
		model.addAttribute("userBean", userBean);
		return "client/user/detail";
	}
}