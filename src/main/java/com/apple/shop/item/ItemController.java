package com.apple.shop.item;

import com.apple.shop.gcp.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository; //dependency injection
    private final ItemService itemService;
    private final StorageService storageService;

    @GetMapping("/list")
    String list(Model model) {
        List<Item> result = itemRepository.findAll();
        model.addAttribute("items", result);
        return "redirect:/list/page/1";
    }

    @GetMapping("/write")
    String write(Model model) {
        return "write.html";
    }

    @PostMapping("/item/create")
    String writePost(String title, Integer price) {
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping("/list/{id}/edit")
    String edit(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("item", result.get());
            return "itemEdit.html";
        } else {
            return "error.html";
        }
    }

    @PostMapping("/list/{id}/edit")
    String update(@PathVariable Long id, String title, Integer price) {
        itemService.updateItem(id, title, price);
        return "redirect:/list";
    }

    @GetMapping("/list/{id}")
    String detail(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("item", result.get());
            return "detail.html";
        } else {
            return "error.html";
        }
    }

    @DeleteMapping("/list/delete")
    HttpEntity<String> test1(@RequestBody Map<String, Long> body) {
        System.out.println(body.get("id"));
        itemService.deleteItem(body.get("id"));
        return ResponseEntity.status(HttpStatus.OK).body("삭제완료");
    }

    @GetMapping("/list/page/{id}")
    String getListPage(@PathVariable Integer id, Model model) {
        Page<Item> pageData = itemRepository.findPageBy(PageRequest.of(id - 1, 5));
        model.addAttribute("items", pageData);
        model.addAttribute("currentPage", id);
        model.addAttribute("totalPages", pageData.getTotalPages());
        return "list.html";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename){
//        storageService.getBucketName();
        String result = storageService.generateSignedURL("item/" + filename);
        System.out.println("filename : " + filename);
        System.out.println(result);
        return result;
    }
}