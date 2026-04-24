package xyz.nanian.owl.sugarcane.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 地理位置表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/location")
public class GeolocationController {

    // 树结构（国家-城市-区域）
//    @GetMapping("/tree")
//    public List<LocationTreeVO> tree(){
//        return null;
//    }

    // 根据父ID查子节点（懒加载）
//    @GetMapping("/children")
//    public List<LocationVO> children(@RequestParam Long parentId){
//        return null;
//    }

    // 附近查询（很关键）
//    @GetMapping("/nearby")
//    public List<LocationVO> nearby(
//            @RequestParam double lat,
//            @RequestParam double lon,
//            @RequestParam double radius){
//        return null;
//    }
}

