package com.xi.bei.shi.da.task.index.utils;

import com.xi.bei.shi.da.task.index.entity.ResponseEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class StatisticalWords {

    public static List<ResponseEntity> statistical(InputStream inputStream) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

        StringBuffer buffer = new StringBuffer();
        String text;
        while ((text = reader.readLine()) != null) {
            buffer.append(text);// 将读取出的字符追加到stringBuffer中
        }
        reader.close();  // 关闭读入流
        String str = buffer.toString().toLowerCase(); // 将stringBuffer转为字符并转换为小写
        String[] words = str.split("[^(a-zA-Z)]+");  // 非单词的字符来分割，得到所有单词
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (String word : words) {
            if (map.get(word) == null) {  // 若不存在说明是第一次，则加入到map,出现次数为1
                map.put(word, 1);
            } else {
                map.put(word, map.get(word) + 1);  // 若存在，次数累加1
            }
        }

        // 排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> left, Map.Entry<String, Integer> right) {
                return (left.getValue().compareTo(right.getValue()));
            }
        };

        // 集合默认升序升序
        Collections.sort(list, comparator);
        List<ResponseEntity> responseEntities = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {// 由高到低输出
            if (i > 9) {
                break;
            }
            ResponseEntity responseEntity = new ResponseEntity();
            responseEntity.setKey(list.get(list.size() - i - 1).getKey());
            responseEntity.setValue(list.get(list.size() - i - 1).getValue());
            responseEntities.add(responseEntity);
            System.out.println(list.get(list.size() - i - 1).getKey() + ":" + list.get(list.size() - i - 1).getValue());
        }
        return responseEntities;
    }


}
