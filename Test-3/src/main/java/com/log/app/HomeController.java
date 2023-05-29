package com.log.app;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@RestController
public class HomeController {
	KakaoAPI kakaoApi = new KakaoAPI();
	NaverAPI naverApi = new NaverAPI();
	
	@RequestMapping(value="/login")
	public ModelAndView kakaologin(@RequestParam("code") String code, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 1번 인증코드 요청 전달
		String accessToken = kakaoApi.getAccessToken(code);
		// 2번 인증코드로 토큰 전달
		HashMap<String, Object> userInfo = kakaoApi.getUserInfo(accessToken);
		
		System.out.println("login info : " + userInfo.toString());
		
		if(userInfo.get("email") != null) {
			session.setAttribute("userId", userInfo.get("email"));
			session.setAttribute("accessToken", accessToken);
		}
		mav.addObject("userId", userInfo.get("email"));
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping(value="/login/naver")
	public ModelAndView naverlogin(@RequestParam("code") String code, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// 1번 인증코드 요청 전달
		String accessToken = naverApi.getAccessToken(code);
		// 2번 인증코드로 토큰 전달
		HashMap<String, Object> userInfo = naverApi.getUserInfo(accessToken);
		
		System.out.println("Login info : " + userInfo.toString());
		
		if(userInfo.get("email") != null) {
			session.setAttribute("userId", userInfo.get("email"));
			session.setAttribute("accessToken", accessToken);
		}
		mav.addObject("userId", userInfo.get("email"));
		mav.setViewName("index");
		return mav;
	}
	
	googleAPI  googleAPI;

    public HomeController(googleAPI googleAPI) {
        this.googleAPI = googleAPI;
    }

    @GetMapping("/login/oauth2/code/{registrationId}")
    public ModelAndView googleLogin(@RequestParam String code, @PathVariable String registrationId) {
    	googleAPI.socialLogin(code, registrationId);
//        return "index";
    	ModelAndView mav = new ModelAndView("index"); // Create ModelAndView with "index" view name
        return mav;
    }
	
	@RequestMapping(value="/logout/kakao")
	public ModelAndView kakaologout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		kakaoApi.kakaoLogout((String)session.getAttribute("accessToken"));
		session.removeAttribute("accessToken");
		session.removeAttribute("userId");
		mav.setViewName("index");
		return mav;
	}
	
	@RequestMapping(value="/logout")
	public ModelAndView logout(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		naverApi.naverLogout((String)session.getAttribute("accessToken"));
		session.removeAttribute("accessToken");
		session.removeAttribute("userId");
	    
	    mav.setViewName("index");
		return mav;
	}
}