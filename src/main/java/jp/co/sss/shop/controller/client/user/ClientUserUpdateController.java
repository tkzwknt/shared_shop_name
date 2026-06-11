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

/**
 * 会員情報更新コントローラ
 */
@Controller
@RequestMapping("/client/user/update")
public class ClientUserUpdateController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 変更入力画面 表示処理
	 *
	 *@param なし
	 * @return 変更入力画面表示処理(GET)へのリダイレクト
	 */
	@RequestMapping(path = "/input", method = RequestMethod.POST)
	public String updateInputPost() {
		// セッションスコープに入力フォーム情報があるかを確認
		UserForm userForm = (UserForm) session.getAttribute("userForm");

		// なければ、ログイン中ユーザーのデータをDBから取得する
		if (userForm == null) {
			UserBean loggedInUser = (UserBean) session.getAttribute("user");
			if (loggedInUser == null) {
				return "redirect:/login";
			}
			// ログインユーザIDを用いてDBから取得
			User user = userRepository.getReferenceById(loggedInUser.getId());

			// 取得データを元に入力画面初期表示用の入力フォーム情報を生成
			userForm = new UserForm();
			BeanUtils.copyProperties(user, userForm);

			// 入力フォーム情報をセッションスコープに保存
			session.setAttribute("userForm", userForm);
		}
		// 変更入力画面表示処理へリダイレクト
		return "redirect:/client/user/update/input";
	}

	/**
	 * 変更入力画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "client/user/update_input"　変更入力画面に遷移
	 */
	@RequestMapping(path = "/input", method = RequestMethod.GET)
	public String updateInputGet(Model model) {
		// セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		//		if (userForm == null) {
		//			return "redirect:/client/user/detail";
		//		}
		// 入力フォーム情報をリクエストスコープに設定
		model.addAttribute("userForm", userForm);

		// セッションスコープに入力エラー情報がある場合
		//		if (session.getAttribute("Errors") != null) {
		//			// 取得した入力エラー情報をリクエストスコープに設定
		//			model.addAttribute("Errors", session.getAttribute("Errors"));
		//			// セッションスコープから、入力エラー情報を削除
		//			session.removeAttribute("Errors");
		//		}
		//resultにセッションスコープに保存されているエラー情報を代入
		BindingResult result = (BindingResult) session.getAttribute("result");
		//resultにエラー情報が入っていたら、userFormにエラー情報を入れ、resultの情報を破棄
		if (result != null) {
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}
		return "client/user/update_input";
	}

	/**
	 * 変更確認画面への遷移・入力値チェック処理（確認ボタン押下時）
	 *
	 * @param userForm フォームの入力値　result   入力チェックの判定結果
	 * @return "redirect:/client/user/update/input" 変更入力画面表示処理にリダイレクト
	 * @return "redirect:/client/user/update/check" 変更確認画面表示処理にリダイレクト
	 */
	@RequestMapping(path = "/check", method = RequestMethod.POST)
	public String updateCheckPost(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
		// セッションスコープから取得した値で不足情報を補完
		UserForm sessionForm = (UserForm) session.getAttribute("userForm");
		if (sessionForm != null) {
			userForm.setId(sessionForm.getId());
			userForm.setAuthority(sessionForm.getAuthority());
		}

		// 画面から入力されたフォームをセッションに保存
		session.setAttribute("userForm", userForm);

		// 入力エラーがある場合
		if (result.hasErrors()) {
			// 入力エラー情報をセッションスコープに設定
			session.setAttribute("result", result);
			// 変更入力画面表示処理にリダイレクト
			return "redirect:/client/user/update/input";
		}

		// 変更確認画面表示処理にリダイレクト
		return "redirect:/client/user/update/check";
	}

	/**
	 * 変更確認画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "client/user/update_check"　変更確認画面に遷移
	 * @return "redirect:/client/user/update/input" userFormがnullなら/inputのコントローラーに遷移
	 */
	@RequestMapping(path = "/check", method = RequestMethod.GET)
	public String updateCheckGet(Model model) {
		// セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			return "redirect:/client/user/update/input";
		}
		// 入力フォーム情報をリクエストスコープに設定
		model.addAttribute("userForm", userForm);

		return "client/user/update_check";
	}

	/**
	 * 変更完了処理（登録ボタン押下時）
	 *
	 * @return "redirect:/client/user/update/complete"　変更完了画面表示処理にリダイレクト
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String updateCompletePost() {
		// セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		if (userForm == null) {
			return "redirect:/client/user/update/input";
		}

		// 入力フォーム情報を元にDBから最新エンティティを取得し、更新内容をコピーしてDB更新
		User user = userRepository.getReferenceById(userForm.getId());
		BeanUtils.copyProperties(userForm, user, "deleteFlag", "insertDate");
		userRepository.save(user);

		// ログインユーザの会員変更のため、セッションスコープの会員情報を更新
		UserBean userBean = new UserBean();
		BeanUtils.copyProperties(user, userBean);
		session.setAttribute("user", userBean);

		// セッションスコープの入力フォーム情報削除
		session.removeAttribute("userForm");

		// 変更完了画面表示処理にリダイレクト
		return "redirect:/client/user/update/complete";
	}

	/**
	 * 変更完了画面 表示処理
	 *
	 * @return "client/user/update_complete"　変更完了画面表示処理にリダイレクト
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.GET)
	public String updateCompleteGet() {
		return "client/user/update_complete";
	}
}