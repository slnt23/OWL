package xyz.nanian.owl.sugarcane.controller;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 价格来源表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/source")
public class SourceController {

//    @GetMapping("/list")
//    public List<SourceVO> list(){
//        return null;
//    }

//    @PostMapping
//    public void create(@RequestBody SourceCreateDTO dto){
//
//    }

//    @PutMapping("/{id}")
//    public void update(@PathVariable Long id,
//                       @RequestBody SourceUpdateDTO dto){
//
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){}


//    @GetMapping("/reliable")
//    public List<SourceVO> getReliableSources(
//            @RequestParam Integer minLevel){
//        return null;
//    }
}

