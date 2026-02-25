package com.demo.nutgrow.controller.auth;

import com.demo.nutgrow.model.User;
import com.demo.nutgrow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLogin(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "auth/login";
    }

    @GetMapping("/404")
    public String notFound() {
        return "auth/404";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/?logout";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String save(@RequestParam String email, @RequestParam String fullName, @RequestParam String password,
            Model model) {

        String validationMessage = userService.validateUserInput(email, fullName, password);

        if (validationMessage != null) {
            model.addAttribute("mess", validationMessage);
            return "auth/login";
        }

        userService.saveUser(email, fullName, password);

        return "redirect:/login";
    }

    @RequestMapping(value = "change-password", method = RequestMethod.GET)
    public String indexResetPass() {
        return "changePass";
    }

    @RequestMapping(value = "change-password", method = RequestMethod.POST)
    public String reset(HttpSession session,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("password") String pass,
            RedirectAttributes redirectAttributes) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User userEntity = userService.findByEmail(email).orElse(null);

        if (userEntity == null) {
            redirectAttributes.addFlashAttribute("error", "Phiên đăng nhập đã hết hạn!");
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(oldPassword, userEntity.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
            return "redirect:/change-password";
        }

        userEntity.setPassword(passwordEncoder.encode(pass));
        userService.saveUser(userEntity);

        redirectAttributes.addFlashAttribute("mess", "Đổi mật khẩu thành công!");
        return "redirect:/";
    }

}
