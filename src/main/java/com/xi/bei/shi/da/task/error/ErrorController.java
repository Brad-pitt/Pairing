package com.xi.bei.shi.da.task.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {


    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        model.addAttribute("url", request.getRequestURL());
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (code == 401) {
            return "/error/401";
        } else if (code == 404) {
            return "/error/404";
        } else if (code == 403) {
            return "/error/403";
        } else {
            return "/error/500";
        }
    }
}
