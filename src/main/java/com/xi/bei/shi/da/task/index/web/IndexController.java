package com.xi.bei.shi.da.task.index.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xi.bei.shi.da.task.index.entity.ResponseEntity;
import com.xi.bei.shi.da.task.index.utils.StatisticalWords;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    @RequestMapping("/task")
    public String index(Model model) {
        model.addAttribute("name", "lucky");
        return "index";
    }

    @RequestMapping(value = "/upload/file", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {

        if (file.isEmpty()) {
            return "上传文件不能为空";
        }

        List<ResponseEntity> list = new ArrayList<>();
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return GSON.toJson("文件格式不正确");
        }
        StringBuffer text = new StringBuffer();
        list = StatisticalWords.statistical(inputStream);
        list.forEach(e -> {
            text.append(e.getKey());
            text.append(":");
            text.append(e.getValue());
            text.append("\r\n");
        });
        request.getSession().setAttribute("result", text);
        return GSON.toJson(list);
    }

    @RequestMapping(value = "/download/txt", method = RequestMethod.GET)
    public void downloadTxt(HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setCharacterEncoding("utf-8");
        // 设置响应的内容类型
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment;filename=work.txt");
        // 得到流
        ServletOutputStream stream = response.getOutputStream();
        // 从session中获取数据
        stream.write(request.getSession().getAttribute("result").toString().getBytes("utf-8"));
        // 关闭流
        stream.flush();
        stream.close();
    }

}
