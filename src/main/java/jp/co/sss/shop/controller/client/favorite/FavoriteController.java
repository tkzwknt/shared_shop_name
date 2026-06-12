package jp.co.sss.shop.controller.client.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.service.FavoriteService;

/**
 * お気に入り管理　一覧表示機能(一般会員用)のコントローラクラス
 */
@Controller
public class FavoriteController {

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	
	/**
	 * お気に入りサービス
	 */
	private final FavoriteService favoriteService;
	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	/**
	 * お気に入り画面 表示処理
	 *
	 * @param model    Viewとの値受渡し   session ログインユーザー
	 * @return "client/favorite/list" お気に入り一覧
	 */
	@RequestMapping(path = "/client/favorite/list" , method = RequestMethod.GET)
	public String list(Model model,HttpSession session) {
		//ログイン中の会員IDを取得
		Integer userId = ((UserBean) session.getAttribute("user")).getId();

		//お気に入り商品情報を取得し、FavoriteBeanへコピー
		List<Favorite> favorite = favoriteService.findAll(userId);
		List<ItemBean> itemBeanList = beanTools.copyrFavoriteBean(favorite);
		
		//リクエストスコープに保存
		model.addAttribute("favoriteItems",itemBeanList);
		return "client/favorite/list";
	}

	/**
	 * お気に入り 登録処理
	 * @param id    商品id   session ログインユーザー
	 * @return "redirect:/client/item/detail/" +id" 商品詳細画面
	 */
	@RequestMapping(path = "/client/detail/favorite/add" , method = RequestMethod.POST)
	public String adddetail(@RequestParam("id") Integer id,HttpSession session) {
		//ログイン中の会員IDを取得
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		//お気に入りに追加
		favoriteService.insertFavorite(userId,id);
		return "redirect:/client/item/detail/" +id;
	}
	
	@RequestMapping(path = "/client/list/favorite/add" , method = RequestMethod.POST)
	public String addlist(@RequestParam("sortType") Integer sortType,@RequestParam("id") Integer id,HttpSession session) {
		//ログイン中の会員IDを取得
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		//お気に入りに追加
		favoriteService.insertFavorite(userId,id);
		return "redirect:/client/item/list/"+sortType;
	}
	
	/**
	 * お気に入り 削除処理
	 * @param id    商品id   session ログインユーザー
	 * @return "redirect:/favorite/list" お気に入り一覧画面
	 */
	@RequestMapping(path = "/client/favorite/delete" , method = RequestMethod.POST)
	public String delete(@RequestParam("id") Integer id,HttpSession session) {
		//お気に入り商品情報を取得
		Integer userId = ((UserBean) session.getAttribute("user")).getId();
		//削除フラグを1にする
		favoriteService.insertFavorite(userId,id);
		return "redirect:/client/favorite/list";
	}
}
