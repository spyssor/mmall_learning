package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".huyfmall.com";
    private final static String COOKIE_NAME = "mmall_login_token";


    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                log.info("cookieName:{}, cookieValue:{}", cookie.getName(), cookie.getValue());

                //StringUtils的equals方法已经包含了判空操作，所以用起来不会报空指针异常
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    log.info("return cookieName:{}, cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    //X: .happymmall.com            cookie:domain= .happymmall.com;path="/"
    //a:A.happymall.com             cookie:domain=A.happymmall.com;path="/"
    //b:B.happymall.com             cookie:domain=B.happymmall.com;path="/"
    //c:A.happymall.com/test/cc     cookie:domain=A.happymmall.com;path="/test/cc"
    //d:A.happymall.com/test/dd     cookie:domain=A.happymmall.com;path="/test/dd"
    //e:A.happymall.com/test        cookie:domain=A.happymmall.com;path="/test"
    public static void writeLoginToken(HttpServletResponse response, String token){
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录，所有页面都能获取cookie
        ck.setHttpOnly(true);
        ck.setMaxAge(60*60*24*365);//如果是-1 就是永久,单位是秒,如果不设置，cookie就不会写入硬盘，只会写入内存
        log.info("write cookieName:{}, cookieValue:{}", ck.getName(), ck.getValue());

        response.addCookie(ck);
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);//设置成0，代表删除此cookie
                    log.info("del cookieName:{}, cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
