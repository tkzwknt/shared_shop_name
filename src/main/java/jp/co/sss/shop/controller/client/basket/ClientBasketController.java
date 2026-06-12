package jp.co.sss.shop.controller.client.basket;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
@Controller
public class ClientBasketController {
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * 買い物かご 追加処理
	 *
	 * @param  id   商品ID   redirectAttributes   Viewとの値受渡し  session ログインユーザー 
	 * @return "redirect:/client/basket/list" 商品の詳細画面
	 */
	@RequestMapping(path = "/client/basket/add" , method = RequestMethod.POST)
	public String basketAdd(@RequestParam("id") Integer id,  HttpSession session,RedirectAttributes redirectAttributes) {
		//Listの生成
		List<BasketBean> basketsList  =(List<BasketBean>)session.getAttribute("basketBeans");
		List<BasketBean> baskets = new ArrayList<>();
		List<String> itemNameListLessThan = new ArrayList<>();
		List<String> itemNameListZero = new ArrayList<>();
		//新規追加商品か判定
		boolean isItem = true;
		//セッションスコープに買い物かご情報があるかを確認
		if(basketsList== null) {
			Item item = itemRepository.findById(id).orElse(null);
			//在庫が無ければ追加しない
			if(item.getStock() == 0) {
				itemNameListZero.add(item.getName());
				session.setAttribute("basketBeans", baskets);
				redirectAttributes.addFlashAttribute("itemNameListZero",itemNameListZero);
				return"redirect:/client/basket/list";
			}
			//商品追加
			BasketBean basketBean = new BasketBean();
			basketBean.setId(id);
			basketBean.setName(item.getName());
			basketBean.setStock(item.getStock());
			basketBean.setOrderNum(basketBean.getOrderNum());
			baskets.add(basketBean);
			session.setAttribute("basketBeans", baskets);
			return"redirect:/client/basket/list";
		}else {
			for(BasketBean beanItem:basketsList) {
				if(beanItem.getId() == id) {
					beanItem.setOrderNum(beanItem.getOrderNum() +1);
					//在庫が足りなければ追加しない
					if(beanItem.getStock()<beanItem.getOrderNum()) {
						beanItem.setOrderNum(beanItem.getOrderNum() -1);
						itemNameListLessThan.add(beanItem.getName());
						redirectAttributes.addFlashAttribute("itemNameListLessThan",itemNameListLessThan);
					}
					//新規商品がない
					isItem =false;
				}
				baskets.add(beanItem);
			}
			//新規商品追加
			if(isItem) {
				//在庫が無ければ追加しない
				Item item = itemRepository.findById(id).orElse(null);
				if(item.getStock() == 0) {
					itemNameListZero.add(item.getName());
					session.setAttribute("basketBeans", baskets);
					redirectAttributes.addFlashAttribute("itemNameListZero",itemNameListZero);
					return"redirect:/client/basket/list";
				}
				//商品追加
				BasketBean basketBean = new BasketBean();
				basketBean.setId(id);
				basketBean.setName(item.getName());
				basketBean.setStock(item.getStock());
				basketBean.setOrderNum(basketBean.getOrderNum());
				baskets.add(basketBean);
			}
				
			session.setAttribute("basketBeans", baskets);
			return"redirect:/client/basket/list";
		}
		
	}
	
	/**
	 * 買い物かご 表示処理
	 *
	 * @param   session ログインユーザー 
	 * @return "redirect:/client/basket/list" 商品の詳細画面
	 */
	@RequestMapping(path="/client/basket/list" ,method = {RequestMethod.GET, RequestMethod.POST})
	public String basketList(HttpSession session) {
		return"client/basket/list";
	}
	
	/**
	 * 買い物かご 商品削除処理
	 *
	 * @param  id   商品ID    session ログインユーザー 
	 * @return "redirect:/client/basket/list" 商品の詳細画面
	 */
	@RequestMapping(path="/client/basket/delete", method = RequestMethod.POST)
	public String basketDelete(@RequestParam("id") Integer id,HttpSession session) {
		//Listの生成
		List<BasketBean> basketsList  =(List<BasketBean>)session.getAttribute("basketBeans");
		List<BasketBean> baskets = new ArrayList<>();
		for(BasketBean beanItem:basketsList) {
			//注文数を減らす
			if(beanItem.getId() == id) {
				beanItem.setOrderNum(beanItem.getOrderNum() -1);
			}
			//注文数が0出なければ追加
			if(beanItem.getOrderNum() != 0) {
				baskets.add(beanItem);
			}
			//買い物かごの中がなければnull
			if(baskets.size() == 0) {
				session.setAttribute("basketBeans",null);
			}else {
				session.setAttribute("basketBeans",baskets);
			}
		}
		
		return"redirect:/client/basket/list";
	}
	
	/**
	 * 買い物かご 全削除処理
	 *
	 * @param   session ログインユーザー 
	 * @return "redirect:/client/basket/list" 商品の詳細画面
	 */
	@RequestMapping(path="/client/basket/allDelete", method = RequestMethod.POST)
	public String basketAllDelete(HttpSession session) {
		session.setAttribute("basketBeans",null);
		return"redirect:/client/basket/list";

	}

}
