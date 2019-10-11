package cn.huyingjie.grabticket;

import cn.huyingjie.grabticket.entity.User;
import cn.huyingjie.grabticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class IndexController {

    @Autowired
    private UserService userService;



   @RequestMapping("/")
   public String uploadFile(final HttpServletResponse response, final HttpServletRequest request){
        return "index";
    }

    @RequestMapping("/test")
    @ResponseBody
    public User test(final HttpServletResponse response, final HttpServletRequest request){

       return userService.findByName("hh");

    }





}
