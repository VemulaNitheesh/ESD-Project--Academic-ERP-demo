package com.esd.project.Config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        
        // Generate a simple token (in production, use JWT)
        String token = "oauth_" + URLEncoder.encode(email, StandardCharsets.UTF_8).replace("@", "_at_");
        System.out.println("OAuth2 Success - Email: " + email + ", Name: " + name + ", Token: " + token);

        // Store user info in session for /auth/user endpoint
        request.getSession().setAttribute("oauth2User", oauth2User);
        request.getSession().setAttribute("userEmail", email);
        request.getSession().setAttribute("userName", name);
        request.getSession().setAttribute("userPicture", picture);
        
        // Redirect to frontend with token
        String redirectUrl = "http://localhost:5173/oauth-callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}

