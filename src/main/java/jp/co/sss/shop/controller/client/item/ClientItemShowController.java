package jp.co.sss.shop.controller.client.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/" , method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
	
		return "index";
	}
	/**
	 * 商品詳細画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "detail" 商品の詳細画面
	 */
	@GetMapping("/client/item/detail/{id}")
	public String detail(@PathVariable Integer id,Model model) {
		//主キーがidの商品のレコードを取得
		Item item = itemRepository.findById(id).orElse(null);
		//Itemの格納するList
		List<Item>entityList = new ArrayList<>();
		//リストにitemを入れる
		entityList.add(item);
		//beanToolsサービスに渡し、ItemBean型のList beanListに戻る
		List<ItemBean> beanList = beanTools.copyEntityListToItemBeanList(entityList);
//		//リクエストスコープにbeanListを保存
		model.addAttribute("item",beanList.get(0));
		return "client/item/detail";
		

	}
}
