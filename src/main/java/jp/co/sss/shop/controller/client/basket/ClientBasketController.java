package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
@Controller
public class ClientBasketController {
	@Autowired
	ItemRepository itemRepository;
	@RequestMapping(path = "/client/basket/add" , method = RequestMethod.POST)
	public String basketadd(@RequestParam("id") Integer id,  HttpSession session) {
		List<BasketBean> basketsList  =(List<BasketBean>)session.getAttribute("basketBeans");
		List<BasketBean> baskets = new ArrayList<>();
		boolean isItem = true;
		if(basketsList== null) {
			Item item = itemRepository.findById(id).orElse(null);
			BasketBean basketBean = new BasketBean();
			basketBean.setId(id);
			basketBean.setName(item.getName());
			basketBean.setStock(item.getStock());
			basketBean.setOrderNum(basketBean.getOrderNum());
			baskets.add(basketBean);
			session.setAttribute("basketBeans", baskets);
			return"redirect:/client/basket/list";
		}else {
			basketsList = (List<BasketBean>)session.getAttribute("basketBeans");
		}
		for(BasketBean beanItem:basketsList) {
			if(beanItem.getId() == id) {
				beanItem.setOrderNum(beanItem.getOrderNum() +1);
				isItem =false;
			}
			baskets.add(beanItem);
		}
		if(isItem) {
			Item item = itemRepository.findById(id).orElse(null);
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
	
	@RequestMapping(path="/client/basket/list" ,method = RequestMethod.GET)
	public String basketlist(HttpSession session) {
		return"client/basket/list";
	}
	
	@RequestMapping(path="/client/basket/delete", method = RequestMethod.POST)
	public String basketdelete(@RequestParam("id") Integer id,HttpSession session) {
		List<BasketBean> basketsList  =(List<BasketBean>)session.getAttribute("basketBeans");
		List<BasketBean> baskets = new ArrayList<>();
		for(BasketBean beanItem:basketsList) {
			if(beanItem.getId() == id) {
				beanItem.setOrderNum(beanItem.getOrderNum() -1);
			}
			if(beanItem.getOrderNum() != 0) {
				baskets.add(beanItem);
			}
			if(baskets.size() == 0) {
				session.setAttribute("basketBeans",null);
			}else {
				session.setAttribute("basketBeans",baskets);
			}
		}
		
		return"redirect:/client/basket/list";
	}

}
