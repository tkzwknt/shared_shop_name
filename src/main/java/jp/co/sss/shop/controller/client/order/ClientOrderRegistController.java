package jp.co.sss.shop.controller.client.order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;

/**
 * 注文処理コントローラー
 */
@Controller
public class ClientOrderRegistController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * お届け先入力画面 表示処理
	 *
	 * @param session    セッションスコープへの保存
	 * @return /client/order/address/inputへリダイレクト
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
	public String addressInput(HttpSession session) {
		//セッションスコープのuser情報をloggedInUserに入れる
		UserBean loggedInUser = (UserBean) session.getAttribute("user");
		User user = userRepository.getReferenceById(loggedInUser.getId());

		UserBean userBean = new UserBean();
		//userがnullでなければuserBeanにuserの内容をコピー
		if (user != null) {
			BeanUtils.copyProperties(user, userBean);
		}
		//OrderBean型の変数にuserBeanの内容をコピー
		OrderBean orderBean = new OrderBean();
		BeanUtils.copyProperties(userBean, orderBean);
		//支払方法をクレジットカードに初期設定
		orderBean.setPayMethod(1);
		session.setAttribute("orderForm", orderBean);

		return "redirect:/client/order/address/input";
	}

	/**
	 * お届け先入力画面 表示処理
	 *
	 * @param model    viewへの値渡し  session    セッションスコープへの保存
	 * @return address_input.htmlへ遷移
	 */
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.GET)
	public String addressInputView(Model model, HttpSession session) {
		//orderFormのセッションスコープ情報をorderBean変数に入れる
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");
		model.addAttribute("orderForm", orderBean);
		//resultのセッション情報を取得
		BindingResult result = (BindingResult) session.getAttribute("result");
		//resultにエラー情報が入っていたら、userFormにエラー情報を入れ、resultの情報を破棄
		if (result != null) {
			model.addAttribute("org.springframework.validation.BindingResult.userForm", result);
			session.removeAttribute("result");
		}
		return "client/order/address_input";
	}

	/**
	 * 支払方法入力画面 表示処理
	 *
	 * @param  orderForm  orderFormの情報を取得、エラーチェック済み  result   エラー内容取得   session    セッションスコープへの保存
	 * @return エラーがあれば/client/order/address/inputへリダイレクト　なければ/client/order/payment/inputへリダイレクト
	 */
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.POST)
	public String paymentInput(@Valid @ModelAttribute OrderForm orderForm, BindingResult result, HttpSession session) {
		//resultにエラーがあればセッションスコープに保存しaddress/inputへリダイレクト
		if (result.hasErrors()) {
			session.setAttribute("result", result);
			return "redirect:/client/order/address/input";
		}
		//orderFormの情報をorderBeanに入れ各種情報を入力
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");
		if (orderBean != null) {
			orderBean.setPostalCode(orderForm.getPostalCode());
			orderBean.setAddress(orderForm.getAddress());
			orderBean.setName(orderForm.getName());
			orderBean.setPhoneNumber(orderForm.getPhoneNumber());
			session.setAttribute("orderForm", orderBean);
		}
		
		return "redirect:/client/order/payment/input";
	}

	/**
	 * 支払方法入力画面 表示処理
	 *
	 * @param  model    viewへの値渡し   session    セッションスコープへの保存
	 * @return orderBeanがnullなら/address/inputへリダイレクト　"client/order/payment_input"でビューへ遷移
	 */
	@RequestMapping(path = "/client/order/payment/input", method = RequestMethod.GET)
	public String paymentInputView(Model model, HttpSession session) {
		// セッションスコープから入力フォーム情報を取得
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");
		if (orderBean == null) {
			return "redirect:/client/order/address/input";
		}
		// 入力フォーム情報をリクエストスコープに設定
		model.addAttribute("orderBean", orderBean);
		return "client/order/payment_input";
	}

	/**
	 * 支払方法入力画面 表示処理
	 *
	 * @param  orderForm  orderFormの情報を取得、エラーチェック済み  model    viewへの値渡し   session    セッションスコープへの保存
	 * @return /order/checkへリダイレクト
	 */
	@RequestMapping(path = "/client/order/check", method = RequestMethod.POST)
	public String orderCheck(@ModelAttribute OrderForm orderForm, Model model, HttpSession session) {
		// セッションスコープから入力フォーム情報を取得
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");
		//orderBeanがnullでなければ支払方法をセットしセッションスコープに保存
		if (orderBean != null) {
			orderBean.setPayMethod(orderForm.getPayMethod());
			session.setAttribute("orderForm", orderBean);
		}
		return "redirect:/client/order/check";
	}

	
	/**
	 * 注文確認画面 表示処理
	 *
	 * @param   model    viewへの値渡し   session    セッションスコープへの保存
	 * @return check.htmlに遷移
	 */
	@RequestMapping(path = "/client/order/check", method = RequestMethod.GET)
	public String orderCheckView(Model model, HttpSession session) {
		// セッションスコープから注文情報を取得
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");

		// セッションスコープから買い物かご情報を取得
		List<BasketBean> basketBeans = (List<BasketBean>) session.getAttribute("basketBeans");

		// 更新後の買い物かごリスト、表示用の注文商品リスト、警告メッセージ用リストを用意
		List<BasketBean> newBasketBeans = new ArrayList<>();
		List<OrderItemBean> orderItemBeans = new ArrayList<>();
		List<String> itemNameListLessThan = new ArrayList<>();
		List<String> itemNameListZero = new ArrayList<>();

		int total = 0; 

		if (basketBeans != null) {
			// 買い物かご内の商品を1つずつチェック
			for (BasketBean basketBean : basketBeans) {

				// 注文商品をDBから取得
				Item item = itemRepository.findById(basketBean.getId()).orElse(null);

				if (item != null) {
					int stock = item.getStock();

					// 在庫切れ商品の場合買い物かごから削除
					if (stock == 0) {
						itemNameListZero.add(item.getName());
						continue;
					}

					// 在庫不足商品の場合
					if (stock < basketBean.getOrderNum()) {
						itemNameListLessThan.add(item.getName());
						// 在庫数にあわせて、買い物かご情報を更新
						basketBean.setOrderNum(stock);
					}

					// DBの最新の在庫数をBasketBeanにセット
					basketBean.setStock(stock);

					// 更新された買い物かご情報を新しいリストに保持
					newBasketBeans.add(basketBean);

					//OrderItemBeanの作成と金額計算
					OrderItemBean orderItemBean = new OrderItemBean();
					orderItemBean.setId(item.getId());
					orderItemBean.setName(item.getName());
					orderItemBean.setPrice(item.getPrice());
					orderItemBean.setImage(item.getImage());
					orderItemBean.setOrderNum(basketBean.getOrderNum());
					
					// 商品ごとの金額小計を算出
					int subtotal = item.getPrice() * basketBean.getOrderNum();
					orderItemBean.setSubtotal(subtotal);

					// 注文商品情報リストに保存
					orderItemBeans.add(orderItemBean);

					// 合計金額を算出
					total += subtotal;
				}
			}
		}

		// 在庫状況を反映した買い物かご情報をセッションに保存
		session.setAttribute("basketBeans", newBasketBeans);

		// 在庫不足、在庫切れ商品がある場合、エラー文をリクエストスコープに保存
		if (!itemNameListZero.isEmpty()) {
			model.addAttribute("itemNameListZero", itemNameListZero);
		}
		if (!itemNameListLessThan.isEmpty()) {
			model.addAttribute("itemNameListLessThan", itemNameListLessThan);
		}

		// 合計金額、注文商品情報リスト、注文入力フォーム情報をリクエストスコープに設定
		model.addAttribute("total", total);
		model.addAttribute("orderItemBeans", orderItemBeans);
		model.addAttribute("orderForm", orderBean);

		return "client/order/check";
	}

	/**
	 * お届け先入力画面 表示処理
	 *
	 * @return /address/inputへリダイレクト
	 */
	@RequestMapping(path = "/client/order/payment/back", method = RequestMethod.POST)
	public String returnAddressInput() {
		return "redirect:/client/order/address/input";
	}

	/**
	 * 注文完了画面 表示処理
	 *
	 * @param  session    セッションスコープへの保存
	 * @return /order/completeへリダイレクト
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.POST)
	public String orderComplete(HttpSession session) {
		//orderFormとuser、basketBeans情報を取得
		OrderBean orderBean = (OrderBean) session.getAttribute("orderForm");
		UserBean userBean = (UserBean) session.getAttribute("user");
		List<BasketBean> basketBeans = (List<BasketBean>) session.getAttribute("basketBeans");

		//ordersテーブルの登録処理
		if (orderBean != null && userBean != null) {
			Order order = new Order();

			// BeanUtilsを用いて、orderBeanをorderへコピー
			BeanUtils.copyProperties(orderBean, order);
			order.setId(null);

			// userIdを個別にセット
			User user = new User();
			user.setId(userBean.getId());
			order.setUser(user);
			
			//日付入力処理
			// java.sql.Date 型に変換して当日の日付をセット
			order.setInsertDate(new java.sql.Date(System.currentTimeMillis()));

			// フォーマットの指定
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			orderBean.setInsertDate(sdf.format(order.getInsertDate()));

			// ordersテーブルに登録
			orderRepository.save(order);

			// OrderItemテーブルへ保存
			if (basketBeans != null) {
				//商品を一つ一つ保存
				for (BasketBean basket : basketBeans) {
					OrderItem orderItem = new OrderItem();
					orderItem.setOrder(order);

					Item item = new Item();
					item.setId(basket.getId());
					//各注文情報をセット
					orderItem.setItem(item);
					orderItem.setQuantity(basket.getOrderNum());
					Item dbItem = itemRepository.getReferenceById(basket.getId());
					orderItem.setPrice(dbItem.getPrice());

					orderItemRepository.save(orderItem);
				}
			}
		}

		// セッションの情報をクリア
		session.removeAttribute("orderForm");
		session.removeAttribute("basketBeans");

		return "redirect:/client/order/complete";
	}

	/**
	 * 注文完了画面 表示処理
	 *
	 * @return complete.htmlへ遷移
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET)
	public String orderCompleteFinish() {
		return "client/order/complete";
	}
}