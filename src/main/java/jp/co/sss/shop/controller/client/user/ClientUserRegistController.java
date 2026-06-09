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
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

@Controller

//パス名の省略
@RequestMapping("/client/user/regist")

public class ClientUserRegistController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;
	
	/**
	 * 会員登録入力画面 userFormセッション破棄処理
	 *
	 * @param なし
	 * @return "redirect:/client/user/regist/input" 一つ下のコントローラーに遷移
	 */
	@RequestMapping(path = "/input/init", method = RequestMethod.GET)
	public String registInputInit() {
		//セッションに残っているuserForm情報を削除
		session.removeAttribute("userForm");
		return "redirect:/client/user/regist/input";
	}
	
	/**
	 * 会員登録入力画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "client/user/regist_input"会員登録入力画面に遷移
	 */
	@RequestMapping(path = "/input", method = RequestMethod.GET)
	public String registInput(Model model) {
		//userFormの内容をuserForm変数に代入
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		
		//userFormがnullなら新しくuserFormを作成しセッションスコープに保存
		if (userForm == null) {
			userForm = new UserForm();
			session.setAttribute("userForm", userForm);
		}
		
		//resultにセッションスコープに保存されているエラー情報を代入
		BindingResult result = (BindingResult) session.getAttribute("result");
		//resultにエラー情報が入っていたら、userFormにエラー情報を入れ、resultの情報を破棄
		if (result != null) {
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}
		
		//userFormの値をリクエストスコープに保存
		model.addAttribute("userForm", userForm);
		return "client/user/regist_input";
	}
	
	/**
	 * 会員登録確認画面 表示処理
	 *
	 * @param userForm  フォームを呼び出し   result   エラーチェック
	 * @return "redirect:/client/user/regist/input" 入力エラーなら会員登録入力画面に遷移
	 * @return "redirect:/client/user/regist/check" 会員登録確認画面に遷移
	 */

	@RequestMapping(path = "/check", method = RequestMethod.POST)
	public String registCheck(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
		//初期値で一般会員に設定
		userForm.setAuthority(2); // 一般会員
		//入力チェックにエラーがあるならエラー情報とフォームの情報をセッションスコープに保存し/checkコントローラーに遷移
		if (result.hasErrors()) {
			session.setAttribute("result", result);
			session.setAttribute("userForm", userForm);
			return "redirect:/client/user/regist/input";
		}
		//エラーがなければフォーム情報をセッションスコープに保存し/checkコントローラーに遷移
		session.setAttribute("userForm", userForm);
		return "redirect:/client/user/regist/check";
	}
	
	/**
	 * 会員登録確認画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "redirect:/client/user/regist/input/init" userFromが空なら会員登録入力画面に遷移
	 * @return "client/user/regist_check" 会員登録確認画面に遷移
	 */

	@RequestMapping(path = "/check", method = RequestMethod.GET)
	public String registCheckView(Model model) {
		//userFormの内容をuserForm変数に代入
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		//もしuserFormが空なら/input/initコントローラーに遷移
		if (userForm == null) {
			return "redirect:/client/user/regist/input/init";
		}
		//userFormをリクエストスコープに保存し遷移
		model.addAttribute("userForm", userForm);
		return "client/user/regist_check";
	}
	
	/**
	 * 会員登録完了画面 表示処理
	 *
	 * @param なし
	 * @return "redirect:/client/user/regist/input/init" userFormが空なら/input/initに遷移
	 * @return "redirect:/client/user/regist/complete" /completeコントローラーに遷移
	 */

	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String registComplete() {
		//userFormの内容をuserForm変数に代入
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		//userFormが空なら/input/initコントローラーに遷移
		if (userForm == null) {
			return "redirect:/client/user/regist/input/init";
		}
		//User型のuser変数を作成しuserFormの内容をコピー、セーブ
		User user = new User();
		BeanUtils.copyProperties(userForm, user);
		user = userRepository.save(user);
//		userRepository.save(user);
		//userの内容をセッションスコープに保存し、userFormの内容を破棄
		session.setAttribute("user", user);
		
		session.removeAttribute("userForm");
		return "redirect:/client/user/regist/complete";
	}
	
	/**
	 * 会員登録完了画面 表示処理
	 *
	 * @param なし
	 * @return "client/user/regist_complete" 会員登録完了画面に遷移
	 */

	@RequestMapping(path = "/complete", method = RequestMethod.GET)
	public String registCompleteView() {
		return "client/user/regist_complete";
	}
}