package jp.co.sss.shop.controller.client.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller
@RequestMapping("/client/user/update")
public class ClientUserUpdateController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 会員登録変更画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "redirect:/login" ログインユーザーがいないならログイン画面に遷移
	 * @return "redirect:/client/user/update/input" updateInputViewに遷移
	 */
	@RequestMapping(path = "/input", method = RequestMethod.POST)
	public String updateInput(Model model) {
		//セッションスコープに入力フォーム情報があるかを確認
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		if (loggedInUser == null) {
			return "redirect:/login";
		}

		//セッションスコープからユーザーフォームにある情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		//もしユーザーフォームが空ならログイン中ユーザーのIDで検索しuser変数に格納
		if (userForm == null) {
			User user = userRepository.findById(loggedInUser.getId()).orElse(null);
			userForm = new UserForm();
			//もしuser情報があればuserの内容をuserFormにコピー
			if (user != null) {
				BeanUtils.copyProperties(user, userForm);
			}
			//userFormをセッションスコープに保存
			session.setAttribute("userForm", userForm);
		}
		return "redirect:/client/user/update/input";

	}
	
	/**
	 * 会員登録変更画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "client/user/update_input" 会員登録変更画面に遷移
	 */
	@RequestMapping(path = "/input", method = RequestMethod.GET)
	public String updateInputView(Model model) {
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		model.addAttribute(userForm);

		//resultにセッションスコープに保存されているエラー情報を代入
		BindingResult result = (BindingResult) session.getAttribute("result");
		//resultにエラー情報が入っていたら、userFormにエラー情報を入れ、resultの情報を破棄
		if (result != null) {
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}
		//userFormの値をリクエストスコープに保存
		model.addAttribute("userForm", userForm);

		return "client/user/update_input";
	}

	@RequestMapping(path = "/check", method = RequestMethod.POST)
	public String updateCheck(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
		userForm = (UserForm) session.getAttribute("userForm");

		return "redirect:/client/user/update/check";
	}

	@RequestMapping(path = "/check", method = RequestMethod.GET)
	public String updateCheckView(Model model) {

		return "client/user/update_check";
	}

	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String updateComplete() {

		return "redirect:/client/user/update/complete";
	}

	@RequestMapping(path = "/complete", method = RequestMethod.GET)
	public String updateCompleteView() {
		return "client/user/update_complete";
	}
}
