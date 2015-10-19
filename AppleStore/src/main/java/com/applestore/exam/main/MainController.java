package com.applestore.exam.main;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/main")
@Controller(value = "mainController")
public class MainController {
	private String tileView = "";

	@RequestMapping(value = "")
	public String main(Locale locale, Model model) {
		System.out.println("메인컨트롤러 호출");
		tileView = "client.main";
		return tileView;
	}
}