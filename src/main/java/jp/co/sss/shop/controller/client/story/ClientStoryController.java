package jp.co.sss.shop.controller.client.story;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ブランドストーリー表示機能(一般会員/非会員共通)のコントローラクラス
 */
@Controller
public class ClientStoryController {

	/**
	 * Learn Our Story 画面表示処理
	 *
	 * @param model 画面表示用モデル
	 * @return "client/story/index" ブランドストーリー画面
	 */
	@RequestMapping(path = "/client/story", method = { RequestMethod.GET, RequestMethod.POST })
	public String showStory(Model model) {
		model.addAttribute("layoutNoSidebar", true);
		model.addAttribute("layoutBodyClass", "story_page_body");
		return "client/story/index";
	}
}
