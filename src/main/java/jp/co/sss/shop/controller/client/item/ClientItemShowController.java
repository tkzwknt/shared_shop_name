package jp.co.sss.shop.controller.client.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.ReviewBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.Review;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.ReviewRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

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
	@Autowired
	OrderItemRepository orderItemRepository;
	@Autowired
	ReviewRepository reviewRepository;

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
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {
		// トップ画面だけサイドバーを非表示にし、トップ専用のbodyクラスを付与
		model.addAttribute("layoutNoSidebar", true);
		model.addAttribute("layoutBodyClass", "top_page_body");

		//注文商品一覧を全件検索
		List<OrderItem> orderItemEntityList = orderItemRepository.findAll();
		//Itemの格納するList
		List<Item> entityList = new ArrayList<>();

		//orderItemEntityListに値が入っているか判定する
		if (orderItemEntityList != null) {
			//entityListに削除フラグが0で注文商品が多い順に検索
			entityList = itemRepository.findByDeleteFlagOrderByOrderItemCountDesc(Constant.NOT_DELETED);
			//リクエストスコープにsortType(売れ筋順)を保存
			model.addAttribute("sortType", 2);
		} else {
			//entityListに削除フラグが0で新しく登録した順に検索
			entityList = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED);
			//リクエストスコープにsortType(新規登録順)を保存
			model.addAttribute("sortType", Constant.DEFAULT_SORT_TYPE);
		}

		//beanToolsサービスに渡し、ItemBean型のList beanListに戻る
		List<ItemBean> beanList = beanTools.copyEntityListToItemBeanList(entityList);
		//リクエストスコープにbeanListを保存
		model.addAttribute("items", beanList);
		return "index";
	}

	/**
	 * 商品詳細画面Get 表示処理
	 *
	 * @param  id   商品ID   model   Viewとの値受渡し  session ログインユーザー
	 * @return ""client/item/detail" 商品の詳細画面
	 */
	@RequestMapping(path = "/client/item/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String detailget(@PathVariable Integer id, Model model, HttpSession session) {
		//主キーがidの商品のレコードを取得
		Item item = itemRepository.findById(id).orElse(null);

		//Itemの格納するList
		List<Item> entityList = new ArrayList<>();
		entityList.add(item);

		//beanToolsサービスに渡し、ItemBean型のList beanListに戻る
		List<ItemBean> beanList = beanTools.copyEntityListToItemBeanList(entityList);
		//リクエストスコープにbeanListを保存
		model.addAttribute("item", beanList.get(0));

		boolean isReview = reviewRepository.findByItemId(id).isEmpty();
		if (isReview) {
			model.addAttribute("reviewAverage", null);
			model.addAttribute("reviewCount", null);
			model.addAttribute("reviewList", null);
		} else {
			List<Review> reviewList = reviewRepository.findByItemId(id);
			List<ReviewBean> reviewBeanList = beanTools.copyrReviewBeanList(reviewList);
			int sum = 0;
			for (ReviewBean review : reviewBeanList) {
				sum += review.getRating();
			}
			double avg = sum / (double) reviewList.size();
			model.addAttribute("reviewAverage", avg);
			model.addAttribute("reviewCount", reviewList.size());
			model.addAttribute("reviewLists", reviewList);
		}
		return "client/item/detail";
	}

	/**
	 * 商品一覧画面 表示処理
	 *
	 * @param  id   商品ID   model   Viewとの値受渡し  session ログインユーザー
	 * @return ""client/item/detail" 商品の詳細画面
	 */
	@RequestMapping(path = "/client/item/list/{sortType}", method = { RequestMethod.GET, RequestMethod.POST })
	public String beansList(@PathVariable Integer sortType, Model model, HttpSession session) {
		List<Item> itemList = itemRepository.findByDeleteFlagOrderByInsertDateDesc(0);
		List<ItemBean> itemBeanList = beanTools.copyEntityListToItemBeanList(itemList);

		model.addAttribute("items", itemBeanList);
		return "client/item/list";
	}

}
