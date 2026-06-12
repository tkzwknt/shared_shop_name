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
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 会員退会コントローラ
 */
@Controller
@RequestMapping("/client/user/delete")
public class ClientUserDeleteController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HttpSession session;

	/**
	 * 退会確認画面　遷移処理
	 *
	 * @return "redirect:/client/user/delete/check" 削除確認画面表示処理へリダイレクト
	 */
	@RequestMapping(path = "/check", method = RequestMethod.POST)
	public String deleteCheckPost() {
		// セッションからログイン中のユーザ情報を取得
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		
		// 削除対象の情報をデータベースから取得
		User user = userRepository.getReferenceById(loggedInUser.getId());
		
		// 取得データを元に削除確認画面表示用の入力フォーム情報を新規生成
		UserForm userForm = new UserForm();
		BeanUtils.copyProperties(user, userForm);
		
		// 入力フォーム情報をセッションスコープに保存
		session.setAttribute("userForm", userForm);
		
		// 削除確認画面表示処理へリダイレクト
		return "redirect:/client/user/delete/check";
	}

	/**
	 * 退会確認画面 表示処理
	 *
	 * @param model Viewとの値受渡し
	 * @return "client/user/delete_check" 退会確認画面に遷移
	 */
	@RequestMapping(path = "/check", method = RequestMethod.GET)
	public String deleteCheckGet(Model model) {
		// セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");
		
		// 入力フォーム情報をリクエストスコープに設定
		model.addAttribute("userForm", userForm);
		
		return "client/user/delete_check";
	}

	/**
	 * 退会完了画面　表示処理
	 *
	 * @return "redirect:/client/user/delete/complete" 削除完了画面表示処理にリダイレクト
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String deleteCompletePost() {
		// セッションスコープから入力フォーム情報を取得
		UserForm userForm = (UserForm) session.getAttribute("userForm");

		// DBから対象のユーザ情報を取得し、論理削除フラグを立てて保存
		User user = userRepository.getReferenceById(userForm.getId());
		user.setDeleteFlag(1);
		userRepository.save(user);
		
		// 買い物かごデータ等を削除し、未ログイン状態に変更するためセッションを破棄
		session.invalidate(); 
		
		// 削除完了画面表示処理にリダイレクト
		return "redirect:/client/user/delete/complete";
	}	

	/**
	 * 退会完了画面　表示処理
	 *
	 * @return "client/user/delete_complete"　退会完了画面に遷移
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.GET)
	public String deleteCompleteGet() {
		return "client/user/delete_complete";
	}
}